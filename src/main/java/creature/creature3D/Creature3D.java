package creature.creature3D;

import com.sun.istack.NotNull;
import creature.base.Creature;
import creature.base.CreatureState;
import graphics.Camera;
import graphics.ObjModel3D;
import math.Vector2i;

import java.util.Arrays;
import java.util.Objects;


/**
 * Класс 3D существа
 */
public class Creature3D extends Creature {

    /**
     * матрица значений сенсоров существа
     */
    @NotNull
    private final short[][] sensorGridValues;

    /**
     * Конструктор существа
     *
     * @param creatureParams параметры существа
     * @param path           путь к описанию существа
     * @param camera         камера
     * @param id             id
     * @param creatureModel  модель существа
     */
    public Creature3D(
            @NotNull Creature3DParams creatureParams, @NotNull String path, @NotNull Camera camera,
            int id, @NotNull ObjModel3D creatureModel
    ) {
        super(
                Objects.requireNonNull(creatureParams), Objects.requireNonNull(path), Objects.requireNonNull(camera),
                id, Objects.requireNonNull(creatureModel)
        );

        sensorGridValues = new short[creatureParams.getSensorGridSize().x][creatureParams.getSensorGridSize().y];
    }

    /**
     * Конструктор класса существа
     *
     * @param creature существо, на основе которого нужно построить новое
     */
    public Creature3D(@NotNull Creature3D creature) {
        super(Objects.requireNonNull(creature));
        sensorGridValues = new short[getCreatureParams().getCreature3DParams().getSensorGridSize().x]
                [getCreatureParams().getCreature3DParams().getSensorGridSize().y];
    }

    /**
     * Задать зрение камеры
     *
     * @param cameraVision буферзрения
     */
    public void setCameraVision(@NotNull short[][][] cameraVision) {
        Vector2i visionGridSize = getCreatureParams().getCreature3DParams().getVisionGridSize();
        for (int i = 0; i < visionGridSize.x; i++) {
            for (int j = 0; j < visionGridSize.y; j++) {
                sensorGridValues[i][j] = (short) Math.max(Math.max(
                        cameraVision[i][j][0],
                        cameraVision[i][j][1]),
                        cameraVision[i][j][2]
                );
            }
        }
    }

    /**
     * Получить состояние существа
     *
     * @return состояние существа
     */
    @NotNull
    public CreatureState getState() {
        return new Creature3DState(this);
    }

    /**
     * Получить значение матрицы зрения
     *
     * @param x  - координата X
     * @param y- координата Y
     * @return значение матрицы зрения
     */
    public int getSensorGridValue(int x, int y) {
        return sensorGridValues[x][y];
    }

    /**
     * Получить нормализованное значение матрицы зрения
     *
     * @param x  - координата X
     * @param y- координата Y
     * @return нормализованное значение матрицы зрения
     */
    public double getNormalizedSensorGridValue(int x, int y) {
        return (double) getSensorGridValue(x, y) / getCreatureParams().getCreature3DParams().getSensorGridMaxValue();
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "Creature3D{getString()}"
     */
    @Override
    public String toString() {
        return "Creature3D{" + getString() + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Creature3D that = (Creature3D) o;

        return Arrays.deepEquals(sensorGridValues, that.sensorGridValues);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(sensorGridValues);
        return result;
    }
}
