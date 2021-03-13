package world.worldStory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import creature.CreatureFactory;
import creature.base.Creature;
import creature.base.CreatureState;
import world.WorldFactory;
import world.params.CreatureInWorldParams;
import world.states.StoryWorldState;
import world.worldStory.actualList.LinkedActualList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


/**
 * Класс истории мира
 */
public class WorldStory {

    /**
     * состояния мира
     */
    @NotNull
    @JsonIgnore
    private final LinkedActualList<StoryWorldState> statesList;
    /**
     * существа
     */
    @NotNull
    private List<Creature> creatures;
    /**
     * индекс выделенного существа
     */
    private int selectedCreatureId;
    /**
     * тип мира
     */
    @NotNull
    private String listName;


    /**
     * Конструктор истории мира
     *
     * @param creatures          существа
     * @param statesList         состояния мира
     * @param selectedCreatureId индекс выделенного существа
     * @param listName           название списка
     */
    private WorldStory(
            @NotNull List<Creature> creatures, @NotNull LinkedActualList<StoryWorldState> statesList,
            int selectedCreatureId, @NotNull String listName
    ) {
        this.creatures = new LinkedList<>(creatures);
        this.selectedCreatureId = selectedCreatureId;
        this.listName = Objects.requireNonNull(listName);
        this.statesList = new LinkedActualList<>(statesList, WorldFactory::clone);
    }

    /**
     * Конструктор истории мира
     *
     * @param worldStory другая история
     */
    public WorldStory(@NotNull WorldStory worldStory) {
        this(worldStory.creatures, worldStory.getStatesList(), worldStory.selectedCreatureId, worldStory.listName);
    }

    /**
     * Конструктор истории мира
     */
    public WorldStory() {
        statesList = new LinkedActualList<>();
    }

    /**
     * Конструктор истории мира
     *
     * @param listName название списка
     */
    public WorldStory(@NotNull String listName) {
        this.creatures = new LinkedList<>();
        this.selectedCreatureId = -1;
        this.listName = Objects.requireNonNull(listName);
        this.statesList = new LinkedActualList<>();
    }

    /**
     * Получить размер истории
     *
     * @return размер истории
     */
    @JsonIgnore
    public long getSize() {
        return statesList.size();
    }

    /**
     * Получить список состояний
     *
     * @return список состояний
     */
    public synchronized LinkedActualList<StoryWorldState> getStatesList() {
        return statesList;
    }

    /**
     * Получить список существ
     *
     * @return список существ
     */
    public List<Creature> getCreatures() {
        return creatures;
    }

    /**
     * Задать существ
     *
     * @param creatures список существ
     */
    public void setCreatures(@NotNull List<Creature> creatures) {
        this.creatures = Objects.requireNonNull(creatures);
    }

    /**
     * Получить список параметров существ в мире(для json)
     *
     * @return список параметров существ в мире
     */
    @NotNull
    @JsonGetter("creatures")
    public List<CreatureInWorldParams> getCreaturesInWorldParams() {
        return creatures.stream().map(Creature::getCreatureInWorldParams).collect(toList());
    }

    /**
     * Задать список к параметров существ в мире(для json)
     *
     * @param creatureInWorldParamsList список к параметров существ
     */
    @JsonSetter("creatures")
    public void setCreaturesInWorldParams(@NotNull List<CreatureInWorldParams> creatureInWorldParamsList) {
        creatures = creatureInWorldParamsList.stream().map(CreatureFactory::getCreature)
                .collect(toList());
    }

    /**
     * Получить список состояний истории
     *
     * @return список состояний истории
     */
    @NotNull
    @JsonProperty("states")
    @JsonGetter
    public List<StoryWorldState> getStates() {
        return statesList;
    }

    /**
     * Задать список состояний истории
     *
     * @param list список состояний истории
     */
    @JsonProperty("states")
    @JsonSetter
    public void setStates(@NotNull List<StoryWorldState> list) {
        statesList.setObjects(list);
    }


    /**
     * Добавить состояния в историю
     *
     * @param worldStory состояния, которые нужно добавить из другой истории
     */
    public void addStates(@NotNull WorldStory worldStory) {
        this.statesList.addAll(worldStory.statesList);
    }


    /**
     * Загрузить историю из файла
     *
     * @param path путь к истории
     * @return история мира
     */
    public static WorldStory loadStory(@NotNull String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(path), WorldStory.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("can not load story from " + path + "\n" + e);
        }

    }

    /**
     * Сохранить историю в фай
     * (сделано так хитро, потому что в json не влезала вся история, он выводил только часть
     * из-за ограничения на размер строки)
     *
     * @param worldStory история мира
     * @param pathToSave путь к файлу истории
     * @param worldPath  пусть к описанию мира
     */
    public static void saveStory(@NotNull WorldStory worldStory, @NotNull String pathToSave, String worldPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(pathToSave), worldStory);

        } catch (IOException e) {
            throw new AssertionError("test error" + e);
        }
    }


    /**
     * Получить существо по его id
     *
     * @param id id существа
     * @return объект сущетсваили null, если существа с таким id нету
     */
    @NotNull
    @JsonIgnore
    public Creature getCreatureById(int id) {
        for (Creature creature : creatures)
            if (creature.getId() == id)
                return creature;
        throw new IllegalArgumentException("can not find selected creature with id " + id);
    }

    /**
     * Получить состояния существ из состояния мира
     *
     * @param storyPos номер состояния мира
     * @return список состояний существ
     */
    @NotNull
    @JsonIgnore
    private List<CreatureState> getCreatureStates(int storyPos) {
        return statesList.get(storyPos).getCreatureStates();
    }

    /**
     * Получить состояния существ из текущего состояния мира
     *
     * @return список состояний существ
     */
    @NotNull
    @JsonIgnore
    public List<CreatureState> getActualCreatureStates() {
        return getStatesList().getActual().getCreatureStates();
    }

    /**
     * Возвращает выбранное существо
     *
     * @return выбранное существо
     */
    @NotNull
    @JsonIgnore
    public Creature getSelectedCreature() {
        if (selectedCreatureId == -1)
            throw new AssertionError("selected creature id is not set");
        return getCreatureById(selectedCreatureId);
    }

    /**
     * Получить id выбранного существа
     *
     * @return id выбранного существа
     */
    public int getSelectedCreatureId() {
        return selectedCreatureId;
    }

    /**
     * Задать id выбранного существа
     *
     * @param selectedCreatureId id выбранного существа
     */
    public void setSelectedCreatureId(int selectedCreatureId) {
        this.selectedCreatureId = selectedCreatureId;
    }

    /**
     * Возвращает состояние выбранного существа в заданном состоянии мира
     *
     * @param storyPos номер состояния мира
     * @return состояние выбранного существа
     */
    @NotNull
    @JsonIgnore
    private CreatureState getSelectedCreatureState(int storyPos) {
        for (CreatureState creatureState : getCreatureStates(storyPos))
            if (creatureState.getCreatureID() == selectedCreatureId)
                return creatureState;

        throw new IllegalArgumentException("can not get selected creature state with id " + selectedCreatureId);
    }

    /**
     * Возвращает состояние выбранного существа в текущем состоянии мира
     *
     * @return состояние выбранного существа
     */
    @NotNull
    @JsonIgnore
    public CreatureState getActualSelectedCreatureState() {
        return getSelectedCreatureState(getStatesList().getActualPos());
    }


    /**
     * Заменить путь у всех существ, имеющих его, на заданный
     *
     * @param oldPath старый путь
     * @param newPath новый путь
     */
    public void replaceCreaturePath(@NotNull String oldPath, @NotNull String newPath) {
        for (Creature creature : creatures)
            if (creature.getPath().equals(oldPath))
                creature.setPath(newPath);
    }

    /**
     * Очистить историю
     */
    public void clear() {
        //System.out.println("clear");
        statesList.clear();
        for (Creature creature : creatures)
            creature.clear();
    }

    /**
     * Проверка, присутствует ли состояние существа с заданным id в текущем состоянии мира
     *
     * @param creatureId id существа
     * @return флаг: да/нет
     */
    private boolean checkCreatureIdInActualWorldState(int creatureId) {
        return checkCreatureIdInWorldState(creatureId, getStatesList().getActualPos());
    }

    /**
     * Проверка, присутствует ли состояние существа с заданным id в указанном состоянии мира
     *
     * @param creatureId id существа
     * @param storyPos   номер состояния мира
     * @return флаг: да/нет
     */
    private boolean checkCreatureIdInWorldState(int creatureId, int storyPos) {
        for (CreatureState creatureState : getCreatureStates(storyPos))
            if (creatureState.getCreatureID() == creatureId)
                return true;

        return false;
    }

    /**
     * Закрыть историю
     */
    public void close() {
        //System.out.println("close " + listName);
    }

    /**
     * Перейти к концу истории
     */
    public void moveToEnd() {
        getStatesList().moveToEnd();
    }

    /**
     * Перейти к концу истории
     */
    public void moveToStart() {
        getStatesList().moveToStart();
    }

    /**
     * Изменить текущее сосотояние мира
     *
     * @param delta смещение по номеру состояния мира
     * @return насколько надо увеличить кол-во состояний мира
     */
    public int changeStoryPos(int delta) {
        return setStoryPos(getStatesList().getActualPos() + delta);
    }

    /**
     * Задать положение мира
     *
     * @param newPos новое положение мира
     * @return насколько надо увеличить кол-во состояний мира
     */
    public int setStoryPos(int newPos) {
        int delta = getStatesList().setActualPos(newPos);
        if (!checkCreatureIdInActualWorldState(selectedCreatureId))
            selectedCreatureId = statesList.get(getStatesList().getActualPos()).getCreatureStates().get(0).getCreatureID();
        return delta;
    }

    /**
     * Обрезать состояния истории по выбранному существу(удалить все состояния мира, в которых нет его состояний)
     */
    public void truncBySelectedCreature() {
        if (creatures.isEmpty())
            return;
        // если существо не выбрано,
        if (selectedCreatureId == -1)
            // выбираем первое из них
            selectedCreatureId = 0;
        getStatesList().remove(
                (StoryWorldState ws) -> {
                    boolean flgFounded = false;
                    for (CreatureState creatureState : ws.getCreatureStates())
                        if (creatureState.getCreatureID() == selectedCreatureId) {
                            flgFounded = true;
                            break;
                        }
                    return flgFounded;
                });

    }

    /**
     * Получить номер состояния существа в текущем состоянии мира по id существа
     *
     * @return номер состояния существа
     */
    @JsonIgnore
    private int getActualCreatureStateBySelectedCreature() {
        List<CreatureState> creatureStates = getActualCreatureStates();
        // если в текущем состоянии мира нет состояний существ
        if (creatureStates.isEmpty())
            return -1;
        else {
            for (int i = 0; i < creatureStates.size(); i++)
                if (creatureStates.get(i).getCreatureID() == selectedCreatureId) {
                    return i;
                }
            return -1;
        }
    }

    /**
     * Выбрать следующее существо из тех существ, состояния которых присутствуют
     * в текущем состоянии истории
     */
    public void selectNextCreature() {
        List<CreatureState> creatureStates = getActualCreatureStates();
        if (creatureStates.isEmpty()) {
            selectedCreatureId = -1;
            return;
        }
        if (selectedCreatureId == -1)
            selectedCreatureId = creatureStates.get(0).getCreatureID();
        int selectedCreatureStateNum = getActualCreatureStateBySelectedCreature();
        if (selectedCreatureStateNum == -1)
            selectedCreatureId = creatureStates.get(0).getCreatureID();
        else {
            selectedCreatureStateNum++;
            if (selectedCreatureStateNum > creatureStates.size() - 1)
                selectedCreatureStateNum = 0;
            selectedCreatureId = creatureStates.get(selectedCreatureStateNum).getCreatureID();
        }
    }

    @NotNull
    public String getListName() {
        return listName;
    }

    /**
     * Выбрать предыдущее существо из тех существ, состояния которых присутствуют
     * в текущем состоянии истории
     */
    public void selectPrevCreature() {
        List<CreatureState> creatureStates = getActualCreatureStates();
        if (creatureStates.isEmpty()) {
            selectedCreatureId = -1;
            return;
        }
        if (selectedCreatureId == -1)
            selectedCreatureId = creatureStates.get(0).getCreatureID();
        int selectedCreatureStateNum = getActualCreatureStateBySelectedCreature();
        if (selectedCreatureStateNum == -1)
            selectedCreatureId = creatureStates.get(0).getCreatureID();
        else {
            selectedCreatureStateNum--;
            if (selectedCreatureStateNum < 0)
                selectedCreatureStateNum = creatureStates.size() - 1;
            selectedCreatureId = creatureStates.get(selectedCreatureStateNum).getCreatureID();
        }
    }

    @Override
    public String toString() {
        return "WorldStory{" +
                ", listName='" + listName + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldStory that = (WorldStory) o;
        if (creatures.size() != that.creatures.size())
            return false;
        for (int i = 0; i < creatures.size(); i++) {
            if (!creatures.get(i).equals(that.creatures.get(i)))
                return false;
        }
        if (statesList.size() != that.statesList.size())
            return false;
        for (int i = 0; i < statesList.size(); i++) {
            if (!statesList.get(i).equals(that.statesList.get(i))) {
                return false;
            }
        }
        return selectedCreatureId == that.selectedCreatureId &&
                Objects.equals(listName, that.listName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statesList, creatures, selectedCreatureId, listName);
    }


}
