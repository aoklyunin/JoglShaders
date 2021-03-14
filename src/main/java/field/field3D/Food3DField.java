package field.field3D;

import com.github.aoklyunin.javaGLHelper.CaptionParams;
import com.github.aoklyunin.javaGLHelper.GLTextController;
import com.github.aoklyunin.javaScrollers.scrollers.ProgressBar;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import field.base.InfluenceFieldState;
import field.field3D.base.Objects3DField;
import field.field3D.params.Food3DFieldParams;
import field.field3D.state.CoordinateSystem3DState;
import field.field3D.state.Food3DFieldState;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem3d;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.Map;
import java.util.Objects;

/**
 * Класс трёхмерного поля еды
 */
public class Food3DField extends Objects3DField {
    /**
     * Кол-во положений скроллера диапазона движения еды
     */
    private static final int FOOD_MOVING_RANGE_SCROLLER_SIZE = 100;
    /**
     * Кол-во положений скроллера скорости движения еды
     */
    private static final int FOOD_MOVING_SPEED_SCROLLER_SIZE = 100;

    /**
     * флаг, движется ли еда влево
     */
    private boolean left;
    /**
     * Прогресс-бар диапазона движения еды
     */
    @NotNull
    private final ProgressBar foodMovingRangeProgressBar;
    /**
     * Шаг прогресс-бара диапазона движения еды
     */
    @NotNull
    private final Vector3d foodMovingRangeStep;
    /**
     * Прогресс-бар скорости движения еды
     */
    @NotNull
    private final ProgressBar foodMovingSpeedProgressBar;
    /**
     * Шаг прогресс-бара скорости движения еды
     */
    private double foodMovingSpeedStep;
    /**
     * словарь контроллеров текста
     */
    @NotNull
    private final Map<String, GLTextController> textControllers;
    /**
     * СК перемещения еды
     */
    @NotNull
    private CoordinateSystem3d foodMovingCS;
    /**
     * Скорость движения еды
     */
    private double foodMovingSpeed;

    /**
     * Конструктор базового класс всех полей воздействия
     *
     * @param food3DFieldParams параметры поля воздействия
     * @param path              путь к описанию поля воздействия
     * @param backGroundColor   цвет фона
     * @param clientWidth       ширина окна
     * @param clientHeight      высота окна
     */
    public Food3DField(
            @NotNull Food3DFieldParams food3DFieldParams, @NotNull String path, @NotNull Vector3d backGroundColor,
            int clientWidth, int clientHeight
    ) {
        super(
                Objects.requireNonNull(food3DFieldParams), Objects.requireNonNull(path),
                Objects.requireNonNull(backGroundColor)
        );
        left = true;

        foodMovingCS = new CoordinateSystem3d(food3DFieldParams.getInitFoodMovingCS());
        foodMovingSpeed = food3DFieldParams.getInitFoodMovingSpeed();

        foodMovingRangeStep = Vector3d.mul(
                (Vector3d) (food3DFieldParams.getFoodMovingRangeChangeRange().max)
                , 1.0 / FOOD_MOVING_RANGE_SCROLLER_SIZE);

        foodMovingRangeProgressBar = ProgressBar.of(
                food3DFieldParams.getFoodMovingRangeScrollerParams(),
                () -> (long) FOOD_MOVING_RANGE_SCROLLER_SIZE,
                (newVal) -> foodMovingCS.setNewSize(
                        Vector3d.mul(foodMovingRangeStep, newVal)
                )
        );

        foodMovingRangeProgressBar.setCursorPos(
                (long) (food3DFieldParams.getInitFoodMovingCS().getSize().y / foodMovingRangeStep.y)
        );

        // прогресс-бар диапазона перемещния шара
        foodMovingSpeedStep = (double) food3DFieldParams.getFoodMovingSpeedChangeRange().getMax()
                / FOOD_MOVING_SPEED_SCROLLER_SIZE;
        foodMovingSpeedProgressBar = ProgressBar.of(
                food3DFieldParams.getFoodMovingSpeedScrollerParams(),
                () -> (long) FOOD_MOVING_SPEED_SCROLLER_SIZE,
                (newVal) -> foodMovingSpeed = foodMovingSpeedStep * newVal
        );
        foodMovingSpeedProgressBar.setCursorPos(
                (long) (food3DFieldParams.getInitFoodMovingSpeed() / foodMovingSpeedStep)
        );
        textControllers = CaptionParams.getTextControllersFromParams(
                food3DFieldParams.getCaptionParamsMap(), clientWidth, clientHeight
        );

    }

    /**
     * Конструктор трёхмерного поля еды
     *
     * @param influenceField поле
     */
    public Food3DField(@NotNull Food3DField influenceField) {
        super(Objects.requireNonNull(influenceField));
        this.left = influenceField.left;
        this.foodMovingRangeProgressBar = influenceField.foodMovingRangeProgressBar;
        this.foodMovingRangeStep = influenceField.foodMovingRangeStep;
        this.foodMovingSpeedProgressBar = influenceField.foodMovingSpeedProgressBar;
        this.textControllers = influenceField.textControllers;
        this.foodMovingCS = influenceField.foodMovingCS;
        this.foodMovingSpeed = influenceField.foodMovingSpeed;
    }

    /**
     * Обработка надатия мыши по полю
     *
     * @param mouseGLPos  положение мыши
     * @param renderCS    СК рисования
     * @param mouseButton кнопка мыши
     * @return обработано ли нажатие на данном уровне абстракции
     */
    public boolean click(@NotNull Vector2d mouseGLPos, @NotNull CoordinateSystem2d renderCS, int mouseButton) {
        if (foodMovingRangeProgressBar.setScrollerCursorPosByClick(Objects.requireNonNull(mouseGLPos)))
            return true;
        return foodMovingSpeedProgressBar.setScrollerCursorPosByClick(mouseGLPos);
    }

    /**
     * Рисовать лог поля
     *
     * @param gl2 переменная OpenGL для рисования
     */
    @Override
    public void renderLog(GL2 gl2, @NotNull InfluenceFieldState influenceFieldState) {
        foodMovingRangeProgressBar.renderScroller(
                gl2, (long) (influenceFieldState.getFood3DFieldState().getFoodMovingCS().getSize().y / foodMovingRangeStep.y)
        );
        foodMovingSpeedProgressBar.renderScroller(
                gl2, (long) (influenceFieldState.getFood3DFieldState().getFoodMovingSpeed() / foodMovingSpeedStep)
        );
        textControllers.get("foodMovingRange").drawText("range");
        textControllers.get("foodMovingSpeed").drawText("speed");
    }

    /**
     * Получить состояние поля воздействия
     *
     * @return состояния поля воздействия
     */
    @NotNull
    @Override
    public InfluenceFieldState getState() {
        return new Food3DFieldState(this);
    }

    /**
     * Такт поля еды
     */
    @Override
    public void tick() {
        Vector3d foodPos = getOffscreenRenderer().getObjectTransforms().get(0).getPosition();
        if (left) {
            foodPos.y += foodMovingSpeed;
        } else {
            foodPos.y -= foodMovingSpeed;
        }
        if (foodPos.y > foodMovingCS.getMax().y) {
            foodPos.y = foodMovingCS.getMax().y;
            left = false;
        }
        if (foodPos.y < foodMovingCS.getMin().y) {
            foodPos.y = foodMovingCS.getMin().y;
            left = true;
        }
        getOffscreenRenderer().getObjectTransforms().get(0).setPosition(foodPos);
    }


    /**
     * Задать значения по состоянию поля
     *
     * @param influenceFieldState состояние поля воздействия
     */
    @Override
    public void setState(@NotNull InfluenceFieldState influenceFieldState) {
        super.setState(Objects.requireNonNull(influenceFieldState));
        left = influenceFieldState.getFood3DFieldState().isLeft();
        foodMovingSpeed = influenceFieldState.getFood3DFieldState().getFoodMovingSpeed();
        CoordinateSystem3DState state = influenceFieldState.getFood3DFieldState().getFoodMovingCS();
        foodMovingCS = new CoordinateSystem3d(state.getMin(), state.getMax());
    }

    /**
     * Получить флаг, движется ли еда влево
     *
     * @return флаг, движется ли еда влево
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * Получить словарь контроллеров текста
     *
     * @return словарь контроллеров текста
     */
    @NotNull
    public Map<String, GLTextController> getTextControllers() {
        return textControllers;
    }

    /**
     * Получить СК перемещения еды
     *
     * @return СК перемещения еды
     */
    @NotNull
    public CoordinateSystem3d getFoodMovingCS() {
        return foodMovingCS;
    }

    /**
     * Получить Скорость движения еды
     *
     * @return Скорость движения еды
     */
    public double getFoodMovingSpeed() {
        return foodMovingSpeed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Food3DField that = (Food3DField) o;

        if (left != that.left) return false;
        if (Double.compare(that.foodMovingSpeedStep, foodMovingSpeedStep) != 0) return false;
        if (Double.compare(that.foodMovingSpeed, foodMovingSpeed) != 0) return false;
        if (!Objects.equals(foodMovingRangeProgressBar, that.foodMovingRangeProgressBar))
            return false;
        if (!Objects.equals(foodMovingRangeStep, that.foodMovingRangeStep))
            return false;
        if (!Objects.equals(foodMovingSpeedProgressBar, that.foodMovingSpeedProgressBar))
            return false;
        if (!Objects.equals(textControllers, that.textControllers))
            return false;
        return Objects.equals(foodMovingCS, that.foodMovingCS);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (left ? 1 : 0);
        result = 31 * result + (foodMovingRangeProgressBar != null ? foodMovingRangeProgressBar.hashCode() : 0);
        result = 31 * result + (foodMovingRangeStep != null ? foodMovingRangeStep.hashCode() : 0);
        result = 31 * result + (foodMovingSpeedProgressBar != null ? foodMovingSpeedProgressBar.hashCode() : 0);
        temp = Double.doubleToLongBits(foodMovingSpeedStep);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (textControllers != null ? textControllers.hashCode() : 0);
        result = 31 * result + (foodMovingCS != null ? foodMovingCS.hashCode() : 0);
        temp = Double.doubleToLongBits(foodMovingSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
