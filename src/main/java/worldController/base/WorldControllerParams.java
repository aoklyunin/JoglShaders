package worldController.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import math.Vector2d;
import math.Vector3d;
import worldController.life3D.Life3DWorldControllerParams;
import worldController.realTime.RealTimeWorldControllerParams;

import java.util.Objects;


/**
 * Класс параметров контроллеров
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class WorldControllerParams {
    /**
     * Типы контроллеров реального времени
     */
    public enum WorldControllerType {
        /**
         * тип контроллера реального мира 3D
         */
        REAL_TIME_3D,
    }

    /**
     * тип контроллера
     */
    @NotNull
    private final WorldControllerType type;
    /**
     * путь к миру
     */
    @NotNull
    private String worldPath;
    /**
     * частота обработки
     */
    private final int frequency;
    /**
     * соотношение в разбивке экрана
     */
    @NotNull
    private final Vector2d windowDivide;
    /**
     * количество кадров в секунду при рисовании
     */
    private final int fps;
    /**
     * цвет фона
     */
    @NotNull
    private final Vector3d backgroundColor;

    /**
     * Конструктор класса параметров контроллера
     *
     * @param worldControllerParams параметры контроллера
     */
    public WorldControllerParams(@NotNull WorldControllerParams worldControllerParams) {
        this.type = worldControllerParams.type;
        this.worldPath = worldControllerParams.worldPath;
        this.frequency = worldControllerParams.frequency;
        this.fps = worldControllerParams.fps;
        this.backgroundColor = worldControllerParams.backgroundColor;
        this.windowDivide = worldControllerParams.windowDivide;
    }

    /**
     * Конструктор класса параметров контроллера
     *
     * @param type            тип контроллера
     * @param worldPath       путь к загружаемому миру
     * @param frequency       частота обработки
     * @param fps             количество кадров в секунду при рисовании
     * @param backgroundColor цвет фона
     */
    @JsonCreator
    public WorldControllerParams(
            @NotNull @JsonProperty("type") WorldControllerType type,
            @NotNull @JsonProperty("worldPath") String worldPath,
            @JsonProperty("frequency") int frequency, @JsonProperty("fps") int fps,
            @NotNull @JsonProperty("backgroundColor") Vector3d backgroundColor,
            @JsonProperty("windowDivide") Vector2d windowDivide
    ) {
        this.type = Objects.requireNonNull(type);
        this.worldPath = Objects.requireNonNull(worldPath);
        this.frequency = frequency;
        this.fps = fps;
        this.backgroundColor = Objects.requireNonNull(backgroundColor);
        this.windowDivide = windowDivide;
    }

    /**
     * Получить тип контроллера
     *
     * @return тип контроллера
     */
    public WorldControllerType getType() {
        return type;
    }

    /**
     * Получить путь к миру
     *
     * @return путь к миру
     */
    public String getWorldPath() {
        return worldPath;
    }

    /**
     * Получить частоту обработки мира
     *
     * @return частота обработки мира
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Получить fps контроллера
     *
     * @return fps контроллера
     */
    public int getFps() {
        return fps;
    }

    /**
     * Получить цвет фона (нельзя менять полученный объект)
     *
     * @return цвет фона
     */
    public Vector3d getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Задать путь к миру
     *
     * @param worldPath путь к миру
     */
    public void setWorldPath(String worldPath) {
        this.worldPath = worldPath;
    }

    /**
     * Получить соотношение в разбивке экрана
     *
     * @return соотношение в разбивке экрана
     */
    @NotNull
    public Vector2d getWindowDivide() {
        return windowDivide;
    }

    /**
     * Строковое представление объекта вида:
     * "'type', 'worldPath', frequency, fps, backgroundColor"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + type + '\'' +
                ", '" + worldPath + '\'' +
                ", " + frequency +
                ", " + fps +
                ", " + backgroundColor;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "WorldControllerParams{getString()}"
     */
    @Override
    public String toString() {
        return "WorldControllerParams{" + getString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorldControllerParams that = (WorldControllerParams) o;

        if (frequency != that.frequency) return false;
        if (fps != that.fps) return false;
        if (type != that.type) return false;
        if (!Objects.equals(worldPath, that.worldPath)) return false;
        return Objects.equals(backgroundColor, that.backgroundColor);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (worldPath != null ? worldPath.hashCode() : 0);
        result = 31 * result + frequency;
        result = 31 * result + fps;
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        return result;
    }


    /**
     * Преобразовать к параметрам контроллера мира реального времени
     *
     * @return параметры контроллера мира реального времени
     */
    @JsonIgnore
    public RealTimeWorldControllerParams getRealTimeWorldControllerParams() {
        if (this instanceof RealTimeWorldControllerParams)
            return (RealTimeWorldControllerParams) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к параметрам контроллера экспериментального мира 3D реального рвемени
     *
     * @return параметры контроллера экспериментального мира 3D реального рвемени
     */
    @JsonIgnore
    public Life3DWorldControllerParams getLife3DWorldControllerParams() {
        if (this instanceof Life3DWorldControllerParams)
            return (Life3DWorldControllerParams) this;
        throw new ClassCastException();
    }


}
