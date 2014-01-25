package com.edgar.opengl2.juegocolores;

import android.app.ListFragment;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.TextView;

public class JuegoColores extends ActionBarActivity implements MyGLSurfaceView.OnShowListener {

    private MyGLSurfaceView mGLView;
    TextView score;
    TextView level;

    int lvl = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        mGLView.setOnShowListener(this);
        setContentView(R.layout.activity_main_juegocolores);
        addContentView(mGLView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        score = new TextView(this);
        score.setText("0");
        score.setTextSize(35);
        score.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        addContentView(score, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        level = new TextView(this);
        level.setText("Nivel: "+lvl);
        level.setTextSize(35);
        level.setGravity(Gravity.TOP | Gravity.RIGHT);
        addContentView(level, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }

    @Override
    public void onScore() {
        lvl++;
        level.setText("Nivel: "+lvl);
        score.setTextColor(Color.BLACK);
        score.setText(String.valueOf(Integer.valueOf(String.valueOf(score.getText()))+1));
    }

    @Override
    public void onMiss() {
        score.setTextColor(Color.RED);
        score.setText(String.valueOf(Integer.valueOf(String.valueOf(score.getText()))-1));
    }
}
