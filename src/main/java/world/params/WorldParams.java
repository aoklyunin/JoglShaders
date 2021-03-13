package world.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import misc.CaptionParams;
import world.world3D.params.RealTime3DWorldParams;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Базовый класс для всех параметров мира
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class WorldParams {
    /**
     * Тип мира
     */
    public enum WorldType {
        /**
         * тип мира с едой 3D
         */
        FOOD_3D_WORLD,
    }

    /**
     * тип мира
     */
    @NotNull
    private final WorldType type;
    /**
     * название мира
     */
    @NotNull
    private final String name;
    /**
     * инструкции по размещению существ при старте мира
     */
    @NotNull
    private final List<CreatureInWorldParams> creatureInWorldPlaceInstructions;
    /**
     * словарь параметров отображаемых блоков текста
     */
    @NotNull
    private final Map<String, CaptionParams> captionParamsMap;
    /**
     * максимальное число строк консоли
     */
    private final int maxConsoleLinesCnt;
    /**
     * кол-во тактов отображения строки консоли
     */
    private final int renderConsoleTickCnt;

    /**
     * Конструктор параметров мира
     *
     * @param type                             тип мира
     * @param name                             название мира
     * @param creatureInWorldPlaceInstructions инструкции по размещению существ при старте мира
     * @param captionParamsMap                 словарь параметров отображаемых блоков текста
     * @param maxConsoleLinesCnt               максимальное число строк консоли
     * @param renderConsoleTickCnt             кол-во тактов отображения строки консоли
     */
    @JsonCreator
    public WorldParams(
            @NotNull @JsonProperty("type") WorldType type, @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("creatureInWorldPlaceInstructions") List<CreatureInWorldParams> creatureInWorldPlaceInstructions,
            @NotNull @JsonProperty("captionParamsMap") Map<String, CaptionParams> captionParamsMap,
            @JsonProperty("maxConsoleLinesCnt") int maxConsoleLinesCnt,
            @JsonProperty("renderConsoleTickCnt") int renderConsoleTickCnt
    ) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.creatureInWorldPlaceInstructions = creatureInWorldPlaceInstructions;
        this.captionParamsMap = Objects.requireNonNull(captionParamsMap);
        this.maxConsoleLinesCnt = maxConsoleLinesCnt;
        this.renderConsoleTickCnt = renderConsoleTickCnt;
    }

    /**
     * Конструктор параметров мира
     *
     * @param worldParams новые параметры мира
     */
    public WorldParams(WorldParams worldParams) {
        this(
                worldParams.type, worldParams.name, worldParams.creatureInWorldPlaceInstructions,
                worldParams.captionParamsMap, worldParams.maxConsoleLinesCnt, worldParams.renderConsoleTickCnt
        );
    }

    /**
     * Заменить существо в инструкциях построения
     *
     * @param selectedCreatureId id заменяемого существа
     * @param path               путь к новому существу
     */
    public void changeCreature(int selectedCreatureId, String path) {
        for (CreatureInWorldParams creatureInWorldParams : creatureInWorldPlaceInstructions)
            if (creatureInWorldParams.getId() == selectedCreatureId)
                creatureInWorldParams.setPath(path);
    }

    /**
     * Получить  тип мира
     *
     * @return тип мира
     */
    public WorldType getType() {
        return type;
    }

    /**
     * Получить название мира
     *
     * @return название мира
     */
    public String getName() {
        return name;
    }

    /**
     * Получить инструкции по размещению существ при старте мира
     *
     * @return инструкции по размещению существ при старте мира
     */
    public List<CreatureInWorldParams> getCreatureInWorldPlaceInstructions() {
        return creatureInWorldPlaceInstructions;
    }

    /**
     * Получить словарь параметров отображаемых блоков текста
     *
     * @return словарь параметров отображаемых блоков текста
     */
    public Map<String, CaptionParams> getCaptionParamsMap() {
        return captionParamsMap;
    }



    /**
     * Получить максимальное число строк консоли
     *
     * @return максимальное число строк консоли
     */
    public int getMaxConsoleLinesCnt() {
        return maxConsoleLinesCnt;
    }

    /**
     * Получить кол-во тактов отображения строки консоли
     *
     * @return кол-во тактов отображения строки консоли
     */
    public int getRenderConsoleTickCnt() {
        return renderConsoleTickCnt;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "WorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "WorldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "type, name"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + type + '\'' + ", " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorldParams that = (WorldParams) o;

        if (maxConsoleLinesCnt != that.maxConsoleLinesCnt) return false;
        if (renderConsoleTickCnt != that.renderConsoleTickCnt) return false;
        if (!Objects.equals(type, that.type)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(creatureInWorldPlaceInstructions, that.creatureInWorldPlaceInstructions))
            return false;
        return !Objects.equals(captionParamsMap, that.captionParamsMap);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (creatureInWorldPlaceInstructions != null ? creatureInWorldPlaceInstructions.hashCode() : 0);
        result = 31 * result + (captionParamsMap != null ? captionParamsMap.hashCode() : 0);
        result = 31 * result + maxConsoleLinesCnt;
        result = 31 * result + renderConsoleTickCnt;
        return result;
    }


    /**
     * Преобразовать к параметрам контроллера мира с историей
     *
     * @return параметры контроллера мира с историей
     */
    @JsonIgnore
    public StoryWorldParams getStoryWorldParams() {
        if (this instanceof StoryWorldParams)
            return (StoryWorldParams) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к параметрам контроллера 3D мира реального времени
     *
     * @return параметры контроллера 3D мира реального времени
     */
    @JsonIgnore
    public RealTime3DWorldParams getRealTime3DWorldParams() {
        if (this instanceof RealTime3DWorldParams)
            return (RealTime3DWorldParams) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к параметрам контроллера мира реального времени
     *
     * @return параметры контроллера мира реального времени
     */
    @JsonIgnore
    public RealTimeWorldParams getRealTimeWorldParams() {
        if (this instanceof RealTimeWorldParams)
            return (RealTimeWorldParams) this;
        throw new ClassCastException();
    }

}
