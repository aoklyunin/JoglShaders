package world.base;

import com.sun.istack.NotNull;
import creature.CreatureFactory;
import creature.base.Creature;
import creature.base.CreatureState;
import math.Transform3d;
import math.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.params.CreatureInWorldParams;
import world.params.StoryWorldParams;
import world.states.StoryWorldState;
import world.states.WorldState;
import world.worldStory.WorldStory;

import java.util.*;

import static constants.Constants.RESOURCE_PATH;


/**
 * Базовый класс для всех миров с историей
 */
public abstract class StoryWorld extends World {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(StoryWorld.class);

    /**
     * трансформация существа для рисования в режиме StandAlone
     */
    @NotNull
    private Transform3d creatureStandAloneTransform;
    /**
     * история мира
     */
    @NotNull
    private WorldStory worldStory;
    /**
     * список существ, определённых в начале всех экспериментов (id:существо)
     */
    @NotNull
    private final List<Creature> initCreaturesList;
    /**
     * Кол-во сделанных тактов
     */
    private int tickCnt;
    /**
     * Словарь соответствий порядковых номеров образцовых существ и оригиналов
     */
    @NotNull
    protected Map<Integer, Integer> sampleCreaturesSourcePosMap;

    /**
     * Конструктор базового класа для всех миров с историей
     *
     * @param worldParams     параметры мира
     * @param path            путь к файлу описания мира
     * @param clientWidth     ширина окна в пикселях
     * @param clientHeight    высота окна в пикселях
     * @param backgroundColor цвет фона в мире
     */
    protected StoryWorld(
            @NotNull StoryWorldParams worldParams, @NotNull String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor
    ) {
        super(
                Objects.requireNonNull(worldParams), Objects.requireNonNull(path), clientWidth, clientHeight,
                Objects.requireNonNull(backgroundColor)
        );
        // начальные значения трансформации для standAlone режима рисования существа
        creatureStandAloneTransform = new Transform3d(
                0, 0, 0,
                0, 0, 0,
                0.1, 0.1, 0.1
        );

        // если локальный путь к истории непустой
        if (!worldParams.getInitStoryPath().equals(""))
            // загружаем историю из файла
            worldStory = WorldStory.loadStory(RESOURCE_PATH + "stories/" + worldParams.getInitStoryPath());
        else {
            worldStory = new WorldStory(worldParams.getStatesListName());
        }
        initCreaturesList = new ArrayList<>();
        initCreatures();
        tickCnt = 0;
    }

    /**
     * Конструктор мира с историей
     *
     * @param storyWorld мир с историей
     */
    public StoryWorld(@NotNull StoryWorld storyWorld) {
        super(Objects.requireNonNull(storyWorld));
        this.creatureStandAloneTransform = new Transform3d(storyWorld.creatureStandAloneTransform);
        this.worldStory = new WorldStory(storyWorld.worldStory);
        initCreaturesList = new ArrayList<>();
        for (Creature creature : storyWorld.initCreaturesList)
            initCreaturesList.add(CreatureFactory.clone(creature));

        this.creatureStandAloneTransform = new Transform3d(storyWorld.creatureStandAloneTransform);
        this.worldStory = new WorldStory(storyWorld.worldStory);
        this.tickCnt = storyWorld.tickCnt;
    }

    /**
     * Закрыть мир
     */
    public void close() {
        worldStory.close();
    }

    /**
     * Получить текущее состояние мира
     *
     * @return текущее состояние мира
     */
    @NotNull
    public WorldState getActualWorldState() {
        if (getWorldParams().getStoryWorldParams().isRecordStory())
            return worldStory.getStatesList().getActual();
        else
            return getState();
    }

    /**
     * Инициализация существ
     */
    private void initCreatures() {
        worldStory.getCreatures().clear();
        // загружаем существ в соответсвии с инструкцией
        for (CreatureInWorldParams creatureInWorldPlaceInstruction : getWorldParams().getCreatureInWorldPlaceInstructions()) {
            // загружаем существо
            Creature creature = CreatureFactory.getCreature(creatureInWorldPlaceInstruction);
            // помещаем существо в положение, указанном в иснтрукции
            // по добавлению существ в мир
            creature.getCamera().setPos(creatureInWorldPlaceInstruction.getPosition());
            // добавляем его в список существ текущего мира
            worldStory.getCreatures().add(creature);
            initCreaturesList.add(CreatureFactory.makeClone(creature));
        }
        // если зщагружено хотя бы одно существо, выделяем первое из них
        if (!worldStory.getCreatures().isEmpty())
            worldStory.setSelectedCreatureId(0);

    }

    /**
     * Получить образцовое существо по id
     *
     * @param id id существа
     * @return образцовое существо по id
     */
    @NotNull
    private Creature getSampleCreatureById(int id) {
        for (Creature creature : worldStory.getCreatures())
            if (creature.getId() == id)
                return creature;
        throw new IllegalArgumentException("can not get sample creature with id " + id);
    }

    /**
     * Получить выбранное образцовое существо
     *
     * @return образцовое существо
     */
    @NotNull
    protected Creature getSelectedSampleCreature() {
        return getSampleCreatureById(worldStory.getSelectedCreatureId());
    }

    /**
     * Получить реальное существо по id
     *
     * @param id id существа
     * @return реальное существо по id
     */
    @NotNull
    public Creature getCreatureById(int id) {
        for (Creature creature : worldStory.getCreatures())
            if (creature.getId() == id)
                return creature;
        throw new IllegalArgumentException("can not get real creature by id " + id);
    }

    /**
     * Получить состояние реального существа по id
     *
     * @param id id существа
     * @return состояние реального существа по id
     */
    @NotNull
    CreatureState getActualCreatureStateById(int id) {
        return worldStory.getStatesList().getActual().getRealTimeWorldState().getRealCreatureStateById(id);
    }


    /**
     * Следующее состояние истории
     */
    public void incStoryPos() {
        changeStoryPos(1);
    }

    /**
     * Предыдущее состояние истории
     */
    public void decStoryPos() {
        changeStoryPos(-1);
    }

    /**
     * Сместить текущий номер состояния в истории
     *
     * @param delta величина, на которую произойдёт смещение
     */
    private void changeStoryPos(int delta) {
        // получаем, насколько надо увеличить историю
        int incStorySize = worldStory.changeStoryPos(delta);
        for (int i = 0; i < incStorySize; i++)
            tick();
    }

    /**
     * Задать текущее состояние истории
     *
     * @param pos номер состояния
     */
    public void setStoryPos(long pos) {
        int incStorySize = worldStory.setStoryPos((int) pos);
        for (int i = 0; i < incStorySize; i++)
            tick();
    }

    /**
     * Задать новое текущее состояние истории в диалоге
     *
     * @param pos новое положение
     */
    public void setStoryPosFromDialog(int pos) {
        setStoryPos(pos);
    }

    /**
     * Получить текущее существо
     *
     * @return текущее существо
     */
    @NotNull
    public Creature getSelectedCreature() {
        return worldStory.getSelectedCreature();
    }

    /**
     * Получить текущее существо
     *
     * @return текущее существо
     */
    @NotNull
    public CreatureState getSelectedCreatureActualState() {
        if (getWorldParams().getStoryWorldParams().isRecordStory())
            return worldStory.getActualSelectedCreatureState();
        else
            return worldStory.getSelectedCreature().getState();
    }

    /**
     * Такт мира
     */
    public void tick() {
        // увеличиваем кол-во тактов на 1
        tickCnt++;
        // если история записывается и нужный нам такт согласно интервалу
        if (getWorldParams().getStoryWorldParams().isRecordStory() &&
                tickCnt % getWorldParams().getStoryWorldParams().getSaveInterval() == 0
        ) {
            // добавляем новый кадр истории
            worldStory.getStatesList().add(getState());
        }
    }

    /**
     * Получить текущее состояние мира
     *
     * @return текущее состояние мира
     */
    @NotNull
    public abstract StoryWorldState getState();

    /**
     * Заменить текущее существо на новое с описанием по адресу path
     *
     * @param path путь к новому сществу
     */
    public void changeSelectedCreature(@NotNull String path) {
        worldStory.getCreatures().set(
                worldStory.getSelectedCreatureId(),
                CreatureFactory.getCreature(path, worldStory.getSelectedCreatureId())
        );
        getWorldParams().changeCreature(worldStory.getSelectedCreatureId(), Objects.requireNonNull(path));
        rebuildStory();
    }

    /**
     * Задать миру новую историю
     *
     * @param newWorldStory новая история
     */
    public void setStory(@NotNull WorldStory newWorldStory) {
        if (worldStory.getStatesList().get(0).getClass().isAssignableFrom(
                newWorldStory.getStatesList().get(0).getClass()
        )) {
            this.worldStory = new WorldStory(newWorldStory);
        }
        logger.warn(
                "setStory error: current story class " + worldStory.getStatesList().get(0).getClass() +
                        " is not assignable from new story class " + newWorldStory.getStatesList().get(0).getClass()
        );
    }

    /**
     * Задать параметры мира по сохранённому состоянию
     *
     * @param worldState сохранённое состояние
     */
    protected void setState(@NotNull WorldState worldState) {
        Set<Integer> ids = new HashSet<>();
        for (CreatureState creatureState : worldState.getStoryWorldState().getCreatureStates()) {
            getCreatureById(creatureState.getCreatureID()).setState(creatureState);
            ids.add(creatureState.getCreatureID());
        }
        // удаляем тех существ, которых нет в текущем состоянии или тех и при этом у них есть предок
        worldStory.getCreatures().removeIf(
                creature -> !(ids.contains(creature.getId()))
        );
        // сохраняем кол-во тактов
        this.tickCnt = ((StoryWorldState) worldState).getTickCnt();
    }

    /**
     * Задать параметры мира по сохранённому состоянию в истории
     *
     * @param pos номер сохранённого состояния
     */
    protected void setState(int pos) {
        setState(worldStory.getStatesList().get(pos));
    }

    /**
     * Задать параметры мира по сохранённому состоянию в истории
     */
    protected void setState() {
        setState(worldStory.getStatesList().getActual());
    }

    /**
     * Перестроить историю от начала до конца
     */
    void rebuildStory() {
        // получаем кол-во тактов истории
        int addTickCnt = worldStory.getStatesList().size();
        // очищаем существ
        for (Creature creature : worldStory.getCreatures())
            creature.clear();
        setState(0);
        // очищаем историю
        worldStory.clear();
        // заново совершаем столько же тактов, сколько было накоплено в истории
        for (int i = 0; i < addTickCnt; i++) {
            tick();
        }
    }

    /**
     * Загрузить историю мира
     *
     * @param path путь к истории мира
     */
    public void loadStory(String path) {
        worldStory = WorldStory.loadStory(path);
    }

    /**
     * Получить трансформация существа для рисования в режиме StandAlone
     *
     * @return трансформация существа для рисования в режиме StandAlone
     */
    public Transform3d getCreatureStandAloneTransform() {
        return creatureStandAloneTransform;
    }

    /**
     * Получить  история мира
     *
     * @return история мира
     */
    public WorldStory getWorldStory() {
        return worldStory;
    }

    /**
     * Получить список существ, определённых в начале всех экспериментов (id:существо)
     *
     * @return список существ, определённых в начале всех экспериментов (id:существо)
     */
    public List<Creature> getInitCreaturesList() {
        return initCreaturesList;
    }

    /**
     * Получить Кол-во сделанных тактов
     *
     * @return Кол-во сделанных тактов
     */
    public int getTickCnt() {
        return tickCnt;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "StoryWorld{getString()}"
     */
    @Override
    public String toString() {
        return "StoryWorld{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "creatureStandAloneTransform, worldStory, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return creatureStandAloneTransform + ", " + worldStory + ", " + super.getString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StoryWorld that = (StoryWorld) o;

        if (tickCnt != that.tickCnt) return false;
        if (!Objects.equals(creatureStandAloneTransform, that.creatureStandAloneTransform))
            return false;
        if (!Objects.equals(worldStory, that.worldStory)) return false;
        if (!Objects.equals(initCreaturesList, that.initCreaturesList))
            return false;
        return Objects.equals(sampleCreaturesSourcePosMap, that.sampleCreaturesSourcePosMap);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (creatureStandAloneTransform != null ? creatureStandAloneTransform.hashCode() : 0);
        result = 31 * result + (worldStory != null ? worldStory.hashCode() : 0);
        result = 31 * result + (initCreaturesList != null ? initCreaturesList.hashCode() : 0);
        result = 31 * result + tickCnt;
        result = 31 * result + (sampleCreaturesSourcePosMap != null ? sampleCreaturesSourcePosMap.hashCode() : 0);
        return result;
    }
}
