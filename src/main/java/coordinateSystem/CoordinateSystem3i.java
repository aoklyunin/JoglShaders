package coordinateSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.NotNull;
import math.*;

import java.util.Objects;

/**
 * Ограниченная трёхмерная целочисленная система координат
 */
@JsonPropertyOrder({"min", "max"})
public class CoordinateSystem3i {
    /**
     * минимальная координата
     */
    @NotNull
    private final Vector3i min;
    /**
     * максимальная координата
     */
    @NotNull
    private final Vector3i max;
    /**
     * размер СК
     */
    @NotNull
    @JsonIgnore
    private final Vector3i size;

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param minX минимальная X координата
     * @param maxX максимальная X координата
     * @param minY минимальная Y координата
     * @param maxY максимальная Y координата
     * @param minZ максимальная Z координата
     * @param maxZ максимальная Z координата
     */
    public CoordinateSystem3i(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        min = new Vector3i(minX, minY, minZ);
        max = new Vector3i(maxX, maxY, maxZ);
        size = Vector3i.subtract(max, min);
        size.inc();
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     * @param maxZ максимальная Z координата
     */
    public CoordinateSystem3i(int maxX, int maxY, int maxZ) {
        this(0, maxX, 0, maxY, 0, maxZ);
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    @JsonCreator
    public CoordinateSystem3i(@NotNull @JsonProperty("min") Vector3i min, @NotNull @JsonProperty("max") Vector3i max) {
        this(min.x, max.x, min.y, max.y, min.z, max.z);
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param max максимальные координаты
     */
    public CoordinateSystem3i(@NotNull Vector3i max) {
        this(0, max.x, 0, max.y, 0, max.z);
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная двумерная вещественная система координат
     */
    public CoordinateSystem3i(@NotNull CoordinateSystem2d coordinateSystem) {

        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y,
                0, 0
        );

    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная двумерная целочисленная система координат
     */
    public CoordinateSystem3i(@NotNull CoordinateSystem2i coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                0, 0
        );
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная вещественная система координат
     */
    public CoordinateSystem3i(@NotNull CoordinateSystem3d coordinateSystem) {
        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y,
                (int) coordinateSystem.getMin().z, (int) coordinateSystem.getMax().z
        );
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная целочисленная система координат
     */
    public CoordinateSystem3i(@NotNull CoordinateSystem3i coordinateSystem) {
        this(
                coordinateSystem.min.x, coordinateSystem.max.x,
                coordinateSystem.min.y, coordinateSystem.max.y,
                coordinateSystem.min.z, coordinateSystem.max.z
        );
    }

    /**
     * Конструктор ограниченной трёхмерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная четырёхмерная вещественная система координат
     */
    public CoordinateSystem3i(@NotNull CoordinateSystem4d coordinateSystem) {
        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y,
                (int) coordinateSystem.getMin().z, (int) coordinateSystem.getMax().z
        );
    }

    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    @NotNull
    @JsonIgnore
    public Vector3i getRandomCoords() {
        return Vector3i.rand(min, max);
    }


    /**
     * Обрезать координаты вектора по границам системы координат
     *
     * @param coords координаты вектора
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector3i truncate(@NotNull Vector3i coords) {
        return new Vector3i(
                Math.max(Math.min(coords.x, max.x), min.x),
                Math.max(Math.min(coords.y, max.y), min.y),
                Math.max(Math.min(coords.z, max.z), min.z)
        );
    }

    /**
     * Обрезать координаты вектора по границам системы координат с учётом отступов относительно самого вектора
     *
     * @param coords     координаты вектора
     * @param minPadding отступ со стороны минимума (отрицательные координаты)
     * @param maxPadding отступ со стороны максимума (положительные координаты)
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector3i truncate(@NotNull Vector3i coords, @NotNull Vector3i minPadding, @NotNull Vector3i maxPadding) {
        return new Vector3i(
                Math.max(Math.min(coords.x, max.x - maxPadding.x), min.x - minPadding.x),
                Math.max(Math.min(coords.y, max.y - maxPadding.y), min.y - minPadding.y),
                Math.max(Math.min(coords.z, max.z - maxPadding.z), min.z - minPadding.z)
        );
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param coords координаты вектора
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(@NotNull Vector3i coords) {
        return checkCoords(coords.x, coords.y, coords.z);
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @param z координата Z
     * @return флаг, попадают ли координаты в границы СК
     */
    boolean checkCoords(int x, int y, int z) {
        return x >= min.x && y >= min.y && z >= min.z && x <= max.x && y <= max.y && z <= max.z;
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector3i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y),
                0
        );
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector3i(
                (x - coordinateSystem.getMin().x) * (size.x - 1) / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * (size.y - 1) / (coordinateSystem.getSize().y - 1) + min.y,
                0
        );
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector3i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y),
                (int) ((z - coordinateSystem.getMin().z) * (size.z - 1) / coordinateSystem.getSize().z + min.z)
        );
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector3i(
                (x - coordinateSystem.min.x) * (size.x - 1) / (coordinateSystem.size.x - 1) + min.x,
                (y - coordinateSystem.min.y) * (size.y - 1) / (coordinateSystem.size.y - 1) + min.y,
                (z - coordinateSystem.min.z) * (size.z - 1) / (coordinateSystem.size.z - 1) + min.z
        );
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coords.w, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param w                координата W вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector3i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y),
                (int) ((z - coordinateSystem.getMin().z) * (size.z - 1) / coordinateSystem.getSize().z + min.z)
        );
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector3i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)),
                0
        );
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector3i(
                (int) Math.floor(
                        (double) (x - coordinateSystem.getMin().x) * (size.x - 1) / (coordinateSystem.getSize().x - 1) + min.x
                ),
                (int) Math.floor(
                        (double) (y - coordinateSystem.getMin().y) * (size.y - 1) / (coordinateSystem.getSize().y - 1) + min.y
                ),
                0
        );
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector3i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)),
                (int) Math.floor(((z - coordinateSystem.getMin().z) * (size.z - 1) / coordinateSystem.getSize().z + min.z))
        );
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector3i(
                (int) Math.floor(
                        (double) (x - coordinateSystem.min.x) * (size.x - 1) / (coordinateSystem.size.x - 1) + min.x
                ),
                (int) Math.floor(
                        (double) (y - coordinateSystem.min.y) * (size.y - 1) / (coordinateSystem.size.y - 1) + min.y
                ),
                (int) Math.floor(
                        (double) (z - coordinateSystem.min.z) * (size.z - 1) / (coordinateSystem.size.z - 1) + min.z
                )
        );
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coords.w, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить округлённые снизу координаты вектора в текущей системе координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param w                координата W вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3i getFloorCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector3i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)),
                (int) Math.floor(((z - coordinateSystem.getMin().z) * (size.z - 1) / coordinateSystem.getSize().z + min.z))
        );
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param coords           размеры объектаа в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getSize(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector3i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y),
                0
        );
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getSize(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector3i(
                x * (size.x - 1) / (coordinateSystem.getSize().x - 1),
                y * (size.y - 1) / (coordinateSystem.getSize().y - 1),
                0
        );
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getSize(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector3i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y),
                (int) (z * (size.z - 1) / coordinateSystem.getSize().z)
        );
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getSize(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector3i(
                x * (size.x - 1) / (coordinateSystem.size.x - 1),
                y * (size.y - 1) / (coordinateSystem.size.y - 1),
                y * (size.z - 1) / (coordinateSystem.size.z - 1)
        );
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getSize(coords.x, coords.y, coords.z, coords.w, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей системе координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param w                размер объекта вдоль оси W вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3i getSize(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector3i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y),
                (int) (z * (size.z - 1) / coordinateSystem.getSize().z)
        );
    }


    /**
     * Получить вектор подобия двух систем координат
     * (значения единичного размера, указанного в переданнной в аргументах СК в текущей СК)
     *
     * @param coordinateSystem система координат, подобие с которой нужно получить
     * @return вектор подобий вдоль соответствующиъ осей координат
     */
    @NotNull
    public Vector3i getSimilarity(@NotNull CoordinateSystem2d coordinateSystem) {
        return getSize(Vector2d.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить вектор подобия двух систем координат
     * (значения единичного размера, указанного в переданнной в аргументах СК в текущей СК)
     *
     * @param coordinateSystem система координат, подобие с которой нужно получить
     * @return вектор подобий вдоль соответствующиъ осей координат
     */
    @NotNull
    public Vector3i getSimilarity(@NotNull CoordinateSystem2i coordinateSystem) {
        return getSize(Vector2i.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить вектор подобия двух систем координат
     * (значения единичного размера, указанного в переданнной в аргументах СК в текущей СК)
     *
     * @param coordinateSystem система координат, подобие с которой нужно получить
     * @return вектор подобий вдоль соответствующиъ осей координат
     */
    @NotNull
    public Vector3i getSimilarity(@NotNull CoordinateSystem3d coordinateSystem) {
        return getSize(Vector3d.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить вектор подобия двух систем координат
     * (значения единичного размера, указанного в переданнной в аргументах СК в текущей СК)
     *
     * @param coordinateSystem система координат, подобие с которой нужно получить
     * @return вектор подобий вдоль соответствующиъ осей координат
     */
    @NotNull
    public Vector3i getSimilarity(@NotNull CoordinateSystem3i coordinateSystem) {
        return getSize(Vector3i.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить вектор подобия двух систем координат
     * (значения единичного размера, указанного в переданнной в аргументах СК в текущей СК)
     *
     * @param coordinateSystem система координат, подобие с которой нужно получить
     * @return вектор подобий вдоль соответствующиъ осей координат
     */
    @NotNull
    public Vector3i getSimilarity(@NotNull CoordinateSystem4d coordinateSystem) {
        return getSize(Vector4d.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить максимальное значение свёртки веткора в число
     *
     * @return максимальное значение свёртки веткора в число
     */
    public int getConvValue() {
        return conv(max);
    }

    /**
     * Свёртка вектора в число
     *
     * @param coords координаты вектора
     * @return значение свёртки вектора в число
     */
    public int conv(@NotNull Vector3i coords) {
        Vector3i positivePos = Vector3i.subtract(coords, min);
        if (size.y == 1)
            if (size.z == 1)
                return positivePos.x;
            else
                return positivePos.z * size.x + positivePos.x;

        return positivePos.y + size.y * positivePos.x + size.y * size.x * positivePos.z;
    }

    /**
     * Обратная свёртка числа в вектор
     *
     * @param val число, из которого нужно получить вектор
     * @return вектор, полученный из обратной свёртки числа в вектор
     */
    @NotNull
    public Vector3i deconv(int val) {
        if (size.y == 1)
            if (size.z == 1)
                return Vector3i.sum(new Vector3i(val, 0, 0), min);
            else
                return Vector3i.sum(new Vector3i(val % size.x, 0, val / size.x), min);

        Vector3i positivePos = new Vector3i();
        positivePos.y = val % size.y;
        val = val / size.y;
        positivePos.x = val % size.x;
        positivePos.z = val / size.x;
        Vector3i res = Vector3i.sum(positivePos, min);
        if (!checkCoords(res)) {
            throw new IllegalArgumentException("res coords are not in CS " + res);
        }
        return res;
    }

    /**
     * Получить ближайшую точку, соответствующую одной из точек на целочисленной СК, в СК вещественной
     *
     * @param coords           координаты, близость к которым при переводе в вещественную СК
     *                         является критерием отбора точки в целочисленной СК
     * @param coordinateSystem вещественная СК
     * @return точка на целочисленной ск
     */
    @NotNull
    public Vector3i getNearestPoint(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        Vector3i newPos = getCoords(Objects.requireNonNull(coords), Objects.requireNonNull(coordinateSystem));
        double minDistance = -1;
        Vector3i intMinDistanceCoords = null;
        // перебираем полученное значение от прямой конвертации и всех его ближайших соседей
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    Vector3i intLoopCoords = new Vector3i(newPos.x + i, newPos.y + j, newPos.z + k);
                    if (checkCoords(intLoopCoords)) {
                        Vector3d doubleLoopCoords = coordinateSystem.getCoords(intLoopCoords, this);
                        if (intMinDistanceCoords == null) {
                            minDistance = Vector3d.getDistance(doubleLoopCoords, coords);
                            intMinDistanceCoords = intLoopCoords;
                        } else {
                            double localDelta = Vector3d.getDistance(doubleLoopCoords, coords);
                            if (localDelta < minDistance) {
                                minDistance = localDelta;
                                intMinDistanceCoords = intLoopCoords;
                            }
                        }
                    }
                }
        return intMinDistanceCoords;
    }

    /**
     * Получить максимальную координата
     *
     * @return максимальная координата
     */
    @NotNull
    public Vector3i getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector3i getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    public Vector3i getSize() {
        return size;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem3i{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem3i{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "pos, maxEnergy, defaultEnergy"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return max + ", " + min;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateSystem3i that = (CoordinateSystem3i) o;

        if (!Objects.equals(max, that.max)) return false;
        return Objects.equals(min, that.min);
    }

    @Override
    public int hashCode() {
        int result = max != null ? max.hashCode() : 0;
        result = 31 * result + (min != null ? min.hashCode() : 0);
        return result;
    }
}
