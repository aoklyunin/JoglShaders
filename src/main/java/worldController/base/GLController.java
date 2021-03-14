package worldController.base;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.sun.istack.Nullable;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Контроллер OpenGL
 */
public class GLController {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(GLController.class);

    /**
     * система координат OpenGL
     */
    public static final CoordinateSystem2d GL_CS = new CoordinateSystem2d(0, 1.0, 0, 1.0);

    /**
     * система координат рисования
     */
    public static final CoordinateSystem2d GL_RENDER_CS =
            new CoordinateSystem2d(0.05, 0.95, 0.05, 0.95);

    /**
     * специальный объект для использования методов библиотеки glut
     */
    private final GLU glu;
    /**
     * Ссылка на область рисования
     */
    @Nullable
    private final GLCanvas glCanvas;
    /**
     * последнее время отрисовки
     */
    private long lastRenderTime;
    /**
     * задержка между срабатываниями тактов главного цикла  в микросекундах
     */
    private final long renderDelayInMicros;

    /**
     * Конструктор контроллера OpenGL
     *
     * @param glCanvas поле рисования
     * @param fps      FPS контроллера
     */
    GLController(@Nullable GLCanvas glCanvas, int fps) {
        this.glCanvas = glCanvas;
        glu = new GLU();
        lastRenderTime = System.nanoTime();
        if (fps != 0)
            renderDelayInMicros = 1_000_000L / fps;
        else
            renderDelayInMicros = 1_000_000L / 25;
    }

    /**
     * Задержка рисования
     *
     * @return реальное время задержки
     */
    long delayForRender() {
        long delta = System.nanoTime() - lastRenderTime;
        while (delta < renderDelayInMicros * 1_000) {
            try {
                TimeUnit.MICROSECONDS.sleep(renderDelayInMicros);
            } catch (InterruptedException e) {
                logger.error("error making delay in mainLoop method");
            }
            delta = System.nanoTime() - lastRenderTime;
        }
        lastRenderTime = System.nanoTime();
        return delta;
    }

    /**
     * Получить GLU
     *
     * @return GLU
     */
    public GLU getGLU() {
        return glu;
    }

    /**
     * Получить ссылку на область рисования
     *
     * @return ссылка на область рисования
     */
    public GLCanvas getGLCanvas() {
        return glCanvas;
    }

    /**
     * Получить задержку между срабатываниями тактов главного цикла  в микросекундах
     *
     * @return задержка между срабатываниями тактов главного цикла  в микросекундах
     */
    public long getRenderDelayInMicros() {
        return renderDelayInMicros;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GLController{renderDelayInMicros}"
     */
    @Override
    public String toString() {
        return "GLController{" + renderDelayInMicros + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GLController that = (GLController) o;

        if (lastRenderTime != that.lastRenderTime) return false;
        return renderDelayInMicros == that.renderDelayInMicros;
    }

    @Override
    public int hashCode() {
        int result = (int) (lastRenderTime ^ (lastRenderTime >>> 32));
        result = 31 * result + (int) (renderDelayInMicros ^ (renderDelayInMicros >>> 32));
        return result;
    }
}
