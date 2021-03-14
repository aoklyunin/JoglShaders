package offscreen.renderer;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import graphics.Camera;
import jMath.aoklyunin.github.com.Transform3d;
import jMath.aoklyunin.github.com.vector.Vector2i;
import offscreen.params.OffscreenRendererParams;

import java.util.List;


/**
 * Класс простого фонового рисовальщика
 */
public class SimpleOffscreenRenderer extends OffscreenRenderer {
    /**
     * Конструктор фонового рисовальзика
     *
     * @param offscreenRendererParams параметры фонового рисовальщика
     */
    public SimpleOffscreenRenderer(OffscreenRendererParams offscreenRendererParams) {
        super(offscreenRendererParams);
    }

    /**
     * Конструктор фонового рисовальзика
     *
     * @param simpleOffscreenRenderer фоновый рисовальщик
     */
    public SimpleOffscreenRenderer(SimpleOffscreenRenderer simpleOffscreenRenderer) {
        super(simpleOffscreenRenderer);
    }

    /**
     * Инициализация рисовальщика
     *
     * @param renderSize размер окна
     */
    public void init(Vector2i renderSize) {
        super.init(renderSize);
    }

    /**
     * Рассчёт буфера зрения
     *
     * @param viewPort         Размер окна
     * @param camera           камера
     * @param objectTransforms список трансформаций объектов
     * @return буфер зрения существа
     */
    @Override
    public short[][][] calculateVisionBuffer(
            @NotNull Vector2i viewPort, @NotNull Camera camera, @NotNull List<Transform3d> objectTransforms
    ) {
        short[][][] res = new short[viewPort.x][viewPort.y][3];

        for (int i = 0; i < getObjectCnt(); i++) {
            int centerX = (int) ((objectTransforms.get(i).getPosition().y + 1.4) / 2.4 * viewPort.x);
            int centerY = viewPort.y / 2;
            int rad = 4;
            for (int posY = centerY - rad; posY <= centerY + rad; posY++) {
                double angle = Math.asin((double) (posY - centerY) / rad);
                int rX = (int) (rad * Math.cos(angle));
                for (int posX = centerX - rX; posX <= centerX + rX; posX++) {
                    if (posX < viewPort.x && posX >= 0) {
                        res[posX][posY][0] = 255;
                        res[posX][posY][1] = 255;
                        res[posX][posY][2] = 255;
                    }
                }
            }
        }

        return res;
    }

    /**
     * Рисование поля
     *
     * @param gl2              переменная OpenGl  для рисования
     * @param objectTransforms список матриц трансформаций объектов
     */
    @Override
    public void render(GL2 gl2, List<Transform3d> objectTransforms) {
        for (int i = 0; i < getObjectCnt(); i++) {
            gl2.glPushMatrix();
            objectTransforms.get(i).apply(gl2);
            objects.get(i).render(gl2);
            gl2.glPopMatrix();
        }
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "SimpleOffscreenRenderer{getString()}"
     */
    @Override
    public String toString() {
        return "SimpleOffscreenRenderer{" + getString() + '}';
    }

}
