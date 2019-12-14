package com.example.mobilegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer rplayer;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    public GameplayScene(){
        rplayer = new RectPlayer(new Rect(100, 100, 100, 100), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        rplayer.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.WHITE);
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        rplayer.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.WHITE);
        movingPlayer = false;
    }

    @Override
    public void update() {
        if(!gameOver) {
            rplayer.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(rplayer)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        rplayer.draw(canvas);
        obstacleManager.draw(canvas);
        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && rplayer.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }


    private void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0,  text.length(), r);
        float x = cWidth / 2f - r.width() /2f - r.left;
        float y = cHeight /2f - r.height() /2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
