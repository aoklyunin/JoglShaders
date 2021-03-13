package world.worldStory.actualList;

import com.sun.istack.NotNull;
import world.states.StoryWorldState;
import world.world3D.states.RealTime3DWorldState;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * Список состояний 3D мира реального времени
 */
public class RealTime3DWorldStateActualList {
    /**
     * id состояния существа
     */
    private int id;

    /**
     * название
     */
    private String name;

    /**
     * Состояния существ
     */
    @NotNull
   private List<StoryWorldState> statesList;

    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     *
     * @param name название списка
     */
    public RealTime3DWorldStateActualList(@NotNull String name) {
        this.name = name;
        statesList = new LinkedList<>();
    }

    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     */
    public RealTime3DWorldStateActualList() {

    }

    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     *
     * @param name   название списка
     * @param states состояния
     */
    public RealTime3DWorldStateActualList(@NotNull String name, @NotNull List<StoryWorldState> states) {
        this.name = Objects.requireNonNull(name);
        statesList = new LinkedList<>(states);
    }

    /**
     * Получить название списка
     *
     * @return название списка
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Получить список состояний
     *
     * @return список состояний
     */
    @NotNull
    public List<StoryWorldState> getStatesList() {
        return statesList;
    }

    /**
     * Получить id
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Инициализация
     *
     * @param listForAdd список для добавления
     */
    public void init(@NotNull LinkedActualList<StoryWorldState> listForAdd) {
        statesList.clear();
        statesList.addAll(listForAdd);
    }

    @Override
    public String toString() {
        return "RealTime3DWorldStateActualList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", statesList=" + statesList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealTime3DWorldStateActualList that = (RealTime3DWorldStateActualList) o;

        if (id != that.id) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(statesList, that.statesList);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (statesList != null ? statesList.hashCode() : 0);
        return result;
    }
}
