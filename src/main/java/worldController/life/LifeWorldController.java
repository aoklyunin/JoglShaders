package worldController.life;

import com.jogamp.opengl.awt.GLCanvas;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import field.InfluenceFieldFactory;
import field.base.InfluenceField;
import gui.GUIApplication;
import world.WorldFactory;
import world.base.World;
import worldController.realTime.RealTimeWorldController;
import worldController.realTime.RealTimeWorldControllerParams;

import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Optional;

import static constants.Constants.RESOURCE_PATH;
import static gui.dialogs.DialogFactory.showFileDialog;

/**
 * Контроллер мира реального времени
 */
public class LifeWorldController extends RealTimeWorldController {

    /**
     * Конструктор контроллера мира реального времени
     *
     * @param worldControllerParams параметры контроллера мира
     * @param glCanvas              область рисования
     */
    public LifeWorldController(@NotNull RealTimeWorldControllerParams worldControllerParams, @Nullable GLCanvas glCanvas) {
        super(worldControllerParams, glCanvas);
    }

    /**
     * Открыть мир
     */
    private void openWorld() {
        // отжать флаги клавиш Ctrl, Alt и Shift
        GUIApplication.dropCAS();
        // показываем диалог загрузки
        Optional<String> path = showFileDialog(
                "Открыть мир", RESOURCE_PATH, getWorld().getPath(), "json", false
        );
        path.ifPresent(this::loadWorld);
    }


    /**
     * Сохранить мир
     *
     * @param world мир, который нужно сохранить
     */
    private static void saveWorld(World world) {
        // отжать флаги клавиш Ctrl, Alt и Shift
        GUIApplication.dropCAS();
        // показываем диалог сохранения
        Optional<String> path = showFileDialog(
                "Сохранить мир", RESOURCE_PATH, world.getPath(), "json", true
        );
        path.ifPresent(s -> WorldFactory.saveWorld(world, s));
    }

    /**
     * Сохранить мир
     */
    private void saveWorld() {
        saveWorld(getWorld());
    }

    /**
     * Открыть историю мира
     */
    private void openResourceField() {
        // отжать флаги клавиш Ctrl, Alt и Shift
        GUIApplication.dropCAS();
        // показываем диалог загрузки
        Optional<String> path = showFileDialog(
                "Открыть ресурсное поле", RESOURCE_PATH + "fields",
                getWorld().getRealTimeWorld().getResourceField().getPath(), "json", false
        );
        if (path.isPresent()) {
            InfluenceField resourceField = InfluenceFieldFactory.load(
                    path.get(), getWorldControllerParams().getBackgroundColor(),
                    GUIApplication.clientWidth, GUIApplication.clientHeight

            );
            getWorld().getRealTimeWorld().setResourceField(resourceField);
        }
    }

    /**
     * Сохранить ресурсное поле
     *
     * @param field поле, которое нужно сохранить
     */
    private static void saveResourceField(@NotNull InfluenceField field) {
        // отжать флаги клавиш Ctrl, Alt и Shift
        GUIApplication.dropCAS();
        // показываем диалог сохранения
        Optional<String> path = showFileDialog(
                "Сохранить ресурсное поле", RESOURCE_PATH + "fields",
                Objects.requireNonNull(field).getPath(), "json", true
        );
        path.ifPresent(s -> InfluenceFieldFactory.save(s, field));
    }

    /**
     * Сохранить ресурсное поле
     */
    private void saveResourceField() {
        saveResourceField(getWorld().getRealTimeWorld().getResourceField());
    }


    /**
     * Обработка нажатия клавиатуры мыши
     *
     * @param keyCode код клавиши
     */
    @Override
    public void processKeyPress(int keyCode) {
        super.processKeyPress(keyCode);
        switch (keyCode) {
            case KeyEvent.VK_J:
                if (GUIApplication.flgCtrl) {
                    if (GUIApplication.flgShift)
                        saveResourceField();
                    else if (!GUIApplication.flgAlt)
                        saveWorld();
                }
                break;
            case KeyEvent.VK_O:
                if (GUIApplication.flgCtrl) {
                    if (GUIApplication.flgShift)
                        openResourceField();
                    else if (!GUIApplication.flgAlt)
                        openWorld();
                }
                break;
            case KeyEvent.VK_R:
                if (!GUIApplication.flgCtrl)
                    getWorld().getRealTimeWorld().getResourceField().getInfluenceFieldParams().switchRandomAdd();
                break;
            case KeyEvent.VK_V:
                getWorld().getRealTimeWorld().switchRenderCreatureConnectorsMode();
                break;
            case KeyEvent.VK_F:
                if (GUIApplication.flgCtrl)
                    getWorld().getStoryWorld().getWorldStory().selectPrevCreature();
                else
                    getWorld().getStoryWorld().getWorldStory().selectNextCreature();
                break;
            default:
                //System.out.println(keyCode);
                break;
        }
    }

    /**
     * Обработка закрытия подпрограммы
     */
    @Override
    public void close() {
        super.close();
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "LifeWorldController{getString()}"
     */
    @Override
    public String toString() {
        return "LifeWorldController{" + getString() + '}';
    }
}