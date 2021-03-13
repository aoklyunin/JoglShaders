package offscreen.renderer;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import graphics.Camera;
import math.Transform3d;
import math.Vector2i;
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
        return new short[][][]{};
    }

    /**
     * Рисование поля
     *
     * @param gl2              переменная OpenGl  для рисования
     * @param objectTransforms список матриц трансформаций объектов
     */
    @Override
    public void render(GL2 gl2, List<Transform3d> objectTransforms) {

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
