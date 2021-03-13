package gui;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Параметры графического прилоэения
 */
public class GuiApplicationConf {
    /**
     * Список путей к параметрам контроллеров приложения
     */
    @NotNull
    private final List<String> controllers;
    /**
     * Номер стартового контроллера
     */
    private final int startController;

    /**
     * Конструктор параметров графического прилоэения
     *
     * @param controllers     список контроллеров
     * @param startController номер стартового контроллера
     */
    @JsonCreator
    protected GuiApplicationConf(
            @NotNull @JsonProperty("controllers") List<String> controllers,
            @JsonProperty("startController") int startController
    ) {
        this.controllers = Objects.requireNonNull(controllers);
        this.startController = startController;
    }

    /**
     * Получить список контроллеров
     *
     * @return список контроллеров
     */
    @NotNull
    public List<String> getControllers() {
        return controllers;
    }

    /**
     * Получить номер стартового контроллера
     *
     * @return номер стартового контроллера
     */
    public int getStartController() {
        return startController;
    }

    /**
     * Строковое представление объекта вида:
     * "startController: [controllers]"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return startController + ": " + controllers;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GuiApplicationConf{getString()}"
     */
    @Override
    public String toString() {
        return "GuiApplicationConf{" + getString() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuiApplicationConf that = (GuiApplicationConf) o;

        if (startController != that.startController) return false;
        return Objects.equals(controllers, that.controllers);
    }

    @Override
    public int hashCode() {
        int result = controllers.hashCode();
        result = 31 * result + startController;
        return result;
    }
}
