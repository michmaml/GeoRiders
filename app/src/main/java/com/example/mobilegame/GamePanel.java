package com.example.mobilegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread gen_thread;
    private RectPlayer rplayer;
    private Point playerPoint;
    private ObstacleManager obstacleManager;


    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        gen_thread = new MainThread(getHolder(), this);
        rplayer = new RectPlayer(new Rect(100, 100, 100, 100), Color.rgb(255, 0, 0));
        playerPoint = new Point(150, 150);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.WHITE);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        gen_thread = new MainThread(getHolder(), this);
        gen_thread.setRunning(true);
        gen_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true){
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
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                playerPoint.set((int)event.getX(), (int)event.getY());
        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        rplayer.update(playerPoint);
        obstacleManager.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        rplayer.draw(canvas);
        obstacleManager.draw(canvas);
    }
}
