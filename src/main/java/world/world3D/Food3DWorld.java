package world.world3D;

import com.github.aoklyunin.javaGLHelper.GLAlgorithms;
import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import creature.base.Creature;
import field.field3D.state.CoordinateSystem3DState;
import jMath.aoklyunin.github.com.coordinateSystem.CoordinateSystem2d;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector3d;
import world.states.WorldState;
import world.world3D.params.RealTime3DWorldParams;

import java.util.Objects;


/**
 * 3D мир с едой
 */
public class Food3DWorld extends Life3DWorld {
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
    public Food3DWorld(
            @NotNull RealTime3DWorldParams realTimeWorldParams, @NotNull String path, int clientWidth, int clientHeight,
            @NotNull Vector3d backgroundColor, @NotNull Vector2d windowDivide
    ) {
        super(realTimeWorldParams, path, clientWidth, clientHeight, backgroundColor, windowDivide);

    }

    @Override
    public void cloneSelectedCreature() {

    }



    /**
     * Обработка надатия мыши по миру
     *
     * @param mouseGLPos  положение мыши
     * @param renderCS    СК рисования
     * @param mouseButton кнопка мыши
     * @return обработано ли нажатие на данном уровне абстракции
     */
    @Override
    public boolean clickWorld(@NotNull Vector2d mouseGLPos, @NotNull CoordinateSystem2d renderCS, int mouseButton) {
        if (super.clickWorld(mouseGLPos, renderCS, mouseButton))
            return true;
        if (getResourceField().getFood3DField().click(mouseGLPos, renderCS, mouseButton)) {
            getWorldStory().getStatesList().getActual().getRealTimeWorldState().getResourceFieldState()
                    .getFood3DFieldState().setFoodMovingSpeed(getResourceField().getFood3DField().getFoodMovingSpeed());
            getWorldStory().getStatesList().getActual().getRealTimeWorldState().getResourceFieldState()
                    .getFood3DFieldState().setFoodMovingCS(new CoordinateSystem3DState(
                    getResourceField().getFood3DField().getFoodMovingCS()
            ));
            return true;
        }
        return false;
    }

    /**
     * рисование коннекторов существа в режиме standAlone
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    protected void renderCreatureModelConnectors(GL2 gl2, @NotNull WorldState worldState) {

    }


    /**
     * Обработка кормления существа
     *
     * @param creature существо, которое надо накормить
     */
    @Override
    public void processCreatureFeeding(@NotNull Creature creature) {

    }

    @Override
    public void breedCreatures() {

    }


    /**
     * Рисовать мир
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    public void render(GL2 gl2, @NotNull CoordinateSystem2d renderCS, @NotNull WorldState worldState) {
        if (getWorldParams().getStoryWorldParams().isRecordStory()) {
            // рисуем ресурсное поле
            getResourceField().getObjects3DField().render(
                    gl2,
                    getWorldStory().getStatesList().getActual().getRealTimeWorldState().getResourceFieldState()
            );
            renderCreature3D(gl2, getWorldStory().getActualCreatureStates().get(0));
        } else {
            // рисуем ресурсное поле
            getResourceField().getObjects3DField().render(
                    gl2, getResourceField().getState()
            );
            renderCreature3D(gl2, getWorldStory().getCreatures().get(0).getState());
        }

        gl2.glColor3d(1.0, 1.0, 1.0);
        for (int i = (int) getWorldParams().getRealTime3DWorldParams().getWorldCS().getMin().x;
             i < (int) getWorldParams().getRealTime3DWorldParams().getWorldCS().getMax().x - 1;
             i++)
            for (int j = (int) getWorldParams().getRealTime3DWorldParams().getWorldCS().getMin().y;
                 j < (int) getWorldParams().getRealTime3DWorldParams().getWorldCS().getMax().y - 1;
                 j++) {
                GLAlgorithms.renderLineQuad(gl2, i, j, 1, 1);
            }

    }


    /**
     * Отображение лога
     *
     * @param gl2 переменная OpenGL
     */
    @Override
    public void renderLog(GL2 gl2, @NotNull WorldState worldState) {
        super.renderLog(gl2, Objects.requireNonNull(worldState));
        if (getWorldParams().getRealTimeWorldParams().isRecordStory()) {
            getWorldInfo().getTextControllers().get("foodInfo").drawText(
                    getWorldStory().getStatesList().getActual().getRealTimeWorldState().getResourceFieldState()
                            .getFood3DFieldState().getObjectTransforms().get(0).getPosition() + ""

            );
        } else {
            getWorldInfo().getTextControllers().get("foodInfo").drawText(
                    getResourceField().getState().getFood3DFieldState().getObjectTransforms().get(0).getPosition() + ""
            );
        }
    }

    @Override
    public void truncateCreaturePos(@NotNull Creature creature) {

    }

    /**
     * Обработка движения существа
     *
     * @param creature рассматриваемое существо
     */
    @Override
    public void processCreatureMoving(@NotNull Creature creature) {

    }

     /**
     * Строковое представление объекта вида:
     *
     * @return "Food3DWorld{getString()}"
     */
    @Override
    public String toString() {
        return "Food3DWorld{" + getString() + '}';
    }

}
