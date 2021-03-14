package graphics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector3d;

import java.util.Objects;

/**
 * Класс параметров 3D модели
 */
public class ObjModel3DParams {
    /**
     * путь к модели
     */
    @NotNull
    private final String path;
    /**
     * цвет модели
     */
    @NotNull
    private final Vector3d color;
    /**
     * масштаб
     */
    @NotNull
    private final Vector3d scale;

    /**
     * Конструктор класса параметров 3D модели
     *
     * @param path  путь к модели
     * @param color цвет модели
     * @param scale масштаб модели
     */
    @JsonCreator
    public ObjModel3DParams(
            @NotNull @JsonProperty("path") String path, @NotNull @JsonProperty("color") Vector3d color,
            @NotNull @JsonProperty("scale") Vector3d scale
    ) {
        this.path = Objects.requireNonNull(path);
        this.color = new Vector3d(color);
        this.scale = new Vector3d(scale);
    }

    /**
     * Конструктор класса параметров 3D модели
     *
     * @param objModel3DParams параметры 3D модели
     */
    public ObjModel3DParams(@NotNull ObjModel3DParams objModel3DParams) {
        this(objModel3DParams.path, objModel3DParams.color, objModel3DParams.scale);
    }

    /**
     * Получить путь к модели
     *
     * @return путь к модели
     */
    @NotNull
    public String getPath() {
        return path;
    }

    /**
     * Получить цвет модели
     *
     * @return цвет модели
     */
    @NotNull
    public Vector3d getColor() {
        return color;
    }

    /**
     * Получить  масштаб
     *
     * @return масштаб
     */
    @NotNull
    public Vector3d getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjModel3DParams that = (ObjModel3DParams) o;

        if (!Objects.equals(path, that.path)) return false;
        if (!Objects.equals(color, that.color)) return false;
        return Objects.equals(scale, that.scale);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (scale != null ? scale.hashCode() : 0);
        return result;
    }
}
