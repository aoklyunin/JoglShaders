package world.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import creature.CreatureFactory;
import creature.base.CreatureState;
import world.base.StoryWorld;
import world.params.StoryWorldParams;
import world.params.WorldParams;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Класс состояния мира с историй
 */
public class StoryWorldState extends WorldState {

    /**
     * Кол-во сделанных тактов
     */
    private int tickCnt;

    /**
     * Конструктор состояния мира с историей
     *
     * @param type           состояние мира
     * @param creatureStates состояния существ
     * @param tickCnt        кол-во тактов
     */
    protected StoryWorldState(
            @NotNull @JsonProperty("type") WorldParams.WorldType type,
            @NotNull @JsonProperty("creatureStates") List<CreatureState> creatureStates,
            @JsonProperty("tickCnt") int tickCnt
    ) {
        super(type);
        this.setCreatureStates(creatureStates);
        this.tickCnt = tickCnt;
    }

    /**
     * Конструктор состояния мира с историей
     *
     * @param storyWorldState состояние мира с историй
     */
    public StoryWorldState(@NotNull StoryWorldState storyWorldState) {
        super(storyWorldState);
        this.setCreatureStates(storyWorldState.getCreatureStates());
        this.tickCnt = storyWorldState.tickCnt;
    }

    /**
     * Конструктор состояния мира с историей
     *
     * @param storyWorld мир с историй
     */
    public StoryWorldState(@NotNull StoryWorld storyWorld) {
        super(storyWorld);
        this.setCreatureStates(CreatureFactory.getCreatureStates(storyWorld.getWorldStory().getCreatures()));
    }

    /**
     * Конструктор состояния мира с историей
     */
    protected StoryWorldState() {

    }

    /**
     * Задать состояния существ
     *
     * @param creatureStates список состояний существ
     */
    public void setCreatureStates(@NotNull List<CreatureState> creatureStates) {
        throw new AssertionError("call setCreatureStates from StoryWorldState");
    }

    /**
     * Получить состояния существ
     *
     * @return состояния существ
     */
    @NotNull
    public List<CreatureState> getCreatureStates() {
        throw new AssertionError("call getCreatureStates from StoryWorldState");
    }


    /**
     * Получить номер такта
     *
     * @return номер такта
     */
    public int getTickCnt() {
        return tickCnt;
    }

    /**
     * Строковое представление объекта вида:
     * "'type'"
     *
     * @return строковое представление объекта
     */
    @Override
    protected String getString() {
        return getCreatureStates().size() + ", " + super.getString();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "StoryWorldState{getString()}"
     */
    @Override
    public String toString() {
        return "StoryWorldState{" + getString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StoryWorldState that = (StoryWorldState) o;

        return tickCnt == that.tickCnt;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + tickCnt;
        return result;
    }
}
