package world.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import creature.base.Creature;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.Objects;

/**
 * Класс параметров существ в мире
 */
public class CreatureInWorldParams {
    /**
     * id существа
     */
    private final int id;
    /**
     * положение существа
     */
    @NotNull
    private final Vector3d position;
    /**
     * путь к описанию существа
     */
    @NotNull
    private String path;

    /**
     * конструктор класса параметров существ в мире
     *
     * @param position положение существа
     * @param path     путь к описанию существа
     * @param id       id существа
     */
    @JsonCreator
    public CreatureInWorldParams(
            @NotNull @JsonProperty("position") Vector3d position, @NotNull @JsonProperty("path") String path,
            @JsonProperty("id") int id
    ) {
        this.id = id;
        this.position = new Vector3d(position);
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Конструктор класса параметров существ в мире
     *
     * @param creatureInWorldParams параметры существ в мире
     */
    public CreatureInWorldParams(@NotNull CreatureInWorldParams creatureInWorldParams) {
        this(
                creatureInWorldParams.position, creatureInWorldParams.path, creatureInWorldParams.id
        );
    }

    /**
     * Конструктор класса параметров существ в мире
     *
     * @param creature существо
     */
    public CreatureInWorldParams(@NotNull Creature creature) {
        this(creature.getCamera().getPos(), creature.getPath(), creature.getId());
    }

    /**
     * Получить id существа
     *
     * @return id существа
     */
    public int getId() {
        return id;
    }

    /**
     * Получить положение существа
     *
     * @return положение существа
     */
    @NotNull
    public Vector3d getPosition() {
        return position;
    }

    /**
     * Получить путь к описанию существа
     *
     * @return путь к описанию существа
     */
    @NotNull
    public String getPath() {
        return path;
    }

    /**
     * Задать путь к описанию существа
     *
     * @param path путь к описанию существа
     */
    public void setPath(@NotNull String path) {
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CreatureInWorldParams{getString()}"
     */
    @Override
    public String toString() {
        return "CreatureInWorldParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "'path', worldParams, realRenderFrequency, realTickFrequency, backgroundColor"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return id + ", " + position + ", " + path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatureInWorldParams that = (CreatureInWorldParams) o;

        if (id != that.id) return false;
        if (!Objects.equals(position, that.position)) return false;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
