package world.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import creature.base.CreatureState;
import field.base.InfluenceFieldState;
import world.base.RealTimeWorld;
import world.params.WorldParams;

import java.util.List;
import java.util.Objects;

/**
 * Класс состояния мира реального времени
 */
public abstract class RealTimeWorldState extends StoryWorldState {

    /**
     * Конструктор состояния мира реального времени
     *
     * @param type               состояние мира с историей
     * @param creatureStates     состояния существ
     * @param resourceFieldState состояние ресурсного поля
     * @param tickCnt            кол-во сделанных тактов
     */
    protected RealTimeWorldState(
            @NotNull @JsonProperty("type") WorldParams.WorldType type,
            @NotNull @JsonProperty("creatureStates") List<CreatureState> creatureStates,
            @JsonProperty("tickCnt") int tickCnt,
            @NotNull @JsonProperty("resourceFieldState") InfluenceFieldState resourceFieldState
    ) {
        super(type, creatureStates, tickCnt);
        setResourceFieldState(Objects.requireNonNull(resourceFieldState));
    }

    /**
     * Конструктор состояния мира реального времени
     *
     * @param realTimeWorldState состояние мира реального времени
     */
    public RealTimeWorldState(@NotNull RealTimeWorldState realTimeWorldState) {
        super(realTimeWorldState);
        setResourceFieldState(realTimeWorldState.getResourceFieldState());
    }

    /**
     * Конструктор состояния мира реального времени
     *
     * @param realTimeWorld мир реального времени
     */
    public RealTimeWorldState(@NotNull RealTimeWorld realTimeWorld) {
        super(realTimeWorld);
        setResourceFieldState(realTimeWorld.getResourceField().getState());
    }

    /**
     * Конструктор состояния мира реального времени
     */
    protected RealTimeWorldState() {

    }

    /**
     * Получить образцовое существо по id
     *
     * @param id id
     * @return образцовое существо
     */@NotNull
    @JsonIgnore
    public CreatureState getSampleCreatureStateById(int id) {
        for (CreatureState creatureState : getCreatureStates())
            if (creatureState.getCreatureID() == id)
                return creatureState;

        throw new IllegalArgumentException("can not get sample creature state with id " + id);
    }

    /**
     * Получить реальное существо по id
     *
     * @param id id
     * @return реальное существо
     */@NotNull
    @JsonIgnore
    public CreatureState getRealCreatureStateById(int id) {
        for (CreatureState creatureState : getCreatureStates())
            if (creatureState.getCreatureID() == id )
                return creatureState;

        throw new IllegalArgumentException("can not get sample real state with id " + id);
    }

    /**
     * Задать состояние ресурсного поля
     *
     * @param resourceFieldState состояние ресурсного поля
     */
    public abstract void setResourceFieldState(InfluenceFieldState resourceFieldState);

    /**
     * Получить состояние ресурсного поля
     *
     * @return состояние ресурсного поля
     */
    public abstract InfluenceFieldState getResourceFieldState();


    /**
     * Строковое представление объекта вида:
     * "getResourceFieldState(), super.getString()"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return getResourceFieldState() + ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTimeWorldState{getString()}"
     */
    @Override
    public String toString() {
        return "RealTimeWorldState{" + getString() + "}";
    }

    /**
     * Проверка двух состояний на равенство
     *
     * @param o объект вравнения
     * @return равны ли два состояния
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        RealTimeWorldState that = (RealTimeWorldState) o;
        return getResourceFieldState().equals(that.getResourceFieldState());
    }

    /**
     * Хэш-функция состояния мира реального времени
     *
     * @return Хэш состояния  мира реального времени
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getResourceFieldState());
    }

}
