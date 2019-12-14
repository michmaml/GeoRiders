package com.example.mobilegame;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private GeneralThread gen_thread;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        gen_thread = new GeneralThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        gen_thread = new GeneralThread(getHolder(), this);
    }
}
