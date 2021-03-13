package gui.dialogs.base;

import com.sun.istack.NotNull;
import gui.GUIApplication;

import java.util.Arrays;

/**
 * Базовый класс окон Help
 */
public class BaseHelpDialog extends BaseTableDialog {
    /**
     * Конструктор базого класса окон Help
     *
     * @param name   название окна
     * @param width  ширина окна
     * @param height высота окна
     */
    public BaseHelpDialog(@NotNull String name, int width, int height) {
        super(name, width, height);
    }


    /**
     * Получить названия колонок таблицы
     *
     * @return названия колонок таблицы
     */
    @Override
    public String[] getColumnNames() {
        return new String[]{"Команды:", "Описание:"};
    }

    /**
     * Метод генерирует строки таблицы в дочерних классах окон Help надо просто переопределить его
     *
     * @return двумерный массив со строками таблицы. Первый индекс - номер строки, второй - номер ячейки в строке
     */
    @Override
    public String[][] getTableContent() {
        return new String[][]{
                {"F1", "Помощь"},
                {"Esc", "Выход"},
                {"H", "Предыдущий мир"},
        };
    }

    /**
     * Закрыть диалог
     */
    @Override
    public void close() {
        // сбрасываем флаги Ctrl, Alt, Shift
        GUIApplication.dropCAS();
        // переводим фокус обратно на канвас
        if (GUIApplication.glCanvas != null)
            GUIApplication.glCanvas.requestFocus();
        // скрываем окно
        hide();
    }

    /**
     * Показать диалог
     */
    public void show() {
        getFrame().setVisible(true);
    }

    /**
     * Отсортировать массив команд в алфовитном порядке без учёта Ctrl, Shift и Alt
     *
     * @param commands массив команд, который нужно отсортировать
     */
    @Override
    protected void sortRows(String[][] commands) {
        Arrays.sort(commands, (a, b) -> {
            String aCommand = a[0];
            String bCommand = b[0];
            aCommand = aCommand.replace("Ctrl", "").replace("Shift", " ")
                    .replace("Alt", "").replace("+", "").trim().toLowerCase();
            bCommand = bCommand.replace("Ctrl", "").replace("Shift", " ")
                    .replace("Alt", "").replace("+", "").trim().toLowerCase();
            return bCommand.compareTo(aCommand);
        });
    }
    /**
     * Строковое представление объекта вида:
     *
     * @return "BaseHelpDialog{getString()}"
     */
    @Override
    public String toString() {
        return "BaseHelpDialog{" + getString() + '}';
    }
}
