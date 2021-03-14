package world;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.base.World;
import world.params.LifeWorldParams;
import world.params.RealTimeWorldParams;
import world.params.StoryWorldParams;
import world.params.WorldParams;
import world.states.StoryWorldState;
import world.states.WorldState;
import world.world3D.Food3DWorld;
import world.world3D.params.RealTime3DWorldParams;
import world.world3D.states.RealTime3DWorldState;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static constants.Constants.RESOURCE_PATH;


/**
 * Класс фабрики миров
 */
public class WorldFactory {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(WorldFactory.class);

    /**
     * словарь загруженных параметров мира
     */
    private static final Map<String, WorldParams> pathDict = new HashMap<>();

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
     * Получить параметры мира из json
     *
     * @param path путь к параметрам мира
     * @return параметры мира
     */
    public static WorldParams fromFile(@NotNull String path) {
        WorldParams worldParams = pathDict.get(Objects.requireNonNull(path));
        if (worldParams != null)
            return clone(worldParams);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WorldParams loadedWorldParams =
                    objectMapper.readValue(new File(path), WorldParams.class);
            pathDict.put(path, loadedWorldParams);
            return loadedWorldParams;
        } catch (IOException e) {
            throw new IllegalArgumentException("Parse params file error: " + path + "\n" + e.getMessage());
        }
    }

    /**
     * Получить копию состояния мира c историей
     *
     * @param storyWorldState состояние мира c историей
     * @return копия состояния мира c историей
     */
    public static StoryWorldState clone(@NotNull StoryWorldState storyWorldState) {
        switch (storyWorldState.getType()) {
            case FOOD_3D_WORLD:
                return new RealTime3DWorldState((RealTime3DWorldState) storyWorldState);
        }
        throw new IllegalArgumentException("unresolved state type in add state " + storyWorldState.getType());
    }


    /**
     * Получить копию состояния мира
     *
     * @param worldState состояние мира
     * @return копия состояния мира
     */
    public static WorldState clone(@NotNull WorldState worldState) {
        if (worldState.getClass().equals(WorldState.class))
            return new WorldState(worldState);
        else if (worldState.getClass().equals(StoryWorldState.class))
            return new StoryWorldState((StoryWorldState) worldState);
        else if (worldState.getClass().equals(RealTime3DWorldState.class))
            return new RealTime3DWorldState((RealTime3DWorldState) worldState);
        throw new IllegalArgumentException("unresolved world state class " + worldState.getClass().getName());
    }

    /**
     * Получить копию параметров мира
     *
     * @param worldParams параметры мира
     * @return копия параметров мира
     */
    public static WorldParams clone(@NotNull WorldParams worldParams) {
        if (worldParams.getClass().equals(WorldParams.class)) {
            return new WorldParams(worldParams);
        } else if (worldParams.getClass().equals(StoryWorldParams.class)) {
            return new StoryWorldParams((StoryWorldParams) worldParams);
        } else if (worldParams.getClass().equals(RealTimeWorldParams.class)) {
            return new RealTimeWorldParams((RealTimeWorldParams) worldParams);
        } else if (worldParams.getClass().equals(LifeWorldParams.class)) {
            return new LifeWorldParams((LifeWorldParams) worldParams);
        } else if (worldParams.getClass().equals(RealTime3DWorldParams.class)) {
            return new RealTime3DWorldParams((RealTime3DWorldParams) worldParams);
        }
        throw new IllegalArgumentException("unresolved world params class " + worldParams.getClass().getName());
    }

    /**
     * Загрузить мир
     *
     * @param path            путь к миру
     * @param clientWidth     ширина окна
     * @param clientHeight    высота окна
     * @param backgroundColor цвет фона мира
     * @return созданный мир
     */
    public static World loadWorld(@NotNull String path, int clientWidth, int clientHeight, Vector3d backgroundColor) {
        return loadWorld(path, clientWidth, clientHeight, backgroundColor, new Vector2d(0.5, 0.5));
    }


    /**
     * Загрузить мир
     *
     * @param path            путь к миру
     * @param clientWidth     ширина окна
     * @param clientHeight    высота окна
     * @param backgroundColor цвет фона мира
     * @param windowDivide    соотношение блоков рисования
     * @return созданный мир
     */
    public static World loadWorld(
            @NotNull String path, int clientWidth, int clientHeight, Vector3d backgroundColor, Vector2d windowDivide
    ) {
        WorldParams worldParams = fromFile(Objects.requireNonNull(path));
        switch (worldParams.getType()) {
            case FOOD_3D_WORLD:
                return new Food3DWorld((RealTime3DWorldParams) worldParams, path, clientWidth, clientHeight, backgroundColor, windowDivide);
            default:
                throw new IllegalArgumentException("Unresolved type: " + worldParams.getType());
        }
    }

    /**
     * Сохранить мир
     *
     * @param world мир
     * @param path  путь сохранения
     */
    public static void saveWorld(@NotNull World world, @NotNull String path) {
        try {
            world.setPath(getWorldLocalPath(Objects.requireNonNull(path)));
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.writeValue(new File(path), world.getWorldParams());
        } catch (IOException e) {
            logger.error("can not write json params to file");
        }

    }

    /**
     * получить локальный путь мира, начиная с папки миров в ресурсах
     *
     * @param absolutePath абсолютный путь
     * @return локальный путь
     */
    private static String getWorldLocalPath(@NotNull String absolutePath) {
        absolutePath = absolutePath.replace("\\", "/");
        return absolutePath.substring(absolutePath.indexOf(RESOURCE_PATH) + RESOURCE_PATH.length());
    }

    /**
     * Конструктор для запрета наследования
     */
    private WorldFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }

}
