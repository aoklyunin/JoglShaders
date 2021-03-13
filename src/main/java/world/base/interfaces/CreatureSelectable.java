package world.base.interfaces;

import creature.base.Creature;
import creature.base.CreatureState;

/**
 * Интерфейс мира, позволяющий получить выбранное существо
 */
public interface CreatureSelectable {
    /**
     * Получить текущее существо
     *
     * @return текущее существо
     */
   Creature getSelectedCreature();
    /**
     * Задать текущее существо
     *
     * @param creature существо, которое должно стать текущим
     */
    void setSelectedCreature(Creature creature);
    /**
     * Получить текущее существо
     *
     * @return текущее существо
     */
    CreatureState getSelectedCreatureActualState();
}
