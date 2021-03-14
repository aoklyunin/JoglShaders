package world.world3D;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import world.base.interfaces.LiveCreatureAcceptable;
import world.states.WorldState;
import world.world3D.params.RealTime3DWorldParams;

import java.util.Objects;


/**
 * Базовый класс для всех 3D миров с жизнью
 */
public abstract class Life3DWorld extends RealTime3DWorld implements LiveCreatureAcceptable {
    /**
     * Конструктор класса мира с затухающим полем еды
     *
     * @param realTimeWorldParams параметры мира реального времени
     * @param path                путь к описанию мира
     * @param clientWidth         ширина окна в пикселях
     * @param clientHeight        высота окна в пикселях
     * @param backgroundColor     цвет фона в мире
     * @param windowDivide        соотношение блоков рисования
     */
    public Life3DWorld(
            @NotNull RealTime3DWorldParams realTimeWorldParams, @NotNull String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor, @NotNull Vector2d windowDivide
    ) {
        super(realTimeWorldParams, path, clientWidth, clientHeight, backgroundColor, windowDivide);

    }

    /**
     * Такт мира
     */
    @Override
    public void tick() {
        // размножаем существ
        breedCreatures();
        //System.out.println("tick");
        super.tick();
    }

    /**
     * Отображение лога
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    public void renderLog(GL2 gl2, @NotNull WorldState worldState) {
        super.renderLog(gl2, Objects.requireNonNull(worldState));
        renderAdditionalLog(gl2, worldState);
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "Life3DWorld{getString()}"
     */
    @Override
    public String toString() {
        return "Life3DWorld{" + getString() + '}';
    }

}
