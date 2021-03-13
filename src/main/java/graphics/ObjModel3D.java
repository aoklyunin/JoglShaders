package graphics;

import com.jogamp.opengl.GL2;
import com.sun.istack.NotNull;
import coordinateSystem.CoordinateSystem3d;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import math.Vector3d;
import math.Vector3f;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static constants.Constants.RESOURCE_PATH;

/**
 * Класс 3D модели
 */
public class ObjModel3D {
    /**
     * массив индексов полигонов
     */
    @NotNull
    private int[] indices;
    /**
     * массив координат вершин(координаты идут подряд, имеет размерность в три раза бОльшую, чем кол-во вершина)
     */
    @NotNull
    private float[] vertices;
    /**
     * массив текстурных координат(координаты идут подряд, имеет размерность в два раза бОльшую, чем кол-во вершина)
     */
    @NotNull
    private float[] texCoords;
    /**
     * массив нормалей(координаты идут подряд, имеет размерность в три раза бОльшую, чем кол-во вершина)
     */
    @NotNull
    private float[] normals;
    /**
     * отступы размера существа(берутся минимальные и максимальные координаты точек модели)
     */
    @NotNull
    private CoordinateSystem3d borderOffsets;
    /**
     * массив координат вершин
     */
    @NotNull
    private final ObjModel3DParams objModel3DParams;
    /**
     * координаты центров полигонов
     */
    @NotNull
    private float[] triangleCenterCoords;

    /**
     * Конструктор класса 3D модели
     *
     * @param objModel3DParams параметры 3D модели
     */
    public ObjModel3D(@NotNull ObjModel3DParams objModel3DParams) {
        this.objModel3DParams = new ObjModel3DParams(objModel3DParams);
        load(RESOURCE_PATH + "models/" + objModel3DParams.getPath());
    }

    /**
     * Конструктор класса 3D модели
     *
     * @param model3D модель, на основе которой нужно построить новую
     */
    public ObjModel3D(@NotNull ObjModel3D model3D) {
        this.objModel3DParams = new ObjModel3DParams(model3D.objModel3DParams);
        this.indices = Arrays.copyOf(model3D.indices, model3D.indices.length);
        this.vertices = Arrays.copyOf(model3D.vertices, model3D.vertices.length);
        this.texCoords = Arrays.copyOf(model3D.texCoords, model3D.texCoords.length);
        this.normals = Arrays.copyOf(model3D.normals, model3D.normals.length);
        this.borderOffsets = new CoordinateSystem3d(model3D.borderOffsets);
        this.triangleCenterCoords = Arrays.copyOf(model3D.triangleCenterCoords, model3D.triangleCenterCoords.length);
    }

    /**
     * Загрузить модель
     *
     * @param path путь к модели
     */
    private void load(@NotNull String path) {
        try (InputStream inputStream = new FileInputStream(new File(path))) {
            Obj obj3D = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            indices = ObjData.getFaceVertexIndicesArray(obj3D);
            vertices = ObjData.getVerticesArray(obj3D);
            texCoords = ObjData.getTexCoordsArray(obj3D, 2);
            calculateNormals();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // находим минимальные и максимальные координаты
        Vector3d maxPos = new Vector3d(
                vertices[0] * objModel3DParams.getScale().x,
                vertices[1] * objModel3DParams.getScale().y,
                vertices[2] * objModel3DParams.getScale().z
        );
        Vector3d minPos = new Vector3d(maxPos);
        for (int i = 0; i < vertices.length / 3; i++) {
            vertices[i * 3] *= objModel3DParams.getScale().x;
            vertices[i * 3 + 1] *= objModel3DParams.getScale().y;
            vertices[i * 3 + 2] *= objModel3DParams.getScale().z;
            Vector3d vert = new Vector3d(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
            maxPos = Vector3d.max(maxPos, vert);
            minPos = Vector3d.min(minPos, vert);
        }
        borderOffsets = new CoordinateSystem3d(minPos, maxPos);
    }

    /**
     * Рассчитать нормали
     */
    private void calculateNormals() {
        // рассчитываем координаты центров треугольников модели
        triangleCenterCoords = new float[indices.length];
        for (int i = 0; i < indices.length / 3; i++) {
            triangleCenterCoords[i * 3] = (
                    vertices[indices[i * 3] * 3] + vertices[indices[i * 3 + 1] * 3] + vertices[indices[i * 3 + 2] * 3]
            ) / 3;
            triangleCenterCoords[i * 3 + 1] = (
                    vertices[indices[i * 3] * 3 + 1] + vertices[indices[i * 3 + 1] * 3 + 1] + vertices[indices[i * 3 + 2] * 3 + 1]
            ) / 3;
            triangleCenterCoords[i * 3 + 2] = (
                    vertices[indices[i * 3] * 3 + 2] + vertices[indices[i * 3 + 2] * 3 + 1] + vertices[indices[i * 3 + 2] * 3 + 2]
            ) / 3;
        }

        // рассчитываем нормали
        normals = new float[vertices.length];
        for (int i = 0; i < vertices.length; i++)
            normals[i] = 0;

        for (int i = 0; i < indices.length / 3; i++) {
            Vector3d A = new Vector3d(
                    vertices[indices[i * 3] * 3],
                    vertices[indices[i * 3] * 3 + 1],
                    vertices[indices[i * 3] * 3 + 2]);
            Vector3d B = new Vector3d(
                    vertices[indices[i * 3 + 1] * 3],
                    vertices[indices[i * 3 + 1] * 3 + 1],
                    vertices[indices[i * 3 + 1] * 3 + 2]);
            Vector3d C = new Vector3d(
                    vertices[indices[i * 3 + 2] * 3],
                    vertices[indices[i * 3 + 2] * 3 + 1],
                    vertices[indices[i * 3 + 2] * 3 + 2]);
            Vector3d localNormal =
                    Vector3d.cross(Vector3d.subtract(B, A), Vector3d.subtract(C, A));

            normals[indices[i * 3] * 3] += localNormal.x;
            normals[indices[i * 3] * 3 + 1] += localNormal.y;
            normals[indices[i * 3] * 3 + 2] += localNormal.z;

            normals[indices[i * 3 + 1] * 3] += localNormal.x;
            normals[indices[i * 3 + 1] * 3 + 1] += localNormal.y;
            normals[indices[i * 3 + 1] * 3 + 2] += localNormal.z;

            normals[indices[i * 3 + 2] * 3] += localNormal.x;
            normals[indices[i * 3 + 2] * 3 + 1] += localNormal.y;
            normals[indices[i * 3 + 2] * 3 + 2] += localNormal.z;
        }

        for (int i = 0; i < normals.length / 3; i++) {
            Vector3f tmpVec = new Vector3f(
                    normals[i * 3], normals[i * 3 + 1], normals[i * 3 + 2]
            );
            Vector3f normVec = tmpVec.norm();
            normals[i * 3] = normVec.x;
            normals[i * 3 + 1] = normVec.y;
            normals[i * 3 + 2] = normVec.z;
        }

    }

    /**
     * Нарисовать модель
     *
     * @param gl2             переменная OpenGL
     */
    public void render(GL2 gl2) {
        gl2.glPushMatrix();

        gl2.glBegin(GL_TRIANGLES);
        for (int index : indices) {
            gl2.glColor3d(
                    objModel3DParams.getColor().x ,
                    objModel3DParams.getColor().y,
                    objModel3DParams.getColor().z );

            gl2.glVertex3f(
                    vertices[index * 3],
                    vertices[index * 3 + 1],
                    vertices[index * 3 + 2]
            );
            gl2.glNormal3f(
                    normals[index * 3],
                    normals[index * 3 + 1],
                    normals[index * 3 + 2]
            );
        }
        gl2.glEnd();

        gl2.glPopMatrix();
    }

    /**
     * Получить массив индексов полигонов
     *
     * @return массив индексов полигонов
     */
    @NotNull
    public int[] getIndices() {
        return indices;
    }

    /**
     * Получить  массив координат вершин
     *
     * @return массив координат вершин
     */
    @NotNull
    public float[] getVertices() {
        return vertices;
    }

    /**
     * Получить  массив координат вершин
     *
     * @return массив координат вершин
     */
    @NotNull
    public ObjModel3DParams getObjModel3DParams() {
        return objModel3DParams;
    }

    /**
     * Получить отступы размера существа
     *
     * @return отступы размера существа
     */
    @NotNull
    public CoordinateSystem3d getBorderOffsets() {
        return borderOffsets;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "ObjModel3D{getString()}"
     */

    @Override
    public String toString() {
        return "ObjModel3D{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "textRenderer, clientWidth, clientHeight, glCS, clientCS"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return indices.length + ", " + vertices.length + ", " + objModel3DParams;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

       ObjModel3D that = (ObjModel3D) o;

        if (!Arrays.equals(indices, that.indices)) return false;
        if (!Arrays.equals(vertices, that.vertices)) return false;
        if (!Arrays.equals(texCoords, that.texCoords)) return false;
        if (!Arrays.equals(normals, that.normals)) return false;
        if (!Objects.equals(borderOffsets, that.borderOffsets))
            return false;
        if (!Objects.equals(objModel3DParams, that.objModel3DParams))
            return false;
        return Arrays.equals(triangleCenterCoords, that.triangleCenterCoords);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(indices);
        result = 31 * result + Arrays.hashCode(vertices);
        result = 31 * result + Arrays.hashCode(texCoords);
        result = 31 * result + Arrays.hashCode(normals);
        result = 31 * result + (borderOffsets != null ? borderOffsets.hashCode() : 0);
        result = 31 * result + (objModel3DParams != null ? objModel3DParams.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(triangleCenterCoords);
        return result;
    }
}
