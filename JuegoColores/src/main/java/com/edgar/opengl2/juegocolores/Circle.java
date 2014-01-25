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

public class Circle implements Shape{

    private  int mProgram, mPositionHandle, mColorHandle, mMVPMatrixHandle ;
    private FloatBuffer mVertexBuffer;
    private float vertices[] = new float[362 * 3];
    float color[] = { 1.0f, 0.0f, 0.0f, 0.7f };
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

    public Circle(){

        rad = Utils.randSize(rad);

        vertices = genCoords();
        float modV[] = Utils.randPos(vertices);

        /*for(int i =1; i <362; i++){
            Log.d("aawq", modV[(i * 3)+ 0]+" "+modV[(i * 3)+ 1]+" "+modV[(i * 3)+ 2]);
        }

        Log.d("aawq", "-------------------------------------------------------------------------");*/

        topPoint = modV[271];
        leftPoint = modV[540];
        rightPoint = modV[3];
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

        while((topPoint>0.85 || botPoint>0.85)&&(leftPoint<-0.85 || rightPoint<-0.85)){
            vertices = genCoords();
            modV = Utils.randPos(vertices);

            topPoint = modV[271];
            leftPoint = modV[540];
            rightPoint = modV[3];
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
        }

        //Log.v("awq2", ""+topPoint+" "+botPoint+" "+leftPoint+" "+rightPoint);

        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(modV.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(modV);
        mVertexBuffer.position(0);
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

    }

    public Circle(int p){

        float modV[] = vertices;

        modV[0] = -0.943f;
        modV[1] = 0.92f;
        modV[2] = 0;

        for(int i =1; i <362; i++){
            modV[(i * 3)+ 0] = (float) (0.037f * Math.cos((Math.PI/180) * (float)i ))-0.943f;
            modV[(i * 3)+ 1] = (float) (0.037f*1.75 * Math.sin((Math.PI/180) * (float)i ))+0.92f;
            modV[(i * 3)+ 2] = 0;
        }


        topPoint = modV[271];
        leftPoint = modV[540];
        rightPoint = modV[3];
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
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

    }

    @Override
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 362);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    @Override
    public float getRightPoint() {
        return rightPoint;
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

    private float[] genCoords(){
        float[] coords = new float[362 * 3];
        rad = Utils.randSize(rad);

        coords[0] = 0;
        coords[1] = 0;
        coords[2] = 0;

        for(int i =1; i <362; i++){
            coords[(i * 3)+ 0] = (float) (rad * Math.cos((Math.PI/180) * (float)i ));
            coords[(i * 3)+ 1] = (float) (rad*1.75 * Math.sin((Math.PI/180) * (float)i ));
            coords[(i * 3)+ 2] = 0;
        }

        return coords;
    }

}

