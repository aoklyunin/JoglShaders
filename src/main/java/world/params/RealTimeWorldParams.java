package world.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.aoklyunin.javaGLHelper.CaptionParams;
import com.github.aoklyunin.javaScrollers.params.SimpleScrollerParams;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem3d;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Базовый класс для всех параметров мира реального времени
 */
@JsonPropertyOrder({"min", "max"})
public class RealTimeWorldParams extends StoryWorldParams {
    /**
     * минимальные координаты мира
     */
    @NotNull
    private final Vector3d min;
    /**
     * максимальные координаты мира
     */
    @NotNull
    private final Vector3d max;
    /**
     * путь к описанию ресурсного поля
     */
    @NotNull
    private final String resourceFieldPath;
    /**
     * система координат мира
     */
    @NotNull
    @JsonIgnore
    private final CoordinateSystem3d worldCS;


    /**
     * Конструктор класса для всех параметров мира реального времени
     *
     * @param type                             тип мира
     * @param name                             название мира
     * @param creatureInWorldPlaceInstructions инструкции по размещению существ при старте мира
     * @param captionParamsMap                 словарь параметров отображаемых блоков текста
     * @param maxConsoleLinesCnt               максимальное число строк консоли
     * @param renderConsoleTickCnt             кол-во тактов отображения строки консоли
     * @param initStoryPath                    путь к истории,  которую нужно загрузить при старте мир
     * @param storyScrollerParams              параметры скроллера истории
     * @param recordStory                      флаг, нужно ли записывать историю
     * @param min                              минимальные координаты мира
     * @param max                              максимальные координаты мира
     * @param resourceFieldPath                путь к описанию ресурсного поля
     * @param saveInterval                     раз в сколько тактов надо сохранять мир
     * @param statesListName                   название списка состояний
     */
    @JsonCreator
    public RealTimeWorldParams(
            @NotNull @JsonProperty("type") WorldType type, @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("creatureInWorldPlaceInstructions") List<CreatureInWorldParams> creatureInWorldPlaceInstructions,
            @NotNull @JsonProperty("captionParamsMap") Map<String, CaptionParams> captionParamsMap,
            @JsonProperty("maxConsoleLinesCnt") int maxConsoleLinesCnt,
            @JsonProperty("renderConsoleTickCnt") int renderConsoleTickCnt,
            @NotNull @JsonProperty("initStoryPath") String initStoryPath,
            @NotNull @JsonProperty("storyScrollerParams") SimpleScrollerParams storyScrollerParams,
            @JsonProperty("recordStory") boolean recordStory, @NotNull @JsonProperty("statesListName") String statesListName,
            @JsonProperty("saveInterval") int saveInterval,
            @NotNull @JsonProperty("min") Vector3d min, @NotNull @JsonProperty("max") Vector3d max,
            @NotNull @JsonProperty("resourceFieldPath") String resourceFieldPath
    ) {
        super(
                type, name, creatureInWorldPlaceInstructions, captionParamsMap, maxConsoleLinesCnt, renderConsoleTickCnt,
                initStoryPath, storyScrollerParams, recordStory, statesListName, saveInterval
        );
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
        this.worldCS = new CoordinateSystem3d(min, max);
        this.resourceFieldPath = Objects.requireNonNull(resourceFieldPath);
    }

    /**
     * Конструктор класса для всех параметров мира реального времени
     *
     * @param realTimeWorldParams параметры реального мира
     */
    public RealTimeWorldParams(@NotNull RealTimeWorldParams realTimeWorldParams) {
        super(realTimeWorldParams);
        this.min = realTimeWorldParams.min;
        this.max = realTimeWorldParams.max;
        this.worldCS = new CoordinateSystem3d(realTimeWorldParams.min, realTimeWorldParams.max);
        this.resourceFieldPath = realTimeWorldParams.resourceFieldPath;
    }

    /**
     * Получить минимальные координаты мира
     *
     * @return минимальные координаты мира
     */
    @NotNull
    public Vector3d getMin() {
        return min;
    }

    /**
     * Получить максимальные координаты мира
     *
     * @return максимальные координаты мира
     */
    @NotNull
    public Vector3d getMax() {
        return max;
    }

    /**
     * Получить путь к описанию ресурсного поля
     *
     * @return путь к описанию ресурсного поля
     */
    @NotNull
    public String getResourceFieldPath() {
        return resourceFieldPath;
    }

    /**
     * Получить  система координат мира
     *
     * @return система координат мира
     */
    @NotNull
    @JsonIgnore
    public CoordinateSystem3d getWorldCS() {
        return worldCS;
    }



    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTimeWorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "RealTimeWorldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "min, max, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return min + ", " + max + ", " +  super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RealTimeWorldParams that = (RealTimeWorldParams) o;

        if (!Objects.equals(min, that.min)) return false;
        if (!Objects.equals(max, that.max)) return false;
        return Objects.equals(resourceFieldPath, that.resourceFieldPath);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (resourceFieldPath != null ? resourceFieldPath.hashCode() : 0);
        return result;
    }
}
