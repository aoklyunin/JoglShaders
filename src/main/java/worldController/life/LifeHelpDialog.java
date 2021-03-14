package worldController.life;

import com.sun.istack.NotNull;
import misc.Algorithms;
import worldController.realTime.RealTimeHelpDialog;

/**
 * Класс окна Help диалога SimpleWorld
 */
public class LifeHelpDialog extends RealTimeHelpDialog {

    /**
     * Конструктор окна Help диалога LifeWorld
     */
    public LifeHelpDialog() {
        super("Life World Help", 650, 701);
    }

    /**
     * Конструктор окна Help диалога LifeWorld
     *
     * @param name   название окна
     * @param width  ширина окна
     * @param height высота окна
     */
    public LifeHelpDialog(@NotNull String name, int width, int height) {
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
                        {"Ctrl+J", "Сохранить мир"},
                        {"Ctrl+Shift+J", "Сохранить ресурсное поле"},
                        {"Ctrl+Alt+J", "Сохранить историю в файл"},
                        {"Alt+J", "Сохранить историю в базу"},
                        {"Ctrl+O", "Загрузить мир"},
                        {"Ctrl+Shift+O", "Загрузить ресурсное поле"},
                        {"Ctrl+Alt+O", "Загрузить историю из файла"},
                        {"Alt+O", "Загрузить историю из базы"},
                        {"Ctrl+P", "Параметры мира"},
                        {"Ctrl+R", "Параметры ресурсного поля"},
                        {"R", "Включить/выключить случайное добавление ресурсов"},
                        {"F", "Выделить следующее существо"},
                        {"Ctrl+F", "Выделить предыдущее существо"},
                }
        );
    }
}
