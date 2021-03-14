package world.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import world.WorldFactory;
import world.base.interfaces.LiveCreatureAcceptable;
import world.params.WorldParams;
import world.states.WorldState;
import world.world3D.RealTime3DWorld;

import java.util.Objects;

/**
 * Базовый класс для всех миров
 */
public abstract class World {
    /**
     * путь к миру
     */
    @NotNull
    private String path;
    /**
     * параметры мира
     */
    @NotNull
    private final WorldParams worldParams;
    /**
     * Информация о мире
     */
    @NotNull
    private final WorldInfo worldInfo;
    /**
     * Цвет фона в мире
     */
    @NotNull
    private final Vector3d backgroundColor;
    /**
     * флаг, нужно ли рисовать лог информацию
     */
    private boolean renderLogInfo;

    /**
     * Конструктор базового мира
     *
     * @param worldParams     параметры мира
     * @param path            путь к файлу описания мира
     * @param clientWidth     ширина окна в пикселях
     * @param clientHeight    высота окна в пикселях
     * @param backgroundColor цвет фона в мире
     */
    public World(
            @NotNull WorldParams worldParams, @NotNull String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor
    ) {
        this.path = Objects.requireNonNull(path);
        this.worldParams = Objects.requireNonNull(worldParams);
        this.backgroundColor = Objects.requireNonNull(backgroundColor);
        this.worldInfo = new WorldInfo(Objects.requireNonNull(worldParams), clientWidth, clientHeight);
        this.renderLogInfo = true;
    }

    /**
     * Конструктор базового мира
     *
     * @param world мир, на сонове которого нужно создать новый
     */
    public World(@NotNull World world) {
        this.path = world.path;
        this.worldParams = WorldFactory.clone(world.worldParams);
        this.worldInfo = new WorldInfo(world.worldInfo);
        this.backgroundColor = new Vector3d(world.backgroundColor);
        this.renderLogInfo = world.renderLogInfo;
    }

    /**
     * Рисовать мир
     *
     * @param gl2        переменная OpenGL
     * @param renderCS   система координат рисования
     * @param worldState состояние мира
     * @implSpec при переопределении необходимо вызывать в конце рисование, реализованное в предке
     */
    public void render(GL2 gl2, CoordinateSystem2d renderCS, WorldState worldState) {
        if (renderLogInfo)
            renderLog(gl2, worldState);
    }

    /**
     * Отображение лога
     *
     * @param gl2        переменная OpenGL
     * @param worldState состояние мира
     */
    public void renderLog(GL2 gl2, @NotNull WorldState worldState) {
        worldInfo.render();
    }

    /**
     * Закрыть мир
     */
    public abstract void close();

    /**
     * Получить путь к миру
     *
     * @return путь к миру
     */
    @NotNull
    public String getPath() {
        return path;
    }

    /**
     * Задать путь
     *
     * @param path путь
     */
    public void setPath(@NotNull String path) {
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Получить параметры мира
     *
     * @return параметры мира
     */
    @NotNull
    public WorldParams getWorldParams() {
        return worldParams;
    }

    /**
     * Получить Цвет фона в мире
     *
     * @return Цвет фона в мире
     */
    @NotNull
    public Vector3d getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Получить  флаг, нужно ли рисовать лог информацию
     *
     * @return флаг, нужно ли рисовать лог информацию
     */
    public boolean isRenderLogInfo() {
        return renderLogInfo;
    }

    /**
     * Переключить режим рисования лога
     */
    public void switchRenderLogInfoMode() {
        renderLogInfo = !renderLogInfo;
    }

    /**
     * Получить информацию о мире
     *
     * @return информация о мире
     */
    @NotNull
    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "World{getString()}"
     */
    @Override
    public String toString() {
        return "World{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "'path', worldParams, realRenderFrequency, realTickFrequency, backgroundColor"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "'" + path + "', " + worldParams + ", " + worldInfo + ", "
                + backgroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;
        if (!Objects.equals(path, world.path)) return false;
        if (renderLogInfo != world.renderLogInfo) return false;
        if (!Objects.equals(worldParams, world.worldParams)) return false;
        if (!Objects.equals(worldInfo, world.worldInfo)) return false;
        return Objects.equals(backgroundColor, world.backgroundColor);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (worldParams != null ? worldParams.hashCode() : 0);
        result = 31 * result + (worldInfo != null ? worldInfo.hashCode() : 0);
        result = 31 * result + (renderLogInfo ? 1 : 0);
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        return result;
    }

    /**
     * Преобразовать к миру с историей
     *
     * @return мир с историей
     */
    @JsonIgnore
    public StoryWorld getStoryWorld() {
        if (this instanceof StoryWorld)
            return (StoryWorld) this;
        throw new ClassCastException();
    }


    /**
     * Преобразовать к миру реального времени
     *
     * @return мир реального времени
     */
    @JsonIgnore
    public RealTimeWorld getRealTimeWorld() {
        if (this instanceof RealTimeWorld)
            return (RealTimeWorld) this;
        throw new ClassCastException();
    }


    /**
     * Преобразовать к 3D миру реального времени
     *
     * @return 3D мир реального времени
     */
    @JsonIgnore
    public RealTime3DWorld getRealTime3DWorld() {
        if (this instanceof RealTime3DWorld)
            return (RealTime3DWorld) this;
        throw new ClassCastException();
    }

    /**
     * Преобразовать к объекту, работающему с живыми существами
     *
     * @return объект, работающий с живыми существами
     */
    @JsonIgnore
    public LiveCreatureAcceptable getLiveCreatureAcceptable() {
        if (this instanceof LiveCreatureAcceptable)
            return (LiveCreatureAcceptable) this;
        throw new ClassCastException();
    }
}
