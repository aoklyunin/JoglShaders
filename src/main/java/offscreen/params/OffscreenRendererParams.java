package offscreen.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import graphics.ObjModel3DParams;
import jMath.aoklyunin.github.com.Transform3d;

import java.util.List;
import java.util.Objects;

/**
 * Класс параметров поля воздействия
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class OffscreenRendererParams {
    public enum OffscreenType {
        SIMPLE,
        GL
    }

    OffscreenType type;
    /**
     * список параметров моделей
     */
    @NotNull
    private final List<ObjModel3DParams> objectModelParams;
    /**
     * список индексов моделей для каждого объекта
     */
    @NotNull
    private final List<Integer> objectModelIndexes;
    /**
     * список трансформаций для каждого объекта
     */
    @NotNull
    private final List<Transform3d> initObjectTransforms;

    /**
     * Конструктор класса параметров градиентного поля воздействия
     *
     * @param objectModelParams    список параметров моделей
     * @param objectModelIndexes   список индексов моделей для каждого объекта
     * @param initObjectTransforms список трансформаций для каждого объекта
     */
    @JsonCreator
    public OffscreenRendererParams(
            @NotNull @JsonProperty("type") OffscreenType type,
            @NotNull @JsonProperty("objectModelParams") List<ObjModel3DParams> objectModelParams,
            @NotNull @JsonProperty("objectModelIndexes") List<Integer> objectModelIndexes,
            @NotNull @JsonProperty("initObjectTransforms") List<Transform3d> initObjectTransforms
    ) {
        this.type = Objects.requireNonNull(type);
        this.objectModelParams = Objects.requireNonNull(objectModelParams);
        this.objectModelIndexes = Objects.requireNonNull(objectModelIndexes);
        this.initObjectTransforms = Objects.requireNonNull(initObjectTransforms);
    }

    /**
     * Конструктор Параметры фонового рисовальщика
     *
     * @param params параметры фонового рисовальщика
     */
    public OffscreenRendererParams(OffscreenRendererParams params) {
        this.type = params.type;
        this.objectModelParams = params.objectModelParams;
        this.objectModelIndexes = params.objectModelIndexes;
        this.initObjectTransforms = params.initObjectTransforms;
    }

    /**
     * Получить список трансформаций для каждого объекта
     *
     * @return список трансформаций для каждого объекта
     */
    @NotNull
    public List<Transform3d> getInitObjectTransforms() {
        return initObjectTransforms;
    }

    /**
     * Получить список параметров моделей
     *
     * @return список параметров моделей
     */
    @NotNull
    public List<ObjModel3DParams> getObjectModelParams() {
        return objectModelParams;
    }

    /**
     * Получить список индексов моделей для каждого объекта
     *
     * @return список индексов моделей для каждого объекта
     */
    @NotNull
    public List<Integer> getObjectModelIndexes() {
        return objectModelIndexes;
    }

    /**
     * Получить тип фонового рисовальщика
     *
     * @return тип фонового рисовальщика
     */
    public OffscreenType getType() {
        return type;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "OffscreenRendererParams{getString()}"
     */
    @Override
    public String toString() {
        return "OffscreenRendererParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "initObjectTransforms.size(), objectModelIndexes.size()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return initObjectTransforms.size() + ", " + objectModelIndexes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OffscreenRendererParams that = (OffscreenRendererParams) o;

        if (type != that.type) return false;
        if (!Objects.equals(objectModelParams, that.objectModelParams))
            return false;
        if (!Objects.equals(objectModelIndexes, that.objectModelIndexes))
            return false;
        return Objects.equals(initObjectTransforms, that.initObjectTransforms);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (objectModelParams != null ? objectModelParams.hashCode() : 0);
        result = 31 * result + (objectModelIndexes != null ? objectModelIndexes.hashCode() : 0);
        result = 31 * result + (initObjectTransforms != null ? initObjectTransforms.hashCode() : 0);
        return result;
    }
}
