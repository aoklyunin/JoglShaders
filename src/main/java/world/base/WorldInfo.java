package world.base;

import com.sun.istack.NotNull;
import graphics.GLTextController;
import misc.CaptionParams;
import misc.GLConsole;
import world.params.WorldParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс информации о мире
 */
public class WorldInfo {
    /**
     * реальная частота рисования
     */
    private long realRenderFrequency;
    /**
     * реальная частота циклов перерасчёта мира
     */
    private long realTickFrequency;
    /**
     * Консоль для вывода текста
     */
    private final GLConsole glConsole;
    /**
     * словарь контроллеров текста
     */
    @NotNull
    private final Map<String, GLTextController> textControllers;

    /**
     * Конструктор информации о мире
     *
     * @param worldParams  параметры мира
     * @param clientWidth  ширина окна
     * @param clientHeight высота окна
     */
    WorldInfo(@NotNull WorldParams worldParams, int clientWidth, int clientHeight) {
        textControllers = CaptionParams.getTextControllersFromParams(
                worldParams.getCaptionParamsMap(), clientWidth, clientHeight
        );
        glConsole = new GLConsole(textControllers.get("console"), worldParams);
    }

    /**
     * Конструктор информации о мире
     *
     * @param worldInfo информация о мире
     */
    WorldInfo(@NotNull WorldInfo worldInfo) {
        this.realRenderFrequency = worldInfo.realRenderFrequency;
        this.realTickFrequency = worldInfo.realTickFrequency;
        this.textControllers = new HashMap<>(worldInfo.textControllers);
        this.glConsole = new GLConsole(worldInfo.glConsole);
    }

    /**
     * Рисование информации о мире
     */
    public void render() {
        textControllers.get("fps").drawText(
                "fps: " + realRenderFrequency
        );
        textControllers.get("frequency").drawText(
                "frequency: " + realTickFrequency
        );
        glConsole.render();
    }

    /**
     * Получить контроллеры текстов
     *
     * @return контроллеры текстов
     */
    public Map<String, GLTextController> getTextControllers() {
        return textControllers;
    }


    /**
     * Задать реальную частоту рисования
     *
     * @param realRenderFrequency реальная частота рисования
     */
    public void setRealRenderFrequency(long realRenderFrequency) {
        this.realRenderFrequency = realRenderFrequency;
    }

    /**
     * Задать реальную частоту тактов мира
     *
     * @param realTickFrequency реальная частота тактов мира
     */
    public void setRealTickFrequency(long realTickFrequency) {
        this.realTickFrequency = realTickFrequency;
    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "WorldInfo{getString()}"
     */
    @Override
    public String toString() {
        return "WorldInfo{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "'path', worldParams, realRenderFrequency, realTickFrequency, backgroundColor"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return realRenderFrequency + ", " + realTickFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorldInfo worldInfo = (WorldInfo) o;

        if (realRenderFrequency != worldInfo.realRenderFrequency) return false;
        return realTickFrequency == worldInfo.realTickFrequency;
    }

    @Override
    public int hashCode() {
        int result = (int) (realRenderFrequency ^ (realRenderFrequency >>> 32));
        result = 31 * result + (int) (realTickFrequency ^ (realTickFrequency >>> 32));
        return result;
    }
}
