package gui.dialogs.base;

import com.sun.istack.NotNull;
import gui.GUIApplication;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Базовый класс для диалоговых окон
 */
public abstract class BaseDialog implements KeyListener, AutoCloseable {
    /**
     * окно базового класса
     */
    @NotNull
    private final JFrame frame;
    /**
     * ширина окна
     */
    private final int width;
    /**
     * высота окна
     */
    private final int height;
    /**
     * флаг того, что окно закрыто
     */
    protected boolean closed;
    /**
     * Главная панель
     */
    @NotNull
    private final JPanel mainPanel;

    /**
     * Конструктор окна
     *
     * @param name          название окна
     * @param width         ширина окна
     * @param height        высота окна
     * @param layoutManager разметка панели
     */
    public BaseDialog(@NotNull String name, int width, int height, LayoutManager layoutManager) {
        this.width = width;
        this.height = height;
        // создаём окно
        frame = new JFrame(Objects.requireNonNull(name));
        // задаём заголовок
        frame.setTitle(name);
        // задаём размеры окна
        frame.setSize(width, height);
        // центрируем окно
        frame.setLocationRelativeTo(null);
        // переводим окно на передний план
        frame.toFront();
        // вышаем обработчик закрытия окна
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        // запрещаем стандартные действия закрытия окна по крестику
        // чтобы не закрылось главное приложение
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // вешаем обработчик нажатия клавиш
        frame.addKeyListener(this);

        // создаём панель
        mainPanel = new JPanel(layoutManager);
        // добавляем отступы
        Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        mainPanel.setBorder(padding);
        // вешаем обработчик клавиатуры
        mainPanel.addKeyListener(this);
        // добавляем в окно
        frame.getContentPane().add(mainPanel);
        // флаг того, что окно закрыто
        closed = false;
        // переводим фокус на главнео окно
        frame.requestFocus();
    }

    /**
     * Закрыть диалог
     */
    public void close() {
        dispose();
    }

    /**
     * Показать диалог
     */
    void hide() {
        frame.setVisible(false);
    }

    /**
     * Уничтожить заголовок
     */
    public void dispose() {
        // сбрасываем флаги Ctrl, Alt, Shift
        GUIApplication.dropCAS();
        // переводим фокус обратно на канвас
        if (GUIApplication.glCanvas != null)
            GUIApplication.glCanvas.requestFocus();
        // уничтожаем окно
        frame.dispose();
        // флаг того, что окно закрыто
        closed = true;
    }

    /**
     * Обработчик зажатия кнопки клавиатуры
     *
     * @param e событие зажатия
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Обработчик клика кнопки клавиатуры
     *
     * @param e событие клика
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            close();
        }
    }

    /**
     * Обработчик отжатия кнопки клавиатуры
     *
     * @param e событие отжатия
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL -> GUIApplication.flgCtrl = false;
            case KeyEvent.VK_ALT -> GUIApplication.flgAlt = false;
            case KeyEvent.VK_SHIFT -> GUIApplication.flgShift = false;
        }
    }

    /**
     * Получить фрейм диалога
     *
     * @return фрейм диалога
     */
    @NotNull
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Получить высоту окна
     *
     * @return высота окна
     */
    public int getHeight() {
        return height;
    }

    /**
     * Получить ширину окна
     *
     * @return ширина окна
     */
    public int getWidth() {
        return width;
    }

    /**
     * Получить главную панель фрейма
     *
     * @return главная панель фрейма
     */
    @NotNull
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Получить флаг, закрыто ли окно
     *
     * @return флаг, закрыто ли окно
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "BaseDialog{getString()}"
     */
    @Override
    public String toString() {
        return "BaseDialog{" + getString() + '}';
    }


    /**
     * Строковое представление объекта вида:
     * "width, height, closed"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return width + ", " + height + ", " + closed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDialog that = (BaseDialog) o;

        if (width != that.width) return false;
        if (height != that.height) return false;
        if (closed != that.closed) return false;
        if (!Objects.equals(frame, that.frame)) return false;
        return Objects.equals(mainPanel, that.mainPanel);
    }

    @Override
    public int hashCode() {
        int result = frame != null ? frame.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (closed ? 1 : 0);
        result = 31 * result + (mainPanel != null ? mainPanel.hashCode() : 0);
        return result;
    }
}
