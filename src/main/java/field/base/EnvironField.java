package field.base;

import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem3d;
import math.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Поле воздействия среды, вычисляется в каждой точке пространства
 */
public abstract class EnvironField extends InfluenceField {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(EnvironField.class);

    /**
     * Конструктор поля с окружением
     *
     * @param influenceFieldParams параметры поля воздействия
     * @param path                 путь к описанию поля воздействия
     * @param backGroundColor      цвет фона
     */
    public EnvironField(
            @NotNull InfluenceFieldParams influenceFieldParams, @NotNull String path, @NotNull Vector3d backGroundColor
    ) {
        super(
                Objects.requireNonNull(influenceFieldParams), Objects.requireNonNull(path),
                Objects.requireNonNull(backGroundColor)
        );
    }

    /**
     * Конструктор поля с окружением
     *
     * @param influenceField поле воздействия
     */
    public EnvironField(@NotNull EnvironField influenceField) {
        super(Objects.requireNonNull(influenceField));
    }

    /**
     * Получить значение поля в заданной точке
     *
     * @param pos                 положение
     * @param worldCS             СК мира, в котором это положение задано
     * @param influenceFieldState состояние поля воздействий
     * @return значение поля в точке
     */
    public abstract double getValue(Vector3d pos, CoordinateSystem3d worldCS, InfluenceFieldState influenceFieldState);


    /**
     * Получить значение поля в заданной точке
     *
     * @param pos     положение
     * @param worldCS СК мира, в котором это положение задано
     * @return значение поля в точке
     */
    public double getValue(@NotNull Vector3d pos, @NotNull CoordinateSystem3d worldCS) {
        return getValue(Objects.requireNonNull(pos), Objects.requireNonNull(worldCS), getState());
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "EnvironField{getString()}"
     */
    @Override
    public String toString() {
        return "EnvironField{" + getString() + '}';
    }

}
