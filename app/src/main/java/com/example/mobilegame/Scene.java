/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;
    /**
     * Simple interface, extended in GameplayScene
     * */

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void receiveTouch(MotionEvent event);
    public void terminate();
}
