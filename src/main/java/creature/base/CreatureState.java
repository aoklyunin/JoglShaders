package creature.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import graphics.Camera;
import java.util.Objects;

/**
 * Класс состояния существа
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class CreatureState {
    /**
     * id состояния существа
     */
    @JsonIgnore
    private int id;
    /**
     * id существа
     */
    private int creatureID;
    /**
     * состояние камеры существа
     */
    @NotNull
    private Camera camera;

    /**
     * Конструктор класса состояния существа
     *
     * @param camera     камера
     * @param creatureID id существа
     */
    protected CreatureState(@NotNull Camera camera, int creatureID) {
        this.creatureID = creatureID;
        this.camera = new Camera(camera);
    }

    /**
     * Конструктор класса состояния существа
     *
     * @param creature существо, состояние которого нужно получить
     */
    protected CreatureState(@NotNull Creature creature) {
        this(creature.getCamera(), creature.getId());
    }

    /**
     * Конструктор класса состояния существа
     *
     * @param creatureState существо, состояние которого нужно получить
     */
    public CreatureState(@NotNull CreatureState creatureState) {
        this(creatureState.camera, creatureState.creatureID);
        this.id = creatureState.id;
    }


    /**
     * Конструктор класса состояния существа
     */
    protected CreatureState() {
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
     * Получить id существа
     *
     * @return id существа
     */
    public int getCreatureID() {
        return creatureID;
    }


    /**
     * Получить камеру
     *
     * @return камера
     */
    @NotNull
    public Camera getCamera() {
        return camera;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CreatureState{getString()}"
     */
    @Override
    public String toString() {
        return "CreatureState{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "camera, creatureID"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return camera + ", " +
                creatureID + ", ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatureState that = (CreatureState) o;

        if (id != that.id) return false;
        if (creatureID != that.creatureID) return false;
        return Objects.equals(camera, that.camera);
    }

    @Override
    public int hashCode() {
        int result= id;
        result = 31 * result + creatureID;
        result = 31 * result + (camera != null ? camera.hashCode() : 0);
        return result;
    }
}
