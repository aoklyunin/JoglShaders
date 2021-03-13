package offscreen;


import offscreen.params.OffscreenRendererParams;
import offscreen.renderer.GLOffscreenRenderer;
import offscreen.renderer.OffscreenRenderer;
import offscreen.renderer.SimpleOffscreenRenderer;

/**
 * Фабрика для работы с фоновыми рисовальщиками
 */
public class OffscreenRendererFactory {
    /**
     * Получить фоновый рисовальщик по его параметрам
     *
     * @param offscreenRendererParams параметры
     * @return фоновый рисовальщик
     */
    public static OffscreenRenderer of(OffscreenRendererParams offscreenRendererParams) {
        if (offscreenRendererParams.getType().equals(OffscreenRendererParams.OffscreenType.SIMPLE))
            return new SimpleOffscreenRenderer(offscreenRendererParams);
        if (offscreenRendererParams.getType().equals(OffscreenRendererParams.OffscreenType.GL))
            return new GLOffscreenRenderer(offscreenRendererParams);
        throw new AssertionError("unexpected offscreen renderer type " + offscreenRendererParams);
    }

    /**
     * Копировать фоновый рисовальщик
     *
     * @param offscreenRenderer фоновый рисовальщик
     * @return копия фонового рисовальщика
     */
    public static OffscreenRenderer clone(OffscreenRenderer offscreenRenderer) {
        if (offscreenRenderer.getClass().equals(SimpleOffscreenRenderer.class))
            return new SimpleOffscreenRenderer((SimpleOffscreenRenderer) offscreenRenderer);
        if (offscreenRenderer.getClass().equals(GLOffscreenRenderer.class))
            return new GLOffscreenRenderer((GLOffscreenRenderer) offscreenRenderer);
        throw new AssertionError("unexpected offscreen renderer type " + offscreenRenderer.getClass());

    }

    /**
     * Копировать параметры фонового рисовальщика
     *
     * @param params параметры фонового рисовальщика
     * @return копия параметров фонового рисовальщика
     */
    public static OffscreenRendererParams clone(OffscreenRendererParams params) {
        return new OffscreenRendererParams(params);
    }

    /**
     * Конструктор для запрета наследования
     */
    private OffscreenRendererFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }

}
