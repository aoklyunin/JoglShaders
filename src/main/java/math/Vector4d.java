package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс четырёхмерного вектора double
 */
public class Vector4d {
    /**
     * Точность вещественных вычислений
     */
    public static final double VECTOR_4D_OPACITY = 0.000001;
    /**
     * x - координата вектора
     */
    public double x;
    /**
     * y - координата вектора
     */
    public double y;
    /**
     * z - координата вектора
     */
    public double z;
    /**
     * w - координата вектора
     */
    public double w;

    /**
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     * @param z z - координата вектора
     * @param w w - координата вектора
     */
    public Vector4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector4d() {
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
    public Vector4d(@NotNull Vector4d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    /**
     * Возвращает нулевой вектор {0,0,0,0}
     *
     * @return нулевой вектор {0,0,0,0}
     */
    @NotNull
    public static Vector4d zeros() {
        return new Vector4d(0, 0, 0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1,1,1}
     *
     * @return единичный вектор {1,1,1,1}
     */
    @NotNull
    public static Vector4d ones() {
        return new Vector4d(1, 1, 1, 1);
    }


    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector4d min(@NotNull Vector4d a, @NotNull Vector4d b) {
        return new Vector4d(
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
    public static Vector4d max(@NotNull Vector4d a, @NotNull Vector4d b) {
        return new Vector4d(
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
    public static Vector4d sum(@NotNull Vector4d a, @NotNull Vector4d b) {
        return new Vector4d(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector4d sum(Vector4d... arr) {
        Vector4d newVector = new Vector4d();

        for (Vector4d v : arr)
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
    public static Vector4d subtract(@NotNull Vector4d a, @NotNull Vector4d b) {
        return new Vector4d(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
    }

    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector4d a, @NotNull Vector4d b) {
        return subtract(a, b).length();
    }

    /**
     * Умножение вектора на число
     *
     * @param v вектор
     * @param a скаляр
     * @return произведение вектора и числа
     */
    @NotNull
    public static Vector4d mul(@NotNull Vector4d v, double a) {
        return new Vector4d(v.x * a, v.y * a, v.z * a, v.w * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector4d mul(@NotNull Vector4d v1, @NotNull Vector4d v2) {
        return new Vector4d(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z, v1.w * v2.w);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static double dot(@NotNull Vector4d a, @NotNull Vector4d b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    /**
     * Получить нормализованный вектор из исходного
     *
     * @param v исходный вектор
     * @return нормализованный вектор из исходного
     */
    @NotNull
    public static Vector4d norm(@NotNull Vector4d v) {
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
    public static Vector4d norm(@NotNull Vector4d v, double a) {
        return mul(v, 1 / v.length() * a);
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector4d a, @NotNull Vector4d b) {
        return a.x - b.x >= -0.00001 &&
                a.y - b.y >= -0.00001 &&
                a.z - b.z >= -0.00001 &&
                a.w - b.w >= -0.00001;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector4d a, @NotNull Vector4d b) {
        return a.x - b.x > 0 &&
                a.y - b.y > 0 &&
                a.z - b.z > 0 &&
                a.w - b.w > 0;
    }

    /**
     * Проверка меньше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector4d a, @NotNull Vector4d b) {
        return biggerOrEqual(b, a);
    }

    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector4d a, @NotNull Vector4d b) {
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
    public static Vector4d rand(@NotNull Vector4d min, @NotNull Vector4d max) {
        return new Vector4d(
                ThreadLocalRandom.current().nextDouble(min.x, max.x),
                ThreadLocalRandom.current().nextDouble(min.y, max.y),
                ThreadLocalRandom.current().nextDouble(min.z, max.z),
                ThreadLocalRandom.current().nextDouble(min.w, max.w)
        );
    }

    /**
     * Получить случайный вектор заданной длины
     *
     * @param length длина нового вектора
     * @return случайный вектор заданной длины
     */
    @NotNull
    public static Vector4d rand(double length) {
        double x = ThreadLocalRandom.current().nextDouble(length);
        double y = ThreadLocalRandom.current().nextDouble(Math.sqrt(length * length - x * x));
        double z = ThreadLocalRandom.current().nextDouble(Math.sqrt(length * length - x * x - y * y));
        double w = Math.sqrt(length * length - x * x - y * y - z * z);
        return new Vector4d(x, y, z, w);
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector4d v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        this.z = this.z + v.z;
        this.w = this.z + v.w;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector4d v) {
        this.x = this.x - v.x;
        this.y = this.y - v.y;
        this.z = this.z - v.z;
        this.w = this.w - v.w;
    }

    /**
     * Скалярное уможение на вектор
     *
     * @param v вектор, на который нужно умножить
     * @return салярное уможение на вектор
     */
    public double dot(@NotNull Vector4d v) {
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    /**
     * Поэлементное умножение на скаляр
     *
     * @param v число, на которое нужно умножить
     */
    public void mul(double v) {
        this.x = this.x * v;
        this.y = this.y * v;
        this.z = this.z * v;
        this.w = this.w * w;
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector4d v) {
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

    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector4d norm() {
        return mul(this, 1 / this.length());
    }

    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @param a множитель
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector4d norm(double a) {
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
                ", " + String.format("%.2f", z) +
                ", " + String.format("%.2f", w) +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector4d vector4d = (Vector4d) o;

        if (Double.compare(vector4d.x, x) != 0) return false;
        if (Double.compare(vector4d.y, y) != 0) return false;
        if (Double.compare(vector4d.z, z) != 0) return false;
        return Double.compare(vector4d.w, w) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(w);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector4d getJOML() {
        return new org.joml.Vector4d(x, y, z, w);
    }


}
