package field.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;

import java.util.Objects;

/**
 * Класс параметров поля воздействия
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class InfluenceFieldParams {

    /**
     * нужно ли случайное добавление
     */
    private boolean randomAdd;

    /**
     * Конструктор параметров поля воздействия
     *
     * @param randomAdd нужно ли случайное добавление
     */
    @JsonCreator
    protected InfluenceFieldParams(@JsonProperty("randomAdd") boolean randomAdd) {
        this.randomAdd = randomAdd;
    }

    /**
     * Конструктор параметров поля воздействия
     *
     * @param influenceFieldParams параметры поля воздействия, на основе которых строится новый объект параметров
     */
    public InfluenceFieldParams(@NotNull InfluenceFieldParams influenceFieldParams) {
        this(Objects.requireNonNull(influenceFieldParams).randomAdd);
    }

    /**
     * Получить флаг, нужно ли случайное добавление
     *
     * @return флаг, нужно ли случайное добавление
     */
    public boolean isRandomAdd() {
        return randomAdd;
    }

    /**
     * Переключить флаг, нужно ли случайное добавление
     */
    public void switchRandomAdd() {
        randomAdd = !randomAdd;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "InfluenceFieldParams{getString()}"
     */
    @Override
    public String toString() {
        return "InfluenceFieldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "randomAdd"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return randomAdd + "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfluenceFieldParams that = (InfluenceFieldParams) o;
        return randomAdd == that.randomAdd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(randomAdd);
    }



}
