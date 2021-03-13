package misc.combiner.ranges.primitive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Integer диапазон
 */
public class IntRange extends PrimitiveRange {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(IntRange.class);

    /**
     * Конструктор диапазона примитивного типа
     *
     * @param min            минимальное значение
     * @param max            максимальное значение
     * @param stepCnt        кол-во шагов диапазона
     * @param name           название диапазона
     * @param enabled        флаг, разрешено ли изменение значения интервала
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    @JsonCreator
    public IntRange(
            @NotNull @JsonProperty("min") Integer min, @NotNull @JsonProperty("max") Integer max,
            @Nullable @JsonProperty("stepCnt") Integer stepCnt,
            @Nullable @JsonProperty("name") String name, @Nullable @JsonProperty("enabled") Boolean enabled,
            @Nullable @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(
                Objects.requireNonNull(min), Objects.requireNonNull(max),
                Objects.requireNonNullElse(stepCnt, max - min), name, enabled, canRepeatValue
        );
        this.size = max - min;
        this.step = (int) this.size / this.stepCnt;
        if (min >= max)
            logger.error(this + " min>=max");
        if (this.stepCnt > (int) this.size)
            logger.error(this + " stepCnt>=size: step is zero");
        setCurrentValue(min);
    }

    /**
     * Конструктор символьного диапазона
     *
     * @param range интервал
     */
    public IntRange(@NotNull IntRange range) {
        super(Objects.requireNonNull(range));
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "IntRange{" + getString() + "}";
    }
}
