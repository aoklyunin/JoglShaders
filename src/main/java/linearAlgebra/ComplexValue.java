package linearAlgebra;

/**
 * Комплексное число
 */
public class ComplexValue {
    /**
     * вещественная компонента
     */
    private final double real;
    /**
     * мнимая компонента
     */
    private final double imag;

    /**
     * Конструктор комплексного числа
     *
     * @param real вещественная компонента
     * @param imag мнимая компонента
     */
    ComplexValue(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Конструктор комплексного числа
     *
     * @param real вещественная компонента
     */
    public ComplexValue(double real) {
        this.real = real;
        this.imag = 0;
    }

    /**
     * Получить модуль вещественного числа
     *
     * @return модуль вещественного числа
     */
    public double getMagnitude() {
        return Math.sqrt(real * real + imag * imag);
    }

    /**
     * Проверка чисел на равенство
     *
     * @param complexValue комплексное число
     * @return равны ли два еомплексных числа
     */
    public boolean equals(ComplexValue complexValue) {
        return Math.abs(this.real - complexValue.real) < 0.0001 && Math.abs(this.imag - complexValue.imag) < 0.0001;
    }

    /**
     * Получить строковое представление числа
     *
     * @return строковое представление числа
     */
    @Override
    public String toString() {
        if (Math.abs(imag) < 0.0001)
            return String.format("%.3f", real);
        return String.format("%.3f", real) + (imag > 0 ? "+" : "") + String.format("%.3f", imag) + "i";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexValue that = (ComplexValue) o;

        if (Double.compare(that.real, real) != 0) return false;
        return Double.compare(that.imag, imag) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(real);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imag);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
