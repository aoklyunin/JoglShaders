package field;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import field.base.InfluenceField;
import field.base.InfluenceFieldParams;
import field.field3D.Food3DField;
import field.field3D.params.Food3DFieldParams;
import field.field3D.params.Objects3DFieldParams;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static constants.Constants.RESOURCE_PATH;

/**
 * Класс фабрики полей воздействия
 */
public class InfluenceFieldFactory {
    /**
     * словарь загруженных параметров мира
     */
    private static final Map<String, InfluenceFieldParams> pathDict = new HashMap<>();

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
     * Получить параметры поля воздействия из json
     *
     * @param path путь к параметрам поля воздействия
     * @return параметры поля воздействия
     */
    public static InfluenceFieldParams fromFile(@NotNull String path) {
        InfluenceFieldParams influenceFieldParams = pathDict.get(Objects.requireNonNull(path));
        if (influenceFieldParams != null)
            return clone(influenceFieldParams);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InfluenceFieldParams loadedInfluenceFieldParams =
                    objectMapper.readValue(new File(path), InfluenceFieldParams.class);
            pathDict.put(path, loadedInfluenceFieldParams);
            return loadedInfluenceFieldParams;
        } catch (IOException e) {
            throw new IllegalArgumentException("can not load influence field params form " + path + "\n" + e);
        }
    }


    /**
     * Получить поле воздействия
     *
     * @param influenceFieldParams параметры ресурсного поля
     * @param path                 путь к ресурсному полю
     * @param backGroundColor      цвет фона
     * @param clientWidth          ширина окна
     * @param clientHeight         высота окна
     * @return поле воздействия
     */
    public static InfluenceField getInfluenceField(
            @NotNull InfluenceFieldParams influenceFieldParams, @NotNull String path, @NotNull Vector3d backGroundColor,
            int clientWidth, int clientHeight
    ) {
        if (influenceFieldParams.getClass().equals(Food3DFieldParams.class))
            return new Food3DField(
                    (Food3DFieldParams) influenceFieldParams,
                    getFieldLocalPath(path), backGroundColor, clientWidth, clientHeight
            );

        throw new IllegalArgumentException("Unresolved type: " + influenceFieldParams.getClass());
    }


    /**
     * Загрузить поле воздействия
     *
     * @param path            путь к полю
     * @param backGroundColor цвет фона
     * @param clientWidth     ширина окна
     * @param clientHeight    высота окна
     * @return новое поле воздействия
     */
    public static InfluenceField load(
            @NotNull String path, @NotNull Vector3d backGroundColor, int clientWidth, int clientHeight
    ) {
        return getInfluenceField(
                fromFile(Objects.requireNonNull(path)), getFieldLocalPath(Objects.requireNonNull(path)),
                Objects.requireNonNull(backGroundColor), clientWidth, clientHeight
        );
    }

    /**
     * Копирование поля воздействия
     *
     * @param influenceField поле воздействия
     * @return копия  поля воздействия
     */
    public static InfluenceField clone(@NotNull InfluenceField influenceField) {
        if (influenceField.getClass().equals(Food3DField.class)) {
            return new Food3DField((Food3DField) influenceField);
        }
        throw new IllegalArgumentException("unresolved influence field class " + influenceField.getClass().getName());
    }

    /**
     * Копирование параметров поля воздействия
     *
     * @param influenceFieldParams параметры поля воздействия
     * @return копия параметров поля воздействия
     */
    public static InfluenceFieldParams clone(@NotNull InfluenceFieldParams influenceFieldParams) {
        if (influenceFieldParams.getClass().equals(InfluenceFieldParams.class)) {
            return new InfluenceFieldParams(influenceFieldParams);
        } else if (influenceFieldParams.getClass().equals(Food3DFieldParams.class)) {
            return new Food3DFieldParams((Food3DFieldParams) influenceFieldParams);
        } else if (influenceFieldParams.getClass().equals(Objects3DFieldParams.class)) {
            return new Objects3DFieldParams((Objects3DFieldParams) influenceFieldParams);
        }

        throw new IllegalArgumentException(
                "unresolved influence field params class " + influenceFieldParams.getClass().getName()
        );
    }

    /**
     * Сохранить поле воздействия
     *
     * @param path           путь сохранения
     * @param influenceField поле воздействия
     */
    public static void save(@NotNull String path, @NotNull InfluenceField influenceField) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(Objects.requireNonNull(path)), Objects.requireNonNull(influenceField));

        } catch (IOException e) {
            throw new IllegalArgumentException("can not save field " + influenceField + " to path " + path);
        }

    }

    /**
     * получить локальный путь поля воздействия, начиная с папки миров в ресурсах
     *
     * @param absolutePath абсолютный путь
     * @return локальный путь
     */
    private static String getFieldLocalPath(@NotNull String absolutePath) {
        absolutePath = absolutePath.replace("\\", "/");
        return absolutePath.substring(absolutePath.indexOf(RESOURCE_PATH) + RESOURCE_PATH.length());
    }

    /**
     * Конструктор для запрета наследования
     */
    private InfluenceFieldFactory() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
