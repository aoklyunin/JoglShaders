package world.world3D.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import creature.base.CreatureState;
import creature.creature3D.Creature3DState;
import field.base.InfluenceFieldState;
import field.field3D.state.Food3DFieldState;
import field.field3D.state.Objects3DFieldState;
import world.params.WorldParams;
import world.states.RealTimeWorldState;
import world.world3D.RealTime3DWorld;
import world.worldStory.actualList.RealTime3DWorldStateActualList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Состояние трёхмерного мира реального времени
 */
public class RealTime3DWorldState extends RealTimeWorldState {
    /**
     * Состояния существ
     */
    @NotNull
    private List<CreatureState> creatureStates;

    /**
     * Состояние трёхмерного поля
     */
    @NotNull
    private Objects3DFieldState resourceFieldState;

    /**
     * Конструктор состояния трёхмерного мира реального времени
     *
     * @param realTimeWorldState состояние  трёхмерного мира реального времени
     */
    public RealTime3DWorldState(@NotNull RealTime3DWorldState realTimeWorldState) {
        super(realTimeWorldState);
    }

    /**
     * Конструктор состояния трёхмерного мира реального времени
     *
     * @param realTime3DWorld трёхмерный мир реального времени
     */
    public RealTime3DWorldState(@NotNull RealTime3DWorld realTime3DWorld) {
        super(realTime3DWorld);
    }

    /**
     * Конструктор состояния трёхмерного мира реального времени
     *
     * @param type               состояние мира с историей
     * @param creatureStates     состояния существ
     * @param resourceFieldState состояние ресурсного поля
     * @param tickCnt            кол-во сделанных тактов
     */
    public RealTime3DWorldState(
            @NotNull @JsonProperty("type") WorldParams.WorldType type,
            @NotNull @JsonProperty("creatureStates") List<CreatureState> creatureStates,
            @JsonProperty("tickCnt") int tickCnt,
            @NotNull @JsonProperty("resourceFieldState") InfluenceFieldState resourceFieldState
    ) {
        super(type, creatureStates, tickCnt, resourceFieldState);
    }

    /**
     * Конструктор состояния трёхмерного мира реального времени
     */
    protected RealTime3DWorldState() {

    }

    /**
     * Получить состояния существ
     *
     * @return состояния существ
     */
    @NotNull
    public List<CreatureState> getCreatureStates() {
        return creatureStates;
    }

    /**
     * Задать состояния существ
     *
     * @param creatureStates список состояний существ
     */
    public void setCreatureStates(@NotNull List<CreatureState> creatureStates) {
        this.creatureStates = new ArrayList<>();
        for (CreatureState creatureState : creatureStates) {
            Creature3DState newCreatureState = new Creature3DState((Creature3DState) creatureState);
            this.creatureStates.add(newCreatureState);
        }
    }

    /**
     * Задать состояние ресурсного поля
     *
     * @param resourceFieldState состояние ресурсного поля
     */
    public void setResourceFieldState(@NotNull InfluenceFieldState resourceFieldState) {
        this.resourceFieldState = new Food3DFieldState(resourceFieldState.getFood3DFieldState());
    }

    /**
     * Получить состояние ресурсного поля
     *
     * @return состояние ресурсного поля
     */
    @NotNull
    public InfluenceFieldState getResourceFieldState() {
        return resourceFieldState;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTime3DWorldState{getString()}"
     */
    @Override
    public String toString() {
        return "RealTime3DWorldState{" + getString() + "}";
    }

}
