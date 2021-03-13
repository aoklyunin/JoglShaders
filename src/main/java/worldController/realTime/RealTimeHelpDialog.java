package worldController.realTime;

import com.sun.istack.NotNull;
import gui.dialogs.base.BaseHelpDialog;
import misc.Algorithms;

/**
 * Класс окна Help диалога RealTimeWorld
 */
public class RealTimeHelpDialog extends BaseHelpDialog {

    /**
     * Конструктор окна Help диалога RealTimeWorld
     */
    public RealTimeHelpDialog() {
        super("RealTime World Help", 650, 701);
    }

    /**
     * Конструктор окна Help диалога SimpleWorld
     *
     * @param name   название окна
     * @param width  ширина окна
     * @param height высота окна
     */
    public RealTimeHelpDialog(@NotNull String name, int width, int height) {
        super(name, 650, 701);
    }

    /**
     * Метод генерирует строки таблицы в дочерних классах окон Help надо просто переопределить его
     *
     * @return двумерный массив со строками таблицы. Первый индекс - номер строки, второй - номер ячейки в строке
     */
    @Override
    @NotNull
    public String[][] getTableContent() {
        return Algorithms.concatenate(
                super.getTableContent(),
                new String[][]{
                        {"Клавиша вправо", "Увеличить номер выделенного кадра истории"},
                        {"Клавиша влево", "Уменьшить номер выделенного кадра истории"},
                        {"Ctrl+C", "Настройки существа"},
                        {"T", "Рисовать мир на весь экран/Рисовать в части экрана"},
                        {"L", "Выводить/не выводить лог на экран"},
                        {"Space", "Остановить/Запустить обработку мира"},
                        {"Ctrl+Space", "Перезапустить мир"},
                        {"G", "Рисовать 3D модель существа/Рисовать решётку коннекторов"},
                        {"T", "Рисовать мир во весь экран/Рисовать мир в своём блоке"}
                }
        );
    }

}
