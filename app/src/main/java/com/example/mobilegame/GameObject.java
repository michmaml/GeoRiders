/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;

import android.graphics.Canvas;

public interface GameObject {
    /**
     * An interface to support GamePanel
     * */
    public void draw(Canvas canvas);
    public void update();
}
