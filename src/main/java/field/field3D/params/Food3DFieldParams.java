package field.field3D.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.aoklyunin.jCollections.combiners.ranges.primitive.DoubleRange;
import com.github.aoklyunin.jCollections.combiners.ranges.vector.Vector3dRange;
import com.github.aoklyunin.javaGLHelper.CaptionParams;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem3d;
import offscreen.params.OffscreenRendererParams;

import java.util.Map;
import java.util.Objects;

/**
 * Параметры трёхмерного поля еды
 */
public class Food3DFieldParams extends Objects3DFieldParams {
    /**
     * скорость движения еды
     */
    private final double initFoodMovingSpeed;
    /**
     * диапазон перемещений еды
     */
    @NotNull
    private final CoordinateSystem3d initFoodMovingCS;
    /**
     * вероятность добавления значения
     */
    private final double addValueProbability;
    /**
     * Параметры прогресс-бара диапазона движения шара
     */
    @NotNull
    private final SimpleScrollerParams foodMovingRangeScrollerParams;
    /**
     * Параметры прогресс-бара скорости движения шара
     */
    @NotNull
    private final SimpleScrollerParams foodMovingSpeedScrollerParams;
    /**
     * словарь параметров отображаемых блоков текста
     */
    @NotNull
    private final Map<String, CaptionParams> captionParamsMap;
    /**
     * Диапазон ищменений даипазона движения еды
     */
    @NotNull
    private final Vector3dRange foodMovingRangeChangeRange;
    /**
     * Диапазон изменений даипазона скорости еды
     */
    @NotNull
    private final DoubleRange foodMovingSpeedChangeRange;

    /**
     * Конструктор класса параметров градиентного поля воздействия
     *
     * @param randomAdd                     нужно ли случайное добавление
     * @param offscreenRendererParams       параметры фонового рисовальщика
     * @param addValueProbability           вероятность добавления еды
     * @param initFoodMovingSpeed           скорость движения еды
     * @param initFoodMovingCS              диапазон перемещений еды
     * @param foodMovingRangeScrollerParams параметры прогресс-бара диапазона движения шара
     * @param foodMovingSpeedScrollerParams параметры прогресс-бара скорости движения шара
     * @param captionParamsMap              словарь параметров отображаемых блоков текста
     * @param foodMovingRangeChangeRange    диапазон ищменений даипазона движения еды
     * @param foodMovingSpeedChangeRange    диапазон изменений даипазона скорости еды
     */
    @JsonCreator
    public Food3DFieldParams(
            @JsonProperty("randomAdd") boolean randomAdd,
            @NotNull @JsonProperty("offscreenRendererParams") OffscreenRendererParams offscreenRendererParams,
            @JsonProperty("initFoodMovingSpeed") double initFoodMovingSpeed,
            @NotNull @JsonProperty("initFoodMovingCS") CoordinateSystem3d initFoodMovingCS,
            @JsonProperty("addValueProbability") double addValueProbability,
            @NotNull @JsonProperty("foodMovingRangeScrollerParams") SimpleScrollerParams foodMovingRangeScrollerParams,
            @NotNull @JsonProperty("foodMovingSpeedScrollerParams") SimpleScrollerParams foodMovingSpeedScrollerParams,
            @NotNull @JsonProperty("captionParamsMap") Map<String, CaptionParams> captionParamsMap,
            @NotNull @JsonProperty("foodMovingRangeChangeRange") Vector3dRange foodMovingRangeChangeRange,
            @NotNull @JsonProperty("foodMovingSpeedChangeRange") DoubleRange foodMovingSpeedChangeRange
    ) {
        super(randomAdd, offscreenRendererParams);
        this.initFoodMovingSpeed = initFoodMovingSpeed;
        this.initFoodMovingCS = Objects.requireNonNull(initFoodMovingCS);
        this.addValueProbability = addValueProbability;
        this.foodMovingRangeScrollerParams = Objects.requireNonNull(foodMovingRangeScrollerParams);
        this.foodMovingSpeedScrollerParams = Objects.requireNonNull(foodMovingSpeedScrollerParams);
        this.captionParamsMap = Objects.requireNonNull(captionParamsMap);
        this.foodMovingRangeChangeRange = Objects.requireNonNull(foodMovingRangeChangeRange);
        this.foodMovingSpeedChangeRange = Objects.requireNonNull(foodMovingSpeedChangeRange);
    }

    /**
     * Конструктор класса параметров градиентного поля воздействия
     *
     * @param influenceFieldParams параметры поля воздействия
     */
    public Food3DFieldParams(@NotNull Food3DFieldParams influenceFieldParams) {
        super(Objects.requireNonNull(influenceFieldParams));
        this.initFoodMovingSpeed = influenceFieldParams.initFoodMovingSpeed;
        this.initFoodMovingCS = influenceFieldParams.initFoodMovingCS;
        this.addValueProbability = influenceFieldParams.addValueProbability;
        this.foodMovingRangeScrollerParams = influenceFieldParams.foodMovingRangeScrollerParams;
        this.foodMovingSpeedScrollerParams = influenceFieldParams.foodMovingSpeedScrollerParams;
        this.captionParamsMap = influenceFieldParams.captionParamsMap;
        this.foodMovingRangeChangeRange = influenceFieldParams.foodMovingRangeChangeRange;
        this.foodMovingSpeedChangeRange = influenceFieldParams.foodMovingSpeedChangeRange;
    }

    /**
     * Получить скорость движения еды
     *
     * @return скорость движения еды
     */
    public double getInitFoodMovingSpeed() {
        return initFoodMovingSpeed;
    }

    /**
     * Получить диапазон перемещений еды
     *
     * @return диапазон перемещений еды
     */
    @NotNull
    public CoordinateSystem3d getInitFoodMovingCS() {
        return initFoodMovingCS;
    }

    /**
     * Получить вероятность добавления значения
     *
     * @return вероятность добавления значения
     */
    public double getAddValueProbability() {
        return addValueProbability;
    }

    /**
     * Получить  Параметры прогресс-бара диапазона движения шара
     *
     * @return Параметры прогресс-бара диапазона движения шара
     */
    @NotNull
    public SimpleScrollerParams getFoodMovingRangeScrollerParams() {
        return foodMovingRangeScrollerParams;
    }

    /**
     * Получить Параметры прогресс-бара скорости движения шара
     *
     * @return Параметры прогресс-бара скорости движения шара
     */
    @NotNull
    public SimpleScrollerParams getFoodMovingSpeedScrollerParams() {
        return foodMovingSpeedScrollerParams;
    }

    /**
     * Получить словарь параметров отображаемых блоков текста
     *
     * @return словарь параметров отображаемых блоков текста
     */
    @NotNull
    public Map<String, CaptionParams> getCaptionParamsMap() {
        return captionParamsMap;
    }

    /**
     * Получить  словарь параметров отображаемых блоков текста
     *
     * @return словарь параметров отображаемых блоков текста
     */
    @NotNull
    public Vector3dRange getFoodMovingRangeChangeRange() {
        return foodMovingRangeChangeRange;
    }

    /**
     * Получить  Диапазон изменений даипазона скорости еды
     *
     * @return Диапазон изменений даипазона скорости еды
     */
    @NotNull
    public DoubleRange getFoodMovingSpeedChangeRange() {
        return foodMovingSpeedChangeRange;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Food3DFieldParams{getString()}"
     */
    @Override
    public String toString() {
        return "Food3DFieldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "initFoodMovingSpeed, initFoodMovingCS, addValueProbability, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return String.format("%.3f", initFoodMovingSpeed) + ", " + initFoodMovingCS + ", " +
                String.format("%.3f", addValueProbability) + ", " + super.getString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Food3DFieldParams that = (Food3DFieldParams) o;

        if (Double.compare(that.initFoodMovingSpeed, initFoodMovingSpeed) != 0) return false;
        if (Double.compare(that.addValueProbability, addValueProbability) != 0) return false;
        if (!Objects.equals(initFoodMovingCS, that.initFoodMovingCS))
            return false;
        if (!Objects.equals(foodMovingRangeScrollerParams, that.foodMovingRangeScrollerParams))
            return false;
        if (!Objects.equals(foodMovingSpeedScrollerParams, that.foodMovingSpeedScrollerParams))
            return false;
        if (!Objects.equals(captionParamsMap, that.captionParamsMap))
            return false;
        if (!Objects.equals(foodMovingRangeChangeRange, that.foodMovingRangeChangeRange))
            return false;
        return Objects.equals(foodMovingSpeedChangeRange, that.foodMovingSpeedChangeRange);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(initFoodMovingSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (initFoodMovingCS != null ? initFoodMovingCS.hashCode() : 0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(addValueProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (foodMovingRangeScrollerParams != null ? foodMovingRangeScrollerParams.hashCode() : 0);
        result = 31 * result + (foodMovingSpeedScrollerParams != null ? foodMovingSpeedScrollerParams.hashCode() : 0);
        result = 31 * result + (captionParamsMap != null ? captionParamsMap.hashCode() : 0);
        result = 31 * result + (foodMovingRangeChangeRange != null ? foodMovingRangeChangeRange.hashCode() : 0);
        result = 31 * result + (foodMovingSpeedChangeRange != null ? foodMovingSpeedChangeRange.hashCode() : 0);
        return result;
    }
}
