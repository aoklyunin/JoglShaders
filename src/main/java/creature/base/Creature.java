package creature.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import creature.CreatureFactory;
import creature.creature3D.Creature3D;
import graphics.Camera;
import graphics.ObjModel3D;
import world.params.CreatureInWorldParams;
import java.util.Objects;

/**
 * Класс существа
 */
public class Creature {
    /**
     * путь к существу
     */
    @NotNull
    private String path;
    /**
     * положение существа
     */
    @NotNull
    private Camera camera;
    /**
     * id существа
     */
    private final int id;
    /**
     * модель существа
     */
    @NotNull
    private final ObjModel3D creatureModel;
    /**
     * параметры существа
     */
    @NotNull
    private final CreatureParams creatureParams;

    /**
     * Конструктор существа
     *
     * @param creatureParams параметры существа
     * @param path           путь к описанию существа
     * @param camera         камера
     * @param id             id
     * @param creatureModel  модель существа
     */
    public Creature(
            @NotNull CreatureParams creatureParams, @NotNull String path, @NotNull Camera camera,
            int id, @NotNull ObjModel3D creatureModel
    ) {
        this.creatureParams = Objects.requireNonNull(creatureParams);
        this.path = Objects.requireNonNull(path);
        this.camera = Objects.requireNonNull(camera);
        this.id = id;
        this.creatureModel = Objects.requireNonNull(creatureModel);
    }

    /**
     * Конструктор существа
     *
     * @param creature существо
     */
    public Creature(@NotNull Creature creature) {
        this(
                CreatureFactory.clone(Objects.requireNonNull(creature.creatureParams)),
                Objects.requireNonNull(creature.path), new Camera(creature.camera),
                creature.id, Objects.requireNonNull(creature.creatureModel)
        );

    }

    /**
     * Получить параметры существа в мире
     *
     * @return параметры существа в мире
     */
    public CreatureInWorldParams getCreatureInWorldParams() {
        return new CreatureInWorldParams(camera.getPos(), path, id);
    }

    /**
     * Такт существа
     */
    public void tick() {

    }


    /**
     * задаём значения существа по его состоянию
     *
     * @param creatureState состояние существа
     */
    public void setState(@NotNull CreatureState creatureState) {
        this.camera = new Camera(creatureState.getCamera());
    }

    /**
     * Получить состояние существа
     *
     * @return состояние существа
     */
    @NotNull
    public CreatureState getState() {
        return new CreatureState(this);
    }



    /**
     * Вернуть существо к начальному состоянию
     */
    public void clear() {
    }


    /**
     * Получить камеру
     *
     * @return камера
     */
    @NotNull
    public Camera getCamera() {
        return camera;
    }

    /**
     * Задать камеру
     *
     * @param camera камера
     */
    public void setCamera(@NotNull Camera camera) {
        this.camera = new Camera(camera);
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
     * Получить параметры существа
     *
     * @return параметры существа
     */
    @NotNull
    public CreatureParams getCreatureParams() {
        return creatureParams;
    }

    /**
     * Получить модель существа
     *
     * @return модель существа
     */
    @NotNull
    public ObjModel3D getCreatureModel() {
        return creatureModel;
    }


    /**
     * Получить путь к описанию сщущества
     *
     * @return путь к описанию сщущества
     */
    public String getPath() {
        return path;
    }

    /**
     * Задать путь к описанию сщущества
     *
     * @param path путь к описанию сщущества
     */
    public void setPath(@NotNull String path) {
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Creature{getString()}"
     */
    @Override
    public String toString() {
        return "Creature{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "id, camera"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return id + ", " + camera;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Creature creature = (Creature) o;

        if (id != creature.id) return false;
        if (!Objects.equals(path, creature.path)) return false;
        if (!Objects.equals(camera, creature.camera)) return false;
        if (!Objects.equals(creatureModel, creature.creatureModel))
            return false;
        return Objects.equals(creatureParams, creature.creatureParams);
    }

    @Override
    public int hashCode() {
        int result= path != null ? path.hashCode() : 0;
        result = 31 * result + (camera != null ? camera.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (creatureModel != null ? creatureModel.hashCode() : 0);
        result = 31 * result + (creatureParams != null ? creatureParams.hashCode() : 0);
        return result;
    }


    /**
     * Преобразовать к 3D существу
     *
     * @return 3D существо
     */
    @JsonIgnore
    public Creature3D getCreature3D() {
        if (this instanceof Creature3D)
            return (Creature3D) this;
        throw new AssertionError();
    }

}
