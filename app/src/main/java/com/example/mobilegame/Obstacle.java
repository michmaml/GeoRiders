/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject{
/**
 * Obstacle class is accountable for drawing and spawning white obstacles randomly on the sreen accordingly to the rules(in on line with a gap in between)
 * */
    private Rect rectangle;
    private Rect rectangle2;
    private int colour;

    public Obstacle(int rectHeight, int colour, int startX, int startY, int playerGap){
        this.colour = colour;
        //l,r,t,b
        rectangle = new Rect(0, startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public boolean playerCollide(RectPlayer player){
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(colour);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {
        //required by GameObject
    }
}
