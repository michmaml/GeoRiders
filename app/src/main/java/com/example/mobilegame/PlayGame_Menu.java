package com.example.mobilegame;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
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
    PlayGame pg = new PlayGame();

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
        inGameMusic = findViewById(R.id.ControlMusic);
        imgpm = findViewById(R.id.background_anim);

        imgpm.setBackgroundResource(R.drawable.spin_animation_drop_window);
        AnimationDrawable frameAnimation = (AnimationDrawable) imgpm.getBackground();
        frameAnimation.start();


        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                finish();
                startActivity(new Intent(PlayGame_Menu.this, PlayGame.class));
            }

        });
        goBackToMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                startActivity(new Intent(PlayGame_Menu.this, MainActivity.class));
                CustomIntent.customType(PlayGame_Menu.this, "bottom-to-up");
                //pg.getPause().setBackgroundResource(R.drawable.pause_notclicked);
            }
        });

        if(External_booleans.game_music_switch)                                 //checks if the game music is enabled
            inGameMusic.setBackgroundResource(R.drawable.switch_notclicked);
        else if(!External_booleans.game_music_switch)                           //if the music was previously disabled set other button
            inGameMusic.setBackgroundResource(R.drawable.switch_clicked);

        inGameMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(External_booleans.getGame_music_switch()){
                    External_booleans.setGame_music_switch(false);
                    inGameMusic.setBackgroundResource(R.drawable.switch_clicked);
                    PlayGame.musicPlayer.stop();
                } else if(!External_booleans.getGame_music_switch()){
                    External_booleans.setGame_music_switch(true);
                    inGameMusic.setBackgroundResource(R.drawable.switch_notclicked);
                    PlayGame.musicPlayer.start();
                }
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
    }
}