package world.base;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem2d;
import creature.CreatureFactory;
import creature.base.Creature;
import creature.base.CreatureState;
import field.InfluenceFieldFactory;
import field.base.InfluenceField;
import math.Vector2d;
import math.Vector3d;
import scrollers.scrollers.SimpleScroller;
import world.base.interfaces.CreatureProcessable;
import world.params.RealTimeWorldParams;
import world.states.StoryWorldState;
import world.states.WorldState;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static constants.Constants.RESOURCE_PATH;


/**
 * Базовый класс для всех миров реального времени
 */
public abstract class RealTimeWorld extends StoryWorld implements CreatureProcessable {
    /**
     * флаг, нужно ли рисовать коннекторы у существ
     */
    private boolean renderCreatureConnectors;
    /**
     * поле ресурса
     */
    @NotNull
    private InfluenceField resourceField;
    /**
     * скроллер истории мира
     */
    @NotNull
    private final SimpleScroller worldStoryScroller;


    /**
     * Конструктор базового класса для всех миров реального времени
     *
     * @param realTimeWorldParams параметры мира реального времени
     * @param path                путь к миру
     * @param clientWidth         ширина окна в пикселях
     * @param clientHeight        высота окна в пикселях
     * @param backgroundColor     цвет фона в мире
     */
    public RealTimeWorld(
            @NotNull RealTimeWorldParams realTimeWorldParams, String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor
    ) {
        super(realTimeWorldParams, path, clientWidth, clientHeight, backgroundColor);

        // загружаем ресурсное поле
        this.resourceField = InfluenceFieldFactory.load(
                RESOURCE_PATH + realTimeWorldParams.getResourceFieldPath(), backgroundColor, clientWidth, clientHeight
        );
        if (getWorldStory().getStatesList().isEmpty())
            getWorldStory().getStatesList().clear(getState());

        // создаём скроллер истории мира
        worldStoryScroller = new SimpleScroller(
                () -> (long) getWorldStory().getStatesList().size(),
                () -> (long) getWorldStory().getStatesList().getActualPos(),
                (newVal) -> {
                    getWorldStory().getStatesList().setActualPos(newVal.intValue());
                    onChangeStoryPosByScroller();
                },
                getWorldParams().getStoryWorldParams().getStoryScrollerParams()
        );

        renderCreatureConnectors = true;

    }

    /**
     * Конструктор базового класса для всех миров реального времени
     *
     * @param realTimeWorld мир реального времени
     */
    public RealTimeWorld(@NotNull RealTimeWorld realTimeWorld) {
        super(realTimeWorld);
        this.resourceField = InfluenceFieldFactory.clone(realTimeWorld.resourceField);
        this.worldStoryScroller = new SimpleScroller(realTimeWorld.worldStoryScroller);
        this.renderCreatureConnectors = realTimeWorld.renderCreatureConnectors;
    }


    /**
     * Обработчик изменения положения истории при помощи скроллера
     * ЕСЛИ ОПЯТЬ БУДУТ ПРОБЛЕМЫ С ПРОИЗВОДИТЕЛЬНОСТЬЮ, ПРОВЕРЬ, НЕ ОБРАБАТЫВАЮТСЯ ЛИ ОБРАЗЦОВЫЕ СУЩЕСТВА!
     */
    public void onChangeStoryPosByScroller() {
        selectCreatureByStoryState();
    }


    /**
     * Обработка надатия мыши по миру
     *
     * @param mouseGLPos  положение мыши
     * @param renderCS    СК рисования
     * @param mouseButton кнопка мыши
     * @return обработано ли нажатие на данном уровне абстракции
     */
    public boolean clickWorld(@NotNull Vector2d mouseGLPos, @NotNull CoordinateSystem2d renderCS, int mouseButton) {
        return false;
    }


    /**
     * Рисовать модель существа
     *
     * @param gl2        переменная OpenGL
     * @param worldState состояние мира
     */
    public void renderCreatureModel(GL2 gl2, @NotNull WorldState worldState) {
        if (getWorldStory().getSelectedCreature() == null)
            return;
        if (!getWorldStory().getCreatures().isEmpty()) {
            gl2.glPushMatrix();

            getCreatureStandAloneTransform().apply(gl2);

            renderCreatureModelConnectors(gl2, worldState);

            getWorldStory().getSelectedCreature().getCreatureModel().render(gl2);

            gl2.glPopMatrix();

        }
    }

    /**
     * рисование коннекторов существа в режиме standAlone
     *
     * @param gl2        переменная OpenGL
     * @param worldState состояние мира
     * @implSpec вызывается при рисовании модели существа
     */
    protected abstract void renderCreatureModelConnectors(GL2 gl2, WorldState worldState);


    /**
     * Возвращает состояние выбранного существа в заданном состоянии мира
     *
     * @param worldState состояние мира
     * @return состояние выбранного существа
     */
    @NotNull
    protected CreatureState getSelectedCreatureState(@NotNull WorldState worldState) {
        StoryWorldState storyWorldState = worldState.getStoryWorldState();
        for (CreatureState creatureState : storyWorldState.getCreatureStates())
            if (creatureState.getCreatureID() == getWorldStory().getSelectedCreatureId())
                return creatureState;

        throw new AssertionError("can not get selected creature state");
    }

    /**
     * Инициализация мира
     */
    public void init() {
        resourceField.clear();
        getWorldStory().clear();
        getWorldStory().getCreatures().clear();
        for (Creature creature : getInitCreaturesList()) {
            getWorldStory().getCreatures().add(CreatureFactory.clone(creature));
        }
        getWorldStory().getStatesList().add(getState());
    }

    /**
     * Задать параметры мира по сохранённому состоянию
     *
     * @param worldState сохранённое состояние
     */
    @Override
    protected void setState(@NotNull WorldState worldState) {
        super.setState(worldState);
        resourceField.setState(worldState.getRealTimeWorldState().getResourceFieldState());
    }

    /**
     * Отображение лога
     *
     * @param gl2        переменная OpenGL
     * @param worldState состояние мира
     */
    public void renderLog(GL2 gl2, @NotNull WorldState worldState) {
        super.renderLog(gl2, worldState);
        resourceField.renderLog(gl2, worldState.getRealTimeWorldState().getResourceFieldState());
    }

    /**
     * Отображение лога
     *
     * @param gl2        переменная OpenGL
     * @param worldState состояние мира
     */
    protected void renderAdditionalLog(GL2 gl2, @NotNull WorldState worldState) {
        if (resourceField.getInfluenceFieldParams().isRandomAdd())
            getWorldInfo().getTextControllers().get("random").drawText("random");

        getWorldInfo().getTextControllers().get("creatureCnt").drawText(
                "creatureCnt: " + getWorldStory().getCreatures().size()
        );
        getWorldInfo().getTextControllers().get("storySize").drawText(
                "storySize: " + getWorldStory().getStatesList().size() + " pos " +
                        getWorldStory().getStatesList().getActualPos()
        );

    }


    /**
     * Задать миру новое ресурсное поле
     *
     * @param influenceField поле, которое будет задано
     */
    public void setResourceField(@NotNull InfluenceField influenceField) {
        this.resourceField = Objects.requireNonNull(influenceField);
        rebuildStory();
    }

    /**
     * Перестроить историю, начиная с заданного положения
     *
     * @param pos положение, начиная с которого надо перестроить историю
     */
    protected void rebuildStory(int pos) {
        if (pos < getWorldStory().getStatesList().size()) {
            setState(pos);
            // получаем кол-во тактов истории
            int tickCnt = getWorldStory().getStatesList().size();
            // заново совершаем столько же тактов, сколько было накоплено в истории
            for (int i = pos + 1; i < tickCnt; i++) {
                tick();
            }
            getWorldStory().getStatesList().setActualPos(pos);
        }
    }

    public void tick() {
        tick(true);
    }

    /**
     * Такт мира
     *
     * @param flgSelectCreatureByStoryState флаг, нужно ли выделять существо по текущему состоянию мира
     *                                      (нужно если при перемещении по состояниям мы пришли в
     *                                      состояние, где текущего выбранного существа, наприиер, ещё нет)
     */
    public void tick(boolean flgSelectCreatureByStoryState) {
        // такт ресурсного поля
        resourceField.tick();
        // обрабатываем существ
        processCreatures();
        if (getWorldParams().getStoryWorldParams().isRecordStory())
            if (flgSelectCreatureByStoryState) {
                // обрезаем историю(если выбранное состояние истории не конечное)
                truncWorldStoryByStoryPos();
                // выбрать существо по текущему состоянию мира
                selectCreatureByStoryState();
            }
        super.tick();
    }


    /**
     * Обработка существ
     */
    private void processCreatures() {
        resourceField.setSensorValues(
                getWorldStory().getCreatures(), getWorldParams().getRealTimeWorldParams().getWorldCS()
        );
        for (Creature creature : getWorldStory().getCreatures()) {
            // обрабатываем движение существа
            processCreatureMoving(creature);
            // обрабатываем кормление существа
            processCreatureFeeding(creature);
        }
    }


    /**
     * Получить текущее существо
     *
     * @return текущее существо
     */
    @NotNull
    @Override
    public CreatureState getSelectedCreatureActualState() {
        return getSelectedCreatureActualState();
    }


    /**
     * Обрезать историю(если выбранное состояние истории не конечное)
     */
    private void truncWorldStoryByStoryPos() {
        if (getWorldStory().getStatesList().truncByActualPos())
            setState();
    }

    /**
     * Выбрать существо по текущему состоянию мира
     */
    private void selectCreatureByStoryState() {
        // получаем состояния существ в выбранном состоянии истории мира
        List<CreatureState> creatureStates = getWorldStory().getActualCreatureStates();
        // если состояний нет, то снимаем выделение существа
        if (creatureStates.size() == 0)
            getWorldStory().setSelectedCreatureId(-1);
        else {
            boolean flgSelectedCreatureInWorld = false;
            int aliveCreatureID = -1;
            // перебираем все состояния живых существ
            for (CreatureState creatureState : creatureStates) {

                // если состояние выбранного существа есть в текущем состоянии истории мира
                if (creatureState.getCreatureID() == getWorldStory().getSelectedCreatureId()) {
                    flgSelectedCreatureInWorld = true;
                    break;
                }
                aliveCreatureID = creatureState.getCreatureID();

            }
            // если выбранное существо не имеет живого состояния в выбранном состоянии мира
            // или существо вообще не выбрано
            if (!flgSelectedCreatureInWorld || getWorldStory().getSelectedCreatureId() == -1)
                // выбираем существо, соответствующее последнему живому состоянию
                getWorldStory().setSelectedCreatureId(aliveCreatureID);
        }
    }

    /**
     * Получить  поле ресурса
     *
     * @return поле ресурса
     */
    @NotNull
    public InfluenceField getResourceField() {
        return resourceField;
    }


    /**
     * Получить скроллер истории мира
     *
     * @return скроллер истории мира
     */
    @NotNull
    public SimpleScroller getWorldStoryScroller() {
        return worldStoryScroller;
    }

    /**
     * Переключить режим рисования коннекторов существа
     */
    public void switchRenderCreatureConnectorsMode() {
        renderCreatureConnectors = !renderCreatureConnectors;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTimeWorld{getString()}"
     */
    @Override
    public String toString() {
        return "RealTimeWorld{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "resourceField, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return resourceField + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RealTimeWorld that = (RealTimeWorld) o;

        if (renderCreatureConnectors != that.renderCreatureConnectors) return false;
        if (!Objects.equals(resourceField, that.resourceField))
            return false;
        return Objects.equals(worldStoryScroller, that.worldStoryScroller);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (renderCreatureConnectors ? 1 : 0);
        result = 31 * result + (resourceField != null ? resourceField.hashCode() : 0);
        result = 31 * result + (worldStoryScroller != null ? worldStoryScroller.hashCode() : 0);
        return result;
    }

    /**
     * анонимный интерфейс проверки доступности нового пути для днк
     */
    public Function<String, Boolean> checkNewNDAPath;

    /**
     * анонимный интерфейс проверки доступности нового пути для существа
     */
    public final Function<String, Boolean> checkNewCreaturePath = (String newCreaturePath) -> {
        for (Creature loopCreature : getWorldStory().getCreatures())
            if (loopCreature.getPath().equals(newCreaturePath))
                return false;
        return true;
    };
}
