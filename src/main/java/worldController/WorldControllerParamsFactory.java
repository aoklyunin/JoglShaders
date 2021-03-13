package worldController;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import worldController.base.WorldControllerParams;
import worldController.life3D.Life3DWorldControllerParams;
import worldController.realTime.RealTimeWorldControllerParams;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика параметров  контроллера мира
 */
public class WorldControllerParamsFactory {

    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(WorldControllerParamsFactory.class);

    /**
     * словарь загруженных параметров мира
     */
    private static final Map<String, WorldControllerParams> pathDict = new HashMap<>();

    /**
     * Очистить словарь путей
     */
    public static void clearPathDict() {
        pathDict.clear();
    }

    /**
     * Удалить из словаря путей конкретный путь
     *
     * @param path путь
     */
    public static void removeWorldControllerParams(String path) {
        pathDict.remove(path);
    }

    /**
     * Загрузить параметры обёртки мира
     *
     * @param path путь к миру
     * @return созданный мир
     */
    public static WorldControllerParams fromFile(String path) {
        WorldControllerParams worldControllerParams = pathDict.get(path);
        if (worldControllerParams != null)
            return WorldControllerParamsFactory.clone(worldControllerParams);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WorldControllerParams loadedWorldControllerParams =
                    objectMapper.readValue(new File(path), WorldControllerParams.class);
            pathDict.put(path, loadedWorldControllerParams);
            return loadedWorldControllerParams;
        } catch (IOException e) {
            throw new AssertionError("Error load worldControllerParams from JSON: " + path + "\n" + e);
        }
    }

    /**
     * Копировать параметры контроллера
     *
     * @param worldControllerParams параметры контроллера
     * @return копия параметров контроллера
     */
    public static WorldControllerParams clone(WorldControllerParams worldControllerParams) {
        if (worldControllerParams.getClass().equals(WorldControllerParams.class))
            return new WorldControllerParams(worldControllerParams);
        if (worldControllerParams.getClass().equals(RealTimeWorldControllerParams.class))
            return new RealTimeWorldControllerParams((RealTimeWorldControllerParams) worldControllerParams);
        if (worldControllerParams.getClass().equals(Life3DWorldControllerParams.class))
            return new Life3DWorldControllerParams((Life3DWorldControllerParams) worldControllerParams);
        throw new IllegalArgumentException("Unexpected worldContollerParams class: " + worldControllerParams.getClass());

    }

    /**
     * Конструктор для запрета наследования
     */
    private WorldControllerParamsFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
