package com.edgar.opengl2.juegocolores;

/**
 * Created by edgar on 12/17/13.
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Point;
import android.opengl.GLES20;
import android.util.Log;

public class Circle1 {

    private  int mProgram, mPositionHandle, mColorHandle, mMVPMatrixHandle ;
    private FloatBuffer mVertexBuffer;
    private float vertices[] = new float[364 * 3];
    float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };
    float rad = (float)Math.random();
    private float topPoint;
    private float botPoint;
    private float leftPoint;
    private float rightPoint;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Circle1(){

        rad = rad>0.30f ? rad-0.70f : rad;
        rad = rad<0.05f ? rad+0.05f : rad;

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;

        for(int i =1; i <364; i++){
            vertices[(i * 3)+ 0] = (float) (rad * Math.cos((Math.PI/180) * (float)i ));
            vertices[(i * 3)+ 1] = (float) (rad*1.75 * Math.sin((Math.PI/180) * (float)i ));
            vertices[(i * 3)+ 2] = 0;
        }

        float modV[] = randPos(vertices);
        topPoint = modV[271];
        leftPoint = modV[540];
        rightPoint = modV[1080];
        botPoint = modV[811];

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

        Log.v("awq2", ""+topPoint+" "+botPoint+" "+leftPoint+" "+rightPoint);

        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(modV.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(modV);
        mVertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }


    public void draw (float[] mvpMatrix){

        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,12
                ,mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");



        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);



        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 364);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    private float[] randPos(float [] verts){
        float v[] = new float[364 * 3];
        float where = (float) Math.random();
        float rdm = (float)Math.random();
        float rdm1, rdm2;
        if(where<0.25f){
            rdm1 = rdm;
            rdm2 = rdm;
        } else if (0.25f<=where && where<0.5f){
            rdm1 = -rdm;
            rdm2 = rdm;
        } else if(0.5f<where && where<0.75f){
            rdm1 = rdm;
            rdm2 = -rdm;
        } else {
            rdm1 = -rdm;
            rdm2 = -rdm;
        }

        for(int i =0; i <364; i++){
            v[(i * 3)+ 0] = verts[(i * 3)+ 0] + rdm1;
            v[(i * 3)+ 1] = verts[(i * 3)+ 1] + rdm2;
        }
        return v;
    }


    public float getRightPoint() {
        return rightPoint;
    }

    public float getTopPoint() {
        return topPoint;
    }

    public float getBotPoint() {
        return botPoint;
    }

    public float getLeftPoint() {
        return leftPoint;
    }

}

