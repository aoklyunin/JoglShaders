package world.base.interfaces;

import com.sun.istack.NotNull;
import creature.base.Creature;

/**
 * Интерфейс существа, которое можно обрабатывать
 */
public interface CreatureProcessable {
    /**
     * Обработка движения существа
     *
     * @param creature рассматриваемое существо
     */
    void processCreatureMoving(@NotNull Creature creature);

    /**
     * обрезаем положение существа с учётом границ мира
     *
     * @param creature существо, положение которого мы обрезаем
     */
    void truncateCreaturePos(Creature creature);

    /**
     * Обработка кормления существа
     *
     * @param creature существо, которое надо накормить
     */
    void processCreatureFeeding(Creature creature);
}
