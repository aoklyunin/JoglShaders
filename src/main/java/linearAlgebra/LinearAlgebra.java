package linearAlgebra;

import com.sun.istack.NotNull;
import math.Vector3d;
import org.apache.commons.math3.linear.*;
import org.joml.Matrix3d;
import org.joml.Matrix4d;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для работыс линейной алгеброй
 */
public class LinearAlgebra {
    /**
     * Получить собчственные значения матрицы
     *
     * @param matrix матрица
     * @return собчственные значения матрицы
     */
    @NotNull
    public static ComplexValue[] getEigenValues(@NotNull double[][] matrix) {
        RealMatrix A = new Array2DRowRealMatrix(matrix);
        EigenDecomposition eigenADecomposition = new EigenDecomposition(A);
        RealMatrix D = eigenADecomposition.getD();
        return getEigenValuesFromDiagonalMatrix(D.getData());
    }

    /**
     * Получить собственные значения из диагональной матрицы
     *
     * @param matrix диагональная матрица
     * @return собственные значения
     */
    @NotNull
    public static ComplexValue[] getEigenValuesFromDiagonalMatrix(@NotNull double[][] matrix) {
        ComplexValue[] result = new ComplexValue[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            if (i < matrix.length - 1) {
                if (Math.abs(matrix[i][i + 1]) > 0.001) {
                    result[i] = new ComplexValue(matrix[i][i], matrix[i][i + 1]);
                    result[i + 1] = new ComplexValue(matrix[i][i], -matrix[i][i + 1]);
                    i++;
                    continue;
                }
            }
            result[i] = new ComplexValue(matrix[i][i]);
        }
        return result;
    }

    /**
     * Получить отклонение собственных чисел из отклонения матрицы
     *
     * @param matrix      исходная матрица
     * @param deltaMatrix отклонение
     * @return отклонение собственных чисел
     */
    @NotNull
    public static ComplexValue[] getdLambdaFromdA(@NotNull double[][] matrix, @NotNull double[][] deltaMatrix) {
        RealMatrix A = new Array2DRowRealMatrix(matrix);
        EigenDecomposition eigenADecomposition = new EigenDecomposition(A);
        RealMatrix V = eigenADecomposition.getV();
        RealMatrix W = MatrixUtils.inverse(V);
        RealMatrix dA = new Array2DRowRealMatrix(deltaMatrix);
        RealMatrix dLambda = W.multiply(dA.multiply(V));
        return getEigenValuesFromDiagonalMatrix(dLambda.getData());
    }

    /**
     * Получить разложение матрицы
     *
     * @param matrix исходная матрица
     * @return первая двумерная матрица -  матрица левых собственных векторов, вторая - диагональная матрица  собственных
     * чисел, третья - матрица правых собственных векторов
     */
    @NotNull
    public double[][][] getEigenDecomposition(@NotNull double[][] matrix) {
        RealMatrix A = new Array2DRowRealMatrix(matrix);
        //System.out.println(A);
        EigenDecomposition eigenADecomposition = new EigenDecomposition(A);
        RealMatrix V = eigenADecomposition.getV();
        RealMatrix D = eigenADecomposition.getD();
        RealMatrix W = MatrixUtils.inverse(V);
        double[][][] result = new double[3][matrix.length][matrix.length];
        result[0] = W.getData();
        result[1] = D.getData();
        result[2] = V.getData();
        return result;
    }

    /**
     * Преобразование матрицы apache к строке
     *
     * @param matrix исходная матрица
     * @return строковое представление матрицы
     */
    @NotNull
    public static String apacheMatrixToString(@NotNull RealMatrix matrix) {
        String result = "[\n";
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            result += " [";
            for (int j = 0; j < matrix.getRowDimension(); j++) {
                result += String.format("%.3f ", matrix.getEntry(i, j));
            }
            result += "]\n";
        }
        return result + "]";
    }

    /**
     * Получить случайную матрицу
     *
     * @param n     порядок матрицы
     * @param range кдиапазон значений
     * @return случайная матрица
     */
    @NotNull
    public static double[][] getRandomMatrix(int n, double range) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (ThreadLocalRandom.current().nextDouble() - 0.5) * range;
            }
        }
        return matrix;
    }

    /**
     * Получить определитель  матрицы
     *
     * @param matrix матрица
     * @return определитель
     */
    public static double getDeterminant(@NotNull double[][] matrix) {
        return new LUDecomposition(new Array2DRowRealMatrix(matrix)).getDeterminant();
    }

    /**
     * Поворот вектора относительно вектора
     *
     * @param vec   вектор, который поворачиваем
     * @param axis  ось поворота
     * @param theta угол поворота в радианах
     * @return новый вектор
     */
    public static Vector3d rotateVectorCC(Vector3d vec, Vector3d axis, double theta) {
        double x, y, z;
        double u, v, w;
        x = vec.x;
        y = vec.y;
        z = vec.z;
        u = axis.x;
        v = axis.y;
        w = axis.z;
        double scalarMultiply = u * x + v * y + w * z;
        double xPrime = u * scalarMultiply * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta);
        double yPrime = v * scalarMultiply * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta);
        double zPrime = w * scalarMultiply * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta);
        return new Vector3d(xPrime, yPrime, zPrime);
    }

    /**
     * Получитьматрицу поворота по оси и  углу
     *
     * @param axis  ось вращения
     * @param theta угол поворота
     * @return матрица поворота
     */
    public static Matrix3d getRotationMatrix3d(Vector3d axis, double theta) {
        return new Matrix3d(
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.x * axis.x,
                (1 - Math.cos(theta)) * axis.x * axis.y - Math.sin(theta) * axis.z,
                (1 - Math.cos(theta)) * axis.x * axis.z + (Math.sin(theta)) * axis.y,

                (1 - Math.cos(theta)) * axis.y * axis.x + Math.sin(theta) * axis.z,
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.y * axis.y,
                (1 - Math.cos(theta)) * axis.y * axis.z - Math.sin(theta) * axis.x,

                (1 - Math.cos(theta)) * axis.z * axis.x - Math.sin(theta) * axis.y,
                (1 - Math.cos(theta)) * axis.z * axis.y + Math.sin(theta) * axis.x,
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.z * axis.z
        );
    }

    /**
     * Получитьматрицу поворота по оси и  углу
     *
     * @param axis  ось вращения
     * @param theta угол поворота
     * @return матрица поворота
     */
    public static Matrix4d getRotationMatrix4d(Vector3d axis, double theta) {
        return new Matrix4d(
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.x * axis.x,
                (1 - Math.cos(theta)) * axis.x * axis.y - Math.sin(theta) * axis.z,
                (1 - Math.cos(theta)) * axis.x * axis.z + (Math.sin(theta)) * axis.y,
                0,

                (1 - Math.cos(theta)) * axis.y * axis.x + Math.sin(theta) * axis.z,
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.y * axis.y,
                (1 - Math.cos(theta)) * axis.y * axis.z - Math.sin(theta) * axis.x,
                0,

                (1 - Math.cos(theta)) * axis.z * axis.x - Math.sin(theta) * axis.y,
                (1 - Math.cos(theta)) * axis.z * axis.y + Math.sin(theta) * axis.x,
                Math.cos(theta) + (1 - Math.cos(theta)) * axis.z * axis.z,
                0,
                0, 0, 0, 1
        );
    }

    /**
     * Получить матрицу поворота вдоль оси X
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси X
     */
    public static Matrix3d getXRotationMatrix3d(double alpha) {
        return new Matrix3d(
                1, 0, 0,
                0, Math.cos(alpha), -Math.sin(alpha),
                0, Math.sin(alpha), Math.cos(alpha)
        );
    }

    /**
     * Получить матрицу поворота вдоль оси X
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси X
     */
    public static Matrix4d getXRotationMatrix4d(double alpha) {
        return new Matrix4d(
                1, 0, 0, 0,
                0, Math.cos(alpha), -Math.sin(alpha), 0,
                0, Math.sin(alpha), Math.cos(alpha), 0,
                0, 0, 0, 1
        );
    }

    /**
     * Получить матрицу поворота вдоль оси Y
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси Y
     */
    public static Matrix3d getYRotationMatrix3d(double alpha) {
        return new Matrix3d(
                Math.cos(alpha), 0, Math.sin(alpha),
                0, 1, 0,
                -Math.sin(alpha), 0, Math.cos(alpha)
        );
    }

    /**
     * Получить матрицу поворота вдоль оси Y
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси Y
     */
    public static Matrix4d getYRotationMatrix4d(double alpha) {
        return new Matrix4d(
                Math.cos(alpha), 0, Math.sin(alpha), 0,
                0, 1, 0, 0,
                -Math.sin(alpha), 0, Math.cos(alpha), 0,
                0, 0, 0, 1
        );
    }

    /**
     * Получить матрицу поворота вдоль оси Z
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси Z
     */
    public static Matrix3d getZRotationMatrix3d(double alpha) {
        return new Matrix3d(
                Math.cos(alpha), -Math.sin(alpha), 0,
                Math.sin(alpha), Math.cos(alpha), 0,
                0, 0, 1
        );
    }

    /**
     * Получить матрицу поворота вдоль оси Z
     *
     * @param alpha угол поворота
     * @return матрица поворота вдоль оси Z
     */
    public static Matrix4d getZRotationMatrix4d(double alpha) {
        return new Matrix4d(
                Math.cos(alpha), -Math.sin(alpha), 0, 0,
                Math.sin(alpha), Math.cos(alpha), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    /**
     * Получить матрицу смещения
     *
     * @param offset вектор смещения
     * @return матрица смещения
     */
    public static Matrix4d getTranslationMatrix4d(Vector3d offset) {
        return new Matrix4d(
                1, 0, 0, offset.x,
                0, 1, 0, offset.y,
                0, 0, 1, offset.z,
                0, 0, 0, 1
        );
    }

    /**
     * Получить матрицу масштабирования
     *
     * @param scale вектор коэффициентов масштабировангия
     * @return матрица масштабирования
     */
    public static Matrix4d getScaleMatrix4d(Vector3d scale) {
        return new Matrix4d(
                scale.x, 0, 0, 0,
                0, scale.y, 0, 0,
                0, 0, scale.z, 0,
                0, 0, 0, 1
        );
    }

    /**
     * Конструктор для запрета наследования
     */
    private LinearAlgebra() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
