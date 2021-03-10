package coordinateSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import math.*;
import org.apache.commons.math3.util.Precision;

import java.util.Objects;

import static math.Vector2d.VECTOR_2D_OPACITY;

/**
 * Ограниченная двумерная вещественная система координат
 */
public class CoordinateSystem2d {
    /**
     * Кол-во знаков после запятой
     */
    private static final int DIGIT_COUNT = 6;
    /**
     * максимальная координата
     */
    @NotNull
    private final Vector2d max;
    /**
     * минимальная координата
     */
    @NotNull
    private final Vector2d min;
    /**
     * размер СК
     */
    @NotNull
    @JsonIgnore
    private Vector2d size;

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param minX минимальная X координата
     * @param maxX максимальная X координата
     * @param minY минимальная Y координата
     * @param maxY максимальная Y координата
     */
    public CoordinateSystem2d(double minX, double maxX, double minY, double maxY) {
        min = new Vector2d(minX, minY);
        max = new Vector2d(maxX, maxY);
        size = Vector2d.subtract(max, min);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     */
    public CoordinateSystem2d(double maxX, double maxY) {
        this(0, maxX, 0, maxY);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    @JsonCreator
    public CoordinateSystem2d(@JsonProperty("min") Vector2d min, @JsonProperty("max") Vector2d max) {
        this(min.x, max.x, min.y, max.y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param max максимальные координаты
     */
    public CoordinateSystem2d(@NotNull Vector2d max) {
        this(0, max.x, 0, max.y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная вещественная система координат
     */
    public CoordinateSystem2d(@NotNull CoordinateSystem2d coordinateSystem) {
        this(coordinateSystem.min.x, coordinateSystem.max.x, coordinateSystem.min.y, coordinateSystem.max.y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная двумерная целочисленная система координат
     */
    public CoordinateSystem2d(@NotNull CoordinateSystem2i coordinateSystem) {
        this(coordinateSystem.getMin().x, coordinateSystem.getMax().x, coordinateSystem.getMin().y, coordinateSystem.getMax().y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная вещественная система координат
     */
    public CoordinateSystem2d(@NotNull CoordinateSystem3d coordinateSystem) {
        this(coordinateSystem.getMin().x, coordinateSystem.getMax().x, coordinateSystem.getMin().y, coordinateSystem.getMax().y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная целочисленная система координат
     */
    public CoordinateSystem2d(@NotNull CoordinateSystem3i coordinateSystem) {
        this(coordinateSystem.getMin().x, coordinateSystem.getMax().x, coordinateSystem.getMin().y, coordinateSystem.getMax().y);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param coordinateSystem ограниченная четырёхмерная вещественная система координат
     */
    public CoordinateSystem2d(@NotNull CoordinateSystem4d coordinateSystem) {
        this(coordinateSystem.getMin().x, coordinateSystem.getMax().x, coordinateSystem.getMin().y, coordinateSystem.getMax().y);
    }

    /**
     * Получить квадратную СК из исходной прямоугольной
     *
     * @param distortion оотношение ширины СК к высоте
     * @return квадратная СК
     */
    @NotNull
    public CoordinateSystem2d getQuadCS(double distortion) {
        return new CoordinateSystem2d(
                min.x,
                max.x,
                min.y + size.y * (distortion * 2 - 1) / (4 * distortion),
                min.y + size.y - size.y * (distortion * 2 - 1) / (4 * distortion)
        );
    }


    /**
     * Задать новый размер СК
     *
     * @param newSize новый размер СК
     */
    public void setNewSize(@NotNull Vector2d newSize) {
        Vector2d sizeDiff = Vector2d.mul(Vector2d.subtract(size, Objects.requireNonNull(newSize)), 0.5);
        min.add(sizeDiff);
        max.subtract(sizeDiff);
        size = newSize;
    }

    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    @NotNull
    @JsonIgnore
    public Vector2d getRandomCoords() {
        return Vector2d.rand(min, max);
    }

    /**
     * Обрезать координаты вектора по границам системы координат
     *
     * @param coords координаты вектора
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector2d truncate(@NotNull Vector2d coords) {
        return new Vector2d(
                Math.max(Math.min(coords.x, max.x), min.x),
                Math.max(Math.min(coords.y, max.y), min.y)
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
    public Vector2d truncate(@NotNull Vector2d coords, @NotNull Vector2d minPadding, @NotNull Vector2d maxPadding) {
        return new Vector2d(
                Math.max(Math.min(coords.x, max.x - maxPadding.x), min.x - minPadding.x),
                Math.max(Math.min(coords.y, max.y - maxPadding.y), min.y - minPadding.y)
        );
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param coords координаты вектора
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(@NotNull Vector2d coords) {
        return Vector2d.biggerOrEqual(Objects.requireNonNull(coords), min) && Vector2d.lessOrEqual(coords, max);
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(double x, double y) {
        return checkCoords(new Vector2d(x, y));
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector2d getCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2d getCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.min.x) * size.x / coordinateSystem.size.x + min.x,
                (y - coordinateSystem.min.y) * size.y / coordinateSystem.size.y + min.y
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
    public Vector2d getCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2d getCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y
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
    public Vector2d getCoords(Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector2d getCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y
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
    public Vector2d getCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2d getCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y
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
    public Vector2d getCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector2d getCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.getMin().x) * size.x / coordinateSystem.getSize().x + min.x,
                (y - coordinateSystem.getMin().y) * size.y / coordinateSystem.getSize().y + min.y
        );
    }

    /**
     * Получить размеры объекта в текущей систему координат
     *
     * @param coords           размеры объектаа в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return размеры объекта в текущей системе координат
     */
    public Vector2d getSize(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2d getSize(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector2d(
                x * size.x / coordinateSystem.size.x,
                y * size.y / coordinateSystem.size.y
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
    public Vector2d getSize(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2d getSize(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector2d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1)
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
    public Vector2d getSize(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector2d getSize(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector2d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y
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
    public Vector2d getSize(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2d getSize(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector2d(
                x * size.x / (coordinateSystem.getSize().x - 1),
                y * size.y / (coordinateSystem.getSize().y - 1)
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
    public Vector2d getSize(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector2d getSize(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector2d(
                x * size.x / coordinateSystem.getSize().x,
                y * size.y / coordinateSystem.getSize().y
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
    public Vector2d getSimilarity(@NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2d getSimilarity(@NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2d getSimilarity(@NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector2d getSimilarity(@NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2d getSimilarity(@NotNull CoordinateSystem4d coordinateSystem) {
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
    public int conv(@NotNull Vector2d coords, int divideCnt) {
        Vector2d delta = Vector2d.mul(size, 1.0 / divideCnt);
        Vector2d positivePosD = Vector2d.subtract(Objects.requireNonNull(coords), min);
        Vector2i positivePos = new Vector2i(
                (int) Math.round(positivePosD.x / Precision.round(delta.x, DIGIT_COUNT)),
                (int) Math.round(positivePosD.y / Precision.round(delta.y, DIGIT_COUNT))
        );
        if (size.y < VECTOR_2D_OPACITY)
            return positivePos.x;

        if (size.x < VECTOR_2D_OPACITY)
            return positivePos.y;

        return positivePos.x + (divideCnt + 1) * positivePos.y;
    }

    /**
     * Обратная свёртка числа в вектор
     *
     * @param val       число, из которого нужно получить вектор
     * @param divideCnt количество делений
     * @return вектор, полученный из обратной свёртки числа в вектор
     */
    public Vector2d deconv(int val, int divideCnt) {
        Vector2d delta = Vector2d.mul(size, 1.0 / divideCnt);
        if (size.y < VECTOR_2D_OPACITY)
            return new Vector2d(val * delta.x + min.x, min.y);

        if (size.x < VECTOR_2D_OPACITY)
            return new Vector2d(min.x, val * delta.y + min.y);

        Vector2i positivePos = new Vector2i();
        positivePos.x = (val % (divideCnt + 1));
        positivePos.y = (val / (divideCnt + 1));
        Vector2d res = new Vector2d(
                positivePos.x * delta.x + min.x,
                positivePos.y * delta.y + min.y
        );
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
    public Vector2d getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector2d getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    public Vector2d getSize() {
        return size;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem2d{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem2d{" + getString() + '}';
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

        CoordinateSystem2d that = (CoordinateSystem2d) o;

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
