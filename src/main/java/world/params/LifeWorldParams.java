package world.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import math.Vector2d;
import math.Vector3d;
import misc.CaptionParams;
import scrollers.params.SimpleScrollerParams;

import java.util.List;
import java.util.Map;

/**
 * Базовый класс для всех параметров мира реального времени
 */
public class LifeWorldParams extends RealTimeWorldParams {
    /**
     * смещение существа при ручном контроле
     */
    private final double manualCreatureControlStep;
    /**
     * флаг, нужно ли перезаписывать историю в базе
     */
    private boolean flgOverrideStory;


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
     * @param manualCreatureControlStep        смещение существа при ручном контроле
     * @param saveInterval                     раз в сколько тактов надо сохранять мир
     * @param statesListName                   название списка состояний
     */
    @JsonCreator
    public LifeWorldParams(
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
            @NotNull @JsonProperty("resourceFieldPath") String resourceFieldPath,
            @JsonProperty("manualCreatureControlStep") double manualCreatureControlStep
    ) {
        super(
                type, name, creatureInWorldPlaceInstructions, captionParamsMap, maxConsoleLinesCnt, renderConsoleTickCnt,
                initStoryPath, storyScrollerParams, recordStory, statesListName, saveInterval,
                min, max, resourceFieldPath
        );
        this.manualCreatureControlStep = manualCreatureControlStep;
    }

    /**
     * Конструктор класса для всех параметров мира реального времени
     *
     * @param lifeWorldParams параметры реального мира
     */
    public LifeWorldParams(@NotNull LifeWorldParams lifeWorldParams) {
        super(lifeWorldParams);
        this.manualCreatureControlStep = lifeWorldParams.manualCreatureControlStep;
    }

    /**
     * Получить смещение существа при ручном контроле
     *
     * @return смещение существа при ручном контроле
     */
    public double getManualCreatureControlStep() {
        return manualCreatureControlStep;
    }


    /**
     * Получить  флаг, нужно ли перезаписывать историю в базе
     *
     * @return флаг, нужно ли перезаписывать историю в базе
     */
    public boolean isFlgOverrideStory() {
        return flgOverrideStory;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "LifeWorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "LifeWorldParams{" + getString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LifeWorldParams that = (LifeWorldParams) o;

        if (Double.compare(that.manualCreatureControlStep, manualCreatureControlStep) != 0) return false;
        return flgOverrideStory == that.flgOverrideStory;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;

        temp = Double.doubleToLongBits(manualCreatureControlStep);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (flgOverrideStory ? 1 : 0);
        return result;
    }
}
