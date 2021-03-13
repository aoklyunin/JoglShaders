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
import world.params.RealTimeWorldParams;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Параметры трёхмерного мира реального времени
 */
public class RealTime3DWorldParams extends RealTimeWorldParams {


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
    public RealTime3DWorldParams(
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
                initStoryPath, storyScrollerParams, recordStory, statesListName, saveInterval,
                min, max, resourceFieldPath
        );
    }

    /**
     * Конструктор класса для всех параметров трёхмерного мира реального времени
     *
     * @param worldParams класс параметров мира
     */
    public RealTime3DWorldParams(@NotNull RealTime3DWorldParams worldParams) {
        super(worldParams);
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTime3DWorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "RealTime3DWorldParams{" + getString() + '}';
    }


}
