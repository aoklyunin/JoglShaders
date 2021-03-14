package worldController.realTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import worldController.base.WorldControllerParams;

/**
 * Параметры контроллера мира реального времени
 */
public class RealTimeWorldControllerParams extends WorldControllerParams {

    /**
     * коэффициент преобразования перемещения мыши в угол поворота существа
     */
    private final double creatureRotationCoeff;

    /**
     * Конструктор класса параметров контроллера
     *
     * @param realTimeWorldControllerParams параметры контроллера
     */
    public RealTimeWorldControllerParams(@NotNull RealTimeWorldControllerParams realTimeWorldControllerParams) {
        super(realTimeWorldControllerParams);
        this.creatureRotationCoeff = realTimeWorldControllerParams.creatureRotationCoeff;
    }

    /**
     * Конструктор класса параметров контроллера
     *
     * @param type                  тип контроллера
     * @param worldPath             путь к загружаемому миру
     * @param frequency             частота обработки
     * @param fps                   количество кадров в секунду при рисовании
     * @param backgroundColor       цвет фона
     * @param windowDivide          соотношение в разбивке экрана
     * @param creatureRotationCoeff коэффициент преобразования перемещения мыши в угол поворота существа
     */
    @JsonCreator
    public RealTimeWorldControllerParams(
            @NotNull @JsonProperty("type") WorldControllerParams.WorldControllerType type,
            @NotNull @JsonProperty("worldPath") String worldPath,
            @JsonProperty("frequency") int frequency, @JsonProperty("fps") int fps,
            @NotNull @JsonProperty("backgroundColor") Vector3d backgroundColor,
            @NotNull @JsonProperty("windowDivide") Vector2d windowDivide,
            @JsonProperty("creatureRotationCoeff") double creatureRotationCoeff
    ) {
        super(type, worldPath, frequency, fps, backgroundColor, windowDivide);
        this.creatureRotationCoeff = creatureRotationCoeff;
    }

    /**
     * Строковое представление объекта вида:
     * "creatureRotationCoeff, super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return String.format("%.3f", creatureRotationCoeff) +
                ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTimeWorldControllerParams{getString()}"
     */
    @Override
    public String toString() {
        return "RealTimeWorldControllerParams{" + getString() + '}';
    }

    /**
     * Получить коэффициент преобразования перемещения мыши в угол поворота существа
     *
     * @return коэффициент преобразования перемещения мыши в угол поворота существа
     */
    public double getCreatureRotationCoeff() {
        return creatureRotationCoeff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RealTimeWorldControllerParams that = (RealTimeWorldControllerParams) o;

        return Double.compare(that.creatureRotationCoeff, creatureRotationCoeff) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(creatureRotationCoeff);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
