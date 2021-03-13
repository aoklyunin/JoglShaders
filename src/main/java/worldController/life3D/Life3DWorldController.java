package worldController.life3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLCanvas;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import coordinateSystem.CoordinateSystem2d;
import creature.base.Creature;
import graphics.Camera;
import gui.GUIApplication;
import math.Vector2d;
import math.Vector2i;
import world.base.World;
import worldController.base.GLController;
import worldController.life.LifeWorldController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Класс Help 3D мира с жизнью
 */
public class Life3DWorldController extends LifeWorldController {
    /**
     * Камера
     */
    @NotNull
    private final Camera camera;

    /**
     * Конструктор контроллера мира реального времени
     *
     * @param worldControllerParams параметры контроллера мира
     * @param glCanvas              область рисования
     */
    public Life3DWorldController(@NotNull Life3DWorldControllerParams worldControllerParams, @Nullable GLCanvas glCanvas) {
        super(worldControllerParams, glCanvas);
        camera = new Camera(
                2, 2, 2,
                -2, -2, -2,
                0, 0, 1
        );
        creatureModelRendering = false;
    }

    /**
     * Рисование мира
     *
     * @param gl2 объект OpenGL
     */
    @Override
    protected void renderWorld(GL2 gl2) {
        // задаём область рисования на экране: x, y, width, height
        gl2.glViewport(
                getWorldCS().getMin().x,
                getWorldCS().getMin().y,
                getWorldCS().getSize().x,
                getWorldCS().getSize().y
        );

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём перспективу: угол обзора, сооношение сторон экрана, ближняя точка отсечения, дальняя точка отсечения
        getGlController().getGLU().gluPerspective(
                45.0, (double) getWorldCS().getSize().x / getWorldCS().getSize().y, 0.1, 500.0
        );

        switch (getWorld().getRealTime3DWorld().getCameraMode()) {
            case OBSERVER -> camera.gluLookAt(getGlController().getGLU());
            case SELECTED_CREATURE -> getSelectedCreature().getCamera().gluLookAt(getGlController().getGLU());
        }

        // разрешаем проверку глубины
        gl2.glEnable(GL_DEPTH_TEST);

        //gl2.glEnable(GL_LIGHTING);
        //gl2.glEnable(GL_LIGHT0);
        // рисование выделенного в мире существа
        getWorld().render(gl2, getGlRenderWorldCS(), getActualWorldState());
        //gl2.glDisable(GL_LIGHTING);

        // запрещаем проверку глубины
        gl2.glDisable(GL_DEPTH_TEST);

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём систему координат minX, maxX, minY, maxY
        getGlController().getGLU().gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);

        if (!isActive() && getWorld().getWorldParams().getStoryWorldParams().isRecordStory())
            getWorldStoryScroller().renderScroller(gl2);

        if (getWorld().getRealTimeWorld().isRenderLogInfo())
            getWorld().getRealTimeWorld().renderLog(gl2, getActualWorldState());

    }

    /**
     * Получить выбранное существо
     *
     * @return выбранное существо
     */
    @NotNull
    protected Creature getSelectedCreature() {
        return getWorld().getStoryWorld().getSelectedCreature();
    }

    /**
     * Рисование существа
     *
     * @param gl2 объект OpenGL
     */
    private void renderConnectorGrid(GL2 gl2) {
        // задаём область рисования на экране: x, y, width, height
        getCreatureCS().setViewPort(gl2);

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём систему координат minX, maxX, minY, maxY
        getGlController().getGLU().gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);

        // вызываем метод рисования мира
        getWorld().getRealTime3DWorld().renderConnectorGrid(gl2, getSelectedCreature());

    }

    /**
     * Перемещения мыши с зажатой кнопкой по области рисования мира
     *
     * @param mousePos положение мыши в СК OpenGL
     */
    @Override
    protected void dragWorld(@NotNull Vector2d mousePos, int mouseButton) {
        if (!getWorldStoryScroller().setScrollerCursorPosByClick(Objects.requireNonNull(mousePos))) {
            Vector2d delta = Vector2d.subtract(mousePos, worldMouseMovingStart);
            worldMouseMovingStart = new Vector2d(mousePos);
            switch (getWorld().getRealTime3DWorld().getCameraMode()) {
                case OBSERVER -> camera.rotate(Vector2d.mul(
                        delta, getWorldControllerParams().getLife3DWorldControllerParams().getCameraRotationSpeed()
                ));
                case SELECTED_CREATURE -> {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().rotate(Vector2d.mul(
                            delta, getWorldControllerParams().getLife3DWorldControllerParams().getCameraRotationSpeed()
                    ));
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
            }
        }
    }

    /**
     * Перемещения мыши с зажатой кнопкой по области рисования мира
     *
     * @param mousePos положение мыши в СК OpenGL
     */
    protected void dragCreature(@NotNull Vector2d mousePos, int mouseButton) {
        if (creatureModelRendering) {
            super.dragCreature(Objects.requireNonNull(mousePos), mouseButton);
        } else {
            getWorld().getRealTime3DWorld().clickConnectorGrid(mousePos, mouseButton);
        }
    }

    /**
     * Обработка прокрутки колёсика мыши
     *
     * @param wheelRotation кол-во дискрет поворота
     */
    @Override
    public void processMouseWheel(int wheelRotation) {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Vector2i mousePos = new Vector2i(mousePoint.x, GUIApplication.clientHeight - mousePoint.y);
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
            case KeyEvent.VK_G:
                if (GUIApplication.flgCtrl) {
                    creatureModelRendering = !creatureModelRendering;
                } else
                    getWorld().getRealTime3DWorld().switchCameraMode();
                break;
            case KeyEvent.VK_C:
                if (!GUIApplication.flgCtrl) {
                    getWorld().getLiveCreatureAcceptable().cloneSelectedCreature();
                }
                break;
        }
    }

    /**
     * Обработка мира
     *
     * @param world мир, который нужно обработать
     */
    @Override
    public long process(@NotNull World world) {
        long delta = super.process(Objects.requireNonNull(world));
        double movingDelta = (double) super.process(world) / 10E9;
        switch (getWorld().getRealTime3DWorld().getCameraMode()) {
            case OBSERVER -> {
                if (flgKeyDown[KeyEvent.VK_W])
                    camera.moveFroward(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().x * movingDelta
                    );
                if (flgKeyDown[KeyEvent.VK_S])
                    camera.moveBackward(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().x * movingDelta
                    );
                if (flgKeyDown[KeyEvent.VK_A])
                    camera.moveLeft(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().y * movingDelta
                    );
                if (flgKeyDown[KeyEvent.VK_D])
                    camera.moveRight(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().y * movingDelta
                    );
                if (flgKeyDown[KeyEvent.VK_Q])
                    camera.moveDown(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().z * movingDelta
                    );
                if (flgKeyDown[KeyEvent.VK_E])
                    camera.moveUp(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().z * movingDelta
                    );
            }
            case SELECTED_CREATURE -> {
                if (flgKeyDown[KeyEvent.VK_W]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveFroward(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().x * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
                if (flgKeyDown[KeyEvent.VK_S]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveBackward(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().x * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
                if (flgKeyDown[KeyEvent.VK_A]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveLeft(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().y * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
                if (flgKeyDown[KeyEvent.VK_D]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveRight(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().y * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
                if (flgKeyDown[KeyEvent.VK_Q]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveDown(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().z * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
                if (flgKeyDown[KeyEvent.VK_E]) {
                    getWorld().getRealTime3DWorld().getSelectedCreature().getCamera().moveUp(
                            getWorldControllerParams().getLife3DWorldControllerParams()
                                    .getCameraMovingSpeed().z * movingDelta
                    );
                    getWorld().getRealTime3DWorld().calculateCreaturesSensorGrid();
                }
            }
        }
        return delta;
    }

    /**
     * Обработка клика мышью
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    @Override
    public void processMouseClick(@NotNull Vector2i coords, int mouseButton) {
        super.processMouseClick(Objects.requireNonNull(coords), mouseButton);
        if (getCreatureCS().checkCoords(coords)) { // если координаты попадают в СК мира
            if (!creatureModelRendering) {
                getWorld().getRealTime3DWorld().clickConnectorGrid(
                        GLController.GL_CS.getCoords(coords, getCreatureCS()), mouseButton
                );
            }
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        super.dispose(drawable);
    }

    /**
     * Отрисовка кадра подпрограммы
     *
     * @param gl2 объект OpenGL
     */
    @Override
    public void display(GL2 gl2) {
        if (GUIApplication.isRenderEnabled()) {
            super.display(gl2);
            if (!isFullScreenWorld()) {
                if (!creatureModelRendering)
                    renderConnectorGrid(gl2);
            }
        }
    }

    /**
     * Строковое представление объекта вида:
     * "camera, super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return camera +
                ", " + super.getString();
    }

    /**
     * Получить камеру контроллера
     *
     * @return камера контроллера
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Life3DWorldController{getString()}"
     */
    @Override
    public String toString() {
        return "Life3DWorldController{" + getString() + '}';
    }

}
