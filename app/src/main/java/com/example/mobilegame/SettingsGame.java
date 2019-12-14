package com.example.mobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import maes.tech.intentanim.CustomIntent;

public class SettingsGame extends AppCompatActivity {

    Button menu_m, game_m, back_b, email_b;
    ImageView imgs;
    HomeWatcher mHomeWatcher;
    Boolean b_m,b_g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_game);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        imgs = findViewById(R.id.background_img_settings);
        imgs.setBackgroundResource(R.drawable.spin_animation_about_settings);
        AnimationDrawable frameAnimation = (AnimationDrawable) imgs.getBackground();
        frameAnimation.start();

        menu_m = findViewById(R.id.musicMenu);
        game_m = findViewById(R.id.musicGame);
        back_b = findViewById(R.id.backButtonSettings);
        email_b = findViewById(R.id.heartSend);

        b_m = true; //menu music is playing, green button
        b_g = true; //game music is playing, green button

        menu_m.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(b_m){
                    if (mServ != null) {
                        mServ.stopMusic();
                    }
                    b_m = false;
                    menu_m.setBackgroundResource(R.drawable.switch_clicked);
                }else if(!b_m){
                    if (mServ != null) {
                        mServ.startMusic();
                    }
                    b_m = true;
                    menu_m.setBackgroundResource(R.drawable.switch_notclicked);
                }
            }
        });

        game_m.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(b_g){
                /*    if (mServ != null) {
                        mServ.stopMusic();
                    }
                    b_g = false;
                    menu_m.setBackgroundResource(R.drawable.switch_clicked);
                }else if(!b_g){
                /*    if (mServ != null) {
                        mServ.startMusic();
                    }*/
                    b_g = true;
                    menu_m.setBackgroundResource(R.drawable.switch_notclicked);
                }
            }
        });

        back_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SettingsGame.this, MainActivity.class));
                CustomIntent.customType(SettingsGame.this,"up-to-bottom");
            }
        });

        email_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openShare();
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

    private void openShare(){
        Intent sendE = new Intent(Intent.ACTION_SEND);
        sendE.putExtra(Intent.EXTRA_TEXT, "Hey! I love this game, check it out! \uD83D\uDE0A");
        sendE.setType("message/rfc882");
        startActivity(Intent.createChooser(sendE,""));
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
