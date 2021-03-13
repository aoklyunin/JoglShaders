package creature.creature3D;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem2i;
import creature.base.CreatureParams;
import graphics.ObjModel3DParams;
import math.Vector2i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Параметры 3D существа
 */
public class Creature3DParams extends CreatureParams {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(Creature3DParams.class);

    /**
     * Размер решётки сенсоров
     */
    @NotNull
    private Vector2i sensorGridSize;
    /**
     * Размер решётки зрения
     */
    @NotNull
    private Vector2i visionGridSize;
    /**
     * СК решётки сенсоров
     */
    @NotNull
    private CoordinateSystem2i sensorGridCS;
    /**
     * СК решётки сенсоров для рисования
     */
    @NotNull
    private CoordinateSystem2i renderSensorGridCS;
    /**
     * СК решётки зрения
     */
    @NotNull
    private CoordinateSystem2i visionGridCS;
    /**
     * СК решётки зрения для рисования
     */
    @NotNull
    private CoordinateSystem2i renderVisionGridCS;
    /**
     * максимальное значение сетки сенсора
     */
    private final int sensorGridMaxValue;

    /**
     * Конструктор класса параметров 3D существа
     *
     * @param creature3DParams параметры 3D существа
     */
    public Creature3DParams(@NotNull Creature3DParams creature3DParams) {
        super(Objects.requireNonNull(creature3DParams));
        this.sensorGridMaxValue = creature3DParams.sensorGridMaxValue;

        setSensorGridSize(creature3DParams.sensorGridSize);
        setVisionGridSize(creature3DParams.visionGridSize);
    }

    /**
     * Конструктор класса параметров существа
     *
     * @param name                   имя существа
     * @param creatureModelParams    параметры модели существа
     * @param sensorGridSize         размер решётки сенсоров
     * @param visionGridSize         размер решётки зрения
     * @param sensorGridMaxValue     максимальное значение сетки сенсора
     */
    @JsonCreator
    public Creature3DParams(
            @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("creatureModelParams") ObjModel3DParams creatureModelParams,
            @NotNull @JsonProperty("sensorGridSize") Vector2i sensorGridSize,
            @NotNull @JsonProperty("visionGridSize") Vector2i visionGridSize,
            @JsonProperty("sensorGridMaxValue") int sensorGridMaxValue
    ) {
        super(Objects.requireNonNull(name), Objects.requireNonNull(creatureModelParams));

        this.sensorGridMaxValue = sensorGridMaxValue;

        setSensorGridSize(Objects.requireNonNull(sensorGridSize));
        setVisionGridSize(Objects.requireNonNull(visionGridSize));

        if (sensorGridSize.x < visionGridSize.x)
            logger.error(
                    "Creature3DParams(): sensorGridSize.x=" + sensorGridSize.x +
                            " is less than visionGridSize.x=" + visionGridSize.x
            );
        if (sensorGridSize.y < visionGridSize.y)
            logger.error(
                    "Creature3DParams(): sensorGridSize.y=" + sensorGridSize.y +
                            " is less than visionGridSize.y=" + visionGridSize.y
            );
    }

    /**
     * Задать размер решётки сенсоров
     *
     * @param sensorGridSize размер решётки сенсоров
     */
    public void setSensorGridSize(@NotNull Vector2i sensorGridSize) {
        this.sensorGridSize = Objects.requireNonNull(sensorGridSize);
        this.sensorGridCS = new CoordinateSystem2i(Vector2i.dec(sensorGridSize));
        this.renderSensorGridCS = new CoordinateSystem2i(Vector2i.inc(sensorGridSize));
    }

    /**
     * Задать размер решётки зрения
     *
     * @param visionGridSize размер решётки зрения
     */
    public void setVisionGridSize(@NotNull Vector2i visionGridSize) {
        this.visionGridSize = Objects.requireNonNull(visionGridSize);
        this.visionGridCS = new CoordinateSystem2i(Vector2i.dec(visionGridSize));
        this.renderVisionGridCS = new CoordinateSystem2i(Vector2i.inc(visionGridSize));
    }

    /**
     * Получить размер решётки сенсоров
     *
     * @return Размер решётки сенсоров
     */
    @NotNull
    public Vector2i getSensorGridSize() {
        return sensorGridSize;
    }

    /**
     * Получить Размер решётки зрения
     *
     * @return Размер решётки зрения
     */
    @NotNull
    public Vector2i getVisionGridSize() {
        return visionGridSize;
    }

    /**
     * Получить СК решётки сенсоров
     *
     * @return СК решётки сенсоров
     */
    @NotNull
    public CoordinateSystem2i getSensorGridCS() {
        return sensorGridCS;
    }

    /**
     * Получить СК решётки сенсоров для рисования
     *
     * @return СК решётки сенсоров для рисования
     */
    @NotNull
    public CoordinateSystem2i getRenderSensorGridCS() {
        return renderSensorGridCS;
    }

    /**
     * Получить СК решётки сенсоров для рисования
     *
     * @return СК решётки сенсоров для рисования
     */
    @NotNull
    public CoordinateSystem2i getVisionGridCS() {
        return visionGridCS;
    }

    /**
     * Получить  СК решётки зрения для рисования
     *
     * @return СК решётки зрения для рисования
     */
    @NotNull
    public CoordinateSystem2i getRenderVisionGridCS() {
        return renderVisionGridCS;
    }


    /**
     * Получить максимальное значение сетки сенсора
     *
     * @return максимальное значение сетки сенсора
     */
    public int getSensorGridMaxValue() {
        return sensorGridMaxValue;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Creature3DParams{getString()}"
     */
    @Override
    public String toString() {
        return "Creature3DParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "sensorGridMaxValue, sensorGridCS, visionGridCS, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return sensorGridMaxValue + ", " + sensorGridCS + ", " + visionGridCS + ", "
                + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Creature3DParams that = (Creature3DParams) o;

        if (sensorGridMaxValue != that.sensorGridMaxValue) return false;
        if (!Objects.equals(sensorGridSize, that.sensorGridSize))
            return false;
        return Objects.equals(visionGridSize, that.visionGridSize);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (sensorGridSize != null ? sensorGridSize.hashCode() : 0);
        result = 31 * result + (visionGridSize != null ? visionGridSize.hashCode() : 0);
        result = 31 * result + sensorGridMaxValue;
        return result;
    }
}
