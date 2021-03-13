package field.field3D.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import field.base.InfluenceFieldParams;
import offscreen.params.OffscreenRendererParams;

import java.util.Objects;

/**
 * Параметры трёхмерного поля объектов
 */
public class Objects3DFieldParams extends InfluenceFieldParams {
    /**
     * Параметры фонового рисовальщика
     */
    OffscreenRendererParams offscreenRendererParams;

    /**
     * Конструктор класса параметров градиентного поля воздействия
     *
     * @param randomAdd               нужно ли случайное добавление
     * @param offscreenRendererParams параметры фонового рисовальщика
     */
    @JsonCreator
    public Objects3DFieldParams(
            @JsonProperty("randomAdd") boolean randomAdd,
            @NotNull @JsonProperty("offscreenRendererParams") OffscreenRendererParams offscreenRendererParams
    ) {
        super(randomAdd);
        this.offscreenRendererParams = Objects.requireNonNull(offscreenRendererParams);
    }

    /**
     * Конструктор класса параметров градиентного поля воздействия
     *
     * @param objects3DFieldParams параметры поля воздействия
     */
    public Objects3DFieldParams(@NotNull Objects3DFieldParams objects3DFieldParams) {
        super(Objects.requireNonNull(objects3DFieldParams));
        this.offscreenRendererParams = new OffscreenRendererParams(objects3DFieldParams.offscreenRendererParams);
    }

    /**
     * Получить параметры фонового рисовальщика
     *
     * @return параметры фонового рисовальщика
     */
    public OffscreenRendererParams getOffscreenRendererParams() {
        return offscreenRendererParams;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Objects3DFieldParams{getString()}"
     */
    @Override
    public String toString() {
        return "Objects3DFieldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "initObjectTransforms.size(), objectModelIndexes.size(), super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return offscreenRendererParams + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Objects3DFieldParams that = (Objects3DFieldParams) o;

        return Objects.equals(offscreenRendererParams, that.offscreenRendererParams);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (offscreenRendererParams != null ? offscreenRendererParams.hashCode() : 0);
        return result;
    }
}
