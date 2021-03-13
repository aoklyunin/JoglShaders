package offscreen.renderer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLBuffers;
import com.sun.istack.NotNull;
import graphics.Camera;
import math.Transform3d;
import math.Vector2i;
import offscreen.params.OffscreenRendererParams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_CLAMP_TO_BORDER;
import static com.jogamp.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import static com.jogamp.opengl.GL2ES3.GL_DEPTH_STENCIL_ATTACHMENT;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Класс фонового рисовальщика GL
 */
public class GLOffscreenRenderer extends OffscreenRenderer {
    /**
     * Переменная окна OpenGL
     */
    @NotNull
    private GLAutoDrawable drawable;
    /**
     * буфер для чтения изображения с камеры существа
     */
    @NotNull
    private ByteBuffer glbuffer;
    /**
     * буфер текстур
     */
    @NotNull
    private int[] texture_map = new int[1];
    /**
     * Буфер фреймов
     */
    @NotNull
    private int[] framebuffer = new int[1];
    /**
     * буфер рисования
     */
    @NotNull
    private int[] renderBuffer = new int[1];
    /**
     * GLU
     */
    @NotNull
    private GLU glu;
    /**
     * Флаг, что поле уже инициализировано
     */
    private boolean inited;

    /**
     * Конструктор фонового рисовальзика
     *
     * @param offscreenRendererParams параметры фонового рисовальщика
     */
    public GLOffscreenRenderer(OffscreenRendererParams offscreenRendererParams) {
        super(offscreenRendererParams);
        inited = false;
    }

    /**
     * Конструктор фонового рисовальзика
     *
     * @param glOffscreenRenderer фоновый рисовальщик
     */
    public GLOffscreenRenderer(GLOffscreenRenderer glOffscreenRenderer) {
        super(glOffscreenRenderer);
        this.glbuffer = glOffscreenRenderer.glbuffer;
        this.texture_map = glOffscreenRenderer.texture_map.clone();
        this.framebuffer = glOffscreenRenderer.framebuffer.clone();
        this.renderBuffer = glOffscreenRenderer.renderBuffer.clone();
        this.glu = glOffscreenRenderer.glu;
        this.drawable = glOffscreenRenderer.drawable;
        this.inited = glOffscreenRenderer.inited;
        this.renderSize = glOffscreenRenderer.renderSize;
    }

    /**
     * Инициализация рисовальщика
     *
     * @param renderSize размер окна
     */
    public void init(Vector2i renderSize) {
        super.init(renderSize);
        if (inited)
            dispose();

        // создаём виртуальное окно OpenGL
        glu = new GLU();
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLDrawableFactory factory = GLDrawableFactory.getFactory(capabilities.getGLProfile());
        drawable = factory.createOffscreenAutoDrawable(null, capabilities, null,
                renderSize.x, renderSize.y);
        drawable.display();

        // инициализируем VBO
        byte[] bbuffer = new byte[renderSize.x * renderSize.y * 3];
        glbuffer = GLBuffers.newDirectByteBuffer(bbuffer);

        GL2 gl2 = drawable.getGL().getGL2();

        gl2.glGenTextures(1, texture_map, 0);
        gl2.glBindTexture(GL_TEXTURE_2D, texture_map[0]);

        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        gl2.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, renderSize.x, renderSize.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);

        gl2.glBindTexture(GL_TEXTURE_2D, 0);

        // Build the texture that will serve as the depth attachment for the framebuffer.
        int[] depth_texture = new int[1];
        gl2.glGenTextures(1, depth_texture, 0);
        gl2.glBindTexture(GL_TEXTURE_2D, depth_texture[0]);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl2.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        gl2.glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, renderSize.x, renderSize.y, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, null);
        gl2.glBindTexture(GL_TEXTURE_2D, 0);

        gl2.glGenRenderbuffers(1, renderBuffer, 0);
        gl2.glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer[0]);
        gl2.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, renderSize.x, renderSize.y);
        gl2.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBuffer[0]);

        gl2.glGenFramebuffers(1, framebuffer, 0);
        gl2.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[0]);
        gl2.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture_map[0], 0);
        gl2.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depth_texture[0], 0);

        int[] buffers = {GL_COLOR_ATTACHMENT0};
        gl2.glDrawBuffers(1, buffers, 0);
        inited = true;
    }

    /**
     * Получить буфер цвета пикселей
     *
     * @param width  ширина
     * @param height высота
     * @return буфер цвета пикселей
     */
    @NotNull
    private short[][][] getColorBytesFromCurrentBuffer(int width, int height) {
        short[][][] result = new short[width][height][3];
        glbuffer.rewind();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                // The color are the three consecutive bytes, it's like referencing
                // to the next consecutive array elements, so we got red, green, blue..
                // red, green, blue, and so on...
                result[w][h][0] = (short) (glbuffer.get() & 0xFF);
                result[w][h][1] = (short) (glbuffer.get() & 0xFF);
                result[w][h][2] = (short) (glbuffer.get() & 0xFF);
            }
        }

        return result;
    }

    /**
     * Сохранить буфер зрения существа в картинку
     *
     * @param path   путь к картинке
     * @param width  ширина
     * @param height высота
     */
    private void saveCurrentBufferToJpg(@NotNull String path, int width, int height) {
        BufferedImage screenshot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = screenshot.getGraphics();

        glbuffer.rewind();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                // The color are the three consecutive bytes, it's like referencing
                // to the next consecutive array elements, so we got red, green, blue..
                // red, green, blue, and so on...
                // the boolean bitise and converts byte to unsigned byte, that actually not exists in java language
                //graphics.setColor(new Color(glbuffer.get()& 0xFF, glbuffer.get()& 0xFF, glbuffer.get()& 0xFF));
                int r = glbuffer.get() & 0xFF;
                int g = glbuffer.get() & 0xFF;
                int b = glbuffer.get() & 0xFF;
                graphics.setColor(new Color(r, g, b));
                graphics.drawRect(w, height - h - 1, 1, 1); // height - h is for flipping the image
            }
        }

        try {
            ImageIO.write(screenshot, "jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Рассчёт буфера зрения
     *
     * @param viewPort     Размер окна
     * @param camera       камера
     * @param transform3ds список трансформаций объектов
     * @return буфер зрения существа
     */
    @NotNull
    public short[][][] calculateVisionBuffer(
            @NotNull Vector2i viewPort, @NotNull Camera camera, @NotNull List<Transform3d> transform3ds
    ) {
        drawable.display();
        GL2 gl2 = drawable.getGL().getGL2();
        drawable.getContext().makeCurrent();
        gl2.glBindFramebuffer(GL_FRAMEBUFFER, framebuffer[0]);
        gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl2.glViewport(0, 0, viewPort.x, viewPort.y);

        // выбираем матрицу проекций
        gl2.glMatrixMode(GL_PROJECTION);
        // делаем её единичной
        gl2.glLoadIdentity();

        // задаём перспективу: угол обзора, сооношение сторон экрана, ближняя точка отсечения, дальняя точка отсечения
        glu.gluPerspective(45.0, 1.0, 0.1, 500.0);

        camera.gluLookAt(glu);

        // разрешаем проверку глубины
        gl2.glEnable(GL_DEPTH_TEST);

        render(gl2, transform3ds);
//        gl2.glEnable(GL_LIGHTING);
//        gl2.glEnable(GL_LIGHT0);
//        gl2.glDisable(GL_LIGHTING);

        // запрещаем проверку глубины
        gl2.glDisable(GL_DEPTH_TEST);

        gl2.glFlush();
        gl2.glFinish();

        glbuffer.rewind();

        gl2.glReadPixels(0, 0,
                viewPort.x,
                viewPort.y,
                GL_RGB, GL_UNSIGNED_BYTE, glbuffer
        );
        short[][][] res = getColorBytesFromCurrentBuffer(viewPort.x, viewPort.y);
//        saveCurrentBufferToJpg(RESOURCE_PATH+"imgs/testCreatureVision.jpg",
//                ((Creature3DParams) creature.creatureParams).visionGridSize.x,
//                ((Creature3DParams) creature.creatureParams).visionGridSize.y
//        );

        gl2.glFlush();
        gl2.glFinish();

        gl2.glBindFramebuffer(GL.GL_DRAW_FRAMEBUFFER, 0);
        drawable.getContext().release();
        return res;
    }

    /**
     * Разрушение объекта поля
     */
    public void dispose() {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glDeleteFramebuffers(1, framebuffer, 0);
        gl2.glDeleteFramebuffers(1, texture_map, 0);
        gl2.glDeleteFramebuffers(1, renderBuffer, 0);
    }

    /**
     * Рисование поля
     *
     * @param gl2              переменная OpenGl  для рисования
     * @param objectTransforms список матриц трансформаций объектов
     */
    public void render(GL2 gl2, @NotNull List<Transform3d> objectTransforms) {
        for (int i = 0; i < getObjectCnt(); i++) {
            gl2.glPushMatrix();
            objectTransforms.get(i).apply(gl2);
            objects.get(i).render(gl2);
            gl2.glPopMatrix();
        }
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GLOffscreenRenderer{getString()}"
     */
    @Override
    public String toString() {
        return "GLOffscreenRenderer{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "inited, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return inited + ", " + super.getString();
    }

}
