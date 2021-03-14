package worldController.realTime;

import com.github.aoklyunin.javaScrollers.scrollers.SimpleScroller;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import gui.GUIApplication;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2i;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector2i;
import jMath.aoklyunin.github.com.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.base.World;
import world.states.WorldState;
import worldController.base.GLController;
import worldController.base.MouseControlled;
import worldController.base.WorldController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Контроллер мира реального времени
 */
public class RealTimeWorldController extends WorldController implements MouseControlled {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(RealTimeWorldController.class);
    /**
     * экранная СК мира
     */
    @NotNull
    private CoordinateSystem2i worldCS;
    /**
     * образцова экранная СК мира в режиме частичного рисования
     */
    @NotNull
    private final CoordinateSystem2i sourcePartScreenWorldCS;
    /**
     * образцова экранная СК мира в режиме рисования во весь экран
     */
    @NotNull
    private final CoordinateSystem2i sourceFullScreenWorldCS;
    /**
     * флаг, рисовать ли мир во весь экран
     */
    @NotNull
    private boolean fullScreenWorld;
    /**
     * координаты начала движения зажатой мыши
     */
    @Nullable
    protected Vector2d worldMouseMovingStart;
    /**
     * система координат рисования мира
     */
    @NotNull
    protected CoordinateSystem2d glRenderWorldCS;
    /**
     * флаг, запущен мир, или нет
     */
    private boolean active;
    /**
     * время выполнения последних вычислений
     */
    private long lastProcessTime;
    /**
     * экранная система координат существа
     */
    @NotNull
    private final CoordinateSystem2i creatureCS;
    /**
     * точка на экране существа, из которой мы начали вести мышь с зажатой кнопкой
     */
    @Nullable
    private Vector2d creatureMouseMovingStart;
    /**
     * углы поворота существа
     */
    @Nullable
    private Vector3d creatureStartRotationAngles;

    /**
     * Конструктор контроллера мира реального времени
     *
     * @param worldControllerParams параметры контроллера мира
     * @param glCanvas              область рисования
     */
    public RealTimeWorldController(
            @NotNull RealTimeWorldControllerParams worldControllerParams, @Nullable GLCanvas glCanvas
    ) {
        super(worldControllerParams, glCanvas);
        active = false;
        fullScreenWorld = false;
        lastProcessTime = System.nanoTime();

        sourceFullScreenWorldCS = new CoordinateSystem2i(
                0,
                GUIApplication.clientWidth,
                0,
                GUIApplication.clientHeight
        );

        sourcePartScreenWorldCS = new CoordinateSystem2i(
                0,
                (int) (getWorldControllerParams().getWindowDivide().x *
                        GUIApplication.clientWidth),
                0,
                GUIApplication.clientHeight
        );

        worldCS = sourcePartScreenWorldCS;


        creatureCS = new CoordinateSystem2i(
                (int) (getWorldControllerParams().getWindowDivide().x
                        * GUIApplication.clientWidth),
                GUIApplication.clientWidth,
                0,
                (int) (GUIApplication.clientHeight *
                        getWorldControllerParams().getWindowDivide().y)
        );
    }

    @Override
    public void loadWorld(String path) {
        worldLoaded = false;
        super.loadWorld(path);
        glRenderWorldCS = new CoordinateSystem2d(
                GLController.GL_RENDER_CS.getMin().x,
                GLController.GL_RENDER_CS.getMax().x,
                GLController.GL_RENDER_CS.getMin().y +
                        getWorld().getWorldParams().getRealTimeWorldParams().getStoryScrollerParams().getRenderPosMax(),
                GLController.GL_RENDER_CS.getMax().y
        );
        worldLoaded = true;
    }

    /**
     * Обработка мира
     *
     * @param world мир, который нужно обработать
     */
    public void process(@NotNull World world) {
        long delta = System.nanoTime() - lastProcessTime;
        if (!active)
            try {
                TimeUnit.MICROSECONDS.sleep(10_000L);
            } catch (InterruptedException e) {
                logger.error("error making delay in mainLoop method");
            }
        else {
            if (delta > getProcessDelay()) {
                //System.out.println("tick");
                lastProcessTime = System.nanoTime();
                world.getWorldInfo().setRealTickFrequency(1_000_000_000L / delta);
                world.getStoryWorld().tick();
            }
        }
    }

    /**
     * Запускаем потоки обработки
     */
    public void startProcessThreads() {
        new Thread(() -> {
            init();
            while (!isTerminated()) {
                process(getWorld());
            }
        }).start();
    }

    @Override
    public void start() {
        super.start();
        startProcessThreads();
    }


    /**
     * Приостановить мир
     */
    protected void pauseWorld() {
        active = false;
    }

    /**
     * Запустить мир
     */
    public void runWorld() {
        active = true;
    }

    /**
     * Проверить, является ли мир активным
     *
     * @return флаг, является ли мир активным
     */
    protected boolean isControllerActive() {
        return active;
    }

    /**
     * Изменить активность
     */
    private void switchWorldActivity() {
        if (active)
            pauseWorld();
        else
            runWorld();
    }


    /**
     * Рисование мира
     *
     * @param gl2 объект OpenGL
     */
    protected void renderWorld(GL2 gl2) {
        // задаём область рисования на экране: x, y, width, height
        worldCS.setViewPort(gl2);

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём систему координат minX, maxX, minY, maxY
        getGlController().getGLU().gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);

        // вызываем метод рисования мира
        getWorld().render(gl2, glRenderWorldCS, getActualWorldState());

        if (!active && getWorld().getWorldParams().getStoryWorldParams().isRecordStory())
            getWorldStoryScroller().renderScroller(gl2);
    }

    /**
     * Получить текущее состояние мира
     *
     * @return текущее состояние мира
     */
    @NotNull
    protected WorldState getActualWorldState() {
        return getWorld().getStoryWorld().getActualWorldState();
    }

    /**
     * Получить скроллер истории мира
     *
     * @return скроллер истории мира
     */
    @NotNull
    protected SimpleScroller getWorldStoryScroller() {
        return getWorld().getRealTimeWorld().getWorldStoryScroller();
    }


    /**
     * Рисование существа
     *
     * @param gl2 объект OpenGL
     */
    private void renderCreatureModel(GL2 gl2) {
        // задаём область рисования на экране: x, y, width, height
        creatureCS.setViewPort(gl2);

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём перспективу: угол обзора, сооношение сторон экрана, ближняя точка отсечения, дальняя точка отсечения
        getGlController().getGLU().gluPerspective(
                45.0, (double) creatureCS.getSize().x / creatureCS.getSize().y, 0.1, 500.0
        );

        // задаём параметры камеры
        getGlController().getGLU().gluLookAt(
                0, 0, -1, // положение камеры
                0, 0, 0, // куда смотрит камера
                0, 1, 0  // вектор "вверх"
        );
        // выбираем модельную матрицу
        gl2.glMatrixMode(GL_MODELVIEW);
        // делаем её единичной
        gl2.glLoadIdentity();

        // разрешаем проверку глубины
        gl2.glEnable(GL_DEPTH_TEST);

        //gl2.glEnable(GL_LIGHTING);
        //gl2.glEnable(GL_LIGHT0);
        // рисование выделенного в мире существа
        getWorld().getRealTimeWorld().renderCreatureModel(gl2, getActualWorldState());
        //gl2.glDisable(GL_LIGHTING);

        // запрещаем проверку глубины
        gl2.glDisable(GL_DEPTH_TEST);

    }


    /**
     * Обработка зажатия кнопки мыши
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    @Override
    public void processMousePress(@NotNull Vector2i coords, int mouseButton) {
        if (mouseButton == MouseEvent.BUTTON1) {
            // перемещение скроллера
            if (worldMouseMovingStart == null && worldCS.checkCoords(coords)) {
                // получаем координаты клика в СК мира
                clickWorld(GLController.GL_CS.getCoords(coords, worldCS), mouseButton);
            }
            if (creatureMouseMovingStart == null && creatureCS.checkCoords(coords) && !fullScreenWorld) {
                clickCreature(GLController.GL_CS.getCoords(coords, creatureCS), mouseButton);
            }
        }
    }

    /**
     * Клик по области рисования мира
     *
     * @param mousePos    положение мыши в СК OpenGL
     * @param mouseButton номер кнопки
     */
    protected void clickWorld(@NotNull Vector2d mousePos, int mouseButton) {
        worldMouseMovingStart = new Vector2d(Objects.requireNonNull(mousePos));
        // если попали по скроллеру, перемещаемся по истории,
        // если нет - добавляем ресурс
        if (!getWorldStoryScroller().setScrollerCursorPosByClick(mousePos))
            getWorld().getRealTimeWorld().clickWorld(mousePos, glRenderWorldCS, mouseButton);
    }


    /**
     * Клик по области рисования существа
     *
     * @param mousePos    положение мыши в СК OpenGL
     * @param mouseButton номер кнопки
     */
    protected void clickCreature(@NotNull Vector2d mousePos, int mouseButton) {
        creatureMouseMovingStart = new Vector2d(Objects.requireNonNull(mousePos));
        creatureStartRotationAngles = getWorld().getStoryWorld().getCreatureStandAloneTransform().getRotation();
    }


    /**
     * Обработка отжатия кнопки мыши
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    @Override
    public void processMouseReleased(@NotNull Vector2i coords, int mouseButton) {
        // обработка отжатия клавиши мыши
        if (mouseButton == MouseEvent.BUTTON1) {
            worldMouseMovingStart = null;
            creatureMouseMovingStart = null;
        }
    }


    /**
     * Обработка перемещения мыши с зажатой кнопкой
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    @Override
    public void processMouseDragged(@NotNull Vector2i coords, int mouseButton) {
        // перемещение скроллера
        if (worldMouseMovingStart != null && worldCS.checkCoords(coords)) {
            // получаем координаты клика в СК мира
            dragWorld(GLController.GL_CS.getCoords(coords, worldCS), mouseButton);
        } else if (creatureMouseMovingStart != null && creatureCS.checkCoords(coords)) {
            dragCreature(GLController.GL_CS.getCoords(coords, creatureCS), mouseButton);
        }
    }

    /**
     * Перемещения мыши с зажатой кнопкой по области рисования существа
     *
     * @param mousePos    положение мыши в СК OpenGL
     * @param mouseButton номер кнопки
     */
    protected void dragCreature(@NotNull Vector2d mousePos, int mouseButton) {
        Vector2d delta = Vector2d.subtract(Objects.requireNonNull(mousePos), creatureMouseMovingStart);
        getWorld().getStoryWorld().getCreatureStandAloneTransform().setRotation(Vector3d.sum(
                creatureStartRotationAngles,
                new Vector3d(
                        0,
                        delta.x * getWorldControllerParams().getRealTimeWorldControllerParams().getCreatureRotationCoeff(),
                        delta.y * getWorldControllerParams().getRealTimeWorldControllerParams().getCreatureRotationCoeff()
                )
        ));
    }

    /**
     * Перемещения мыши с зажатой кнопкой по области рисования мира
     *
     * @param mousePos    положение мыши в СК OpenGL
     * @param mouseButton номер кнопки
     */
    protected void dragWorld(@NotNull Vector2d mousePos, int mouseButton) {
        getWorldStoryScroller().setScrollerCursorPosByClick(Objects.requireNonNull(mousePos));
    }

    /**
     * Обработка клика мышью
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    @Override
    public void processMouseClick(@NotNull Vector2i coords, int mouseButton) {
        if (worldCS.checkCoords(coords)) { // если координаты попадают в СК мира
            clickWorld(GLController.GL_CS.getCoords(Objects.requireNonNull(coords), worldCS), mouseButton);
        }
    }

    @Override
    public void processMouseWheel(int wheelRotation) {

    }

    /**
     * Инициализация миров
     */
    public void init() {
        GUIApplication.disableRender();
        getWorld().getRealTimeWorld().init();
        GUIApplication.enableRender();
    }


    /**
     * Обработка нажатия клавиатуры
     *
     * @param keyCode код клавиши
     */
    @Override
    public void processKeyPress(int keyCode) {
        super.processKeyPress(keyCode);
        switch (keyCode) {
            case KeyEvent.VK_C:
                break;
            case KeyEvent.VK_SPACE:
                if (GUIApplication.flgCtrl) {
                    if (!isControllerActive()) {
                        restart();
                    }
                } else
                    switchWorldActivity();
                break;
            case KeyEvent.VK_T:
                fullScreenWorld = !fullScreenWorld;
                worldCS = fullScreenWorld ? sourceFullScreenWorldCS : sourcePartScreenWorldCS;
                break;
            case KeyEvent.VK_RIGHT:
                if (!GUIApplication.flgCtrl)
                    // переходим к следующему состоянию в истории ДНК-построения
                    incWorldStoryPos();
                break;
            case KeyEvent.VK_LEFT:
                if (!GUIApplication.flgCtrl)
                    decWorldStoryPos();
                break;
            case KeyEvent.VK_L:
                getWorld().getRealTimeWorld().switchRenderLogInfoMode();
                break;
            default:
                //System.out.println(keyCode);
                break;
        }
    }

    /**
     * Перезапустить мир
     */
    protected void restart() {
        init();
        runWorld();
    }

    /**
     * Отрисовка кадра подпрограммы
     *
     * @param gl2 объект OpenGL
     */
    @Override
    public void display(GL2 gl2) {
        if (GUIApplication.isRenderEnabled()) {
            if (!fullScreenWorld) {
                renderCreatureModel(gl2);
            }
            renderWorld(gl2);
        }
    }

    /**
     * Строковое представление объекта вида:
     * "worldCS, glRenderWorldCS, active, creatureCS, fullScreenWorld, super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return worldCS +
                ", " + glRenderWorldCS +
                ", " + active +
                ", " + creatureCS +
                ", " + fullScreenWorld +
                ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTimeWorldController{getString()}"
     */
    @Override
    public String toString() {
        return "RealTimeWorldController{" + getString() + '}';
    }

    /**
     * Получить СК мира
     *
     * @return СК мира
     */
    @NotNull
    public CoordinateSystem2i getWorldCS() {
        return worldCS;
    }

    /**
     * Получить СК рисования мира
     *
     * @return СК рисования мира
     */
    @NotNull
    public CoordinateSystem2d getGlRenderWorldCS() {
        return glRenderWorldCS;
    }

    /**
     * Получить флаг, активен ли мир
     *
     * @return активен ли мир
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Получить флаг, включенн ли режим рисования мира на весь экран
     *
     * @return включенн ли режим рисования мира на весь экран
     */
    public boolean isFullScreenWorld() {
        return fullScreenWorld;
    }

    /**
     * Получить СК существа
     *
     * @return СК существа
     */
    @NotNull
    public CoordinateSystem2i getCreatureCS() {
        return creatureCS;
    }


}