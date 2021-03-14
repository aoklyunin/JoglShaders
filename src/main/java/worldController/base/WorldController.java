package worldController.base;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import gui.GUIApplication;
import world.WorldFactory;
import world.base.World;

import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static constants.Constants.RESOURCE_PATH;

/**
 * Контроллер мира
 * hashCode и equals не нужно переопределять, т.к. при передаче GLEventListener канвасу они вызываются
 * и либо замедляют работу, либо вообще её ломают
 */
public abstract class WorldController implements GLEventListener, AutoCloseable {
    /**
     * Контроллер OpenGL
     */
    @NotNull
    private final GLController glController;
    /**
     * непосредственно сам мир
     */
    @NotNull
    protected World world;
    /**
     * параметры контроллера мира
     */
    @NotNull
    private final WorldControllerParams worldControllerParams;
    /**
     * флаг, что приложение надо остановить
     */
    private boolean terminated;
    /**
     * задержка между срабатываниями тактов обработки в наносекундах
     */
    private final long processDelay;
    /**
     * флаг, используется контроллер консольным приложением
     */
    private final boolean console;
    /**
     * Массив флагов, зажата ли та или иная кнопка
     */
    protected boolean[] flgKeyDown;
    /**
     * Флаг, загружен ли мир
     */
    protected boolean worldLoaded;

    /**
     * Конструктор контроллера мира
     *
     * @param worldControllerParams параметры контроллера мира
     * @param glCanvas              область рисования
     */
    public WorldController(@NotNull WorldControllerParams worldControllerParams, @Nullable GLCanvas glCanvas) {
        this.glController = new GLController(glCanvas, worldControllerParams.getFps());
        this.worldControllerParams = Objects.requireNonNull(worldControllerParams);
        this.console = glCanvas == null;
        this.terminated = false;

        if (worldControllerParams.getFrequency() != 0) {
            processDelay = 1_000_000_000L / worldControllerParams.getFrequency();
        } else {
            processDelay = 0;
        }

        flgKeyDown = new boolean[1024];
        worldLoaded = false;
    }


    /**
     * Загрузить мир,определённый в параметрах контроллера
     */
    public void loadWorld() {
        loadWorld(RESOURCE_PATH + worldControllerParams.getWorldPath());
    }


    /**
     * Загрузить мир
     *
     * @param path путь к файлу описания мира
     * @implSpec вызывается в методе loadWorld(). При переопределении метода необходимо поддерживать
     * значение флага worldLoaded false на протяжении всего метода и только в конце задать значение true
     */
    public void loadWorld(@NotNull String path) {
        worldLoaded = false;
        world = WorldFactory.loadWorld(
                Objects.requireNonNull(path), GUIApplication.clientWidth, GUIApplication.clientHeight,
                getWorldControllerParams().getBackgroundColor(),
                getWorldControllerParams().getWindowDivide()
        );
        worldLoaded = true;
    }


    /**
     * Приостановить контроллер
     */
    public void pause() {
        terminated = true;
    }


    /**
     * Запустить контроллер
     */
    public void start() {
        terminated = false;
        if (!console)
            glController.getGLCanvas().repaint();
    }

    /**
     * Обработка нажатия клавиатуры мыши
     *
     * @param keyCode код клавиши
     */
    public void processKeyPress(int keyCode) {
        flgKeyDown[keyCode] = true;
    }

    /**
     * Следующее состояние истории мира
     */
    protected void incWorldStoryPos() {
        getWorld().getStoryWorld().incStoryPos();
    }

    /**
     * Предыдущее состояние истории мира
     */
    protected void decWorldStoryPos() {
        getWorld().getStoryWorld().decStoryPos();
    }

    /**
     * Обработка закрытия подпрограммы
     */
    public void close() {
        // закрываем мир
        world.close();
    }

    /**
     * Рисование, которое напрямую использует OpenGL
     *
     * @param drawable переменная рисования
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (worldLoaded)
            display(gl2);

        if (!terminated) {
            getWorld().getWorldInfo().setRealRenderFrequency(1_000_000_000L / glController.delayForRender());
            glController.getGLCanvas().repaint();
        }
    }

    /**
     * Отрисовка кадра контроллером
     *
     * @param gl2 объект OpenGL
     * @implSpec вызывается из метода display(GLAutoDrawable drawable)
     */
    public abstract void display(GL2 gl2);

    /**
     * Закрытие окна
     *
     * @param drawable переменная рисования
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.exit(0);
    }

    /**
     * Инициализация рисования
     *
     * @param drawable переменная рисования
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();

        // прозрачность
        gl2.glEnable(GL_BLEND);
        gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Красивая перспектива
        gl2.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        gl2.glClearColor(
                (float) worldControllerParams.getBackgroundColor().x,
                (float) worldControllerParams.getBackgroundColor().y,
                (float) worldControllerParams.getBackgroundColor().z,
                1.0f
        );
    }

    /**
     * Изменение параметров окна
     *
     * @param drawable объект рисования окна
     * @param x        координата X
     * @param y        координата Y
     * @param width    ширина окна
     * @param height   высота окна
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glViewport(x, y, width, height);
    }

    /**
     * Обработка отжатия клавиш
     *
     * @param keyCode код клавиши
     */
    public void processKeyReleased(int keyCode) {
        flgKeyDown[keyCode] = false;
    }

    /**
     * Флаг, приостановлен ли мир
     *
     * @return приостановлен ли мир
     */
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * Получить контроллер OpenGL
     *
     * @return контроллер OpenGL
     */
    @NotNull
    public GLController getGlController() {
        return glController;
    }

    /**
     * Получить выбранный мир
     *
     * @return выбранный мир
     */
    @NotNull
    public World getWorld() {
        return world;
    }

    /**
     * Получить параметры контроллера мира
     *
     * @return параметры контроллера мира
     */
    public WorldControllerParams getWorldControllerParams() {
        return worldControllerParams;
    }

    /**
     * Получить задержку обработки мира
     *
     * @return задержка обработки мира
     */
    public long getProcessDelay() {
        return processDelay;
    }

    /**
     * Получить флаг, запущен ли контроллер в консольном режиме
     *
     * @return запущен ли контроллер в консольном режиме
     */
    public boolean isConsole() {
        return console;
    }

    /**
     * Строковое представление объекта вида:
     * "glController, world, worldControllerParams, terminated, processDelay, console"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return glController +
                ", " + world +
                ", " + worldControllerParams +
                ", " + terminated +
                ", " + processDelay +
                ", " + console;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "WorldController{getString()}"
     */
    @Override
    public String toString() {
        return "WorldController{" + getString() + '}';
    }

    /**
     * Преобразовать к управляемому мышью объекту
     *
     * @return управляемый мышью объект
     */
    public MouseControlled getMouseControlled() {
        if (this instanceof MouseControlled)
            return (MouseControlled) this;
        throw new AssertionError();
    }


}

