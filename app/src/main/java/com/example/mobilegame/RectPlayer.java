package com.example.mobilegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int colour;

    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;
    private AnimationManager animationManager;

    public RectPlayer(Rect rectangle, int colour) {
        this.rectangle = rectangle;
        this.colour = colour;
/*
        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap idleImg = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player_img0);
        Bitmap walk1 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player_img1);
        Bitmap walk2 = bitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player_img1);

        idle = new Animation(new Bitmap[]{idleImg, walk1}, 2);
        walkRight = new Animation(new Bitmap[]{walk1}, 0.5f);
        //vertical reverse  the image here
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 1f);

        animationManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});
        //animationManager = new AnimationManager(new Animation[]{idle});*/
    }

    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(colour);
        canvas.drawRect(rectangle, paint);
        //animationManager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        //animationManager.update();

    }

    public void update(Point point){
        //float oldLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.width()/2, point.x + rectangle.width()/2, point.y + rectangle.width()/2);
        //rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.width()/2);

/*
        int state = 0;
        if(rectangle.left - oldLeft > 5){
            state = 1;
        } else if(rectangle.left - oldLeft < -5){
            state = 2;
        }

        animationManager.playAnim(state);
        animationManager.update();*/
    }
}
