package field.field3D.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem3d;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.Objects;

/**
 * Класс состояния 3D системы координат
 */
public class CoordinateSystem3DState {
    /**
     * id состояния
     */
    @JsonIgnore
    private int id;
    /**
     * минимальная X координата
     */
    private double minX;
    /**
     * минимальная Y координата
     */
    private double minY;
    /**
     * минимальная Z координата
     */
    private double minZ;
    /**
     * максимальная X координата
     */
    private double maxX;
    /**
     * максимальная Y координата
     */
    private double maxY;
    /**
     * максимальная Z координата
     */
    private double maxZ;

    /**
     * Конструктор  состояния 3D системы координат
     *
     * @param minX минимальная X координата
     * @param minY минимальная Y координата
     * @param minZ минимальная Z координата
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     * @param maxZ максимальная Z координата
     */
    public CoordinateSystem3DState(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /**
     * Конструктор  состояния 3D системы координат
     *
     * @param coordinateSystem3d 3D система координат
     */
    public CoordinateSystem3DState(@NotNull CoordinateSystem3d coordinateSystem3d) {
        minX = coordinateSystem3d.getMin().x;
        minY = coordinateSystem3d.getMin().y;
        minZ = coordinateSystem3d.getMin().z;

        maxX = coordinateSystem3d.getMax().x;
        maxY = coordinateSystem3d.getMax().y;
        maxZ = coordinateSystem3d.getMax().z;
    }

    /**
     * Конструктор  состояния 3D системы координат
     *
     * @param coordinateSystem3dState состояние 3D системы координат
     */
    public CoordinateSystem3DState(@NotNull CoordinateSystem3DState coordinateSystem3dState) {
        this(
                coordinateSystem3dState.minX,
                coordinateSystem3dState.minY,
                coordinateSystem3dState.minZ,
                coordinateSystem3dState.maxX,
                coordinateSystem3dState.maxY,
                coordinateSystem3dState.maxZ
        );
    }

    /**
     * Конструктор  состояния 3D системы координат
     *
     * @param min минимальная координата
     * @param max максимальная координата
     */
    public CoordinateSystem3DState(@NotNull Vector3d min, @NotNull Vector3d max) {
        minX = min.x;
        minY = min.y;
        minZ = min.z;

        maxX = max.x;
        maxY = max.y;
        maxZ = max.z;
    }

    /**
     * Конструктор  состояния 3D системы координат
     */
    private CoordinateSystem3DState() {

    }

    /**
     * Получить минимальную координату
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector3d getMin() {
        return new Vector3d(minX, minY, minZ);
    }

    /**
     * Задать минимум
     *
     * @param min минимум
     */
    public void setMin(@NotNull Vector3d min) {
        this.minX = min.x;
        this.minY = min.y;
        this.minZ = min.z;
    }

    /**
     * Задать максимум
     *
     * @param max максимум
     */
    public void setMax(@NotNull Vector3d max) {
        this.maxX = max.x;
        this.maxY = max.y;
        this.maxZ = max.z;
    }

    /**
     * Получить максимальную координату
     *
     * @return максимальную координата
     */
    @NotNull
    public Vector3d getMax() {
        return new Vector3d(maxX, maxY, maxZ);
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    @JsonIgnore
    public Vector3d getSize() {
        return Vector3d.subtract(getMax(), getMin());
    }


    /**
     * Получить id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem3DState{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem3DState{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "getMin(), getMax()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return getMin() + " " + getMax();
    }

    /**
     * Проверка двух состояний на равенство
     *
     * @param o объект вравнения
     * @return равны ли два состояния
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateSystem3DState that = (CoordinateSystem3DState) o;
        return id == that.id &&
                Double.compare(that.minX, minX) == 0 &&
                Double.compare(that.minY, minY) == 0 &&
                Double.compare(that.minZ, minZ) == 0 &&
                Double.compare(that.maxX, maxX) == 0 &&
                Double.compare(that.maxY, maxY) == 0 &&
                Double.compare(that.maxZ, maxZ) == 0;
    }

    /**
     * Хэш-функция состояния СК
     *
     * @return Хэш состояния СК
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, minX, minY, minZ, maxX, maxY, maxZ);
    }
}
