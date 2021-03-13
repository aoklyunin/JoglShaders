package field.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import field.field3D.state.Food3DFieldState;
import field.field3D.state.Objects3DFieldState;

import java.util.Objects;

/**
 * Класс состояния поля воздействия
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class InfluenceFieldState {
    /**
     * id состояния
     */
    @JsonIgnore
    private int id;

    /**
     * Конструктор класса поля воздействия
     */
    public InfluenceFieldState() {

    }

    /**
     * Получить id состояния поля
     *
     * @return id состояния поля
     */
    public int getId() {
        return id;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "InfluenceFieldState{getString()}"
     */
    @Override
    public String toString() {
        return "InfluenceFieldState{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * ""
     *
     * @return строковое представление объекта
     */

    protected String getString() {
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfluenceFieldState that = (InfluenceFieldState) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    /**
     * Преобразовать к состоянию  поля с 3D едой
     *
     * @return состояние поля с 3D едой
     */
    @JsonIgnore
    public Food3DFieldState getFood3DFieldState() {
        if (this instanceof Food3DFieldState)
            return (Food3DFieldState) this;
        throw new AssertionError();
    }

    /**
     * Преобразовать к состоянию 3D поля
     *
     * @return состояние 3D поля
     */
    @JsonIgnore
    public Objects3DFieldState getObjects3DFieldState() {
        if (this instanceof Objects3DFieldState)
            return (Objects3DFieldState) this;
        throw new AssertionError();
    }

}
