/**
 * @Author: Michal J Sekulski
 * Dec 2019, mjsekulski1@gmail.com
 * */
package com.example.mobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
/**
 * Main activity, user starts here. Simple menu which includes intents to other menu activities and contains a MediaPlayer and HomeWatcher object.
 * */
    Button playB, settingsB, aboutB;
    HomeWatcher mHomeWatcher;
    public static ImageView img;
    public static ImageView getImg() {
        return MainActivity.img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        img = findViewById(R.id.background_img);
        img.setBackgroundResource(R.drawable.spin_animation_main);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        playB = findViewById(R.id.playButton);
        settingsB = findViewById(R.id.settingsButton);
        aboutB = findViewById(R.id.aboutButton);

        playB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(MainActivity.this, PlayGame_Menu.class));
                img.setBackgroundResource(R.drawable.bcg_index);
            }
        });

        settingsB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(MainActivity.this, SettingsGame.class));
                CustomIntent.customType(MainActivity.this, "bottom-to-up");
            }
        });

        aboutB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(External_booleans.button_vibration_effects)                          //checks if the button sound effect is enabled
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(MainActivity.this, AboutGame.class));
                CustomIntent.customType(MainActivity.this, "left-to-right");
            }
        });

        /*-------------Music Handler----------------*/
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        //Start HomeWatcher
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
        /*-----------End of Music Handler-----------*/
    }

    /*-------------Music Handler----------------*/
    //Bind/Unbind music service
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if(mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }
        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);
    }
    /*-------------Music Handler----------------*/
}

