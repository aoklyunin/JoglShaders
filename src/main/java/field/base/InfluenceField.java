package field.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import creature.base.Creature;
import field.InfluenceFieldFactory;
import field.field3D.Food3DField;
import field.field3D.base.Objects3DField;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem3d;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.List;
import java.util.Objects;

/**
 * Базовый класс всех полей воздействия
 */
public abstract class InfluenceField {
    /**
     * параметры поля воздействия
     */
    @NotNull
    private final InfluenceFieldParams influenceFieldParams;
    /**
     * путь к описанию поля воздействия
     */
    @NotNull
    private final String path;
    /**
     * цвет фона
     */
    protected Vector3d backGroundColor;

    /**
     * Конструктор базового класс всех полей воздействия
     *
     * @param influenceFieldParams параметры поля воздействия
     * @param path                 путь к описанию поля воздействия
     * @param backGroundColor      цвет фона
     */
    protected InfluenceField(
            @NotNull InfluenceFieldParams influenceFieldParams, @NotNull String path, @NotNull Vector3d backGroundColor
    ) {
        this.influenceFieldParams = Objects.requireNonNull(influenceFieldParams);
        this.path = Objects.requireNonNull(path);
        this.backGroundColor = Objects.requireNonNull(backGroundColor);
    }

    /**
     * Конструктор базового класс всех полей воздействия
     *
     * @param influenceField поле воздействия
     */
    public InfluenceField(@NotNull InfluenceField influenceField) {
        this.influenceFieldParams =
                InfluenceFieldFactory.clone(Objects.requireNonNull(influenceField).influenceFieldParams);
        this.path = influenceField.path;
        this.backGroundColor = new Vector3d(influenceField.backGroundColor);
    }

    /**
     * Такт поля воздействия
     */
    public abstract void tick();

    /**
     * Задать значения сенсоров существ
     *
     * @param creatures список существ
     * @param worldCS   СК мира
     */
    public abstract void setSensorValues(@NotNull List<Creature> creatures, @NotNull CoordinateSystem3d worldCS);


    /**
     * Получить состояние поля воздействия
     *
     * @return состояния поля воздействия
     */
    public abstract InfluenceFieldState getState();

    /**
     * Задать значения по состоянию поля
     *
     * @param influenceFieldState состояние поля воздействия
     */
    public abstract void setState(InfluenceFieldState influenceFieldState);

    /**
     * Очистить поле
     */
    public abstract void clear();

    /**
     * Рисовать лог поля
     *
     * @param gl2                 переменная OpenGL для рисования
     * @param influenceFieldState состояние ресурсного поля
     */
    public abstract void renderLog(GL2 gl2, InfluenceFieldState influenceFieldState);

    /**
     * Получить параметры поля воздействия
     *
     * @return параметры поля воздействия
     */
    public InfluenceFieldParams getInfluenceFieldParams() {
        return influenceFieldParams;
    }

    /**
     * Получить путь к описанию поля воздействия
     *
     * @return путь к описанию поля воздействия
     */
    @NotNull
    public String getPath() {
        return path;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "InfluenceField{getString()}"
     */
    @Override
    public String toString() {
        return "InfluenceField{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "'path', influenceFieldParams"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + path + '\'' + ", " + influenceFieldParams;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfluenceField that = (InfluenceField) o;

        if (!Objects.equals(influenceFieldParams, that.influenceFieldParams))
            return false;
        if (!Objects.equals(path, that.path)) return false;
        return Objects.equals(backGroundColor, that.backGroundColor);
    }

    @Override
    public int hashCode() {
        int result = influenceFieldParams != null ? influenceFieldParams.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (backGroundColor != null ? backGroundColor.hashCode() : 0);
        return result;
    }

    /**
     * Преобразовать к полю с окружением
     *
     * @return поле с окружением
     */
    @JsonIgnore
    public EnvironField getEnvironField() {
        if (this instanceof EnvironField)
            return (EnvironField) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к полю с 3D объектами
     *
     * @return поле с 3D объектами
     */
    @JsonIgnore
    public Objects3DField getObjects3DField() {
        if (this instanceof Objects3DField)
            return (Objects3DField) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к полю с 3D едой
     *
     * @return  поле с 3D едой
     */
    @JsonIgnore
    public Food3DField getFood3DField() {
        if (this instanceof Food3DField)
            return (Food3DField) this;
        throw new ClassCastException();
    }


}
