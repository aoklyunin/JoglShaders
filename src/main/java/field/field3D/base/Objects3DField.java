package field.field3D.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem3d;
import creature.base.Creature;
import creature.creature3D.Creature3D;
import creature.creature3D.Creature3DParams;
import field.base.InfluenceField;
import field.base.InfluenceFieldState;
import field.field3D.params.Objects3DFieldParams;
import math.Transform3d;
import math.Vector2i;
import math.Vector3d;
import offscreen.OffscreenRendererFactory;
import offscreen.renderer.OffscreenRenderer;

import java.util.List;
import java.util.Objects;

/**
 * Поле 3D объектов
 */
public abstract class Objects3DField extends InfluenceField {
    /**
     * Фоновый рисовальщик
     */
    @NotNull
    @JsonIgnore
    private final OffscreenRenderer offscreenRenderer;

    /**
     * Конструктор базового класса всех полей  3D объектов
     *
     * @param object3DFieldParams параметры поля воздействия
     * @param path                путь к описанию поля воздействия
     * @param backGroundColor     цвет фона
     */
    protected Objects3DField(
            @NotNull Objects3DFieldParams object3DFieldParams, @NotNull String path, @NotNull Vector3d backGroundColor
    ) {
        super(
                Objects.requireNonNull(object3DFieldParams), Objects.requireNonNull(path),
                Objects.requireNonNull(backGroundColor)
        );
        offscreenRenderer = OffscreenRendererFactory.of(object3DFieldParams.getOffscreenRendererParams());
    }

    /**
     * Конструктор базового класса всех полей  3D объектов
     *
     * @param influenceField поле 3D объектов
     */
    public Objects3DField(@NotNull Objects3DField influenceField) {
        super(Objects.requireNonNull(influenceField));
        offscreenRenderer = OffscreenRendererFactory.clone(influenceField.offscreenRenderer);

    }

    /**
     * Инициализация поля
     *
     * @param creatures существа
     */
    public void init(@NotNull List<Creature> creatures) {
        Vector2i creatureVisionMaxSize = new Vector2i(0, 0);
        for (Creature creature : creatures)
            creatureVisionMaxSize = Vector2i.max(
                    creatureVisionMaxSize, ((Creature3DParams) creature.getCreatureParams()).getVisionGridSize());

        offscreenRenderer.init(creatureVisionMaxSize);

    }

    /**
     * Задать значения сенсоров существ
     *
     * @param creatures список существ
     * @param worldCS   СК мира
     */
    @Override
    public void setSensorValues(@NotNull List<Creature> creatures, CoordinateSystem3d worldCS) {
        setSensorValues(Objects.requireNonNull(creatures), Objects.requireNonNull(worldCS), getState());
    }

    /**
     * Задать значения сенсоров существ
     *
     * @param creatures           список существ
     * @param worldCS             СК мира
     * @param influenceFieldState состояние ресурсного поля
     */
    public void setSensorValues(
            @NotNull List<Creature> creatures, @NotNull CoordinateSystem3d worldCS,
            @NotNull InfluenceFieldState influenceFieldState
    ) {
        for (Creature creature : creatures) {
            short[][][] cameraVision = offscreenRenderer.calculateVisionBuffer(
                    creature.getCreatureParams().getCreature3DParams().getVisionGridSize(),
                    creature.getCamera(), influenceFieldState.getObjects3DFieldState().getObjectTransforms()
            );
            ((Creature3D) creature).setCameraVision(cameraVision);
        }
    }

    /**
     * Рисование поля
     *
     * @param gl2                 переменная OpenGl  для рисования
     * @param influenceFieldState состояние поля
     */
    public void render(GL2 gl2, @NotNull InfluenceFieldState influenceFieldState) {
        offscreenRenderer.render(gl2, influenceFieldState.getObjects3DFieldState().getObjectTransforms());
    }


    /**
     * Задать состояние поля
     *
     * @param influenceFieldState состояние поля воздействия
     */
    @Override
    public void setState(@NotNull InfluenceFieldState influenceFieldState) {
        for (int i = 0; i < offscreenRenderer.getObjectCnt(); i++) {
            offscreenRenderer.getObjectTransforms().set(
                    i, new Transform3d(influenceFieldState.getObjects3DFieldState().getObjectTransforms().get(i))
            );
        }
    }

    /**
     * Получить фоновый рисовальщик
     * @return фоновый рисовальщик
     */
    public OffscreenRenderer getOffscreenRenderer() {
        return offscreenRenderer;
    }

    /**
     * Очистить поле
     */
    @Override
    public void clear() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Objects3DField that = (Objects3DField) o;

        return Objects.equals(offscreenRenderer, that.offscreenRenderer);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (offscreenRenderer != null ? offscreenRenderer.hashCode() : 0);
        return result;
    }
}
