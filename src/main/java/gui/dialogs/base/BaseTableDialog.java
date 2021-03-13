package gui.dialogs.base;

import com.sun.istack.NotNull;
import gui.GUIApplication;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Базовый класс окон Help
 */
public abstract class BaseTableDialog extends BaseDialog {
    /**
     * максимальная ширина колонки
     */
    private static final int MAX_COLUMN_WIDTH = 1000;
    /**
     * минимальная ширина колонки
     */
    private static final int MIN_COLUMN_WIDTH = 10;

    /**
     * Конструктор базого класса окон Help
     *
     * @param name   название окна
     * @param width  ширина окна
     * @param height высота окна
     */
    public BaseTableDialog(@NotNull String name, int width, int height) {
        super(name, width, height, null);
        init(getTableContent(), getColumnNames());
    }

    /**
     * Инициализация табличного диалога
     *
     * @param tableContent содержимое таблицы
     * @param columns      названия колонок
     */
    protected void init(@NotNull String[][] tableContent, @NotNull String[] columns) {
        // задаём размеры
        getMainPanel().setSize(getWidth(), getHeight());
        // сортируем команды
        sortRows(tableContent);
        // создаём таблицу
        JTable table = new JTable(tableContent, columns);

        // подстраиваем ширину колонок
        resizeColumnWidth(table);
        // вешаем обработчик клавиатуры на таблицу
        table.addKeyListener(this);

        // создаём панель со скролером
        JScrollPane scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // добавляем панель со скролером на главную панель
        getMainPanel().add(scrollPane);
        // добавляем панели со скролеером отступы
        scrollPane.setBounds(15, 15, getWidth() - 50, getHeight() - 70);

        // добавляем панель в окно
        getFrame().add(getMainPanel());
        // скрываем окно
        getFrame().setVisible(false);
    }

    /**
     * Получить названия колонок таблицы
     *
     * @return названия колонок таблицы
     */
    @NotNull
    public String[] getColumnNames() {
        return new String[]{""};
    }

    /**
     * Метод генерирует строки таблицы в дочерних классах окон Help надо просто переопределить его
     *
     * @return двумерный массив со строками таблицы. Первый индекс - номер строки, второй - номер ячейки в строке
     */
    @NotNull
    public String[][] getTableContent() {
        return new String[][]{{""}};
    }

    /**
     * Подстраивает ширину колонок под размер их содержимого
     *
     * @param table таблица, в которой нужно изменить ширину колонок
     */
    private static void resizeColumnWidth(@NotNull JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        // перебираем колонки
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = MIN_COLUMN_WIDTH;
            // перебираем строки и находим максимальную ширину колонки
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            // если ширина больше максимально допустимой
            if (width > MAX_COLUMN_WIDTH)
                // меняем ширину на максимально допустимую
                width = MAX_COLUMN_WIDTH;
            // устанавливаем новую ширину колонки
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * Показать диалог
     */
    public void show() {
        if (!closed)
            getFrame().setVisible(true);
    }

    @Override
    public void close() {
        // скрываем форму
        getFrame().dispose();
        // сбрасываем флаги Ctrl, Alt, Shift
        GUIApplication.dropCAS();
        // переводим фокус обратно на канвас
        GUIApplication.glCanvas.requestFocus();
    }

    /**
     * Отсортировать массив команд в алфовитном порядке без учёта Ctrl, Shift и Alt
     *
     * @param commands массив команд, который нужно отсортировать
     */
    protected abstract void sortRows(@NotNull String[][] commands);

    /**
     * Строковое представление объекта вида:
     *
     * @return "BaseTableDialog{getString()}"
     */
    @Override
    public String toString() {
        return "BaseTableDialog{" + getString() + '}';
    }
}
