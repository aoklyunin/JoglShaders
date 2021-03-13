package creature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import creature.base.Creature;
import creature.base.CreatureBuilder;
import creature.base.CreatureParams;
import creature.base.CreatureState;
import creature.creature3D.Creature3D;
import creature.creature3D.Creature3DParams;
import creature.creature3D.Creature3DState;
import graphics.Camera;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.params.CreatureInWorldParams;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static constants.Constants.RESOURCE_PATH;
import static java.util.stream.Collectors.toList;

/**
 * Фабрика существ
 */
public class CreatureFactory {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(CreatureFactory.class);

    /**
     * словарь загруженных параметров мира
     */
    private static final Map<String, CreatureParams> pathDict = new HashMap<>();

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
     * Загрузить параметры существа из файла
     *
     * @param path путь к параметрам существа
     * @return параметры существа
     */
    public static CreatureParams fromFile(@NotNull String path) {
        CreatureParams creatureParams = pathDict.get(Objects.requireNonNull(path));
        if (creatureParams != null)
            return clone(creatureParams);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CreatureParams loadedCreatureParams = objectMapper.readValue(new File(path), CreatureParams.class);
            pathDict.put(path, loadedCreatureParams);
            return loadedCreatureParams;
        } catch (IOException e) {
            throw new IllegalArgumentException("can not load creature params from " + path + ": " + e);
        }
    }

    /**
     * Сохранить существо
     *
     * @param creature существо
     * @param path     путь к сохранинию
     */
    public static void saveCreature(@NotNull Creature creature, @NotNull String path) {
        // с тилдьдой можно хранить только внутренние описания внутри истории
        if (path.contains("~"))
            logger.warn("saveCreature warning: path " + path + "contains tilda");
        path = path.replace("~", "");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File(path), creature.getPath());
            creature.setPath(getCreatureLocalPath(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Получить локальный путь существа, начиная с папки существ
     *
     * @param absolutePath абсолютный путь к описанию существа
     * @return локальный путь описания существа
     */
    private static String getCreatureLocalPath(@NotNull String absolutePath) {
        absolutePath = absolutePath.replace("\\", "/");
        return absolutePath.substring(absolutePath.indexOf(RESOURCE_PATH) + RESOURCE_PATH.length());
    }

    /**
     * Загрузить существо
     *
     * @param creatureInWorldParams параметры существа
     * @return загруженное существо
     */
    public static Creature getCreature(@NotNull CreatureInWorldParams creatureInWorldParams) {
        return new CreatureBuilder(RESOURCE_PATH + creatureInWorldParams.getPath(), creatureInWorldParams.getId())
                .path(creatureInWorldParams.getPath())
                .camera(Camera.getDefaultCamera(creatureInWorldParams.getPosition())).build();
    }


    /**
     * Загрузить существо
     *
     * @param path       путь к существу
     * @param creatureId creatureId сущесвта
     * @return загружденное существо
     */
    public static Creature getCreature(@NotNull String path, int creatureId) {
        return new CreatureBuilder(Objects.requireNonNull(path), creatureId).build();
    }


    /**
     * Копия состояния существа
     *
     * @param creatureState состояние, которое нужно скопировать
     * @return копия
     */
    public static CreatureState clone(@NotNull CreatureState creatureState) {
        if (creatureState.getClass().equals(Creature3DState.class))
            return new Creature3DState((Creature3DState) creatureState);
        throw new IllegalArgumentException("unexpected creatureState class " + creatureState.getClass());
    }

    /**
     * Копия существа
     *
     * @param creature существо, которое нужно скопировать
     * @return копия
     */
    public static Creature clone(@NotNull Creature creature) {
        if (creature.getClass().equals(Creature3D.class))
            return new Creature3D((Creature3D) creature);
        throw new IllegalArgumentException("unexpected creature class " + creature.getClass().getName());
    }

    /**
     * Получить копию парамтеров существа
     *
     * @param creatureParams парамтеры существа
     * @return копия парамтеров существа
     */
    public static CreatureParams clone(@NotNull CreatureParams creatureParams) {
        if (creatureParams.getClass().equals(CreatureParams.class))
            return new CreatureParams(creatureParams);
        if (creatureParams.getClass().equals(Creature3DParams.class))
            return new Creature3DParams((Creature3DParams) creatureParams);
        throw new IllegalArgumentException("clone(): unexpected creature params type: " + creatureParams.getClass());
    }


    /**
     * Клонировать существо
     *
     * @param creature   существо, которое надо клонировать
     * @param creatureId id нового сущетсва
     * @return новое существо
     */
    @NotNull
    public static Creature makeClone(@NotNull Creature creature, int creatureId) {
        return makeClone(
                Objects.requireNonNull(creature), creature.getCreatureParams(), creatureId,
                creature.getPath()
        );
    }

    /**
     * Клонировать существо
     *
     * @param creature существо, которое надо клонировать
     * @return новое существо
     */
    @NotNull
    public static Creature makeClone(@NotNull Creature creature) {
        return makeClone(
                Objects.requireNonNull(creature), creature.getCreatureParams(), creature.getId(),
                creature.getPath()
        );
    }

    /**
     * Клонировать существо
     *
     * @param creature       существо, которое надо клонировать
     * @param creatureParams параметры существа
     * @param creatureId     id нового сущетсва
     * @param path           путь к существу
     * @return новое существо
     */
    @NotNull
    public static Creature makeClone(
            @NotNull Creature creature, @NotNull CreatureParams creatureParams, int creatureId,
            @NotNull String path
    ) {
        return new CreatureBuilder(Objects.requireNonNull(creatureParams), creatureId)
                .path(Objects.requireNonNull(path))
                .camera(creature.getCamera()).build();
    }

    /**
     * Получить состояния существ по существам
     *
     * @param creatures список существ
     * @return список состояний существ
     */
    public static List<CreatureState> getCreatureStates(@NotNull List<Creature> creatures) {
        return creatures.stream()
                .map(Creature::getState)
                .collect(toList());
    }

    /**
     * Конструктор для запрета наследования
     */
    private CreatureFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
