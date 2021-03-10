package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс четырёхмерного вектора int
 */
public class Vector4i {
    /**
     * x - координата вектора
     */
    public int x;
    /**
     * y - координата вектора
     */
    public int y;
    /**
     * z - координата вектора
     */
    public int z;
    /**
     * w - координата вектора
     */
    public int w;

    /**
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     * @param z z - координата вектора
     * @param w w - координата вектора
     */
    public Vector4i(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector4i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    /**
     * Конструктор копии
     *
     * @param v исходный вкетор
     */
    public Vector4i(@NotNull Vector4i v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    /**
     * Возвращает нулевой вектор {0,0,0}
     *
     * @return нулевой вектор {0,0,0}
     */
    @NotNull
    public static Vector4i zeros() {
        return new Vector4i(0, 0, 0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1,1}
     *
     * @return единичный вектор {1,1,1}
     */
    @NotNull
    public static Vector4i ones() {
        return new Vector4i(1, 1, 1, 1);
    }


    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector4i min(@NotNull Vector4i a, @NotNull Vector4i b) {
        return new Vector4i(
                Math.min(a.x, b.x),
                Math.min(a.y, b.y),
                Math.min(a.z, b.z),
                Math.min(a.w, b.w)
        );
    }

    /**
     * Получить вектор с максимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector4i max(@NotNull Vector4i a, @NotNull Vector4i b) {
        return new Vector4i(
                Math.max(a.x, b.x),
                Math.max(a.y, b.y),
                Math.max(a.z, b.z),
                Math.max(a.w, b.w)
        );
    }

    /**
     * Сложить два вектора
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector4i sum(@NotNull Vector4i a, @NotNull Vector4i b) {
        return new Vector4i(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector4i sum(Vector4i... arr) {
        Vector4i newVector = new Vector4i();

        for (Vector4i v : arr)
            newVector.add(v);

        return newVector;
    }

    /**
     * Вычесть второй вектор из первого
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return разность двух векторов
     */
    @NotNull
    public static Vector4i subtract(@NotNull Vector4i a, @NotNull Vector4i b) {
        return new Vector4i(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
    }


    /**
     * Прибавить к вектору единичный
     *
     * @param a вектор, к которому прибавляем
     * @return сумма единичного вектора и переданного в параметрах
     */
    @NotNull
    public static Vector4i inc(@NotNull Vector4i a) {
        return sum(a, ones());
    }

    /**
     * Вычесть из вектора единичный
     *
     * @param a вектор, из которого вычетаем
     * @return разность переданного в параметрах вектора и единичного
     */
    @NotNull
    public static Vector4i dec(@NotNull Vector4i a) {
        return subtract(a, ones());
    }

    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector4i a, @NotNull Vector4i b) {
        return subtract(a, b).length();
    }

    /**
     * Умножение вектора на число
     *
     * @param v вектор
     * @param a число
     * @return результат умножения вектора на число
     */
    @NotNull
    public static Vector4i mul(@NotNull Vector4i v, int a) {
        return new Vector4i(v.x * a, v.y * a, v.z * a, v.w * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector4i mul(@NotNull Vector4i v1, @NotNull Vector4i v2) {
        return new Vector4i(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z, v1.w * v2.w);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static int dot(@NotNull Vector4i a, @NotNull Vector4i b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector4i a, @NotNull Vector4i b) {
        return a.x >= b.x && a.y >= b.y && a.z >= b.z && a.w >= b.w;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector4i a, @NotNull Vector4i b) {
        return a.x - b.x > 0 &&
                a.y - b.y > 0 &&
                a.z - b.z > 0 &&
                a.w - b.w > 0;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector4i a, @NotNull Vector4i b) {
        return a.x <= b.x && a.y <= b.y && a.z <= b.z && a.w <= b.w;
    }

    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector4i a, @NotNull Vector4i b) {
        return bigger(b, a);
    }

    /**
     * Сместиться на заданное расстояние в случайном направлении
     *
     * @param coords текущие координаты, от которых нужно сместиться
     * @param val    текущие координаты, от которых нужно сместиться
     * @return новые координаты со смещением в случайном направлении
     */
    @NotNull
    public static Vector4i offsetRandomDirection(@NotNull Vector4i coords, int val) {
        Vector4i newCoords = new Vector4i(coords);
        switch (ThreadLocalRandom.current().nextInt(8)) {
            case 0:
                newCoords.x += val;
                break;
            case 1:
                newCoords.x -= val;
                break;
            case 2:
                newCoords.y += val;
                break;
            case 3:
                newCoords.y -= val;
                break;
            case 4:
                newCoords.z += val;
                break;
            case 5:
                newCoords.z -= val;
                break;
            case 6:
                newCoords.w += val;
                break;
            case 7:
                newCoords.w -= val;
                break;
        }
        return newCoords;
    }

    /**
     * Получить случайное значение в заданном диапазоне [min,max)
     *
     * @param min нижняя граница
     * @param max верхняя граница
     * @return случайное значение в заданном диапазоне [min,max)
     */
    @NotNull
    public static Vector4i rand(@NotNull Vector4i min, @NotNull Vector4i max) {
        return new Vector4i(
                ThreadLocalRandom.current().nextInt(min.x, max.x),
                ThreadLocalRandom.current().nextInt(min.y, max.y),
                ThreadLocalRandom.current().nextInt(min.z, max.z),
                ThreadLocalRandom.current().nextInt(min.w, max.w)
        );
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector4i v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        this.z = this.z + v.z;
        this.w = this.w + v.w;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector4i v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
        this.z = this.z - v.z;
        this.w = this.w - v.w;
    }

    /**
     * Увеличить каждую из координат на 1
     */
    public void inc() {
        this.x++;
        this.y++;
        this.z++;
        this.w++;
    }

    /**
     * уменьшить каждую из координат на 1
     */
    public void dec() {
        this.x--;
        this.y--;
        this.z--;
        this.w--;
    }

    /**
     * Скалярное уможение на вектор
     *
     * @param v вектор, на который нужно умножить
     * @return салярное уможение на вектор
     */
    public double dot(@NotNull Vector4i v) {
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    /**
     * Поэлементное умножение на скаляр
     *
     * @param v число, на которое нужно умножить
     */
    public void mul(int v) {
        this.x = this.x * v;
        this.y = this.y * v;
        this.z = this.z * v;
        this.w = this.w * v;
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector4i v) {
        this.x = this.x * v.x;
        this.y = this.y * v.y;
        this.z = this.z * v.z;
        this.w = this.w * v.w;
    }

    /**
     * Получить длину вектора
     *
     * @return длина вектора
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }


    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ", " + z +
                ", " + w +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector4i vector4i = (Vector4i) o;

        if (x != vector4i.x) return false;
        if (y != vector4i.y) return false;
        if (z != vector4i.z) return false;
        return w == vector4i.w;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + w;
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector4i getJOML() {
        return new org.joml.Vector4i(x, y, z, w);
    }
}
