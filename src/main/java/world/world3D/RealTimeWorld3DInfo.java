package world.world3D;

import com.sun.istack.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import world.base.WorldInfo;
import world.world3D.params.RealTime3DWorldParams;

import java.util.Objects;


/**
 * Класс информации о 3D мире реального времени
 */
public class RealTimeWorld3DInfo {
    /**
     * логгер
     */
    private static final Logger logger = LogManager
            .getLogger(RealTimeWorld3DInfo.class);


    /**
     * Информация о мире
     */
    @NotNull
    private final WorldInfo worldInfo;
    /**
     * Параметры 3D мира реального времени
     */
    @NotNull
    private final RealTime3DWorldParams worldParams;

    /**
     * Конструктор класса информации о 3D мире реального времени
     *
     * @param worldParams              Параметры 3D мира реального времени
     * @param worldInfo                Информация о мире
     */
    RealTimeWorld3DInfo(
            @NotNull RealTime3DWorldParams worldParams, @NotNull WorldInfo worldInfo
    ) {
        this.worldInfo = Objects.requireNonNull(worldInfo);
        this.worldParams = worldParams;

    }


    /**
     * Строковое представление объекта вида:
     *
     * @return "RealTime3DWorldInfo{getString()}"
     */
    @Override
    public String toString() {
        return "RealTime3DWorldInfo{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * ""
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealTimeWorld3DInfo that = (RealTimeWorld3DInfo) o;


        if (!Objects.equals(worldInfo, that.worldInfo)) return false;
        return Objects.equals(worldParams, that.worldParams);
    }

    @Override
    public int hashCode() {
        int result = worldInfo.hashCode();
        result = 31 * result + (worldParams != null ? worldParams.hashCode() : 0);
        return result;
    }
}
