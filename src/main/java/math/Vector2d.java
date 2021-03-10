package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс двумерного вектора double
 */
public class Vector2d {
    /**
     * Точность вещественных вычислений
     */
    public static final double VECTOR_2D_OPACITY = 0.000001;
    /**
     * x - координата вектора
     */
    public double x;
    /**
     * y - координата вектора
     */
    public double y;

    /**
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Конструктор копии
     *
     * @param v исходный вкетор
     */
    public Vector2d(@NotNull Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Возвращает нулевой вектор {0,0}
     *
     * @return нулевой вектор {0,0}
     */
    @NotNull
    public static Vector2d zeros() {
        return new Vector2d(0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1}
     *
     * @return единичный вектор {1,1}
     */
    @NotNull
    public static Vector2d ones() {
        return new Vector2d(1, 1);
    }

    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector2d min(@NotNull Vector2d a, @NotNull Vector2d b) {
        return new Vector2d(
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
    public static Vector2d max(@NotNull Vector2d a, @NotNull Vector2d b) {
        return new Vector2d(
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
    public static Vector2d sum(@NotNull Vector2d a, @NotNull Vector2d b) {
        return new Vector2d(a.x + b.x, a.y + b.y);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    public static @NotNull
    Vector2d sum(Vector2d... arr) {
        Vector2d newVector = new Vector2d();

        for (Vector2d v : arr)
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
    public static @NotNull
    Vector2d subtract(@NotNull Vector2d a, @NotNull Vector2d b) {
        return new Vector2d(a.x - b.x, a.y - b.y);
    }

    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector2d a, @NotNull Vector2d b) {
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
    public static Vector2d mul(@NotNull Vector2d v, double a) {
        return new Vector2d(v.x * a, v.y * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector2d mul(@NotNull Vector2d v1, @NotNull Vector2d v2) {
        return new Vector2d(v1.x * v2.x, v1.y * v2.y);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static double dot(@NotNull Vector2d a, @NotNull Vector2d b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * Получить нормализованный вектор из исходного
     *
     * @param v исходный вектор
     * @return нормализованный вектор из исходного
     */
    @NotNull
    public static Vector2d norm(@NotNull Vector2d v) {
        return mul(v, 1 / v.length());
    }

    /**
     * Получить нормализованный вектор из исходного
     *
     * @param v исходный вектор
     * @param a длина нового вектора
     * @return нормализованный вектор из исходного
     */
    @NotNull
    public static Vector2d norm(@NotNull Vector2d v, double a) {
        return mul(v, 1 / v.length() * a);
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector2d a, @NotNull Vector2d b) {
        return a.x - b.x >= -VECTOR_2D_OPACITY && a.y - b.y >= -VECTOR_2D_OPACITY;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector2d a, @NotNull Vector2d b) {
        return a.x - b.x > 0 && a.y - b.y > 0;
    }

    /**
     * Проверка меньше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector2d a, @NotNull Vector2d b) {
        return biggerOrEqual(b, a);
    }

    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector2d a, @NotNull Vector2d b) {
        return bigger(b, a);
    }

    /**
     * Получить случайное значение в заданном диапазоне [min,max)
     *
     * @param min нижняя граница
     * @param max верхняя граница
     * @return случайное значение в заданном диапазоне [min,max)
     */
    @NotNull
    public static Vector2d rand(@NotNull Vector2d min, @NotNull Vector2d max) {
        return new Vector2d(
                ThreadLocalRandom.current().nextDouble(min.x, max.x),
                ThreadLocalRandom.current().nextDouble(min.y, max.y)
        );
    }

    /**
     * Получить случайный вектор заданной длины
     *
     * @param length длина нового вектора
     * @return случайный вектор заданной длины
     */
    @NotNull
    public static Vector2d rand(double length) {
        double x = ThreadLocalRandom.current().nextDouble(length);
        double y = Math.sqrt(length * length - x * x);
        return new Vector2d(x, y);
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector2d v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector2d v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
    }

    /**
     * Скалярное уможение на вектор
     *
     * @param v вектор, на который нужно умножить
     * @return салярное уможение на вектор
     */
    public double dot(@NotNull Vector2d v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Поэлементное умножение на скаляр
     *
     * @param v число, на которое нужно умножить
     */
    public void mul(double v) {
        this.x = this.x * v;
        this.y = this.y * v;
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector2d v) {
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

    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector2d norm() {
        return mul(this, 1 / this.length());
    }

    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @param a множитель
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector2d norm(double a) {
        return mul(this, 1 / this.length() * a);
    }

    /**
     * Нормализовать текущий вектор
     */
    public void normalize() {
        this.mul(1 / length());
    }

    /**
     * Нормализовать текущий вектор
     *
     * @param a множитель
     */
    public void normalize(double a) {
        this.mul(1 / length() * a);
    }


    @Override
    public String toString() {
        return "(" + String.format("%.2f", x) +
                ", " + String.format("%.2f", y) +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2d vector2d = (Vector2d) o;

        if (Double.compare(vector2d.x, x) != 0) return false;
        return Double.compare(vector2d.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector2d getJOML() {
        return new org.joml.Vector2d(x, y);
    }
}
