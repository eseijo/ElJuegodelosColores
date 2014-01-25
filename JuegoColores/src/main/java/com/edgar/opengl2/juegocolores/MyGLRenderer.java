package com.edgar.opengl2.juegocolores;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by edgar on 12/17/13.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer{

        private static final String TAG = "MyGLRenderer";

        double refShape;

        ArrayList<Shape> refShapes = new ArrayList<Shape>();
        ArrayList<Shape> shapes = new ArrayList<Shape>();

        int store = -1;

        private Triangle mTriangle;
        private Square mSquare;
        private Circle mCircle;
        private Rectangle mRectangle;
        private Triangle mTriangle0;
        private Square mSquare0;
        private Circle mCircle0;
        private Rectangle mRectangle0;
        private Triangle mTriangle1;
        private Square mSquare1;
        private Circle mCircle1;
        private Rectangle mRectangle1;

        // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];
        private final float[] mRotationMatrix = new float[16];

        private float mAngle;

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {

            // Set the background frame color
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
            refShape = Math.random();
            Log.d("log123", ""+refShape);
            if(refShape<0.25){
                mTriangle = new Triangle(1);
                store = 0;
            }else if(refShape>=0.25 && refShape<0.5){
                mSquare   = new Square(1);
                store = 1;
            }else if(refShape>=0.5 && refShape<0.75){
                mCircle = new Circle(1);
                store = 2;
            } else {
                mRectangle = new Rectangle(1);
                store = 3;
            }
            mCircle0 = new Circle();
            mCircle1 = new Circle();
            mTriangle0 = new Triangle();
            mTriangle1 = new Triangle();
            mSquare0 = new Square();
            mSquare1 = new Square();
            mRectangle0 = new Rectangle();
            mRectangle1 = new Rectangle();

            shapes.add(mCircle0);
            shapes.add(mCircle1);
            shapes.add(mTriangle0);
            shapes.add(mTriangle1);
            shapes.add(mSquare0);
            shapes.add(mSquare1);
            shapes.add(mRectangle0);
            shapes.add(mRectangle1);

            if(store==2){
                refShapes.add(mCircle0);
                refShapes.add(mCircle1);
            }else if(store==0){
                refShapes.add(mTriangle0);
                refShapes.add(mTriangle1);
            }else if(store==1){
                refShapes.add(mSquare0);
                refShapes.add(mSquare1);
            } else {
                refShapes.add(mRectangle0);
                refShapes.add(mRectangle1);
            }
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            float[] scratch = new float[16];

            // Draw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            // Set the camera position (View matrix)
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            // Draw square
            //mSquare.draw(mMVPMatrix);

            // Create a rotation for the triangle

            // Use the following code to generate constant rotation.
            // Leave this code out when using TouchEvents.
            //long time = SystemClock.uptimeMillis() % 4000L;
            //float angle = 0.090f * ((int) time);

            //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0.5f, 0.5f, 0.5f);
            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0.0f, 0.0f, 1.0f);

            // Combine the rotation matrix with the projection and camera view
            // Note that the mMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

            GLES20.glEnable(GLES20.GL_CULL_FACE);

            if(refShape<0.25){
                mTriangle.draw(scratch);
            }else if(refShape>=0.25 && refShape<0.5){
                mSquare.draw(scratch);
            }else if(refShape>=0.5 && refShape<0.75){
                mCircle.draw(scratch);
            } else {
                mRectangle.draw(scratch);
            }

            for(Shape mSh : shapes){
                mSh.draw(scratch);
            }

            //if(mSquare!=null)
              //  mSquare.draw(scratch);
            // Draw triangle
            //mTriangle.draw(scratch);
            //mCircle.draw(scratch);
            //mCircle1.draw(scratch);

        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            // Adjust the viewport based on geometry changes,
            // such as screen rotation
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        }

        /**
         * Utility method for compiling a OpenGL shader.
         *
         * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
         * method to debug shader coding errors.</p>
         *
         * @param type - Vertex or fragment shader type.
         * @param shaderCode - String containing the shader code.
         * @return - Returns an id for the shader.
         */
        public static int loadShader(int type, String shaderCode){

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;
        }

        /**
         * Utility method for debugging OpenGL calls. Provide the name of the call
         * just after making it:
         *
         * <pre>
         * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
         * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
         *
         * If the operation is not successful, the check throws an error.
         *
         * @param glOperation - Name of the OpenGL call to check.
         */
        public static void checkGlError(String glOperation) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                Log.e(TAG, glOperation + ": glError " + error);
                throw new RuntimeException(glOperation + ": glError " + error);
            }
        }

        /**
         * Returns the rotation angle of the triangle shape (mTriangle).
         *
         * @return - A float representing the rotation angle.
         */
        public float getAngle() {
            return mAngle;
        }

        /**
         * Sets the rotation angle of the triangle shape (mTriangle).
         */
        public void setAngle(float angle) {
            mAngle = angle; GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        public float getTopPoint(Shape mShape) {
            return mShape.getTopPoint();
        }

        public float getBotPoint(Shape mShape) {
            return mShape.getBotPoint();
        }

        public float getRightPoint(Shape mShape) {
            return mShape.getRightPoint();
        }

        public float getLeftPoint(Shape mShape) {
            return mShape.getLeftPoint();
        }

        public ArrayList<Shape> getRefShapes() {
            return refShapes;
        }

        public void resetShapes() {
            shapes.clear();
    }
}
