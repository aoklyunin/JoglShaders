package creature.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import creature.creature3D.Creature3DParams;
import graphics.ObjModel3DParams;
import math.Vector3d;

import java.util.Objects;


/**
 * Класс параметров существа
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class CreatureParams {

    /**
     * параметры модели существа
     */
    @NotNull
    private final ObjModel3DParams creatureModelParams;
    /**
     * имя существа
     */
    @NotNull
    private final String name;

    /**
     * Получить параметры модели существа
     *
     * @return параметры модели существа
     */
    @NotNull
    public ObjModel3DParams getCreatureModelParams() {
        return creatureModelParams;
    }

    /**
     * Конструктор класса параметров существа
     *
     * @param name                имя существа
     * @param creatureModelParams параметры модели существа
     */
    @JsonCreator
    public CreatureParams(
            @NotNull @JsonProperty("name") String name,
            @NotNull @JsonProperty("creatureModelParams") ObjModel3DParams creatureModelParams
    ) {
        this.name = Objects.requireNonNull(name);
        this.creatureModelParams = Objects.requireNonNull(creatureModelParams);
    }

    /**
     * Конструктор класса параметров существа
     *
     * @param creatureParams параметры существа, на основе которых нужно построить новые парамтеры
     */
    public CreatureParams(@NotNull CreatureParams creatureParams) {
        this(creatureParams.name, creatureParams.creatureModelParams);
    }

    /**
     * Получить имя существа
     *
     * @return имя существа
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CreatureParams{getString()}"
     */
    @Override
    public String toString() {
        return "CreatureParams{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * ""
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatureParams that = (CreatureParams) o;
        if (!Objects.equals(creatureModelParams, that.creatureModelParams))
            return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = creatureModelParams != null ? creatureModelParams.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    /**
     * Преобразовать к параметрам 3D существа
     *
     * @return параметры 3D существа
     */
    @JsonIgnore
    public Creature3DParams getCreature3DParams() {
        if (this instanceof Creature3DParams)
            return (Creature3DParams) this;
        throw new AssertionError();
    }
}
