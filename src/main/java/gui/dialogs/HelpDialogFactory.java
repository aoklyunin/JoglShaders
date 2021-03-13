package gui.dialogs;


import gui.dialogs.base.BaseHelpDialog;
import worldController.base.WorldControllerParams;
import worldController.life3D.Life3DHelpDialog;

import java.util.EnumMap;
import java.util.Map;


/**
 * Фабрика диалогов Help
 */
public class HelpDialogFactory {
    /**
     * Словарь диалогов Help
     */
    private static final Map<WorldControllerParams.WorldControllerType, BaseHelpDialog> dialogMap =
            new EnumMap<>(WorldControllerParams.WorldControllerType.class);

    /**
     * Показать окно Help
     *
     * @param worldControllerType тип контроллера
     */
    public static void showHelpDialog(WorldControllerParams.WorldControllerType worldControllerType) {
        if (dialogMap.containsKey(worldControllerType))
            dialogMap.get(worldControllerType).show();
        else {
            BaseHelpDialog baseHelpDialog = switch (worldControllerType) {
                case REAL_TIME_3D -> new Life3DHelpDialog();
            };
            dialogMap.put(worldControllerType, baseHelpDialog);
            baseHelpDialog.show();
        }
    }

    /**
     * Уничтожить все диалоги
     */
    public static void disposeAll() {
        for (Map.Entry<WorldControllerParams.WorldControllerType, BaseHelpDialog> entry : dialogMap.entrySet())
            entry.getValue().dispose();
        dialogMap.clear();
    }


    /**
     * Конструктор для запрета наследования
     */
    private HelpDialogFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
