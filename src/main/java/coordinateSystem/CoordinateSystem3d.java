package coordinateSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.sun.istack.NotNull;
import math.*;
import org.apache.commons.math3.util.Precision;

import java.util.Objects;

import static math.Vector3d.VECTOR_3D_OPACITY;


/**
 * Ограниченная трёхмерная вещественная система координат
 */
@JsonPropertyOrder({"min", "max"})
public class CoordinateSystem3d {
    /**
     * Кол-во знаков после запятой
     */
    public static final int DIGIT_COUNT = 6;
    /**
     * минимальная координата
     */
    @NotNull
    private Vector3d min;
    /**
     * максимальная координата
     */
    @NotNull
    private Vector3d max;
    /**
     * размер СК
     */
    @NotNull
    @JsonIgnore
    private Vector3d size;

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param minX минимальная X координата
     * @param maxX максимальная X координата
     * @param minY минимальная Y координата
     * @param maxY максимальная Y координата
     * @param minZ максимальная Z координата
     * @param maxZ максимальная Z координата
     */
    public CoordinateSystem3d(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        min = new Vector3d(minX, minY, minZ);
        max = new Vector3d(maxX, maxY, maxZ);
        size = Vector3d.subtract(max, min);
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     * @param maxZ максимальная Z координата
     */
    public CoordinateSystem3d(double maxX, double maxY, double maxZ) {
        this(0, maxX, 0, maxY, 0, maxZ);
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    @JsonCreator
    public CoordinateSystem3d(@NotNull @JsonProperty("min") Vector3d min, @NotNull @JsonProperty("max") Vector3d max) {
        this(min.x, max.x, min.y, max.y, min.z, max.z);
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param max максимальные координаты
     */
    public CoordinateSystem3d(@NotNull Vector3d max) {
        this(0, max.x, 0, max.y, 0, max.z);
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная вещественная система координат
     */
    public CoordinateSystem3d(@NotNull CoordinateSystem2d coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                0, 0
        );
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная целочисленная система координат
     */
    public CoordinateSystem3d(@NotNull CoordinateSystem2i coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                0, 0
        );
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная вещественная система координат
     */
    public CoordinateSystem3d(@NotNull CoordinateSystem3d coordinateSystem) {
        this(
                coordinateSystem.min.x, coordinateSystem.max.x,
                coordinateSystem.min.y, coordinateSystem.max.y,
                coordinateSystem.min.z, coordinateSystem.max.z
        );
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная целочисленная система координат
     */
    public CoordinateSystem3d(@NotNull CoordinateSystem3i coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                coordinateSystem.getMin().z, coordinateSystem.getMax().z
        );
    }

    /**
     * Конструктор ограниченной трёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная четырёхмерная вещественная система координат
     */
    public CoordinateSystem3d(@NotNull CoordinateSystem4d coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                coordinateSystem.getMin().z, coordinateSystem.getMax().z
        );
    }


    /**
     * Задать новый размер СК
     *
     * @param newSize новый размер СК
     */
    public void setNewSize(@NotNull Vector3d newSize) {
        Vector3d sizeDiff = Vector3d.mul(Vector3d.subtract(size, Objects.requireNonNull(newSize)), 0.5);
        min = Vector3d.sum(min, sizeDiff);
        max = Vector3d.subtract(max, sizeDiff);
        size = newSize;
    }

    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    @NotNull
    @JsonIgnore
    public Vector3d getRandomCoords() {
        return Vector3d.rand(min, max);
    }

    /**
     * Обрезать координаты вектора по границам системы координат
     *
     * @param coords координаты вектора
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector3d truncate(@NotNull Vector3d coords) {
        return new Vector3d(
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
    public Vector3d truncate(@NotNull Vector3d coords, @NotNull Vector3d minPadding, @NotNull Vector3d maxPadding) {
        return new Vector3d(
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
    public boolean checkCoords(@NotNull Vector3d coords) {
        return Vector3d.biggerOrEqual(Objects.requireNonNull(coords), min) && Vector3d.lessOrEqual(coords, max);
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @param z координата Z
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(double x, double y, double z) {
        return checkCoords(new Vector3d(x, y, z));
    }


    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector3d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y,
                0
        );
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector3d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y,
                0
        );
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector3d(
                (x - coordinateSystem.min.x) * size.x / coordinateSystem.size.x + min.x,
                (y - coordinateSystem.min.y) * size.y / coordinateSystem.size.y + min.y,
                (z - coordinateSystem.min.z) * size.z / coordinateSystem.size.z + min.z
        );
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coords.z, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector3d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y,
                (z - coordinateSystem.getMin().z) * size.z / (coordinateSystem.getSize().z - 1) + min.z
        );
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coords.w, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param z                координата Z вектора в другой системе координат
     * @param w                координата W вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector3d getCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector3d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y,
                (z - coordinateSystem.getMin().z) * size.z / coordinateSystem.getSize().z + min.z
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объектаа в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getSize(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector3d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y,
                0
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getSize(coords.x, coords.y, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector3d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1),
                0
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getSize(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector3d(
                x * size.x / coordinateSystem.size.x,
                y * size.y / coordinateSystem.size.y,
                z * size.z / coordinateSystem.size.z
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getSize(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector3d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1),
                z * size.z / (coordinateSystem.getSize().y - 1)
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объекта в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getSize(coords.x, coords.y, coords.z, coords.w, Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param x                размер объекта вдоль оси X вектора в другой системе координат
     * @param y                размер объекта вдоль оси Y вектора в другой системе координат
     * @param z                размер объекта вдоль оси Z вектора в другой системе координат
     * @param w                размер объекта вдоль оси W вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    @NotNull
    public Vector3d getSize(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector3d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y,
                z * size.z / coordinateSystem.getSize().z
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
    public Vector3d getSimilarity(@NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector3d getSimilarity(@NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector3d getSimilarity(@NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector3d getSimilarity(@NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector3d getSimilarity(@NotNull CoordinateSystem4d coordinateSystem) {
        return getSize(Vector4d.ones(), Objects.requireNonNull(coordinateSystem));
    }

    /**
     * Получить максимальное значение свёртки веткора в число
     *
     * @param divideCnt число делений диапазона
     * @return максимальное значение свёртки веткора в число
     */
    public int getConvValue(int divideCnt) {
        return conv(max, divideCnt);
    }

    /**
     * Свёртка вектора в число
     *
     * @param coords    координаты вектора
     * @param divideCnt количество делений
     * @return значение свёртки вектора в число
     */
    public int conv(@NotNull Vector3d coords, int divideCnt) {
        Vector3d delta = Vector3d.mul(size, 1.0 / divideCnt);
        Vector3d positivePosD = Vector3d.subtract(Objects.requireNonNull(coords), min);
        Vector3i positivePos = new Vector3i(
                (int) Math.round(positivePosD.x / Precision.round(delta.x, DIGIT_COUNT)),
                (int) Math.round(positivePosD.y / Precision.round(delta.y, DIGIT_COUNT)),
                (int) Math.round(positivePosD.z / Precision.round(delta.z, DIGIT_COUNT))
        );
        if (size.y < VECTOR_3D_OPACITY)
            if (size.z < VECTOR_3D_OPACITY)
                return positivePos.x;
            else
                return positivePos.z + positivePos.x * (divideCnt + 1);

        if (size.x < VECTOR_3D_OPACITY)
            if (size.z < VECTOR_3D_OPACITY)
                return positivePos.y;
            else
                return positivePos.z + positivePos.y * (divideCnt + 1);

        return positivePos.x + (divideCnt + 1) * positivePos.y + (divideCnt + 1) * (divideCnt + 1) * positivePos.z;
    }

    /**
     * Обратная свёртка числа в вектор
     *
     * @param val       число, из которого нужно получить вектор
     * @param divideCnt количество делений
     * @return вектор, полученный из обратной свёртки числа в вектор
     */
    @NotNull
    public Vector3d deconv(int val, int divideCnt) {
        Vector3d delta = Vector3d.mul(size, 1.0 / divideCnt);
        if (size.y < VECTOR_3D_OPACITY)
            if (size.z < VECTOR_3D_OPACITY)
                return Vector3d.sum(new Vector3d(val * delta.x, 0, 0), min);
            else
                return Vector3d.sum(new Vector3d((val / (divideCnt + 1)) * delta.x, 0, (val % (divideCnt + 1)) * delta.z), min);

        if (size.x < VECTOR_3D_OPACITY)
            if (size.z < VECTOR_3D_OPACITY)
                return Vector3d.sum(new Vector3d(0, val * delta.y, 0), min);
            else
                return Vector3d.sum(new Vector3d(0, (val / (divideCnt + 1)) * delta.y, (val % (divideCnt + 1)) * delta.z), min);

        Vector3i positivePos = new Vector3i();
        positivePos.x = (val % (divideCnt + 1));
        val = val / (divideCnt + 1);
        positivePos.y = (val % (divideCnt + 1));
        positivePos.z = (val / (divideCnt + 1));
        Vector3d positivePosD = new Vector3d(
                positivePos.x * delta.x,
                positivePos.y * delta.y,
                positivePos.z * delta.z
        );
        Vector3d res = Vector3d.sum(positivePosD, min);
        if (!checkCoords(res)) {
            throw new IllegalArgumentException("res coords are not in CS " + res);
        }
        return res;
    }

    /**
     * Получить максимальную координата
     *
     * @return максимальная координата
     */
    @NotNull
    public Vector3d getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector3d getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    public Vector3d getSize() {
        return size;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem3d{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem3d{" + getString() + '}';
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

        CoordinateSystem3d that = (CoordinateSystem3d) o;

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

