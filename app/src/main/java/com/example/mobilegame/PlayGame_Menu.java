/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import maes.tech.intentanim.CustomIntent;

public class PlayGame_Menu extends Activity {
    /**
     * Pop-up window where the user can choose the controls method and start playing or come back to the menu.s
     * */

    Button startGame, goBackToMenu, controlsMan, controlsTil;
    ImageView imgpm;

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

        startGame = findViewById(R.id.PlayGame);
        goBackToMenu = findViewById(R.id.GoBackToMenu);
        controlsMan = findViewById(R.id.ManualC);
        controlsTil = findViewById(R.id.TiltingC);
        imgpm = findViewById(R.id.background_anim);

        imgpm.setBackgroundResource(R.drawable.spin_animation_drop_window);
        AnimationDrawable frameAnimation = (AnimationDrawable) imgpm.getBackground();
        frameAnimation.start();


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(PlayGame_Menu.this, PlayGame.class));
                CustomIntent.customType(PlayGame_Menu.this, "fadein-to-fadeout");
            }

        });

        goBackToMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(PlayGame_Menu.this, MainActivity.class));
                //CustomIntent.customType(PlayGame_Menu.this, "bottom-to-up");
            }
        });

        controlsMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects && !External_booleans.getControls_button())
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(!External_booleans.getControls_button()){
                    controlsMan.setBackgroundResource(R.drawable.dis_manual);                        //false means that manual is enabled
                    controlsTil.setBackgroundResource(R.drawable.steering_til_btn);                  //true indicates that tilting is enabled
                    External_booleans.setControls_button(true);
                }
            }
        });

        controlsTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects && External_booleans.getControls_button())
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(External_booleans.getControls_button()) {
                    controlsTil.setBackgroundResource(R.drawable.dis_tilting);
                    controlsMan.setBackgroundResource(R.drawable.steering_man_btn);
                    External_booleans.setControls_button(false);
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