package com.example.mobilegame;

import android.graphics.Canvas;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int colour;
    private long startTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int colour){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.colour = colour;

        startTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();
        populateObstacles();
    }

    public void populateObstacles(){
        int currentY = -5*Constants.SCREEN_HEIGHT/4;
        //keep on generating obstacles once they have not reached the screen
        while(currentY < 0){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, colour, xStart, currentY, playerGap));
            currentY += obstacleGap + obstacleHeight;
        }
    }

    public void update(){
        int passedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        //how fast is it moving
        float speed = Constants.SCREEN_HEIGHT/10000.0f;
        for(Obstacle obstacle : obstacles){
            obstacle.incrementY(speed * passedTime);
        }                                                                   //change to width
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, colour, xStart, obstacles.get(0).getRectangle().top -  obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
        }
    }

    public void draw(Canvas canvas){
        for(Obstacle obstacle : obstacles){
            obstacle.draw(canvas);
        }
    }
}
