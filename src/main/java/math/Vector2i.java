package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс двумерного вектора int
 */
public class Vector2i {
    /**
     * x - координата вектора
     */
    public int x;
    /**
     * y - координата вектора
     */
    public int y;

    /**
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Конструктор копии
     *
     * @param v исходный вкетор
     */
    public Vector2i(@NotNull Vector2i v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Возвращает нулевой вектор {0,0}
     *
     * @return нулевой вектор {0,0}
     */
    @NotNull
    public static Vector2i zeros() {
        return new Vector2i(0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1}
     *
     * @return единичный вектор {1,1}
     */
    @NotNull
    public static Vector2i ones() {
        return new Vector2i(1, 1);
    }

    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector2i min(@NotNull Vector2i a, @NotNull Vector2i b) {
        return new Vector2i(
                Math.min(a.x, b.x),
                Math.min(a.y, b.y)
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
    public static Vector2i max(@NotNull Vector2i a, @NotNull Vector2i b) {
        return new Vector2i(
                Math.max(a.x, b.x),
                Math.max(a.y, b.y)
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
    public static Vector2i sum(@NotNull Vector2i a, @NotNull Vector2i b) {
        return new Vector2i(a.x + b.x, a.y + b.y);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector2i sum(Vector2i... arr) {
        Vector2i newVector = new Vector2i();

        for (Vector2i v : arr)
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
    public static Vector2i subtract(@NotNull Vector2i a, @NotNull Vector2i b) {
        return new Vector2i(a.x - b.x, a.y - b.y);
    }

    /**
     * Прибавить к вектору единичный
     *
     * @param a вектор, к которому прибавляем
     * @return сумма единичного вектора и переданного в параметрах
     */
    @NotNull
    public static Vector2i inc(@NotNull Vector2i a) {
        return sum(a, ones());
    }

    /**
     * Вычесть из вектора единичный
     *
     * @param a вектор, из которого вычетаем
     * @return разность переданного в параметрах вектора и единичного
     */
    @NotNull
    public static Vector2i dec(@NotNull Vector2i a) {
        return subtract(a, ones());
    }


    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector2i a, @NotNull Vector2i b) {
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
    public static Vector2i mul(@NotNull Vector2i v, int a) {
        return new Vector2i(v.x * a, v.y * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector2i mul(@NotNull Vector2i v1, @NotNull Vector2i v2) {
        return new Vector2i(v1.x * v2.x, v1.y * v2.y);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static int dot(@NotNull Vector2i a, @NotNull Vector2i b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector2i a, @NotNull Vector2i b) {
        return a.x >= b.x && a.y >= b.y;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector2i a, @NotNull Vector2i b) {
        return a.x - b.x > 0 && a.y - b.y > 0;
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector2i a, @NotNull Vector2i b) {
        return a.x <= b.x && a.y <= b.y;
    }

    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector2i a, @NotNull Vector2i b) {
        return bigger(b, a);
    }

    /**
     * Сместиться на единичное расстояние в случайном направлении
     *
     * @param coords текущие координаты, от которых нужно сместиться
     * @return новые координаты со смещением в случайном направлении
     */
    @NotNull
    public static Vector2i offsetRandomDirection(@NotNull Vector2i coords) {
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
    public static Vector2i offsetRandomDirection(@NotNull Vector2i coords, int val) {
        Vector2i newCoords = new Vector2i(coords);
        switch (ThreadLocalRandom.current().nextInt(4)) {
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
    public static Vector2i rand(@NotNull Vector2i min, @NotNull Vector2i max) {
        return new Vector2i(
                ThreadLocalRandom.current().nextInt(min.x, max.x),
                ThreadLocalRandom.current().nextInt(min.y, max.y)
        );
    }

    /**
     * Увеличить каждую из координат на 1
     */
    public void inc() {
        this.x++;
        this.y++;
    }

    /**
     * уменьшить каждую из координат на 1
     */
    public void dec() {
        this.x--;
        this.y--;
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector2i v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector2i v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
    }

    /**
     * Скалярное уможение на вектор
     *
     * @param v вектор, на который нужно умножить
     * @return салярное уможение на вектор
     */
    public int dot(@NotNull Vector2i v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Поэлементное умножение на скаляр
     *
     * @param v число, на которое нужно умножить
     */
    public void mul(int v) {
        this.x = this.x * v;
        this.y = this.y * v;
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector2i v) {
        this.x = this.x * v.x;
        this.y = this.y * v.y;
    }

    /**
     * Получить длину вектора
     *
     * @return длина вектора
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2i vector2i = (Vector2i) o;

        if (x != vector2i.x) return false;
        return y == vector2i.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector2i getJOML() {
        return new org.joml.Vector2i(x, y);
    }
}
