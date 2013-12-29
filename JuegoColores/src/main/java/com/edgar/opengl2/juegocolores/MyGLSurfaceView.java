package com.edgar.opengl2.juegocolores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by edgar on 12/17/13.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private MyGLRenderer mRenderer;
    private float topPoint;
    private float botPoint;
    private float leftPoint;
    private float rightPoint;
    OnScoreListener onScoreListener;
    ArrayList<Shape> shapes;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            /*case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
                break;*/
            case MotionEvent.ACTION_DOWN:
                Log.v("awq1", ""+y+","+x);
                shapes = mRenderer.getRefShapes();
                shapes=isShape(x,y,shapes);
                if(shapes.isEmpty()){
                    mRenderer.resetShapes();
                    onScoreListener.onScore();
                    onPause();
                    onResume();
                }
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private ArrayList<Shape> isShape(float x, float y, ArrayList<Shape> arrayList){
        for(Shape mSh : arrayList){
            topPoint = getHeight()-(((mRenderer.getTopPoint(mSh)+1)*getHeight())/2);
            botPoint = getHeight()-(((mRenderer.getBotPoint(mSh)+1)*getHeight())/2);
            leftPoint = ((mRenderer.getLeftPoint(mSh)+1)*getWidth())/2;
            rightPoint = ((mRenderer.getRightPoint(mSh)+1)*getWidth())/2;
            Log.v("awq1", ""+topPoint+","+botPoint+","+leftPoint+","+rightPoint);

            if(x<rightPoint && x>leftPoint && y>topPoint && y<botPoint){
                arrayList.remove(arrayList.indexOf(mSh));
                return arrayList;
            }
        }
        return arrayList;
    }

    public interface OnScoreListener {
        public abstract void onScore();
    }

    public void setOnScoreListener(OnScoreListener listener) {
        onScoreListener = listener;
    }

}
