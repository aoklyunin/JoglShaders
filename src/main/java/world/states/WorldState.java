package world.states;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import world.base.World;
import world.params.WorldParams;

import java.util.Objects;

/**
 * Класс состояния мира
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class WorldState {
    /**
     * id состояния мира
     */
    @JsonIgnore
    private int id;

    /**
     * тип мира
     */
    @NotNull
    private WorldParams.WorldType type;

    /**
     * Конструктор состояния мира по его типа
     *
     * @param type тип мира
     */
    @JsonCreator
    public WorldState(@NotNull @JsonProperty("type") WorldParams.WorldType type) {
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Конструктор состояния мира по другому состоянию мира
     *
     * @param worldState новое состонияе мира
     */
    public WorldState(@NotNull WorldState worldState) {
        this.type = worldState.type;
    }

    /**
     * Конструктор состояния мира по другому состоянию мира
     *
     * @param world мир
     */
    public WorldState(@NotNull World world) {
        this.type = world.getWorldParams().getType();
    }

    /**
     * Конструктор состояния мира
     */
    protected WorldState() {

    }

    /**
     * Получить id состояния мира
     *
     * @return id состояния мира
     */
    public int getId() {
        return id;
    }

    /**
     * Получить тип
     *
     * @return тип
     */
    @NotNull
    public WorldParams.WorldType getType() {
        return type;
    }

    /**
     * Строковое представление объекта вида:
     * "'type'"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + type + '\'';
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "WorldState{getString()}"
     */
    @Override
    public String toString() {
        return "WorldState{" + getString() + "}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorldState that = (WorldState) o;

        if (id != that.id) return false;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    /**
     * Преобразовать к состоянию мира с историей
     *
     * @return состояние мира с историей
     */
    @JsonIgnore
    public StoryWorldState getStoryWorldState() {
        if (this instanceof StoryWorldState)
            return (StoryWorldState) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к состоянию мира реального времени
     *
     * @return состояние мира реального времени
     */
    @JsonIgnore
    public RealTimeWorldState getRealTimeWorldState() {
        if (this instanceof RealTimeWorldState)
            return (RealTimeWorldState) this;
        throw new ClassCastException();
    }

}
