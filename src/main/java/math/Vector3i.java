package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс трёхмерного вектора int
 */
public class Vector3i {
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
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     * @param z z - координата вектора
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Конструктор копии
     *
     * @param v исходный вкетор
     */
    public Vector3i(@NotNull Vector3i v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Возвращает нулевой вектор {0,0,0}
     *
     * @return нулевой вектор {0,0,0}
     */
    @NotNull
    public static Vector3i zeros() {
        return new Vector3i(0, 0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1,1}
     *
     * @return единичный вектор {1,1,1}
     */
    @NotNull
    public static Vector3i ones() {
        return new Vector3i(1, 1, 1);
    }


    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector3i min(@NotNull Vector3i a, @NotNull Vector3i b) {
        return new Vector3i(
                Math.min(a.x, b.x),
                Math.min(a.y, b.y),
                Math.min(a.z, b.z)
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
    public static Vector3i max(@NotNull Vector3i a, @NotNull Vector3i b) {
        return new Vector3i(
                Math.max(a.x, b.x),
                Math.max(a.y, b.y),
                Math.max(a.z, b.z)
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
    public static Vector3i sum(@NotNull Vector3i a, @NotNull Vector3i b) {
        return new Vector3i(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector3i sum(Vector3i... arr) {
        Vector3i newVector = new Vector3i();

        for (Vector3i v : arr)
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
    public static Vector3i subtract(@NotNull Vector3i a, @NotNull Vector3i b) {
        return new Vector3i(a.x - b.x, a.y - b.y, a.z - b.z);
    }


    /**
     * Прибавить к вектору единичный
     *
     * @param a вектор, к которому прибавляем
     * @return сумма единичного вектора и переданного в параметрах
     */
    @NotNull
    public static Vector3i inc(@NotNull Vector3i a) {
        return sum(a, ones());
    }

    /**
     * Вычесть из вектора единичный
     *
     * @param a вектор, из которого вычетаем
     * @return разность переданного в параметрах вектора и единичного
     */
    @NotNull
    public static Vector3i dec(@NotNull Vector3i a) {
        return subtract(a, ones());
    }

    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector3i a, @NotNull Vector3i b) {
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
    public static Vector3i mul(@NotNull Vector3i v, int a) {
        return new Vector3i(v.x * a, v.y * a, v.z * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector3i mul(@NotNull Vector3i v1, @NotNull Vector3i v2) {
        return new Vector3i(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static int dot(@NotNull Vector3i a, @NotNull Vector3i b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector3i a, @NotNull Vector3i b) {
        return a.x >= b.x && a.y >= b.y && a.z >= b.z;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector3i a, @NotNull Vector3i b) {
        return a.x - b.x > 0 &&
                a.y - b.y > 0 &&
                a.z - b.z > 0;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector3i a, @NotNull Vector3i b) {
        return a.x <= b.x && a.y <= b.y && a.z <= b.z;
    }


    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector3i a, @NotNull Vector3i b) {
        return bigger(b, a);
    }

    /**
     * Сместиться на единичное расстояние в случайном направлении
     *
     * @param coords текущие координаты, от которых нужно сместиться
     * @return новые координаты со смещением в случайном направлении
     */
    @NotNull
    public static Vector3i offsetRandomDirection(@NotNull Vector3i coords) {
        return offsetRandomDirection(coords, 1);
    }

    /**
     * Сместиться на заданное расстояние в случайном направлении
     *
     * @param coords текущие координаты, от которых нужно сместиться
     * @param val    текущие координаты, от которых нужно сместиться
     * @return новые координаты со смещением в случайном направлении
     */
    @NotNull
    public static Vector3i offsetRandomDirection(@NotNull Vector3i coords, int val) {
        Vector3i newCoords = new Vector3i(coords);
        switch (ThreadLocalRandom.current().nextInt(6)) {
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
    public static Vector3i rand(@NotNull Vector3i min, @NotNull Vector3i max) {
        return new Vector3i(
                ThreadLocalRandom.current().nextInt(min.x, max.x),
                ThreadLocalRandom.current().nextInt(min.y, max.y),
                ThreadLocalRandom.current().nextInt(min.z, max.z)
        );
    }

    /**
     * Увеличить каждую из координат на 1
     */
    public void inc() {
        this.x++;
        this.y++;
        this.z++;
    }

    /**
     * уменьшить каждую из координат на 1
     */
    public void dec() {
        this.x--;
        this.y--;
        this.z--;
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector3i v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        this.z = this.z + v.z;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector3i v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
        this.z = this.z - v.z;
    }

    /**
     * Скалярное уможение на вектор
     *
     * @param v вектор, на который нужно умножить
     * @return салярное уможение на вектор
     */
    public int dot(@NotNull Vector3i v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
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
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector3i v) {
        this.x = this.x * v.x;
        this.y = this.y * v.y;
        this.z = this.z * v.z;
    }

    /**
     * Получить длину вектора
     *
     * @return длина вектора
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }


    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ", " + z +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3i vector3i = (Vector3i) o;

        if (x != vector3i.x) return false;
        if (y != vector3i.y) return false;
        return z == vector3i.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector3i getJOML() {
        return new org.joml.Vector3i(x, y, z);
    }

}
