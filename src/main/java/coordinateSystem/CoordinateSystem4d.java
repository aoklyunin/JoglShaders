package coordinateSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.NotNull;
import math.*;
import org.apache.commons.math3.util.Precision;

import java.util.Objects;

import static math.Vector4d.VECTOR_4D_OPACITY;


/**
 * Ограниченная четырёхмерная вещественная система координат
 */
@JsonPropertyOrder({"min", "max"})
public class CoordinateSystem4d {
    /**
     * Кол-во знаков после запятой
     */
    public static final int DIGIT_COUNT = 6;
    /**
     * минимальная координата
     */
    @NotNull
    private Vector4d min;
    /**
     * максимальная координата
     */
    @NotNull
    private Vector4d max;
    /**
     * размер СК
     */
    @NotNull
    @JsonIgnore
    private Vector4d size;

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param minX минимальная X координата
     * @param maxX максимальная X координата
     * @param minY минимальная Y координата
     * @param maxY максимальная Y координата
     * @param minZ максимальная Z координата
     * @param maxZ максимальная Z координата
     * @param minW максимальная W координата
     * @param maxW максимальная W координата
     */
    public CoordinateSystem4d(
            double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double minW, double maxW
    ) {
        min = new Vector4d(minX, minY, minZ, minW);
        max = new Vector4d(maxX, maxY, maxZ, maxW);
        size = Vector4d.subtract(max, min);
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     * @param maxZ максимальная Z координата
     * @param maxW максимальная W координата
     */
    public CoordinateSystem4d(double maxX, double maxY, double maxZ, double maxW) {
        this(0, maxX, 0, maxY, 0, maxZ, 0, maxW);
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    @JsonCreator
    public CoordinateSystem4d(@NotNull @JsonProperty("min") Vector4d min, @NotNull @JsonProperty("max") Vector4d max) {
        this(min.x, max.x, min.y, max.y, min.z, max.z, min.w, max.w);
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param max максимальные координаты
     */
    public CoordinateSystem4d(@NotNull Vector4d max) {
        this(0, max.x, 0, max.y, 0, max.z, 0, max.w);
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная вещественная система координат
     */
    public CoordinateSystem4d(@NotNull CoordinateSystem2d coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                0, 0, 0, 0
        );
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная целочисленная система координат
     */
    public CoordinateSystem4d(@NotNull CoordinateSystem2i coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                0, 0, 0, 0
        );
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная вещественная система координат
     */
    public CoordinateSystem4d(@NotNull CoordinateSystem3d coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                coordinateSystem.getMin().z, coordinateSystem.getMax().z,
                0, 0
        );
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная целочисленная система координат
     */
    public CoordinateSystem4d(@NotNull CoordinateSystem3i coordinateSystem) {
        this(
                coordinateSystem.getMin().x, coordinateSystem.getMax().x,
                coordinateSystem.getMin().y, coordinateSystem.getMax().y,
                coordinateSystem.getMin().z, coordinateSystem.getMax().z,
                0, 0
        );
    }

    /**
     * Конструктор ограниченной четырёхмерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная четырёхмерная вещественная система координат
     */
    public CoordinateSystem4d(@NotNull CoordinateSystem4d coordinateSystem) {
        this(
                coordinateSystem.min.x, coordinateSystem.max.x,
                coordinateSystem.min.y, coordinateSystem.max.y,
                coordinateSystem.min.z, coordinateSystem.max.z,
                coordinateSystem.min.w, coordinateSystem.max.w
        );
    }


    /**
     * Задать новый размер СК
     *
     * @param newSize новый размер СК
     */
    public void setNewSize(@NotNull Vector4d newSize) {
        Vector4d sizeDiff = Vector4d.mul(Vector4d.subtract(size, Objects.requireNonNull(newSize)), 0.5);
        min = Vector4d.sum(min, sizeDiff);
        max = Vector4d.subtract(max, sizeDiff);
        size = newSize;
    }


    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    @NotNull
    @JsonIgnore
    public Vector4d getRandomCoords() {
        return Vector4d.rand(min, max);
    }

    /**
     * Обрезать координаты вектора по границам системы координат
     *
     * @param coords координаты вектора
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector4d truncate(@NotNull Vector4d coords) {
        return new Vector4d(
                Math.max(Math.min(coords.x, max.x), min.x),
                Math.max(Math.min(coords.y, max.y), min.y),
                Math.max(Math.min(coords.z, max.z), min.z),
                Math.max(Math.min(coords.w, max.w), min.w)
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
    public Vector4d truncate(@NotNull Vector4d coords, @NotNull Vector4d minPadding, @NotNull Vector4d maxPadding) {
        return new Vector4d(
                Math.max(Math.min(coords.x, max.x - maxPadding.x), min.x - minPadding.x),
                Math.max(Math.min(coords.y, max.y - maxPadding.y), min.y - minPadding.y),
                Math.max(Math.min(coords.z, max.z - maxPadding.z), min.z - minPadding.z),
                Math.max(Math.min(coords.w, max.w - maxPadding.w), min.w - minPadding.w)
        );
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param coords координаты вектора
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(@NotNull Vector4d coords) {
        return Vector4d.biggerOrEqual(coords, min) && Vector4d.lessOrEqual(coords, max);
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @param z координата Z
     * @param w координата W
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(double x, double y, double z, double w) {
        return checkCoords(new Vector4d(x, y, z, w));
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector4d getCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector4d getCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector4d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y,
                0,
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
    public Vector4d getCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector4d getCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector4d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y,
                0,
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
    public Vector4d getCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
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
    public Vector4d getCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector4d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y,
                (z - coordinateSystem.getMin().z) * size.z / coordinateSystem.getSize().z + min.z,
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
    public Vector4d getCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
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
    public Vector4d getCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector4d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y,
                (z - coordinateSystem.getMin().z) * size.z / (coordinateSystem.getSize().z - 1) + min.z,
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
    public Vector4d getCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector4d getCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector4d(
                (x - coordinateSystem.min.x) * size.x / coordinateSystem.size.x + min.x,
                (y - coordinateSystem.min.y) * size.y / coordinateSystem.size.y + min.y,
                (z - coordinateSystem.min.z) * size.z / coordinateSystem.size.z + min.z,
                0
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
    public Vector4d getSize(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector4d getSize(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector4d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y,
                0,
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
    public Vector4d getSize(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector4d getSize(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector4d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1),
                0,
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
    public Vector4d getSize(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector4d getSize(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector4d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y,
                z * size.z / coordinateSystem.getSize().z,
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
    public Vector4d getSize(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector4d getSize(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector4d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1),
                z * size.z / (coordinateSystem.getSize().y - 1),
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
    public Vector4d getSize(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector4d getSize(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector4d(
                x * size.x / coordinateSystem.size.x,
                y * size.y / coordinateSystem.size.y,
                z * size.z / coordinateSystem.size.z,
                0
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
    public Vector4d getSimilarity(@NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector4d getSimilarity(@NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector4d getSimilarity(@NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector4d getSimilarity(@NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector4d getSimilarity(@NotNull CoordinateSystem4d coordinateSystem) {
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
    public int conv(@NotNull Vector4d coords, int divideCnt) {
        Vector4d delta = Vector4d.mul(size, 1.0 / divideCnt);
        Vector4d positivePosD = Vector4d.subtract(Objects.requireNonNull(coords), min);
        Vector4i positivePos = new Vector4i(
                (int) Math.round(positivePosD.x / Precision.round(delta.x, DIGIT_COUNT)),
                (int) Math.round(positivePosD.y / Precision.round(delta.y, DIGIT_COUNT)),
                (int) Math.round(positivePosD.z / Precision.round(delta.z, DIGIT_COUNT)),
                (int) Math.round(positivePosD.w / Precision.round(delta.w, DIGIT_COUNT))
        );
        if (size.y < VECTOR_4D_OPACITY)
            if (size.z < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return positivePos.x;
                else if (size.x < VECTOR_4D_OPACITY)
                    return positivePos.w;
                else
                    return positivePos.x + positivePos.w * (divideCnt + 1);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.x < VECTOR_4D_OPACITY)
                    return positivePos.z;
                else
                    return positivePos.x + positivePos.z * (divideCnt + 1);
            else if (size.x < VECTOR_4D_OPACITY)
                return positivePos.z + (divideCnt + 1) * positivePos.w;
            else
                return positivePos.x + (divideCnt + 1) * positivePos.z + (divideCnt + 1) * (divideCnt + 1) * positivePos.w;

        if (size.z < VECTOR_4D_OPACITY)
            if (size.x < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return positivePos.y;
                else if (size.y < VECTOR_4D_OPACITY)
                    return positivePos.w;
                else
                    return positivePos.y + positivePos.w * (divideCnt + 1);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.y < VECTOR_4D_OPACITY)
                    return positivePos.x;
                else
                    return positivePos.x + positivePos.y * (divideCnt + 1);
            else if (size.y < 0)
                return positivePos.x + (divideCnt + 1) * positivePos.w;
            else
                return positivePos.x + (divideCnt + 1) * positivePos.y + (divideCnt + 1) * (divideCnt + 1) * positivePos.w;

        if (size.x < VECTOR_4D_OPACITY)
            if (size.z < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return positivePos.y;
                else if (size.y < VECTOR_4D_OPACITY)
                    return positivePos.w;
                else
                    return positivePos.y + positivePos.w * (divideCnt + 1);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.y < VECTOR_4D_OPACITY)
                    return positivePos.z;
                else
                    return positivePos.y + positivePos.z * (divideCnt + 1);
            else if (size.y < VECTOR_4D_OPACITY)
                return positivePos.z + (divideCnt + 1) * positivePos.w;
            else
                return positivePos.y + (divideCnt + 1) * positivePos.z + (divideCnt + 1) * (divideCnt + 1) * positivePos.w;

        return positivePos.x + (divideCnt + 1) * positivePos.y + (divideCnt + 1) * (divideCnt + 1) * positivePos.z +
                (divideCnt + 1) * (divideCnt + 1) * (divideCnt + 1) * positivePos.w;
    }

    /**
     * Обратная свёртка числа в вектор
     *
     * @param val       число, из которого нужно получить вектор
     * @param divideCnt количество делений
     * @return вектор, полученный из обратной свёртки числа в вектор
     */
    @NotNull
    public Vector4d deconv(int val, int divideCnt) {
        Vector4d delta = Vector4d.mul(size, 1.0 / divideCnt);
        if (size.y < VECTOR_4D_OPACITY)
            if (size.z < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(val * delta.x, 0, 0, 0), min);
                else if (size.x < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, 0, 0, val * delta.w), min);
                else
                    return Vector4d.sum(new Vector4d((val % (divideCnt + 1)) * delta.x, 0, 0, (val / (divideCnt + 1)) * delta.w), min);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.x < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, 0, val * delta.z, 0), min);
                else
                    return Vector4d.sum(new Vector4d((val % (divideCnt + 1)) * delta.x, 0, (val / (divideCnt + 1)) * delta.z, 0), min);
            else if (size.x < VECTOR_4D_OPACITY)
                return Vector4d.sum(new Vector4d(0, 0, (val % (divideCnt + 1)) * delta.z, (val / (divideCnt + 1)) * delta.w), min);
            else {
                Vector4i positivePos = new Vector4i();
                positivePos.x = (val % (divideCnt + 1));
                val = val / (divideCnt + 1);
                positivePos.z = (val % (divideCnt + 1));
                positivePos.w = (val / (divideCnt + 1));
                Vector4d positivePosD = new Vector4d(
                        positivePos.x * delta.x,
                        positivePos.y * delta.y,
                        positivePos.z * delta.z,
                        positivePos.w * delta.w
                );
                return Vector4d.sum(positivePosD, min);
            }

        if (size.z < VECTOR_4D_OPACITY)
            if (size.x < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, val * delta.y, 0, 0), min);
                else if (size.y < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, 0, 0, val * delta.w), min);
                else
                    return Vector4d.sum(new Vector4d(0, (val % (divideCnt + 1)) * delta.y, 0, (val / (divideCnt + 1)) * delta.w), min);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.y < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(val * delta.x, 0, 0, 0), min);
                else
                    return Vector4d.sum(new Vector4d((val % (divideCnt + 1)) * delta.x, (val / (divideCnt + 1)) * delta.y, 0, 0), min);
            else if (size.y < VECTOR_4D_OPACITY)
                return Vector4d.sum(new Vector4d((val % (divideCnt + 1)) * delta.x, 0, 0, (val / (divideCnt + 1)) * delta.w), min);
            else {
                Vector4i positivePos = new Vector4i();
                positivePos.x = (val % (divideCnt + 1));
                val = val / (divideCnt + 1);
                positivePos.y = (val % (divideCnt + 1));
                positivePos.w = (val / (divideCnt + 1));
                Vector4d positivePosD = new Vector4d(
                        positivePos.x * delta.x,
                        positivePos.y * delta.y,
                        positivePos.z * delta.z,
                        positivePos.w * delta.w
                );
                return Vector4d.sum(positivePosD, min);
            }

        if (size.x < VECTOR_4D_OPACITY)
            if (size.z < VECTOR_4D_OPACITY)
                if (size.w < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, val * delta.y, 0, 0), min);
                else if (size.y < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, 0, 0, val * delta.w), min);
                else
                    return Vector4d.sum(new Vector4d(0, (val % (divideCnt + 1)) * delta.y, 0, (val / (divideCnt + 1)) * delta.w), min);
            else if (size.w < VECTOR_4D_OPACITY)
                if (size.y < VECTOR_4D_OPACITY)
                    return Vector4d.sum(new Vector4d(0, 0, val * delta.z, 0), min);
                else
                    return Vector4d.sum(new Vector4d(0, (val % (divideCnt + 1)) * delta.y, (val / (divideCnt + 1)) * delta.z, 0), min);
            else if (size.y < VECTOR_4D_OPACITY)
                return Vector4d.sum(new Vector4d(0, 0, (val % (divideCnt + 1)) * delta.z, (val / (divideCnt + 1)) * delta.w), min);
            else {
                Vector4i positivePos = new Vector4i();
                positivePos.y = (val % (divideCnt + 1));
                val = val / (divideCnt + 1);
                positivePos.z = (val % (divideCnt + 1));
                positivePos.w = (val / (divideCnt + 1));
                Vector4d positivePosD = new Vector4d(
                        positivePos.x * delta.x,
                        positivePos.y * delta.y,
                        positivePos.z * delta.z,
                        positivePos.w * delta.w
                );
                return Vector4d.sum(positivePosD, min);
            }

        Vector4i positivePos = new Vector4i();
        positivePos.x = (val % (divideCnt + 1));
        val = val / (divideCnt + 1);
        positivePos.y = (val % (divideCnt + 1));
        val = val / (divideCnt + 1);
        positivePos.z = (val % (divideCnt + 1));
        positivePos.w = (val / (divideCnt + 1));
        Vector4d positivePosD = new Vector4d(
                positivePos.x * delta.x,
                positivePos.y * delta.y,
                positivePos.z * delta.z,
                positivePos.w * delta.w
        );
        Vector4d res = Vector4d.sum(positivePosD, min);
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
    public Vector4d getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector4d getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    public Vector4d getSize() {
        return size;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem4d{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem4d{" + getString() + '}';
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

        CoordinateSystem4d that = (CoordinateSystem4d) o;

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

