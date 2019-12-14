package com.example.mobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import maes.tech.intentanim.CustomIntent;

public class PlayGame_Menu extends Activity {

    Button closeWindow, goBackToMenu, inGameMusic;
    ImageView imgpm;
    Boolean b_g;
    PlayGame pg = new PlayGame();
    HomeWatcher hm_g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game__menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        closeWindow = findViewById(R.id.Resume);
        goBackToMenu = findViewById(R.id.GoBackToMenu);
        //inGameMusic = findViewById(R.id.ControlMusic);
        imgpm = findViewById(R.id.background_anim);

        imgpm.setBackgroundResource(R.drawable.spin_animation_drop_window);
        AnimationDrawable frameAnimation = (AnimationDrawable) imgpm.getBackground();
        frameAnimation.start();


        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //pg.getPause().setBackgroundResource(R.drawable.pause_notclicked);
            }

        });
        goBackToMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PlayGame_Menu.this, MainActivity.class));
                CustomIntent.customType(PlayGame_Menu.this, "bottom-to-up");
                //pg.getPause().setBackgroundResource(R.drawable.pause_notclicked);
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

/*

        b_g = true;
        inGameMusic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PlayGame_Menu.this, MainActivity.class));
               /* if(b_g){
                    if (mServ != null) {
                        mServ.stopMusic();
                    }
                    b_g = false;
                    inGameMusic.setBackgroundResource(R.drawable.switch_clicked);
                }else if(!b_g){
                    if (mServ != null) {
                        mServ.startMusic();
                    }
                    b_g = true;
                    inGameMusic.setBackgroundResource(R.drawable.switch_notclicked);
                }*/
    }
    //});


}