package linearAlgebra;

import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для работы с полиномами
 */
public class Polinomial {
    /**
     * Генерирует случайный монический шуровский полином
     *
     * @param n степень полинома
     * @return коэффициенты полинома начиная с коэффициента при старшей степени и заканчивая при свободном члене
     */
    @NotNull
    public static Double[] generateShurPolynomialNormalDestribution(int n) {
        Double[] coeffs = new Double[n + 1];
        coeffs[0] = 1.0;
        for (int i = 1; i < n + 1; i++)
            coeffs[i] = 0.0;
        for (int i = 1; i < n; i++) {
            //System.out.println("i="+i);
            double t = ThreadLocalRandom.current().nextDouble();
            Double[] qBar = new Double[i];
            for (int j = 0; j < i; j++)
                qBar[i - j - 1] = coeffs[j] * t;
            for (int j = 0; j < i; j++) {
                coeffs[j + 1] = coeffs[j + 1] + qBar[j];
            }
        }
        return coeffs;
    }


    /**
     * Генерируем случайный шуровский полином
     *
     * @param n                   степень полином
     * @param maxCoefficientValue максимально возможное значение коэффициентов полинома
     * @return коэффициенты полинома начиная с коэффициента при старшей степени и заканчивая при свободном члене
     */
    @NotNull
    public static Double[] generateRandomShurPolynomial(int n, double maxCoefficientValue) {
        ComplexValue[] roots = new ComplexValue[n];
        Double[] coeffs = new Double[n + 1];
        coeffs[0] = 1.0;
        for (int i = 1; i < n + 1; i++)
            coeffs[i] = 0.0;

        int j = 0;
        while (j < n) {
            if (ThreadLocalRandom.current().nextBoolean() && j < n - 2) {
                double angle = 2 * Math.PI * ThreadLocalRandom.current().nextDouble();
                double r = ThreadLocalRandom.current().nextDouble();
                double a = r * Math.cos(angle);
                double b = r * Math.sin(angle);
                double[] t = new double[]{1, -2 * a, a * a + b * b};
                if (j != 0) {
                    coeffs[j + 2] = coeffs[j] * t[2];
                    coeffs[j + 1] = coeffs[j] * t[1] + coeffs[j - 1] * t[2];
                    double firstElem = coeffs[0] * t[0];
                    double secondElem = coeffs[1] * t[0] + coeffs[0] * t[1];
                    for (int i = j; i > 1; i--) {
                        coeffs[i] = coeffs[i] * t[0] + coeffs[i - 1] * t[1] + coeffs[i - 2] * t[2];
                    }
                    coeffs[0] = firstElem;
                    coeffs[1] = secondElem;
                } else {
                    coeffs[0] = t[0];
                    coeffs[1] = t[1];
                    coeffs[2] = t[2];
                }
                roots[j] = new ComplexValue(a, b);
                roots[j + 1] = new ComplexValue(a, -b);
                j += 2;
            } else {
                double v = ThreadLocalRandom.current().nextDouble();
                //double v = 0.01;
                if (ThreadLocalRandom.current().nextBoolean())
                    v = -v;
                double[] t = new double[]{1, -v};
                coeffs[j + 1] = coeffs[j] * t[1];
                double firstElem = coeffs[0] * t[0];
                for (int i = j; i > 0; i--) {
                    coeffs[i] = coeffs[i] * t[0] + coeffs[i - 1] * t[1];
                }
                coeffs[0] = firstElem;
                roots[j] = new ComplexValue(v);
                j++;
            }
            double maxCoeff = Collections.max(Arrays.asList(coeffs));
            if (maxCoeff > maxCoefficientValue)
                for (int i = 0; i < n + 1; i++) {
                    coeffs[i] = coeffs[i] / maxCoefficientValue;
                }
        }
        //assert checkRoots(roots, findRoots(coeffs), 0.0001);
        return coeffs;
    }

    /**
     * Генерация полинома с равными корнями
     *
     * @param n                   степень полинома
     * @param maxCoefficientValue максимальное значение коэффициента полинома
     * @return коэффициенты полинома начиная с коэффициента при старшей степени и заканчивая при свободном члене
     */
    @NotNull
    public static Double[] generateShurPolynmialEqualRoots(int n, double maxCoefficientValue) {
        double r = 0.1;
        Double[] coeffs = new Double[n + 1];
        ComplexValue[] roots;
        do {
            System.out.println(r);
            coeffs[0] = 1.0;
            for (int i = 1; i < n + 1; i++)
                coeffs[i] = 0.0;

            for (int i = 0; i < n; i++) {
                double[] t = new double[]{1, -r};
                coeffs[i + 1] = coeffs[i] * t[1];
                double firstElem = coeffs[0] * t[0];
                for (int j = i; j > 0; j--) {
                    coeffs[j] = coeffs[j] * t[0] + coeffs[j - 1] * t[1];
                }
                coeffs[0] = firstElem;
                double maxCoeff = Collections.max(Arrays.asList(coeffs));
                if (maxCoeff > maxCoefficientValue)
                    for (int j = 0; j < n + 1; j++) {
                        coeffs[j] = coeffs[j] / maxCoefficientValue;
                    }
            }
            r *= 0.1;
            Double[] coeffsForCheck = coeffs.clone();
            Collections.reverse(Arrays.asList(coeffsForCheck));
            roots = findRoots(coeffsForCheck);
        } while (Arrays.stream(roots).anyMatch(root -> root.getMagnitude() > 1));

        return coeffs;
    }

    /**
     * <p>
     * Given a set of polynomial coefficients, compute the roots of the polynomial.  Depending on
     * the polynomial being considered the roots may contain complex number.  When complex numbers are
     * present they will come in pairs of complex conjugates.
     * </p>
     *
     * <p>
     * Coefficients are ordered from least to most significant, e.g: y = c[0] + x*c[1] + x*x*c[2].
     * </p>
     *
     * @param coefficients Coefficients of the polynomial.
     * @return The roots of the polynomial
     */
    @NotNull
    public static ComplexValue[] findRoots(@NotNull Double[] coefficients) {
        int N = coefficients.length - 1;

        double[][] matrix = new double[N][N];

        double a = coefficients[N];
        for (int i = 0; i < N; i++) {
            matrix[i][N - 1] = -coefficients[i] / a;
        }
        for (int i = 1; i < N; i++) {
            matrix[i][i - 1] = 1;
        }

        return LinearAlgebra.getEigenValues(matrix);
    }

    /**
     * Сверка корней
     *
     * @param expectedRoots ожидаемые корни
     * @param gotRoots      полученные корни
     * @param opacity       точность, с которой надо проводить сравнение
     * @return флаг, есть ли биективная связь между списками корней с заданной точностью
     */
    public static boolean checkRoots(
            @NotNull ComplexValue[] expectedRoots, @NotNull ComplexValue[] gotRoots, double opacity
    ) {
        //System.out.println("checkRoots");
        ArrayList<ComplexValue> remainedRoots = new ArrayList<>(Arrays.asList(expectedRoots));
        for (ComplexValue gotRoot : gotRoots) {
            ComplexValue rootToRemote = null;
            for (ComplexValue remainedRoot : remainedRoots) {
                if (remainedRoot.equals(gotRoot)) {
                    rootToRemote = remainedRoot;
                    break;
                }
            }
            if (rootToRemote != null)
                remainedRoots.remove(rootToRemote);
        }
        return remainedRoots.isEmpty();
    }

    /**
     * Конструктор для запрета наследования
     */
    private Polinomial() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
