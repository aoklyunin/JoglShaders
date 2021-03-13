package world.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import misc.CaptionParams;
import scrollers.params.SimpleScrollerParams;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Базовый класс для всех параметров мира с историей
 */
public class StoryWorldParams extends WorldParams {
    /**
     * Параметры скроллера истории
     */
    @NotNull
    private final SimpleScrollerParams storyScrollerParams;
    /**
     * Флаг, нужно ли записывать историю
     */
    private final boolean recordStory;
    /**
     * название списка состояний
     */
    @NotNull
    private final String statesListName;
    /**
     * Раз в сколько тактов надо сохранять мир
     */
    private final int saveInterval;
    /**
     * путь к истории,  которую нужно загрузить при старте мира
     */
    @NotNull
    private final String initStoryPath;

    /**
     * Конструктор класса для всех параметров мира с историей
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
     * @param statesListName                   название списка состояний
     * @param saveInterval                     раз в сколько тактов надо сохранять мир
     */
    @JsonCreator
    public StoryWorldParams(
            @NotNull @JsonProperty("type") WorldType type, @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("creatureInWorldPlaceInstructions") List<CreatureInWorldParams> creatureInWorldPlaceInstructions,
            @NotNull @JsonProperty("captionParamsMap") Map<String, CaptionParams> captionParamsMap,
            @JsonProperty("maxConsoleLinesCnt") int maxConsoleLinesCnt,
            @JsonProperty("renderConsoleTickCnt") int renderConsoleTickCnt,
            @NotNull @JsonProperty("initStoryPath") String initStoryPath,
            @NotNull @JsonProperty("storyScrollerParams") SimpleScrollerParams storyScrollerParams,
            @JsonProperty("recordStory") boolean recordStory,
            @NotNull @JsonProperty("statesListName") String statesListName,
            @JsonProperty("saveInterval") int saveInterval
    ) {
        super(
                type, name, creatureInWorldPlaceInstructions, captionParamsMap,
                maxConsoleLinesCnt, renderConsoleTickCnt
        );
        this.initStoryPath = Objects.requireNonNull(initStoryPath);
        this.storyScrollerParams = new SimpleScrollerParams(storyScrollerParams);
        this.recordStory = recordStory;
        this.statesListName = Objects.requireNonNull(statesListName);
        this.saveInterval = saveInterval;
    }

    /**
     * Конструктор класса для всех параметров мира с историей
     *
     * @param worldParams параметры мира с историей
     */
    public StoryWorldParams(@NotNull StoryWorldParams worldParams) {
        super(worldParams);
        this.storyScrollerParams = new SimpleScrollerParams(worldParams.storyScrollerParams);
        this.recordStory = worldParams.recordStory;
        this.statesListName = worldParams.statesListName;
        this.saveInterval = worldParams.saveInterval;
        this.initStoryPath = worldParams.initStoryPath;
    }

    /**
     * Получить  путь к истории,  которую нужно загрузить при старте мира
     *
     * @return путь к истории,  которую нужно загрузить при старте мира
     */
    public String getInitStoryPath() {
        return initStoryPath;
    }

    /**
     * Получить Параметры скроллера истории
     *
     * @return Параметры скроллера истории
     */
    @NotNull
    public SimpleScrollerParams getStoryScrollerParams() {
        return storyScrollerParams;
    }

    /**
     * Получить  Флаг, нужно ли записывать историю
     *
     * @return Флаг, нужно ли записывать историю
     */
    public boolean isRecordStory() {
        return recordStory;
    }

    /**
     * Получить название списка состояний
     *
     * @return название списка состояний
     */
    public String getStatesListName() {
        return statesListName;
    }

    /**
     * Получить Раз в сколько тактов надо сохранять мир
     *
     * @return Раз в сколько тактов надо сохранять мир
     */
    public int getSaveInterval() {
        return saveInterval;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "StoryWorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "StoryWorldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "'statesListName', saveInterval, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + statesListName + '\'' + ", " + saveInterval + ", " +
                super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StoryWorldParams that = (StoryWorldParams) o;

        if (recordStory != that.recordStory) return false;
        if (saveInterval != that.saveInterval) return false;
        if (!Objects.equals(storyScrollerParams, that.storyScrollerParams))
            return false;
        return Objects.equals(statesListName, that.statesListName);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (storyScrollerParams != null ? storyScrollerParams.hashCode() : 0);
        result = 31 * result + (recordStory ? 1 : 0);
        result = 31 * result + (statesListName != null ? statesListName.hashCode() : 0);
        result = 31 * result + saveInterval;
        return result;
    }
}
