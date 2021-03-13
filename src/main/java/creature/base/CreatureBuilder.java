package creature.base;

import com.sun.istack.NotNull;
import creature.CreatureFactory;
import creature.creature3D.Creature3D;
import creature.creature3D.Creature3DParams;
import graphics.Camera;
import graphics.ObjModel3D;

import java.util.Objects;

/**
 * Строитель существ
 */
public class CreatureBuilder {
    /**
     * параметры существа
     */
    @NotNull
    private final CreatureParams creatureParams;
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
    private ObjModel3D creatureModel;

    /**
     * Конструктор строителя существ
     *
     * @param creatureParams параметры существа
     * @param id             id существа
     */
    public CreatureBuilder(@NotNull CreatureParams creatureParams, int id) {
        this.creatureParams = Objects.requireNonNull(creatureParams);
        this.id = id;
        this.path = "";
    }

    /**
     * Конструктор строителя существ
     *
     * @param path путь к параметрам существа
     * @param id   id существа
     */
    public CreatureBuilder(@NotNull String path, int id) {
        this.creatureParams = CreatureFactory.fromFile(Objects.requireNonNull(path));
        this.id = id;
    }

    /**
     * Построить существо
     *
     * @return существо
     */
    @NotNull
    public Creature build() {
        // если не задана модель существа вручную
        if (creatureModel == null) {
            // строим модель по параметрам
            creatureModel = new ObjModel3D(creatureParams.getCreatureModelParams());
        }


        if (path == null)
            path = "";
        // если не задана камера
        if (camera == null) {
            // задаём камеру по умолчанию
            camera = Camera.getDefaultCamera();
        }
        // строим существо по параметрам
        if (creatureParams.getClass().equals(CreatureParams.class))
            return new Creature(creatureParams, path, camera,  id, creatureModel);
        else if (creatureParams.getClass().equals(Creature3DParams.class)) {
            return new Creature3D(
                    (Creature3DParams) creatureParams, path, camera,  id, creatureModel);
        }

        throw new IllegalArgumentException("build() error: unexpected creatureParams type " + CreatureParams.class);

    }

    /**
     * Задаём путь к существу
     *
     * @param path путь к существу
     * @return текущий экземпляр строителя
     */
    public CreatureBuilder path(@NotNull String path) {
        this.path = Objects.requireNonNull(path);
        return this;
    }

    /**
     * Задаём модели
     *
     * @param creatureModel модель существа
     * @return текущий экземпляр строителя
     */
    public CreatureBuilder models(@NotNull ObjModel3D creatureModel) {
        this.creatureModel = Objects.requireNonNull(creatureModel);
        return this;
    }

    /**
     * Задаём камеру
     *
     * @param camera камера
     * @return текущий экземпляр строителя
     */
    public CreatureBuilder camera(@NotNull Camera camera) {
        this.camera = new Camera(camera);
        return this;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "CreatureBuilder{getString()}"
     */
    @Override
    public String toString() {
        return "CreatureBuilder{" + getString() + '}';
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

        CreatureBuilder that = (CreatureBuilder) o;

        if (id != that.id) return false;
        if (!Objects.equals(creatureParams, that.creatureParams))
            return false;
        if (!Objects.equals(path, that.path)) return false;
        if (!Objects.equals(camera, that.camera)) return false;
        return Objects.equals(creatureModel, that.creatureModel);
    }

    @Override
    public int hashCode() {
        int result = creatureParams != null ? creatureParams.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (camera != null ? camera.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (creatureModel != null ? creatureModel.hashCode() : 0);
        return result;
    }
}