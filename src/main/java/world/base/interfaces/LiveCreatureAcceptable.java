package world.base.interfaces;

/**
 * Интерфейс с жизнеспособными существами
 */
public interface LiveCreatureAcceptable {
    /**
     * Размножить существ
     */
    void breedCreatures();

    /**
     * Клонировать выбранное существо
     */
    void cloneSelectedCreature();

}
