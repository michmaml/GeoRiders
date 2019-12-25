/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
//import java.util.Collections;

import static com.example.mobilegame.Constants.SCREEN_HEIGHT;

public class GameplayScene implements Scene {
/**
 * GameplayScene creates and draws everything related to the gameplay scene with the player and obstacles.
 * */

    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private static boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    //private ArrayList<Integer> sortedInts = new ArrayList<>();

    public GameplayScene() {

        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(225,24,166));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * SCREEN_HEIGHT / 4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(500, 600, 130, Color.WHITE);
        if(!External_booleans.getControls_button()){
            orientationData = new OrientationData();
            orientationData.register();
            frameTime = System.currentTimeMillis();
        }
    }

    private void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * SCREEN_HEIGHT / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(500, 600, 130, Color.WHITE);
        movingPlayer = false;
        obstacleManager.setScore(0);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }
    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                    if(!External_booleans.getControls_button())
                        orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(15, 13, 35));

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if (gameOver) {
            //add the sql connection here
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over! Better luck next time :)!");//, you ranked " + rank());
        }
    }

    @Override
    public void update() {
        if (!gameOver) {

            if(!External_booleans.getControls_button()) {
                if (frameTime < Constants.INIT_TIME)
                    frameTime = Constants.INIT_TIME;
                int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
                frameTime = System.currentTimeMillis();
                if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                    float xSpeed = (orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1]) * Constants.SCREEN_WIDTH / 1000f;

                    playerPoint.x -= Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                    playerPoint.y = 3 * SCREEN_HEIGHT / 4;
                }

                if (playerPoint.x < 0)
                    playerPoint.x = 0;
                else if (playerPoint.x > Constants.SCREEN_WIDTH)
                    playerPoint.x = Constants.SCREEN_WIDTH;
            }

            player.update(playerPoint);
            obstacleManager.update();

            if (obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    /*private String rank() {
        sortedInts.add(obstacleManager.getScore());
        Collections.sort(sortedInts, Collections.reverseOrder());
        int position = sortedInts.indexOf(obstacleManager.getScore());
        return String.valueOf(position + 1);
    }*/
}