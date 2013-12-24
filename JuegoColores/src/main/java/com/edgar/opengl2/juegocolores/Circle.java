package com.edgar.opengl2.juegocolores;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by edgar on 12/17/13.
 */
public class Circle {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 a_Position;" +
                    //"attribute vec4 a_Color;" +
                    //"varying vec4 v_Color;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * a_Position;" +
                    //"  v_Color = a_Color;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    //"varying vec4 v_Color;" +
                    "uniform vec4 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    //private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    int vertexCount = 30;
    float radius = 1.0f;
    float center_x = 0.0f;
    float center_y = 0.0f;

    /*static float circleCoords[] = {
            0.0f,  0.0f, 0.0f,   // top left front
            -0.5f, -0.5f, 0.5f,   // bottom left front
            0.5f, -0.5f, 0.5f,   // bottom right front
            0.5f,  0.5f, 0.5f,  // top right front
    };*/

    //private short drawOrder[] = {0, 1, 2, 3};

    private float vertices[] = new float[364 * 3];

    float color[] = {1.0f, 0.0f, 0.0f, 1.0f};


    public Circle(){

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;

        for(int i =1; i <364; i++){
            vertices[(i * 3)+ 0] = (float) (0.5 * Math.cos((3.14/180) * (float)i ));
            vertices[(i * 3)+ 1] = (float) (0.5 * Math.sin((3.14/180) * (float)i ));
            vertices[(i * 3)+ 2] = 0;
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        /*ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);*/

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

    }


    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "v_Color");

        /*mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, COLORCOORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, colorStride, colorBuffer);*/

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        /*GLES20.glDrawElements(
                GLES20.GL_TRIANGLE_FAN, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);*/

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 364);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

}

