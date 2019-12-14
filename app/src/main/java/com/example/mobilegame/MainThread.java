package com.example.mobilegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public  MainThread(SurfaceHolder holder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

        @Override
    public void run(){
        long startTime;
        long timeMiliseconds = 1000/MAX_FPS;
        long waitingTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;


        while(running){
            startTime = System.nanoTime();
            canvas = null;
            try{
                canvas =this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMiliseconds = (System.nanoTime() - startTime)/1000000;
            waitingTime = targetTime - timeMiliseconds;
            try{
                if(waitingTime > 0)
                    this.sleep(waitingTime);
            } catch(Exception e) {
                e.printStackTrace();
            }
            totalTime +=System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                averageFPS =1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
}
