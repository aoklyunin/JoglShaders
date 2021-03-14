package worldController.life3D;

import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.Algorithms;
import worldController.life.LifeHelpDialog;

/**
 * Класс окна Help диалога RealTimeWorld3D
 */
public class Life3DHelpDialog extends LifeHelpDialog {

    /**
     * Конструктор окна Help диалога LifeWorld
     */
    public Life3DHelpDialog() {
        super("Life 3D World Help", 650, 701);
    }

    /**
     * Конструктор окна Help диалога LifeWorld
     *
     * @param name   название окна
     * @param width  ширина окна
     * @param height высота окна
     */
    public Life3DHelpDialog(@NotNull String name, int width, int height) {
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
                        {"Ctrl+G", "Переключение режимов камеры"},
                }
        );
    }
}
