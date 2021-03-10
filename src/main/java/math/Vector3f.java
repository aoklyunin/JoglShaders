package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс трёхмерного вектора float
 */
public class Vector3f {
    /**
     * Точность вещественных вычислений
     */
    public static final double VECTOR_3F_OPACITY = 0.000001;
    /**
     * x - координата вектора
     */
    public float x;
    /**
     * y - координата вектора
     */
    public float y;
    /**
     * z - координата вектора
     */
    public float z;

    /**
     * Конструктор вектора
     *
     * @param x x - координата вектора
     * @param y y - координата вектора
     * @param z z - координата вектора
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Конструктор вектора создаёт нулевой ветктор
     */
    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Конструктор копии
     *
     * @param v исходный вкетор
     */
    public Vector3f(@NotNull Vector3f v) {
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
    public static Vector3f zeros() {
        return new Vector3f(0, 0, 0);
    }

    /**
     * Возвращает единичный вектор {1,1,1}
     *
     * @return единичный вектор {1,1,1}
     */
    @NotNull
    public static Vector3f ones() {
        return new Vector3f(1, 1, 1);
    }


    /**
     * Получить вектор с минимальными координатами из двух переданных векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return вектор с минимальными координатами из двух переданных векторов
     */
    @NotNull
    public static Vector3f min(@NotNull Vector3f a, @NotNull Vector3f b) {
        return new Vector3f(
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
    public static Vector3f max(@NotNull Vector3f a, @NotNull Vector3f b) {
        return new Vector3f(
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
    public static Vector3f sum(@NotNull Vector3f a, @NotNull Vector3f b) {
        return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Сложить векторы
     *
     * @param arr массив векторов
     * @return сумма двух векторов
     */
    @NotNull
    public static Vector3f sum(Vector3f... arr) {
        Vector3f newVector = new Vector3f();

        for (Vector3f v : arr)
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
    public static Vector3f subtract(@NotNull Vector3f a, @NotNull Vector3f b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Получить расстояние между векторами
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return расстояние между векторами
     */
    public static double getDistance(@NotNull Vector3f a, @NotNull Vector3f b) {
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
    public static Vector3f mul(@NotNull Vector3f v, int a) {
        return new Vector3f(v.x * a, v.y * a, v.z * a);
    }

    /**
     * Умножение вектора на число
     *
     * @param v вектор
     * @param a число
     * @return результат умножения вектора на число
     */
    @NotNull
    public static Vector3f mul(@NotNull Vector3f v, float a) {
        return new Vector3f(v.x * a, v.y * a, v.z * a);
    }

    /**
     * Поэлементное умножение векторов
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return поэлементное умножение векторов
     */
    @NotNull
    public static Vector3f mul(@NotNull Vector3f v1, @NotNull Vector3f v2) {
        return new Vector3f(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    /**
     * Скалярное умножение векторов
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return Скалярное умножение векторов
     */
    public static float dot(@NotNull Vector3f a, @NotNull Vector3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /**
     * Получить нормализованный вектор из исходного
     *
     * @param v исходный вектор
     * @return нормализованный вектор из исходного
     */
    @NotNull
    public static Vector3f norm(@NotNull Vector3f v) {
        return mul(v, 1 / (float) v.length());
    }

    /**
     * Получить нормализованный вектор из исходного
     *
     * @param v исходный вектор
     * @param a длина нового вектора
     * @return нормализованный вектор из исходного
     */
    @NotNull
    public static Vector3f norm(@NotNull Vector3f v, float a) {
        return mul(v, 1 / (float) v.length() * a);
    }

    /**
     * Векторное умножение векторов
     *
     * @param v  первый вектор
     * @param v2 второй вектор
     * @return Векторное умножение векторов
     */
    @NotNull
    public static Vector3f cross(@NotNull Vector3f v, @NotNull Vector3f v2) {
        return new Vector3f(
                v.y * v2.z - v.z * v2.y,
                v.z * v2.x - v.x * v2.z,
                v.x * v2.y - v.y * v2.x
        );
    }

    /**
     * Проверка больше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше или равен ли вектор a ветору b
     */
    public static boolean biggerOrEqual(@NotNull Vector3f a, @NotNull Vector3f b) {
        return a.x - b.x >= -VECTOR_3F_OPACITY &&
                a.y - b.y >= -VECTOR_3F_OPACITY &&
                a.z - b.z >= -VECTOR_3F_OPACITY;
    }

    /**
     * Проверка больше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return больше ли вектор a ветора b
     */
    public static boolean bigger(@NotNull Vector3f a, @NotNull Vector3f b) {
        return a.x - b.x > 0 &&
                a.y - b.y > 0 &&
                a.z - b.z > 0;
    }

    /**
     * Проверка меньше или равен ли вектор a ветору b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше или равен ли вектор a ветору b
     */
    public static boolean lessOrEqual(@NotNull Vector3f a, @NotNull Vector3f b) {
        return biggerOrEqual(b, a);
    }

    /**
     * Проверка меньше ли вектор a ветора b
     *
     * @param a первый вектор
     * @param b второй вектор
     * @return меньше ли вектор a ветора b
     */
    public static boolean less(@NotNull Vector3f a, @NotNull Vector3f b) {
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
    public static Vector3f rand(@NotNull Vector3f min, @NotNull Vector3f max) {
        return new Vector3f(
                (float) ThreadLocalRandom.current().nextDouble(min.x, max.x),
                (float) ThreadLocalRandom.current().nextDouble(min.y, max.y),
                (float) ThreadLocalRandom.current().nextDouble(min.z, max.z)
        );
    }

    /**
     * Получить случайный вектор заданной длины
     *
     * @param length длина нового вектора
     * @return случайный вектор заданной длины
     */
    @NotNull
    public static Vector3f rand(float length) {
        float x = (float) ThreadLocalRandom.current().nextDouble(length);
        float y = (float) ThreadLocalRandom.current().nextDouble(Math.sqrt(length * length - x * x));
        float z = (float) Math.sqrt(length * length - x * x - y * y);
        return new Vector3f(x, y, z);
    }

    /**
     * Добавить вектор к текущему вектору
     *
     * @param v вектор, который нужно добавить
     */
    public void add(@NotNull Vector3f v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        this.z = this.z + v.z;
    }

    /**
     * Вычесть вектор из текущего
     *
     * @param v вектор, который нужно вычесть
     */
    public void subtract(@NotNull Vector3f v) {
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
    public float dot(@NotNull Vector3f v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    /**
     * Поэлементное умножение на скаляр
     *
     * @param v число, на которое нужно умножить
     */
    public void mul(float v) {
        this.x = this.x * v;
        this.y = this.y * v;
        this.z = this.z * v;
    }

    /**
     * Поэлементное умножение на ветор
     *
     * @param v вектор, на который нужно умножить
     */
    public void mul(@NotNull Vector3f v) {
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


    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector3f norm() {
        return mul(this, 1 / (float) this.length());
    }

    /**
     * Получить новый вектор, соответствующий нормализованному текущему
     *
     * @param a множитель
     * @return новый вектор, соответствующий нормализованному текущему
     */
    @NotNull
    public Vector3f norm(float a) {
        return mul(this, 1 / (float) this.length() * a);
    }

    /**
     * Нормализовать текущий вектор
     */
    public void normalize() {
        this.mul(1 / (float) length());
    }

    /**
     * Нормализовать текущий вектор
     *
     * @param a множитель
     */
    public void normalize(float a) {
        this.mul(1 / (float) length() * a);
    }


    @Override
    public String toString() {
        return "(" + String.format("%.2f", x) +
                ", " + String.format("%.2f", y) +
                ", " + String.format("%.2f", z) +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3f vector3f = (Vector3f) o;

        if (Float.compare(vector3f.x, x) != 0) return false;
        if (Float.compare(vector3f.y, y) != 0) return false;
        return Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    /**
     * Преобразовать к вектору JOML
     *
     * @return вектор JOML
     */
    @JsonIgnore
    public org.joml.Vector3f getJOML() {
        return new org.joml.Vector3f(x, y, z);
    }

}
