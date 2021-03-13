package gui.dialogs;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import misc.Procedure;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс статичных методов для работы с диалогом
 */
public class DialogFactory {
    /**
     * Показать диалог сохранения
     *
     * @param title           заголовок
     * @param folderPath      путь к папке сохранения
     * @param defaultFileName имя файла по усолчанию
     * @param extension       расширение файла
     * @param flgSave         флаг, сохранить или загрузить
     * @return путь к выбранному файлу
     */
    public static Optional<String> showFileDialog(
            @NotNull String title, @NotNull String folderPath, @Nullable String defaultFileName, @NotNull String extension,
            boolean flgSave
    ) {
        // создаём диалог
        JFileChooser fileChooser = new JFileChooser();
        // задаём заголовок
        fileChooser.setDialogTitle(Objects.requireNonNull(title));
        // задаём директорию для диалога выбора файла
        fileChooser.setCurrentDirectory(new File(Objects.requireNonNull(folderPath)));
        // если передан файл по умолчанию
        if (defaultFileName != null)
            // задаём его
            fileChooser.setSelectedFile(new File(folderPath + defaultFileName));
        // фильтруем файлы по переданному расширению
        fileChooser.setFileFilter(new FileFilter() {
            public String getDescription() {
                return "(*." + extension + ")";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith("." + extension);
                }
            }
        });
        // в зависимости от флага созхранения, показываем нужный диалог
        int userSelection;
        if (flgSave)
            userSelection = fileChooser.showSaveDialog(null);
        else
            userSelection = fileChooser.showOpenDialog(null);

        // если пользователь выбрал кнопку сохранить/открыть
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // возвращаем новый путь
            return Optional.of(fileToSave.getAbsolutePath());
        }

        return Optional.empty();
    }

    /**
     * Показать диалог выбора папки
     *
     * @param title      заголовок
     * @param folderPath путь к папке сохранения
     * @param flgSave    флаг, сохранить или загрузить
     * @return путь к выбранному файлу
     */
    public static Optional<String> showDirectoryDialog(@NotNull String title, @NotNull String folderPath, boolean flgSave) {
        // создаём диалог
        JFileChooser fileChooser = new JFileChooser();
        // задаём заголовок
        fileChooser.setDialogTitle(Objects.requireNonNull(title));
        // задаём директорию для диалога выбора файла
        fileChooser.setCurrentDirectory(new File(Objects.requireNonNull(folderPath)));
        // говорим, что нужно выбирать только папки
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // в зависимости от флага созхранения, показываем нужный диалог
        int userSelection;
        if (flgSave)
            userSelection = fileChooser.showSaveDialog(null);
        else
            userSelection = fileChooser.showOpenDialog(null);

        // если пользователь выбрал кнопку сохранить/открыть
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // возвращаем новый путь
            return Optional.of(fileToSave.getAbsolutePath());
        }

        return Optional.empty();
    }

    /**
     * Показать диалог подтверждения
     *
     * @param msg  сообщение диалога
     * @param func что нужно сделать, если подтверждение получено
     */
    public static void showApplyDialog(@NotNull String msg, @NotNull Procedure func) {
        int input = JOptionPane.showConfirmDialog(null, Objects.requireNonNull(msg));
        if (input == 0)
            Objects.requireNonNull(func).operate();
    }

    /**
     * Показать информационный диалог
     *
     * @param msg сообщение диалога
     */
    public static void showMessageDialog(@NotNull String msg) {
        JOptionPane.showMessageDialog(null, Objects.requireNonNull(msg));
    }

    /**
     * Конструктор для запрета наследования
     */
    private DialogFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
