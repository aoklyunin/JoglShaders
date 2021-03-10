package graphics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jogamp.opengl.glu.GLU;
import com.sun.istack.NotNull;
import linearAlgebra.LinearAlgebra;
import math.Vector2d;
import math.Vector3d;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * Состояние камеры 2D существа
 */
public class Camera {

    /**
     * id состояния мозга
     */
    @JsonIgnore
    private int id;
    /**
     * X координата положения камеры
     */
    @JsonIgnore
    private double posX;
    /**
     * Y координата положения камеры
     */
    @JsonIgnore
    private double posY;
    /**
     * Z координата положения камеры
     */
    @JsonIgnore
    private double posZ;
    /**
     * X координата направления камеры
     */
    @JsonIgnore
    private double dirX;
    /**
     * Y координата направления камеры
     */
    @JsonIgnore
    private double dirY;
    /**
     * Z координата направления камеры
     */
    @JsonIgnore
    private double dirZ;
    /**
     * X координата вектора "вверх" камеры
     */
    @JsonIgnore
    private double upX;
    /**
     * Y координата вектора "вверх" камеры
     */
    @JsonIgnore
    private double upY;
    /**
     * Z координата вектора "вверх" камеры
     */
    @JsonIgnore
    private double upZ;

    float fov_y = 90f;
    Vector2f vp = new Vector2f(640, 480);
    float near = 0.01f;
    float far = 100.0f;

    /**
     * Конструктор камеры
     *
     * @param pos положение
     * @param dir направления
     * @param up  вектор "вверх"
     */
    public Camera(@NotNull Vector3d pos, @NotNull Vector3d dir, @NotNull Vector3d up) {
        this.posX = pos.x;
        this.posY = pos.y;
        this.posZ = pos.z;
        this.dirX = dir.x;
        this.dirY = dir.y;
        this.dirZ = dir.z;
        this.upX = up.x;
        this.upY = up.y;
        this.upZ = up.z;
    }

    /**
     * Конструктор камеры
     *
     * @param posX X координата положения камеры
     * @param posY Y координата положения камеры
     * @param posZ Z координата положения камеры
     * @param dirX X координата направления камеры
     * @param dirY Y координата направления камеры
     * @param dirZ Z координата направления камеры
     * @param upX  X координата вектора "вверх" камеры
     * @param upY  Y координата вектора "вверх" камеры
     * @param upZ  Z координата вектора "вверх" камеры
     */
    public Camera(
            double posX, double posY, double posZ,
            double dirX, double dirY, double dirZ,
            double upX, double upY, double upZ
    ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    /**
     * Конструктор камеры
     *
     * @param camera камера
     */
    public Camera(@NotNull Camera camera) {
        this(camera.posX, camera.posY, camera.posZ, camera.dirX, camera.dirY, camera.dirZ, camera.upX, camera.upY, camera.upZ);
    }

    /**
     * Конструктор камеры
     */
    public Camera() {

    }

    public FloatBuffer getPerspectiveBuffer() {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        return getPerspectiveMatrix().get(fb);
    }

    public Matrix4f getPerspectiveMatrix() {
        return new Matrix4f().perspective(
                (float) Math.toRadians(fov_y), vp.x / vp.y, near, far
        ).m33(1);
//        float fn = far + near;
//        float f_n = far - near;
//        float r = vp.x / vp.y;
//        float t = 1.0f / (float)Math.tan(Math.toRadians(fov_y) / 2.0f);
//
//        return new Matrix4f(
//                t / r, 0.0f, 0.0f, 0.0f,
//                0.0f, t, 0.0f, 0.0f,
//                0.0f, 0.0f, -fn / f_n, -1.0f,
//                0.0f, 0.0f, -2.0f * far * near / f_n, 1.0f
//        );
    }

    public FloatBuffer getLookAtBuffer() {
      //  System.out.println(getLookAtMatrix());
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        return getLookAtMatrix().get(fb);
    }

    public Matrix4f getLookAtMatrix() {
       // System.out.println("________________________");
        //System.out.println(
//       return new Matrix4f().setLookAt(
//                (float)posX,(float)posY,(float)posZ,
//               (float)dirX+(float)posX,(float)dirY+(float)posY,(float)dirZ+(float)posZ,
//                (float)upX,(float)upY,(float)upZ
//        );
        Vector3d mz = getDir().norm();
        Vector3d my = getUp();
        Vector3d mx = Vector3d.cross(my, mz).norm();
        my = Vector3d.cross(mz, mx);
        return new Matrix4f(
                (float)mx.x,  (float)my.x,  (float)mz.x, 0.0f,
                (float)mx.y,  (float)my.y,  (float)mz.y, 0.0f,
                (float)mx.z,  (float)my.z,  (float)mz.z, 0.0f,
                (float)Vector3d.dot(mx, getPos()),  (float)Vector3d.dot(my, getPos()),
                (float)Vector3d.dot(new Vector3d(-mz.x, -mz.y, -mz.z), getPos()), 1.0f
        );
    }

    /**
     * Получить камеру по умолчанию
     *
     * @return камера по умолчанию
     */
    @NotNull
    public static Camera getDefaultCamera() {
        return new Camera(
                0, 0, 0,
                0, 1, 0,
                0, 0, 1
        );
    }

    /**
     * Получить камеру по умолчанию
     *
     * @param pos положение
     * @return камера по умолчанию с заданным положением
     */
    @NotNull
    public static Camera getDefaultCamera(@NotNull Vector3d pos) {
        return new Camera(
                pos.x, pos.y, pos.z,
                0, 1, 0,
                0, 0, 1
        );
    }

    /**
     * Задать матрицу камеры OpenGL
     *
     * @param glu переменная OpenGL
     */
    public void gluLookAt(@NotNull GLU glu) {
        // задаём параметры камеры
        glu.gluLookAt(
                posX, posY, posZ, // положение камеры
                dirX + posX, dirY + posY, dirZ + posZ, // куда смотрит камера
                upX, upY, upZ  // вектор "вверх"
        );
    }

    /**
     * Получить вектор положения камеры
     *
     * @return вектор положения камеры
     */
    @NotNull
    public Vector3d getPos() {
        return new Vector3d(posX, posY, posZ);
    }

    /**
     * Получить вектор направления камеры
     *
     * @return вектор направления камеры
     */
    @NotNull
    public Vector3d getDir() {
        return new Vector3d(dirX, dirY, dirZ);
    }

    /**
     * Получить вектор "вверх" камеры
     *
     * @return вектор "вверх" камеры
     */
    @NotNull
    public Vector3d getUp() {
        return new Vector3d(upX, upY, upZ);
    }

    /**
     * Задать вектор положения камеры
     *
     * @param pos вектор положения камеры
     */
    public void setPos(@NotNull Vector3d pos) {
        posX = pos.x;
        posY = pos.y;
        posZ = pos.z;
    }

    /**
     * Задать вектор направления камеры
     *
     * @param dir вектор направления камеры
     */
    public void setDir(@NotNull Vector3d dir) {
        dirX = dir.x;
        dirY = dir.y;
        dirZ = dir.z;
    }

    /**
     * Задать вектор "вверх" камеры
     *
     * @param up вектор "вверх" камеры
     */
    public void setUp(@NotNull Vector3d up) {
        upX = up.x;
        upY = up.y;
        upZ = up.z;
    }

    /**
     * Повернуть камеру на y координату вектора вдоль вектора "вверх"
     * и на x координату вектора вдоль вектора [0,0,1]
     *
     * @param rotation вектор поворота
     */
    public void rotate(@NotNull Vector2d rotation) {
        Vector3d upRot = new Vector3d(0, 0, 1);
        Vector3d dir = getDir();
        dir = LinearAlgebra.rotateVectorCC(dir, upRot, rotation.x).norm();
        Vector3d up = getUp();
        up = LinearAlgebra.rotateVectorCC(up, upRot, rotation.x).norm();
        Vector3d rot = Vector3d.cross(dir, up).norm();
        dir = LinearAlgebra.rotateVectorCC(dir, rot, -rotation.y).norm();
        up = LinearAlgebra.rotateVectorCC(up, rot, -rotation.y).norm();
        setDir(dir);
        setUp(up);
    }

    /**
     * Переместиться вперёд с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveFroward(double speed) {
        posX += dirX * speed;
        posY += dirY * speed;
        posZ += dirZ * speed;
    }

    /**
     * Переместиться назад с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveBackward(double speed) {
        posX -= dirX * speed;
        posY -= dirY * speed;
        posZ -= dirZ * speed;
    }

    /**
     * Переместиться влево с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveLeft(double speed) {
        Vector3d rot = Vector3d.cross(getDir(), getUp()).norm();
        posX -= rot.x * speed;
        posY -= rot.y * speed;
        posZ -= rot.z * speed;
    }

    /**
     * Переместиться вправо с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveRight(double speed) {
        Vector3d rot = Vector3d.cross(getDir(), getUp()).norm();
        posX += rot.x * speed;
        posY += rot.y * speed;
        posZ += rot.z * speed;
    }

    /**
     * Переместиться вниз с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveDown(double speed) {
        posX -= upX * speed;
        posY -= upY * speed;
        posZ -= upZ * speed;
    }

    /**
     * Переместиться вверх с задланной скоростью
     *
     * @param speed скорость
     */
    public void moveUp(double speed) {
        posX += upX * speed;
        posY += upY * speed;
        posZ += upZ * speed;
    }


    /**
     * Получить матрицу преобразования
     *
     * @return матрица преобразования
     */
    @NotNull
    @JsonIgnore
    public Matrix4d getTransformMatrix() {
        Vector3d defaultDir = new Vector3d(0, 0, -1);
        Vector3d axis = Vector3d.cross(defaultDir, getDir());
        double angle = Math.acos(Vector3d.dot(defaultDir, getDir()));
        return new Matrix4d().translate(getPos().getJOML())
                .rotate(angle, axis.getJOML());
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
     * Изменить положение на заданное смещение
     *
     * @param delta заданное смещение
     */
    public void changePos(@NotNull Vector3d delta) {
        posX += delta.x;
        posY += delta.y;
        posZ += delta.z;
    }

    /**
     * Нормализовать вектор направления
     */
    public void normalizeDir() {
        setDir(getDir().norm());
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Camera{getString()}"
     */
    @Override
    public String toString() {
        return "Camera{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "getPos(), getDir(), getUp()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return getPos() + ", " + getDir() + ", " + getUp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Camera camera = (Camera) o;

        if (id != camera.id) return false;
        if (Double.compare(camera.posX, posX) != 0) return false;
        if (Double.compare(camera.posY, posY) != 0) return false;
        if (Double.compare(camera.posZ, posZ) != 0) return false;
        if (Double.compare(camera.dirX, dirX) != 0) return false;
        if (Double.compare(camera.dirY, dirY) != 0) return false;
        if (Double.compare(camera.dirZ, dirZ) != 0) return false;
        if (Double.compare(camera.upX, upX) != 0) return false;
        if (Double.compare(camera.upY, upY) != 0) return false;
        return Double.compare(camera.upZ, upZ) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dirX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dirY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dirZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
