package com.example.mobilegame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread gen_thread;
    private SceneManager manager;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        gen_thread = new MainThread(getHolder(), this);
        manager = new SceneManager();
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        gen_thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        gen_thread.setRunning(true);
        gen_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry){
            try {
                gen_thread.setRunning(false);
                gen_thread.join();
            } catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        manager.receiveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        manager.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        manager.draw(canvas);
    }
}
