package worldController.life3D;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import worldController.base.WorldControllerParams;
import worldController.realTime.RealTimeWorldControllerParams;

import java.util.Objects;

/**
 * Параметры контроллера 3D мира реального времени c жизнью
 */
public class Life3DWorldControllerParams extends RealTimeWorldControllerParams {
    /**
     * Скорость поворота камеры
     */
    @NotNull
    private final Vector2d cameraRotationSpeed;
    /**
     * Скорость движения камеры
     */
    @NotNull
    private final Vector3d cameraMovingSpeed;


    /**
     * Конструктор класса параметров контроллера
     *
     * @param life3DWorldControllerParams параметры контроллера
     */
    public Life3DWorldControllerParams(@NotNull Life3DWorldControllerParams life3DWorldControllerParams) {
        super(life3DWorldControllerParams);
        this.cameraRotationSpeed = new Vector2d(life3DWorldControllerParams.cameraRotationSpeed);
        this.cameraMovingSpeed = new Vector3d(life3DWorldControllerParams.cameraMovingSpeed);
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
     * @param cameraRotationSpeed   Скорость поворота камеры
     * @param cameraMovingSpeed     Скорость движения камеры
     */
    @JsonCreator
    public Life3DWorldControllerParams(
            @NotNull @JsonProperty("type") WorldControllerParams.WorldControllerType type,
            @NotNull @JsonProperty("worldPath") String worldPath, @JsonProperty("frequency") int frequency,
            @JsonProperty("fps") int fps,
            @NotNull @JsonProperty("backgroundColor") Vector3d backgroundColor,
            @NotNull @JsonProperty("windowDivide") Vector2d windowDivide,
            @JsonProperty("creatureRotationCoeff") double creatureRotationCoeff,
            @NotNull @JsonProperty("cameraRotationSpeed") Vector2d cameraRotationSpeed,
            @NotNull @JsonProperty("cameraMovingSpeed") Vector3d cameraMovingSpeed
    ) {
        super(type, worldPath, frequency, fps, backgroundColor, windowDivide, creatureRotationCoeff);
        this.cameraRotationSpeed = cameraRotationSpeed;
        this.cameraMovingSpeed = cameraMovingSpeed;
    }

    /**
     * Получить скорость вращения камеры
     *
     * @return скорость вращения камеры
     */
    @NotNull
    public Vector2d getCameraRotationSpeed() {
        return cameraRotationSpeed;
    }

    /**
     * Получить скорость перемещения камеры
     *
     * @return скорость перемещения камеры
     */
    @NotNull
    public Vector3d getCameraMovingSpeed() {
        return cameraMovingSpeed;
    }

    /**
     * Строковое представление объекта вида:
     * "cameraRotationSpeed, cameraMovingSpeed, super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return cameraRotationSpeed +
                ", " + cameraMovingSpeed +
                ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Life3DWorldControllerParams{getString()}"
     */
    @Override
    public String toString() {
        return "Life3DWorldControllerParams{" + getString() + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Life3DWorldControllerParams that = (Life3DWorldControllerParams) o;

        if (!Objects.equals(cameraRotationSpeed, that.cameraRotationSpeed))
            return false;
        return Objects.equals(cameraMovingSpeed, that.cameraMovingSpeed);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (cameraRotationSpeed != null ? cameraRotationSpeed.hashCode() : 0);
        result = 31 * result + (cameraMovingSpeed != null ? cameraMovingSpeed.hashCode() : 0);
        return result;
    }
}
