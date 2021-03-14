package field.field3D.state;

import com.sun.istack.NotNull;
import field.base.InfluenceFieldState;
import field.field3D.base.Objects3DField;
import jMath.aoklyunin.github.com.Transform3d;

import java.util.List;
import java.util.Objects;

/**
 * Класс состояния поля объектов 3D
 */
public abstract class Objects3DFieldState extends InfluenceFieldState {

    /**
     * Конструктор класса трёъмерного поля объектов
     *
     * @param objectTransforms трансформации объектов мира
     */
    public Objects3DFieldState(@NotNull List<Transform3d> objectTransforms) {
        setObjectTransforms(Objects.requireNonNull(objectTransforms));
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     *
     * @param objects3DFieldState сосотояние градиентного поля воздействия
     */
    Objects3DFieldState(@NotNull Objects3DFieldState objects3DFieldState) {
        setObjectTransforms(objects3DFieldState.getObjectTransforms());
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     *
     * @param objects3DField градиентное поле воздействия
     */
    Objects3DFieldState(@NotNull Objects3DField objects3DField) {
        setObjectTransforms(objects3DField.getOffscreenRenderer().getObjectTransforms());
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     */
    Objects3DFieldState() {

    }

    /**
     * Получить трансформации объектов
     *
     * @return трансформации объектов
     */
    @NotNull
    public abstract List<Transform3d> getObjectTransforms();

    /**
     * Задать трансформации объектов
     *
     * @param objectTransforms трансформации объектов
     */
    public abstract void setObjectTransforms(@NotNull List<Transform3d> objectTransforms);

    /**
     * Строковое представление объекта вида:
     *
     * @return "Objects3DFieldState{getString()}"
     */
    @Override
    public String toString() {
        return "Objects3DFieldState{" + getString() + "}";
    }

    /**
     * Проверка двух состояний поля на равенство
     *
     * @param o объект вравнения
     * @return равны ли два состояния
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objects3DFieldState that = (Objects3DFieldState) o;
        if (getObjectTransforms().size() != that.getObjectTransforms().size())
            return false;
        for (int i = 0; i < getObjectTransforms().size(); i++) {
            if (!getObjectTransforms().get(i).equals(that.getObjectTransforms().get(i)))
                return false;
        }
        return true;
    }

    /**
     * Хэш-функция состояний поля
     *
     * @return Хэш состояний поля
     */
    @Override
    public int hashCode() {
        return Objects.hash(getObjectTransforms());
    }
}
