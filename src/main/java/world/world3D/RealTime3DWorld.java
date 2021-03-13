package world.world3D;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem2d;
import creature.base.Creature;
import creature.base.CreatureState;
import math.Vector2d;
import math.Vector2i;
import math.Vector3d;
import misc.GLAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.base.RealTimeWorld;
import world.states.RealTimeWorldState;
import world.states.WorldState;
import world.world3D.params.RealTime3DWorldParams;
import world.world3D.states.RealTime3DWorldState;

import java.util.Map;
import java.util.Objects;


/**
 * Базовый класс всех 3D миров реального времени
 */
public abstract class RealTime3DWorld extends RealTimeWorld {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(RealTime3DWorld.class);

    /**
     * Режим камеры
     */
    public enum CameraMode {
        /**
         * режим камеры "наблюдатель"
         */
        OBSERVER,
        /**
         * режим камеры "выделенное существо"
         */
        SELECTED_CREATURE;
        /**
         * Варианты
         */
        private static final CameraMode[] vals = values();

        /**
         * Получить следующий режим
         *
         * @return следующий режим
         */
        @NotNull
        public CameraMode next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    /**
     * режим камеры
     */
    @NotNull
    private CameraMode cameraMode = CameraMode.OBSERVER;

    /**
     * СК рисования сетки сенсоров
     */
    @NotNull
    private CoordinateSystem2d renderSensorGridCS;
    /**
     * СК рисования  сетки моторов
     */
    @NotNull
    private CoordinateSystem2d renderMotorGridCS;
    /**
     * Отношение ширины окна к высоте
     */
    private final double connectorGridDistortion;
    /**
     * информация о мире
     */
    private final RealTimeWorld3DInfo realTimeWorld3DInfo;

    /**
     * Конструктор базового класса для всех миров реального времени
     *
     * @param realTimeWorldParams параметры мира реального времени
     * @param path                путь к миру
     * @param clientWidth         ширина окна в пикселях
     * @param clientHeight        высота окна в пикселях
     * @param backgroundColor     цвет фона в мире
     * @param windowDivide        пропорции разбиения экрана
     */
    public RealTime3DWorld(
            @NotNull RealTime3DWorldParams realTimeWorldParams, @NotNull String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor, @NotNull Vector2d windowDivide
    ) {
        super(realTimeWorldParams, path, clientWidth, clientHeight, backgroundColor);
        realTimeWorld3DInfo = new RealTimeWorld3DInfo(
                getWorldParams().getRealTime3DWorldParams(), getWorldInfo()
        );


        for (Creature creature : getWorldStory().getCreatures()) {
            creature.getCamera().setPos(new Vector3d(0, 0, 0.5));
            creature.getCamera().setDir(new Vector3d(1.0, 0, 0));
            creature.getCamera().setUp(new Vector3d(0.0, 0, 1.0));
        }

        for (Creature creature : getInitCreaturesList()) {
            creature.getCamera().setPos(new Vector3d(0, 0, 0.5));
            creature.getCamera().setDir(new Vector3d(1.0, 0, 0));
            creature.getCamera().setUp(new Vector3d(0.0, 0, 1.0));
        }

        connectorGridDistortion = (double) clientHeight / clientWidth * windowDivide.y / (1 - windowDivide.x);
        // создаём системы для рисования решёток коннекторов
        double offset = 0.05;
        renderSensorGridCS = new CoordinateSystem2d(offset, 0.5 - offset / 2, offset, 1.0 - offset);
        renderMotorGridCS = new CoordinateSystem2d(0.5 + offset / 2, 1.0 - offset, offset, 1.0 - offset);

        // делаем обе системы квадратными
        renderSensorGridCS = renderSensorGridCS.getQuadCS(connectorGridDistortion);
        renderMotorGridCS = renderMotorGridCS.getQuadCS(connectorGridDistortion);

        checkNewNDAPath = (String newDNAPath) -> true;
    }

    /**
     * Обработка надатия мыши по решётке коннекторов
     *
     * @param mouseGLPos  положение мыши
     * @param mouseButton кнопка мыши
     * @return обработано ли нажатие на данном уровне абстракции
     */
    public boolean clickConnectorGrid(@NotNull Vector2d mouseGLPos, int mouseButton) {
        // клик по решётке сенсоров
        if (renderSensorGridCS.checkCoords(Objects.requireNonNull(mouseGLPos))) {
            Vector2i sensorGridPos = getSelectedCreature().getCreatureParams().getCreature3DParams()
                    .getRenderSensorGridCS().getCoords(mouseGLPos, renderSensorGridCS);
            System.out.println("sensor pos " + sensorGridPos);
            return true;
        }
        System.out.println("nop");
        return false;
    }


    /**
     * Рисовать сетку коннкеторов
     *
     * @param gl2      переменная OpenGL
     * @param creature существо
     */
    public void renderConnectorGrid(GL2 gl2, @NotNull Creature creature) {
        Vector2d quadSize = renderSensorGridCS.getSimilarity(
                creature.getCreatureParams().getCreature3DParams().getRenderSensorGridCS()
        );
        Vector2d quadOffset = new Vector2d(quadSize);

        // проходим по всме x-координатам СК отображения
        for (int i = 0; i < creature.getCreatureParams().getCreature3DParams().getSensorGridSize().x; i++) {
            for (int j = 0; j < creature.getCreatureParams().getCreature3DParams().getSensorGridSize().y; j++) {
                Vector3d color = new Vector3d(0, 1, 0);
                gl2.glColor4d(
                        color.x, color.y, color.z,
                        creature.getCreature3D().getNormalizedSensorGridValue(i, j) * 0.5 + 0.5
                );
                // получаем размер квадрата коннкетора
                Vector2d quadPos = Vector2d.sum(renderSensorGridCS.getCoords(
                        i, j, creature.getCreatureParams().getCreature3DParams().getRenderSensorGridCS()
                        ), quadOffset
                );
                // рисуем квадрат
                GLAlgorithms.renderFilledQuad(gl2, quadPos, quadSize);
            }
        }
    }

    @Override
    public void init() {
        getResourceField().getObjects3DField().init(getWorldStory().getCreatures());
        calculateCreaturesSensorGrid();
    }


    /**
     * Получить текущее состояние мира
     *
     * @return текущее состояние мира
     */
    @NotNull
    @Override
    public RealTime3DWorldState getState() {
        return new RealTime3DWorldState(this);
    }


    /**
     * Рисование существа 3D
     *
     * @param gl2           переменная OpenGL
     * @param creatureState состояние существа
     */
    protected void renderCreature3D(GL2 gl2, @NotNull CreatureState creatureState) {
        gl2.glPushMatrix();

        // задаём OpenGL трансформацию
        getCreatureStandAloneTransform().apply(gl2);

        getWorldStory().getSelectedCreature().getCreatureModel().render(gl2);

        //renderStandAloneCreatureConnectors(gl2);

        gl2.glPopMatrix();
    }

    /**
     * Задать текущее существо
     *
     * @param creature существо, которое должно стать текущим
     */
    public void setSelectedCreature(@NotNull Creature creature) {
        getInitCreaturesList().set(0, Objects.requireNonNull(creature));
        creature.getCamera().setPos(getWorldStory().getCreatures().get(0).getCamera().getPos());
        getWorldStory().getCreatures().set(0, creature);
    }

    /**
     * Отображение лога
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    public void renderLog(GL2 gl2, @NotNull WorldState worldState) {
        super.renderLog(gl2, Objects.requireNonNull(worldState));
        switch (cameraMode) {
            case OBSERVER -> getWorldInfo().getTextControllers().get("cameraMode").drawText(
                    "Camera: observer"
            );
            case SELECTED_CREATURE -> getWorldInfo().getTextControllers().get("cameraMode").drawText(
                    "Camera: selected creature"
            );
        }
    }


    /**
     * Изменить режим камеры
     */
    public void switchCameraMode() {
        cameraMode = cameraMode.next();
    }


    /**
     * Обработчик изменения положения истории при помощи скроллера
     */
    @Override
    public void onChangeStoryPosByScroller() {
        super.onChangeStoryPosByScroller();
        calculateCreaturesSensorGrid();
    }

    /**
     * рассчёт сетки сенсоров существ
     *
     * @param worldState состояние мира
     */
    public void calculateCreaturesSensorGrid(@NotNull WorldState worldState) {
        getResourceField().getFood3DField().setSensorValues(
                getWorldStory().getCreatures(),
                getWorldParams().getRealTimeWorldParams().getWorldCS(),
                worldState.getRealTimeWorldState().getResourceFieldState()
        );
    }

    /**
     * рассчёт сетки сенсоров существ
     */
    public void calculateCreaturesSensorGrid() {
        calculateCreaturesSensorGrid(getWorldStory().getStatesList().getActual());
    }

    /**
     * Получить режим камеры
     *
     * @return режим камеры
     */
    @NotNull
    public CameraMode getCameraMode() {
        return cameraMode;
    }

    /**
     * Получить информацию о мире
     *
     * @return информацию о мире
     */
    @NotNull
    public RealTimeWorld3DInfo getRealTimeWorld3DInfo() {
        return realTimeWorld3DInfo;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTime3DWorld{getString()}"
     */
    @Override
    public String toString() {
        return "RealTime3DWorld{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "cameraMode, realTimeWorld3DInfo, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return cameraMode + ", " + realTimeWorld3DInfo + ", " + super.getString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RealTime3DWorld that = (RealTime3DWorld) o;

        if (cameraMode != that.cameraMode) return false;
        if (Double.compare(that.connectorGridDistortion, connectorGridDistortion) != 0) return false;
        if (!Objects.equals(renderSensorGridCS, that.renderSensorGridCS))
            return false;
        if (!Objects.equals(renderMotorGridCS, that.renderMotorGridCS))
            return false;
        return Objects.equals(realTimeWorld3DInfo, that.realTimeWorld3DInfo);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + cameraMode.hashCode();
        result = 31 * result + (renderSensorGridCS != null ? renderSensorGridCS.hashCode() : 0);
        result = 31 * result + (renderMotorGridCS != null ? renderMotorGridCS.hashCode() : 0);
        temp = Double.doubleToLongBits(connectorGridDistortion);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (realTimeWorld3DInfo != null ? realTimeWorld3DInfo.hashCode() : 0);
        return result;
    }
}
