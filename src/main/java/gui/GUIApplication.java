package gui;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import gui.dialogs.HelpDialogFactory;
import jMath.aoklyunin.github.com.vector.Vector2i;
import worldController.WorldControllerParamsFactory;
import worldController.base.WorldController;
import worldController.base.WorldControllerParams;
import worldController.life3D.Life3DWorldController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static constants.Constants.RESOURCE_PATH;
import static gui.dialogs.DialogFactory.showApplyDialog;

/**
 * Графическое приложение для работы со всеми типами миров
 */
public class GUIApplication implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    /**
     * Путь к приложению
     */
    private static final String APPLICATION_CONF_PATH = RESOURCE_PATH + "conf/guiApplication.json";

    /**
     * Канвас для рисования OpenGL
     */
    @NotNull
    public static GLCanvas glCanvas;
    /**
     * Окно приложения
     */
    @NotNull
    public static JFrame frame;
    /**
     * Ширина окна
     */
    public static int clientWidth;
    /**
     * Высота окна
     */
    public static int clientHeight;
    /**
     * Флаг, что зажатая клавиша Alt
     */
    public static boolean flgAlt = false;
    /**
     * Флаг, что зажатая клавиша Ctrl
     */
    public static boolean flgCtrl = false;
    /**
     * Флаг, что зажатая клавиша Shift
     */
    public static boolean flgShift = false;
    /**
     * Массив подпрограмм
     */
    @NotNull
    protected static List<WorldController> worldControllers;
    /**
     * Номер текущей подпрограммы
     */
    private static int worldControllerPos;
    /**
     * Номер предыдущей подпрограммы
     */
    private static int prevWorldControllerPos;
    /**
     * Последняя нажатая клавиша мыши
     */
    private int lastMouseButtonPressed = -1;

    /**
     * Флаг, что мир инициализирован
     */
    private static final boolean[] renderEnabled = new boolean[]{false};

    /**
     * Главный метод приложения
     *
     * @param args - аргументы, переданные через командную строку
     */
    public static void main(String[] args) {
        new GUIApplication(APPLICATION_CONF_PATH);
    }

    /**
     * Конструктор класса графического приложения
     *
     * @param path путь к параметрам приложения
     */
    public GUIApplication(@NotNull String path) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices(); //возвращает массив устройств
        System.out.println(gs.length);

        // Задаём версию OpenGL
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // Создаём канвас
        glCanvas = new GLCanvas(capabilities);

        // Создаём окно
        frame = new JFrame("");

        // растягиваем его на весь экран
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        int width = 1920;
        int height = 1040;
        frame.setSize(width, height);
        // обработка закрытия окна по крестику
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // добавляем в окно канвас OpenGL
        frame.getContentPane().add(glCanvas);
        // делаем окно видимым
        frame.setVisible(true);

        // получаем ширину окна
        clientWidth = width;
        // получаем высоту окна
        if (System.getProperty("os.name").equals("Linux"))
            clientHeight = height - 80;
        else
            clientHeight = height - 40;

        // загрузить параметры главного приложения
        loadApplicationConf(Objects.requireNonNull(path), glCanvas);
        // переходим к выбранной подпрограмме
        startSelectedWorldController();


        // вешаем обработчики
        glCanvas.addKeyListener(this);
        glCanvas.addMouseListener(this);
        glCanvas.addMouseMotionListener(this);
        glCanvas.addMouseWheelListener(this);

    }

    /**
     * Разрешить рисование
     */
    public static void enableRender() {
        synchronized (renderEnabled) {
            renderEnabled[0] = true;
        }
    }

    /**
     * Запретить рисование
     */
    public static void disableRender() {
        synchronized (renderEnabled) {
            renderEnabled[0] = false;
        }
    }

    /**
     * Проверка, можно ли рисовать
     *
     * @return можно ли рисовать
     */
    public static boolean isRenderEnabled() {
        synchronized (renderEnabled) {
            return renderEnabled[0];
        }
    }

    /**
     * Загрузить параметры главного приложения
     *
     * @param path     путь к файлу параметров
     * @param glCanvas область рисования
     */
    private void loadApplicationConf(@NotNull String path, @NotNull GLCanvas glCanvas) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            GuiApplicationConf applicationConf = objectMapper.readValue(new File(path), GuiApplicationConf.class);
            worldControllerPos = applicationConf.getStartController();
            createControllers(
                    applicationConf.getControllers().stream().map(controller ->
                            WorldControllerParamsFactory.fromFile(
                                    RESOURCE_PATH + controller
                            )
                    ).collect(Collectors.toList()), glCanvas
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Сбросить флаги клавиш Ctrl, Alt и Shift
     */
    public static void dropCAS() {
        flgCtrl = false;
        flgAlt = false;
        flgShift = false;
    }

    /**
     * Создать контроллеров
     *
     * @param worldControllerParamsList список параметров контроллеров мира
     * @param glCanvas                  область рисования
     */
    private void createControllers(
            @NotNull List<WorldControllerParams> worldControllerParamsList, @Nullable GLCanvas glCanvas
    ) {
        worldControllers = new ArrayList<>();

        for (int i = 0; i < worldControllerParamsList.size(); i++) {
            WorldControllerParams worldControllerParams = worldControllerParamsList.get(i);
            WorldController worldController = switch (worldControllerParams.getType()) {
                case REAL_TIME_3D -> new Life3DWorldController(
                        worldControllerParams.getLife3DWorldControllerParams(), glCanvas
                );
                default -> throw new AssertionError("unexpected world controller params type " + worldControllerParams.getType());
            };
            worldController.loadWorld();
            worldControllers.add(worldController);
        }
        prevWorldControllerPos = 0;
    }

    /**
     * Переход к выбранной подпрограмме
     */
    private void startSelectedWorldController() {
        WorldController worldController = getSelectedWorldController();

        // удаляем все обработчики у канваса
        glCanvas.removeGLEventListener(getPrevWorldController());
        // добавляем новые обработчики от выбранной подпрограммы
        glCanvas.addGLEventListener(worldController);
        // переводим фокус на канвас
        glCanvas.requestFocus();

        // меняем заголовок окна на название мира в выбранной подпрограмме
        frame.setTitle(worldController.getWorld().getWorldParams().getName());
        getSelectedWorldController().start();
    }


    /**
     * Выход из приложения
     */
    private void exit() {
        getSelectedWorldController().pause();
        // закрываем все подпрограммы
        for (WorldController worldController : worldControllers) {
            worldController.close();
        }

        HelpDialogFactory.disposeAll();

        // закрываем окно
        frame.dispose();

        // выходим из приложения
        System.exit(0);
    }

    /**
     * Возвращает текущую подпрограмму
     *
     * @return текущая подпрограмма
     */
    @NotNull
    private static WorldController getSelectedWorldController() {
        return worldControllers.get(worldControllerPos);
    }

    /**
     * Вернуть предыдущую подпрограмму
     *
     * @return предыдущая подпрограмма
     */
    @NotNull
    private WorldController getPrevWorldController() {
        return worldControllers.get(prevWorldControllerPos);
    }


    /**
     * Перейти к предыдущий подпрограмме
     */
    private void setPrevWorldController() {
        int tmp = prevWorldControllerPos;
        prevWorldControllerPos = worldControllerPos;
        worldControllerPos = tmp;
        startSelectedWorldController();
    }

    /**
     * Перейти к подпрограмме по номеру, переданному в параметрах
     *
     * @param controllerPos номер контроллера
     */
    private void setWorldControllerPos(int controllerPos) {
        if (controllerPos >= worldControllers.size() - 2)
            controllerPos = worldControllers.size() - 3;
        if (controllerPos < 0)
            controllerPos = 0;
        if (getSelectedWorldController() != null)
            getSelectedWorldController().pause();

        prevWorldControllerPos = worldControllerPos;
        worldControllerPos = controllerPos;

        startSelectedWorldController();
    }

    /**
     * Показать окно Help
     */
    protected void showHelpDialog() {
        HelpDialogFactory.showHelpDialog(getSelectedWorldController().getWorldControllerParams().getType());
    }

    /**
     * Обработчик клика кнопки клавиатуры
     *
     * @param e событие клика
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                flgCtrl = true;
                break;
            case KeyEvent.VK_ALT:
                flgAlt = true;
                break;
            case KeyEvent.VK_SHIFT:
                flgShift = true;
                break;
            case KeyEvent.VK_ESCAPE:
                showApplyDialog("Do you want to exit?", this::exit);
                break;
            case KeyEvent.VK_1:
                if (!flgCtrl)
                    setWorldControllerPos(0);
                break;
            case KeyEvent.VK_2:
                if (!flgCtrl)
                    setWorldControllerPos(1);
                break;
            case KeyEvent.VK_3:
                if (!flgCtrl)
                    setWorldControllerPos(2);
                break;
            case KeyEvent.VK_4:
                if (!flgCtrl)
                    setWorldControllerPos(3);
                break;
            case KeyEvent.VK_5:
                if (!flgCtrl)
                    setWorldControllerPos(4);
                break;
            case KeyEvent.VK_6:
                if (!flgCtrl)
                    setWorldControllerPos(5);
                break;
            case KeyEvent.VK_H:
                setPrevWorldController();
                break;

            case KeyEvent.VK_F1:
                showHelpDialog();
                break;
            case 192: // тильда
            case KeyEvent.VK_F4:
                if (GUIApplication.flgAlt)
                    exit();
                break;
        }
        getSelectedWorldController().processKeyPress(e.getKeyCode());
    }

    /**
     * Запустить рисование
     */
    public static void start() {
        frame.setVisible(true);
    }

    /**
     * Остановить рисование
     */
    private void stop() {
        frame.setVisible(false);
    }

    /**
     * Обработчик отжатия кнопки клавиатуры
     *
     * @param e событие отжатия
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL -> GUIApplication.flgCtrl = false;
            case KeyEvent.VK_ALT -> GUIApplication.flgAlt = false;
            case KeyEvent.VK_SHIFT -> GUIApplication.flgShift = false;
        }
        getSelectedWorldController().processKeyReleased(e.getKeyCode());
    }

    /**
     * Обработчик зажатия кнопки клавиатуры
     *
     * @param e событие зажатия
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Преобразовать координаты из СК java в СК приложения
     * Для этого вс еоординаты конвертируются по Y
     *
     * @param e событие нажатия кнопки мыши
     * @return Целочисленный вектор положения мыши в СК приложения
     */
    private Vector2i getMouseCoords(MouseEvent e) {
        return new Vector2i(e.getX(), clientHeight - e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        getSelectedWorldController().getMouseControlled().processMouseClick(getMouseCoords(e), e.getButton());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // обработка нажатия клавиши мыши
        Vector2i coords = getMouseCoords(e);
        lastMouseButtonPressed = e.getButton();
        getSelectedWorldController().getMouseControlled().processMousePress(coords, e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vector2i coords = getMouseCoords(e);
        lastMouseButtonPressed = -1;
        getSelectedWorldController().getMouseControlled().processMouseReleased(coords, e.getButton());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vector2i coords = getMouseCoords(e);
        getSelectedWorldController().getMouseControlled().processMouseDragged(coords, lastMouseButtonPressed);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        getSelectedWorldController().getMouseControlled().processMouseWheel(-e.getWheelRotation());
    }
}
