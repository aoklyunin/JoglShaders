package math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import org.joml.Matrix4d;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

/**
 * Класс матрицы трансформации
 */
public class Transform3d {
    /**
     * id матрицы трансформации
     */
    @JsonIgnore
    private int id;
    /**
     * X координата положения
     */
    private double positionX;
    /**
     * Y координата положения
     */
    private double positionY;
    /**
     * Z координата положения
     */
    private double positionZ;
    /**
     * O угол поворота
     */
    private double rotationO;
    /**
     * A угол поворота
     */
    private double rotationA;
    /**
     * T угол поворота
     */
    private double rotationT;
    /**
     * X коэффициент масштабирования
     */
    private double scaleX;
    /**
     * Y коэффициент масштабирования
     */
    private double scaleY;
    /**
     * Z коэффициент масштабирования
     */
    private double scaleZ;

    /**
     * Конструктор матрицы трансформации
     *
     * @param positionX X координата положения
     * @param positionY Y координата положения
     * @param positionZ Z координата положения
     * @param rotationO O угол поворота
     * @param rotationA A угол поворота
     * @param rotationT T угол поворота
     * @param scaleX    X коэффициент масштабирования
     * @param scaleY    Y коэффициент масштабирования
     * @param scaleZ    Z коэффициент масштабирования
     */
    public Transform3d(
            double positionX, double positionY, double positionZ,
            double rotationO, double rotationA, double rotationT,
            double scaleX, double scaleY, double scaleZ
    ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;

        this.rotationO = rotationO;
        this.rotationA = rotationA;
        this.rotationT = rotationT;

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    /**
     * Конструктор матрицы трансформации
     *
     * @param position положение
     * @param rotation углы поворота в градусах
     * @param scale    коэффициенты масштабирования
     */
    public Transform3d(@NotNull Vector3d position, @NotNull Vector3d rotation, @NotNull Vector3d scale) {
        this.positionX = position.x;
        this.positionY = position.y;
        this.positionZ = position.z;

        this.rotationO = rotation.x;
        this.rotationA = rotation.y;
        this.rotationT = rotation.z;

        this.scaleX = scale.x;
        this.scaleY = scale.y;
        this.scaleZ = scale.z;
    }

    /**
     * Конструктор матрицы трансформации
     */
    private Transform3d() {
    }

    /**
     * Конструктор матрицы трансформации
     *
     * @param transform3d матрица трансформации
     */
    public Transform3d(@NotNull Transform3d transform3d) {
        this(
                transform3d.positionX, transform3d.positionY, transform3d.positionZ,
                transform3d.rotationO, transform3d.rotationA, transform3d.rotationT,
                transform3d.scaleX, transform3d.scaleY, transform3d.scaleZ
        );
    }

    /**
     * Получить id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Получить матрицу преобразования
     *
     * @return матрица преобразования
     */
    @NotNull
    @JsonIgnore
    public Matrix4d getTransformMatrix() {
        return new Matrix4d().translate(getPosition().getJOML())
                .rotateX(rotationO / 180 * Math.PI)
                .rotateY(rotationA / 180 * Math.PI)
                .rotateZ(rotationT / 180 * Math.PI)
                .scale(getScale().getJOML());
    }

    /**
     * Получить положение из матрицы трансформации
     *
     * @return положение из матрицы трансформации
     */
    @NotNull
    public Vector3d getPosition() {
        return new Vector3d(positionX, positionY, positionZ);
    }

    /**
     * Получить углы поворота из матрицы трансформации
     *
     * @return углы поворота из матрицы трансформации
     */
    @NotNull
    public Vector3d getRotation() {
        return new Vector3d(rotationO, rotationA, rotationT);
    }

    /**
     * Получить коэффициенты масштабирования из матрицы трансформации
     *
     * @return коэффициенты масштабирования из матрицы трансформации
     */
    @NotNull
    public Vector3d getScale() {
        return new Vector3d(scaleX, scaleY, scaleZ);
    }

    /**
     * Задать матрице трансформации положение
     *
     * @param position положение
     */
    public void setPosition(@NotNull Vector3d position) {
        positionX = position.x;
        positionY = position.y;
        positionZ = position.z;
    }

    /**
     * Задать матрице трансформации углы поворота
     *
     * @param rotation углы поворота
     */
    public void setRotation(@NotNull Vector3d rotation) {
        rotationO = rotation.x;
        rotationA = rotation.y;
        rotationT = rotation.z;
    }

    /**
     * Задать матрице трансформации коэффициенты масштабирования
     *
     * @param scale коэффициенты масштабирования
     */
    public void setScale(@NotNull Vector3d scale) {
        scaleX = scale.x;
        scaleY = scale.y;
        scaleZ = scale.z;
    }

    /**
     * Получить буфер с матрицей трансформации
     *
     * @return буфер с матрицей трансформации
     */
    @NotNull
    @JsonIgnore
    public DoubleBuffer getDoubleBufferTransform() {
        DoubleBuffer fb = BufferUtils.createDoubleBuffer(16);
        return getTransformMatrix().get(fb);
    }

    /**
     * Применить трансформацию к OpenGL
     *
     * @param gl2 переменная OpenGL
     */
    public void apply(GL2 gl2) {
        gl2.glTranslated(positionX, positionY, positionZ);
        gl2.glRotated(rotationO, 1, 0, 0);
        gl2.glRotated(rotationA, 0, 1, 0);
        gl2.glRotated(rotationT, 0, 0, 1);
        gl2.glScaled(scaleX, scaleY, scaleZ);
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return Transform3d{length}
     */
    @Override
    public String toString() {
        return "Transform3d{" + getPosition() + ", " + getRotation() + ", " + getScale() + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform3d that = (Transform3d) o;

        if (id != that.id) return false;
        if (Double.compare(that.positionX, positionX) != 0) return false;
        if (Double.compare(that.positionY, positionY) != 0) return false;
        if (Double.compare(that.positionZ, positionZ) != 0) return false;
        if (Double.compare(that.rotationO, rotationO) != 0) return false;
        if (Double.compare(that.rotationA, rotationA) != 0) return false;
        if (Double.compare(that.rotationT, rotationT) != 0) return false;
        if (Double.compare(that.scaleX, scaleX) != 0) return false;
        if (Double.compare(that.scaleY, scaleY) != 0) return false;
        return (Double.compare(that.scaleZ, scaleZ) == 0);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(positionX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(positionY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(positionZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationO);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationA);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationT);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(scaleX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(scaleY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(scaleZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
