package gui.dialogs.base;

import com.sun.istack.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Базовый класс всех диалогов редактирования
 */
public abstract class BaseEditDialog extends BaseDialog {
    /**
     * Конструктор всех диалогов редактирования
     *
     * @param name          название окна
     * @param width         ширина окна
     * @param height        высота окна
     * @param layoutManager разметка панели
     */
    public BaseEditDialog(@NotNull String name, int width, int height, @NotNull LayoutManager layoutManager) {
        super(name, width, height, layoutManager);
    }

    /**
     * Обработчик клика кнопки клавиатуры
     *
     * @param e событие клика
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            save();
        }
    }

    /**
     * Сохранение
     */
    protected void save() {
        close();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "BaseEditDialog{getString()}"
     */
    @Override
    public String toString() {
        return "BaseEditDialog{" + getString() + '}';
    }
}
