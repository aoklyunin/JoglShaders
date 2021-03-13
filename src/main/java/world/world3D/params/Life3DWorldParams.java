package world.world3D.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import math.Vector2d;
import math.Vector3d;
import misc.CaptionParams;
import scrollers.params.RangeScrollerParams;
import scrollers.params.SimpleScrollerParams;
import world.params.CreatureInWorldParams;
import world.world3D.RealTimeWorld3DInfo;

import java.util.List;
import java.util.Map;

/**
 * Параметры трёхмерного мира с жизнью
 */
public class Life3DWorldParams extends RealTime3DWorldParams {
    /**
     * флаг, нужно ли перезаписывать историю в базе
     */
    private final boolean flgOverrideStory;

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
     * @param flgOverrideStory                 флаг, нужно ли перезаписывать историю в базе
     */
    @JsonCreator
    public Life3DWorldParams(
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
            @JsonProperty("flgOverrideStory") boolean flgOverrideStory
    ) {
        super(
                type, name, creatureInWorldPlaceInstructions, captionParamsMap, maxConsoleLinesCnt, renderConsoleTickCnt,
                initStoryPath, storyScrollerParams, recordStory, statesListName, saveInterval,
                min, max, resourceFieldPath
        );
        this.flgOverrideStory = flgOverrideStory;
    }

    /**
     * Конструктор класса для всех параметров мира реального времени
     *
     * @param worldParams параметры мира
     */
    public Life3DWorldParams(@NotNull Life3DWorldParams worldParams) {
        super(worldParams);
        this.flgOverrideStory = worldParams.flgOverrideStory;
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

        Life3DWorldParams that = (Life3DWorldParams) o;

        return flgOverrideStory != that.flgOverrideStory;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (flgOverrideStory ? 1 : 0);
        return result;
    }
}
