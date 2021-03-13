package creature.creature3D;

import com.sun.istack.NotNull;
import creature.base.CreatureState;
import graphics.Camera;

import java.util.Objects;

/**
 * Класс состояния 3D существа
 */
public class Creature3DState extends CreatureState {

    /**
     * Конструктор класса состояния 3D существа
     *
     * @param camera                     камера существа
     * @param creatureID                 id существа
     */
    public Creature3DState(            @NotNull Camera camera, int creatureID
    ) {
        super(
                Objects.requireNonNull(camera),  creatureID
        );
    }

    /**
     * Конструктор состояния трёхмерного существа
     *
     * @param creatureState состояние трёхмерного существа
     */
    public Creature3DState(@NotNull Creature3DState creatureState) {
        super(Objects.requireNonNull(creatureState));
    }

    /**
     * Конструктор состояния трёхмерного существа
     *
     * @param creature трёхмерное существо
     */
    public Creature3DState(@NotNull Creature3D creature) {
        super(Objects.requireNonNull(creature));
    }


    /**
     * Конструктор класса состояния 3D существа
     */
    protected Creature3DState() {
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Creature3DState{getString()}"
     */
    @Override
    public String toString() {
        return "Creature3DState{" + getString() + '}';
    }
}
