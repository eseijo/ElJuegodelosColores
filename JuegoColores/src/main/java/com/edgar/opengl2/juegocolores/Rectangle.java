package com.edgar.opengl2.juegocolores;

/**
 * Created by edgar on 12/20/13.
 */
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Rectangle implements Shape{

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            //"uniform mat4 uMVPMatrix;" +
                    "attribute vec4 a_Position;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = a_Position;" +
                    "}";

    private final String altVertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "attribute vec4 a_Position;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = a_Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    float l = (float)Math.random();
    private float topPoint;
    private float botPoint;
    private float leftPoint;
    private float rightPoint;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float rectCoords[] = new float[4 * 3];

    private final short drawOrder[] = {0, 1, 2, 3}; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1.0f, 1.0f, 0.0f, 0.7f};

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Rectangle(){

        rectCoords = genCoords();
        float rTCoords[] = Utils.randPos(rectCoords);

        topPoint = rTCoords[1];
        botPoint = rTCoords[4];
        leftPoint = rTCoords[0];
        rightPoint = rTCoords[6];

        if(topPoint<botPoint){
            float aux = topPoint;
            topPoint = botPoint;
            botPoint = aux;
        }
        if(rightPoint<leftPoint){
            float aux = rightPoint;
            rightPoint = leftPoint;
            leftPoint = aux;
        }

        while((topPoint>0.85 || botPoint>0.85)&&(leftPoint<-0.85 || rightPoint<-0.85)){
            rectCoords = genCoords();
            rTCoords = Utils.randPos(rectCoords);

            topPoint = rTCoords[1];
            botPoint = rTCoords[4];
            leftPoint = rTCoords[0];
            rightPoint = rTCoords[6];

            if(topPoint<botPoint){
                float aux = topPoint;
                topPoint = botPoint;
                botPoint = aux;
            }
            if(rightPoint<leftPoint){
                float aux = rightPoint;
                rightPoint = leftPoint;
                leftPoint = aux;
            }
        }

        //Log.v("awq2", "" + rTCoords[0] + " " + rTCoords[1] + " " + rTCoords[2]);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                rTCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(rTCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public Rectangle(int p){

        float rTCoords[] = rectCoords;

        for(int i =0; i <4; i++)
            rTCoords[(i * 3) + 2] = 0.5f;

        rTCoords[0] = -0.99f;
        rTCoords[1] = 0.99f;
        rTCoords[3] = -0.99f;
        rTCoords[4] = 0.85f;
        rTCoords[6] = -0.85f;
        rTCoords[7] = 0.85f;
        rTCoords[9] = -0.85f;
        rTCoords[10] = 0.99f;

        //Log.v("awq2", "" + rTCoords[0] + " " + rTCoords[1] + " " + rTCoords[2]);

        // initialize vertex byte buffer for shape coordinatesº
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                rTCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(rTCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                altVertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }


    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    @Override
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

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLE_FAN, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

    @Override
    public float getTopPoint() {
        return topPoint;
    }
    @Override
    public float getBotPoint() {
        return botPoint;
    }
    @Override
    public float getLeftPoint() {
        return leftPoint;
    }
    @Override
    public float getRightPoint() {
        return rightPoint;
    }

    private float[] genCoords(){
        float[] coords = new float[4 * 3];
        l = Utils.randSize(l);

        for(int i =0; i <4; i++)
            coords[(i * 3) + 2] = 0.5f;

        coords[0] = -l;
        coords[1] = l;
        coords[3] = -l;
        coords[4] = -l;
        coords[6] = l*3/3.0f;
        coords[7] = -l;
        coords[9] = l*3/3.0f;
        coords[10] = l;

        return coords;
    }
}