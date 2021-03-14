package field.field3D.state;

import com.sun.istack.NotNull;
import field.field3D.Food3DField;
import jMath.aoklyunin.github.com.Transform3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс состояния поля еды
 */
public class Food3DFieldState extends Objects3DFieldState {
    /**
     * флаг, происходит ли движение влево
     */
    private boolean left;
    /**
     * состояние  СК перемещения еды
     */
    @NotNull
    private CoordinateSystem3DState foodMovingCS;
    /**
     * Скорость движения еды
     */
    private double foodMovingSpeed;
    /**
     * Список трансформаций объекта
     */
    @NotNull
    private List<Transform3d> objectTransforms;

    /**
     * Конструктор класса трёъмерного поля объектов
     *
     * @param left             флаг, что еда движется влево
     * @param foodMovingCS     СК движения еды
     * @param objectTransforms трансформации объектов мира
     * @param foodMovingSpeed  скорость движения еды
     */
    public Food3DFieldState(
            @NotNull List<Transform3d> objectTransforms, boolean left,
            @NotNull CoordinateSystem3DState foodMovingCS, double foodMovingSpeed
    ) {
        super(Objects.requireNonNull(objectTransforms));
        this.left = left;
        this.foodMovingCS = new CoordinateSystem3DState(foodMovingCS);
        this.foodMovingSpeed = foodMovingSpeed;
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     *
     * @param food3DFieldState сосотояние градиентного поля воздействия
     */
    public Food3DFieldState(@NotNull Food3DFieldState food3DFieldState) {
        super(Objects.requireNonNull(food3DFieldState));
        this.left = food3DFieldState.left;
        this.foodMovingCS = new CoordinateSystem3DState(food3DFieldState.foodMovingCS);
        this.foodMovingSpeed = food3DFieldState.foodMovingSpeed;
        this.objectTransforms = new ArrayList<>(food3DFieldState.objectTransforms);
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     *
     * @param foodField3D градиентное поле воздействия
     */
    public Food3DFieldState(@NotNull Food3DField foodField3D) {
        super(Objects.requireNonNull(foodField3D));
        this.left = foodField3D.isLeft();
        this.foodMovingCS = new CoordinateSystem3DState(foodField3D.getFoodMovingCS());
        this.foodMovingSpeed = foodField3D.getFoodMovingSpeed();
    }

    /**
     * Конструктор класса состояния градиентого поля воздействия
     */
    protected Food3DFieldState() {

    }

    /**
     * Получить  флаг, происходит ли движение влево
     *
     * @return флаг, происходит ли движение влево
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Получить состояние  СК перемещения еды
     *
     * @return состояние  СК перемещения еды
     */
    @NotNull
    public CoordinateSystem3DState getFoodMovingCS() {
        return foodMovingCS;
    }

    /**
     * Получить  Скорость движения еды
     *
     * @return Скорость движения еды
     */
    public double getFoodMovingSpeed() {
        return foodMovingSpeed;
    }

    /**
     * Задать состояние  СК перемещения еды
     *
     * @param foodMovingCS состояние  СК перемещения еды
     */
    public void setFoodMovingCS(@NotNull CoordinateSystem3DState foodMovingCS) {
        this.foodMovingCS = Objects.requireNonNull(foodMovingCS);
    }

    /**
     * Задать Скорость движения еды
     *
     * @param foodMovingSpeed Скорость движения еды
     */
    public void setFoodMovingSpeed(double foodMovingSpeed) {
        this.foodMovingSpeed = foodMovingSpeed;
    }

    /**
     * Получить трансформации объектов
     *
     * @return трансформации объектов
     */
    @NotNull
    @Override
    public List<Transform3d> getObjectTransforms() {
        return objectTransforms;
    }

    /**
     * Задать трансформации объектов
     *
     * @param objectTransforms трансформации объектов
     */
    @Override
    public void setObjectTransforms(@NotNull List<Transform3d> objectTransforms) {
        this.objectTransforms = new ArrayList<>();
        for (Transform3d transform3d : objectTransforms) {
            Transform3d newTransform3d = new Transform3d(transform3d);
            this.objectTransforms.add(newTransform3d);
        }
    }

    /**
     * Строковое представление объекта вида:
     * "getObjectTransforms().get(0).getPosition(), super.getString()"
     *
     * @return строковое представление объекта
     */

    protected String getString() {
        return getObjectTransforms().get(0).getPosition() + ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Food3DFieldState{getString()}"
     */
    @Override
    public String toString() {
        return "Food3DFieldState{" + getString() + "}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Food3DFieldState that = (Food3DFieldState) o;

        if (left != that.left) return false;
        if (Double.compare(that.foodMovingSpeed, foodMovingSpeed) != 0) return false;
        if (!Objects.equals(foodMovingCS, that.foodMovingCS)) return false;
        return objectTransforms.containsAll(that.objectTransforms);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (left ? 1 : 0);
        result = 31 * result + (foodMovingCS != null ? foodMovingCS.hashCode() : 0);
        temp = Double.doubleToLongBits(foodMovingSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (objectTransforms != null ? objectTransforms.hashCode() : 0);
        return result;
    }
}
