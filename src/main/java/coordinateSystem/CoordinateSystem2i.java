package coordinateSystem;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import math.*;

import java.util.Objects;

/**
 * Ограниченная двумерная целочисленная система координат
 */
public class CoordinateSystem2i {
    /**
     * максимальная координата
     */
    @NotNull
    private final Vector2i max;
    /**
     * минимальная координата
     */
    @NotNull
    private final Vector2i min;
    /**
     * размер СК
     */
    @NotNull
    @JsonIgnore
    private final Vector2i size;

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param minX минимальная X координата
     * @param maxX максимальная X координата
     * @param minY минимальная Y координата
     * @param maxY максимальная Y координата
     */
    public CoordinateSystem2i(int minX, int maxX, int minY, int maxY) {
        min = new Vector2i(minX, minY);
        max = new Vector2i(maxX, maxY);
        size = Vector2i.subtract(max, min);
        size.inc();
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param maxX максимальная X координата
     * @param maxY максимальная Y координата
     */
    public CoordinateSystem2i(int maxX, int maxY) {
        this(0, maxX, 0, maxY);
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    @JsonCreator
    public CoordinateSystem2i(@NotNull @JsonProperty("min") Vector2i min, @NotNull @JsonProperty("max") Vector2i max) {
        this(min.x, max.x, min.y, max.y);
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param max максимальные координаты
     */
    public CoordinateSystem2i(@NotNull Vector2i max) {
        this(0, max.x, 0, max.y);
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная двумерная вещественная система координат
     */
    public CoordinateSystem2i(@NotNull CoordinateSystem2d coordinateSystem) {
        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y
        );
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная двумерная целочисленная система координат
     */
    public CoordinateSystem2i(@NotNull CoordinateSystem2i coordinateSystem) {
        this(coordinateSystem.min.x, coordinateSystem.max.x, coordinateSystem.min.y, coordinateSystem.max.y);
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная вещественная система координат
     */
    public CoordinateSystem2i(@NotNull CoordinateSystem3d coordinateSystem) {
        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y
        );
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная трёхмерная целочисленная система координат
     */
    public CoordinateSystem2i(@NotNull CoordinateSystem3i coordinateSystem) {
        this(coordinateSystem.getMin().x, coordinateSystem.getMax().x, coordinateSystem.getMin().y, coordinateSystem.getMax().y);
    }

    /**
     * Конструктор ограниченной двумерной целочисленной системы координат
     *
     * @param coordinateSystem ограниченная четырёхмерная вещественная система координат
     */
    public CoordinateSystem2i(@NotNull CoordinateSystem4d coordinateSystem) {
        this(
                (int) coordinateSystem.getMin().x, (int) coordinateSystem.getMax().x,
                (int) coordinateSystem.getMin().y, (int) coordinateSystem.getMax().y
        );
    }

    /**
     * Задать область рисования OpenGL
     *
     * @param gl2 переменная OpenGL
     */
    public void setViewPort(GL2 gl2) {
        gl2.glViewport(min.x, min.y, size.x, size.y);
    }


    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    @NotNull
    @JsonIgnore
    public Vector2i getRandomCoords() {
        return Vector2i.rand(min, max);
    }

    /**
     * Обрезать координаты вектора по границам системы координат
     *
     * @param coords координаты вектора
     * @return вектор с обрезанными координатами
     */
    @NotNull
    public Vector2i truncate(@NotNull Vector2i coords) {
        return new Vector2i(
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
    public Vector2i truncate(@NotNull Vector2i coords, @NotNull Vector2i minPadding, @NotNull Vector2i maxPadding) {
        return new Vector2i(
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
    public boolean checkCoords(@NotNull Vector2i coords) {
        return checkCoords(coords.x, coords.y);
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(int x, int y) {
        return x >= min.x && y >= min.y && x <= max.x && y <= max.y;
    }

    /**
     * Получить координаты вектора в текущей системе координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    @NotNull
    public Vector2i getCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        return getCoords(coords.x, coords.y, coordinateSystem);
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
    public Vector2i getCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector2i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)
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
    public Vector2i getCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coordinateSystem);
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
    public Vector2i getCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector2i(
                (x - coordinateSystem.min.x) * (size.x - 1) / (coordinateSystem.size.x - 1) + min.x,
                (y - coordinateSystem.min.y) * (size.y - 1) / (coordinateSystem.size.y - 1) + min.y
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
    public Vector2i getCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, Objects.requireNonNull(coordinateSystem));
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
    public Vector2i getCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector2i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)
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
    public Vector2i getCoords(@NotNull Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2i getCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector2i(
                (x - coordinateSystem.getMin().x) * (size.x - 1) / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * (size.y - 1) / (coordinateSystem.getSize().y - 1) + min.y
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
    public Vector2i getCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coords.w, Objects.requireNonNull(coordinateSystem));
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
    public Vector2i getCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector2i(
                (int) ((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x),
                (int) ((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y)
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
    public Vector2i getFloorCoords(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2i getFloorCoords(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector2i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y))
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
    public Vector2i getFloorCoords(@NotNull Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2i getFloorCoords(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector2i(
                (int) Math.floor((double) (x - coordinateSystem.min.x) * (size.x - 1) / (coordinateSystem.size.x - 1) + min.x),
                (int) Math.floor((double) (y - coordinateSystem.min.y) * (size.y - 1) / (coordinateSystem.size.y - 1) + min.y)
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
    public Vector2i getFloorCoords(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        return getCoords(coords.x, coords.y, coords.z, coordinateSystem);
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
    public Vector2i getFloorCoords(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector2i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y))
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
    public Vector2i getFloorCoords(Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2i getFloorCoords(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector2i(
                (int) Math.floor(
                        (double) (x - coordinateSystem.getMin().x) * (size.x - 1) / (coordinateSystem.getSize().x - 1) + min.x
                ),
                (int) Math.floor(
                        (double) (y - coordinateSystem.getMin().y) * (size.y - 1) / (coordinateSystem.getSize().y - 1) + min.y
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
    public Vector2i getFloorCoords(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector2i getFloorCoords(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector2i(
                (int) Math.floor(((x - coordinateSystem.getMin().x) * (size.x - 1) / coordinateSystem.getSize().x + min.x)),
                (int) Math.floor(((y - coordinateSystem.getMin().y) * (size.y - 1) / coordinateSystem.getSize().y + min.y))
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
    public Vector2i getSize(Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2i getSize(double x, double y, @NotNull CoordinateSystem2d coordinateSystem) {
        return new Vector2i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y)
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
    public Vector2i getSize(Vector2i coords, @NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2i getSize(int x, int y, @NotNull CoordinateSystem2i coordinateSystem) {
        return new Vector2i(
                x * (size.x - 1) / (coordinateSystem.size.x - 1),
                y * (size.y - 1) / (coordinateSystem.size.y - 1)
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
    public Vector2i getSize(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector2i getSize(double x, double y, double z, @NotNull CoordinateSystem3d coordinateSystem) {
        return new Vector2i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y)
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
    public Vector2i getSize(Vector3i coords, @NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2i getSize(int x, int y, int z, @NotNull CoordinateSystem3i coordinateSystem) {
        return new Vector2i(
                x * (size.x - 1) / (coordinateSystem.getSize().x - 1),
                y * (size.y - 1) / (coordinateSystem.getSize().y - 1)
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
    public Vector2i getSize(@NotNull Vector4d coords, @NotNull CoordinateSystem4d coordinateSystem) {
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
    public Vector2i getSize(double x, double y, double z, double w, @NotNull CoordinateSystem4d coordinateSystem) {
        return new Vector2i(
                (int) (x * (size.x - 1) / coordinateSystem.getSize().x),
                (int) (y * (size.y - 1) / coordinateSystem.getSize().y)
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
    public Vector2i getSimilarity(@NotNull CoordinateSystem2d coordinateSystem) {
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
    public Vector2i getSimilarity(@NotNull CoordinateSystem2i coordinateSystem) {
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
    public Vector2i getSimilarity(@NotNull CoordinateSystem3d coordinateSystem) {
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
    public Vector2i getSimilarity(@NotNull CoordinateSystem3i coordinateSystem) {
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
    public Vector2i getSimilarity(@NotNull CoordinateSystem4d coordinateSystem) {
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
    public int conv(@NotNull Vector2i coords) {
        Vector2i positivePos = Vector2i.subtract(Objects.requireNonNull(coords), min);
        if (size.x == 1)
            return positivePos.y;
        return positivePos.x + size.x * positivePos.y;
    }

    /**
     * Обратная свёртка числа в вектор
     *
     * @param val число, из которого нужно получить вектор
     * @return вектор, полученный из обратной свёртки числа в вектор
     */
    @NotNull
    public Vector2i deconv(int val) {
        Vector2i positivePos = new Vector2i();
        if (size.x == 1) {
            positivePos.x = 0;
            positivePos.y = val;
        } else {
            positivePos.x = val % size.x;
            positivePos.y = val / size.x;
        }
        Vector2i res = Vector2i.sum(positivePos, min);
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
    public Vector2i getNearestPoint(@NotNull Vector2d coords, @NotNull CoordinateSystem2d coordinateSystem) {
        Vector2i newPos = getCoords(Objects.requireNonNull(coords), Objects.requireNonNull(coordinateSystem));
        double minDistance = 0;
        Vector2i intMinDistanceCoords = null;
        // перебираем полученное значение от прямой конвертации и всех его ближайших соседей
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                Vector2i intLoopCoords = new Vector2i(newPos.x + i, newPos.y + j);
                if (checkCoords(intLoopCoords)) {
                    Vector2d doubleLoopCoords = coordinateSystem.getCoords(intLoopCoords, this);
                    if (intMinDistanceCoords == null) {
                        minDistance = Vector2d.getDistance(doubleLoopCoords, coords);
                        intMinDistanceCoords = intLoopCoords;
                    } else {
                        double localDelta = Vector2d.getDistance(doubleLoopCoords, coords);
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
     * Получить ближайшую точку, соответствующую одной из точек на целочисленной СК, в СК вещественной
     *
     * @param coords           координаты, близость к которым при переводе в вещественную СК
     *                         является критерием отбора точки в целочисленной СК
     * @param coordinateSystem вещественная СК
     * @return точка на целочисленной ск
     */
    @NotNull
    public Vector2i getNearestPoint(@NotNull Vector3d coords, @NotNull CoordinateSystem3d coordinateSystem) {
        Vector2i newPos = getCoords(Objects.requireNonNull(coords), Objects.requireNonNull(coordinateSystem));
        double minDistance = 0;
        Vector2i intMinDistanceCoords = null;
        // перебираем полученное значение от прямой конвертации и всех его ближайших соседей
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                Vector2i intLoopCoords = new Vector2i(newPos.x + i, newPos.y + j);
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
    public Vector2i getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    @NotNull
    public Vector2i getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    @NotNull
    public Vector2i getSize() {
        return size;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem2i{getString()}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem2i{" + getString() + '}';
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

        CoordinateSystem2i that = (CoordinateSystem2i) o;

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
