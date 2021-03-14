package worldController.base;

import jMath.aoklyunin.github.com.vector.Vector2i;

/**
 * Интерфейс управления при помощи мыши
 */
public interface MouseControlled {
    /**
     * Обработка зажатия кнопки мыши
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    void processMousePress(Vector2i coords, int mouseButton);

    /**
     * Обработка отжатия кнопки мыши
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    void processMouseReleased(Vector2i coords, int mouseButton);

    /**
     * Обработка перемещения мыши с зажатой кнопкой
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    void processMouseDragged(Vector2i coords, int mouseButton);

    /**
     * Обработка клика мышью
     *
     * @param coords      координаты мыши
     * @param mouseButton номер кнопки
     */
    void processMouseClick(Vector2i coords, int mouseButton);

    /**
     * Обработка прокрутки колёсика мыши
     *
     * @param wheelRotation кол-во дискрет поворота
     */
     void processMouseWheel(int wheelRotation);
}
