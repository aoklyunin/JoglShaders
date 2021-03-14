package offscreen.renderer;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import graphics.Camera;
import graphics.ObjModel3D;
import jMath.aoklyunin.github.com.Transform3d;
import jMath.aoklyunin.github.com.vector.Vector2i;
import offscreen.params.OffscreenRendererParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Класс фонового рисовальщика
 */
public abstract class OffscreenRenderer {
    /**
     * список трансформаций для каждого объекта
     */
    @NotNull
    private final List<Transform3d> objectTransforms;
    /**
     * список объектов
     */
    @NotNull
    protected final List<ObjModel3D> objects;
    /**
     * кол-во объектов
     */
    private final int objectCnt;
    /**
     * Параметры фонового рисовальщика
     */
    private final OffscreenRendererParams offscreenRendererParams;
    /**
     * Размер рисования
     */
    Vector2i renderSize;

    /**
     * Конструктор фонового рисовальзика
     *
     * @param offscreenRendererParams параметры фонового рисовальщика
     */
    public OffscreenRenderer(OffscreenRendererParams offscreenRendererParams) {
        List<ObjModel3D> uniqueModelArr = offscreenRendererParams.getObjectModelParams().stream()
                .map(ObjModel3D::new)
                .collect(toList());

        objects = offscreenRendererParams.getObjectModelIndexes().stream()
                .map(uniqueModelArr::get)
                .collect(toList());

        objectTransforms = offscreenRendererParams.getInitObjectTransforms().stream()
                .map(Transform3d::new)
                .collect(toList());

        objectCnt = offscreenRendererParams.getObjectModelIndexes().size();
        this.offscreenRendererParams = offscreenRendererParams;
    }

    /**
     * Конструктор фонового рисовальзика
     *
     * @param offscreenRenderer фоновый рисовальщик
     */
    public OffscreenRenderer(OffscreenRenderer offscreenRenderer) {
        this.objectTransforms = new ArrayList<>(offscreenRenderer.objectTransforms);
        this.objects = new ArrayList<>(offscreenRenderer.objects);
        this.objectCnt = offscreenRenderer.objectCnt;
        this.offscreenRendererParams = offscreenRenderer.offscreenRendererParams;
    }

    /**
     * Инициализация рисовальщика
     *
     * @param renderSize размер окна
     */
    public void init(Vector2i renderSize) {
        this.renderSize = renderSize;
    }

    /**
     * Рассчёт буфера зрения
     *
     * @param viewPort         Размер окна
     * @param camera           камера
     * @param objectTransforms список трансформаций объектов
     * @return буфер зрения существа
     */
    public abstract short[][][] calculateVisionBuffer(
            @NotNull Vector2i viewPort, @NotNull Camera camera, @NotNull List<Transform3d> objectTransforms
    );

    /**
     * Рисование поля
     *
     * @param gl2              переменная OpenGl  для рисования
     * @param objectTransforms список матриц трансформаций объектов
     */
    public abstract void render(GL2 gl2, @NotNull List<Transform3d> objectTransforms);

    /**
     * Получить кол-во объектов
     *
     * @return кол-во объектов
     */
    public int getObjectCnt() {
        return objectCnt;
    }

    /**
     * Получить параметры фонового рисоавльщика
     *
     * @return параметры фонового рисоавльщика
     */
    public OffscreenRendererParams getOffscreenRendererParams() {
        return offscreenRendererParams;
    }

    /**
     * Получить  список трансформаций объектов
     *
     * @return список трансформаций объектов
     */
    public List<Transform3d> getObjectTransforms() {
        return objectTransforms;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "OffscreenRenderer{getString()}"
     */
    @Override
    public String toString() {
        return "OffscreenRenderer{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "objectCnt"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return objectCnt + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OffscreenRenderer that = (OffscreenRenderer) o;

        if (objectCnt != that.objectCnt) return false;
        if (!Objects.equals(objectTransforms, that.objectTransforms))
            return false;
        if (!Objects.equals(objects, that.objects)) return false;
        if (!Objects.equals(offscreenRendererParams, that.offscreenRendererParams))
            return false;
        return Objects.equals(renderSize, that.renderSize);
    }

    @Override
    public int hashCode() {
        int result = objectTransforms != null ? objectTransforms.hashCode() : 0;
        result = 31 * result + (objects != null ? objects.hashCode() : 0);
        result = 31 * result + objectCnt;
        result = 31 * result + (offscreenRendererParams != null ? offscreenRendererParams.hashCode() : 0);
        result = 31 * result + (renderSize != null ? renderSize.hashCode() : 0);
        return result;
    }
}
