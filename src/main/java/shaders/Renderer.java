package shaders;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import graphics.Camera;
import graphics.Cube;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static com.jogamp.opengl.GL.GL_FALSE;
import static com.jogamp.opengl.GL2ES2.GL_MAX_VERTEX_ATTRIBS;

/**
 * Performs the rendering.
 *
 * @author serhiy
 */
public class Renderer implements GLEventListener {

    private Cube cube = new Cube();
    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private FloatBuffer colorBuffer;
    private ShaderProgram shaderProgram;
    private Camera camera = new Camera(-1, 0, 0, 1, 0, 0, 0, 0, 1);

    private GLJPanel gljPanel;

    public Renderer(GLJPanel gljPanel) {
        this.gljPanel = gljPanel;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        File vertexShader = new File("src/main/resources/shaders/default.vs");
        File fragmentShader = new File("src/main/resources/shaders/default.fs");

        shaderProgram = new ShaderProgram();
        if (!shaderProgram.init(gl2, vertexShader, fragmentShader)) {
            throw new IllegalStateException("Unable to initiate the shaders!");
        }
        vertexBuffer = Buffers.newDirectFloatBuffer(cube.getVertices().length);
        indexBuffer = Buffers.newDirectIntBuffer(cube.getIndices().length);
        colorBuffer = Buffers.newDirectFloatBuffer(cube.getColors().length);

        vertexBuffer.put(cube.getVertices());
        indexBuffer.put(cube.getIndices());
        colorBuffer.put(cube.getColors());

        int[] res = new int[1];
        gl2.glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, res, 0);
        System.out.println(Arrays.toString(res));

        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glDepthMask(true);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        shaderProgram.dispose(gl2);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();

        gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl2.glUseProgram(shaderProgram.getProgramId());


        gl2.glEnableVertexAttribArray(shaderProgram
                .getShaderAttributeLocation(EShaderAttribute.POSITION));
        gl2.glEnableVertexAttribArray(shaderProgram
                .getShaderAttributeLocation(EShaderAttribute.COLOR));

        gl2.glVertexAttribPointer(shaderProgram
                        .getShaderAttributeLocation(EShaderAttribute.POSITION), 3,
                GL2.GL_FLOAT, false, 0, vertexBuffer.rewind());

        gl2.glVertexAttribPointer(shaderProgram
                        .getShaderAttributeLocation(EShaderAttribute.COLOR), 3,
                GL2.GL_FLOAT, false, 0, colorBuffer.rewind());

        FloatBuffer objectTransform = BufferUtils.createFloatBuffer(16);
        objectTransform = new Matrix4d(
                1, 0, 0, 0.3f,
                0, 1, 0, 0.2f,
                0, 0, 1, 0,
                0, 0, 0, 1
        ).transpose().get(objectTransform);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);

        gl2.glUniformMatrix4fv(
                gl2.glGetUniformLocation(shaderProgram.getProgramId(), "u_modelMat44"),
                1, true, objectTransform
        );

        gl2.glUniformMatrix4fv(
                gl2.glGetUniformLocation(shaderProgram.getProgramId(), "u_projectionMat44"),
                1, true, camera.getPerspectiveBuffer()
        );

        gl2.glUniformMatrix4fv(
                gl2.glGetUniformLocation(shaderProgram.getProgramId(), "u_viewMat44"),
                1, true, camera.getLookAtBuffer()
        );

        gl2.glDrawElements(GL2.GL_TRIANGLES, cube.getIndices().length,
                GL2.GL_UNSIGNED_INT, indexBuffer.rewind());

        gl2.glDisableVertexAttribArray(shaderProgram
                .getShaderAttributeLocation(EShaderAttribute.POSITION));
        gl2.glDisableVertexAttribArray(shaderProgram
                .getShaderAttributeLocation(EShaderAttribute.COLOR));

        gl2.glUseProgram(0);
        //System.out.println("test");
        gljPanel.repaint();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width,
                        int height) {
        /* no action to be taken on reshape */
    }
}
