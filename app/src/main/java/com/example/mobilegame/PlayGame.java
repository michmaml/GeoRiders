package com.example.mobilegame;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class PlayGame extends Activity {

    public ArrayList<String> dbResults = new ArrayList<>(); //takes the strings from the db
    public ArrayList<Integer> sortedInts = new ArrayList<>(); //sorted dbResults and parsed to integers

    /*After fetching the results from the database to the dbResults I modify them in sortArray method, changing
    *the type from varchar to int and sorting them in the descending order. After doing so, in GameplayScene
    *a method rank() is responsible for getting the position/rank of the user's result and formats the output.
    * Additonally, for some reason the rank() method always prints position -1 which I assume indicates an error... */



    public static MediaPlayer musicPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.CURRENT_CONTEXT = this;

        //if External_booleans.getControls_button() is set to false then both tilting and manual
        //if External_booleans.getControls_button() is set to true disable magnetometer&accelerometer

        if(External_booleans.getControls_button())
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        setContentView(new GamePanel(this));

        /*------------------------------------*/

        musicPlayer.create(this, R.raw.game_tune);
        if(External_booleans.getGame_music_switch())
            musicPlayer.start();

    }

    //connect to the daabase and fetch best results
    private String ReadBufferedHTML(BufferedReader reader, char[] htmlBuffer, int bufSz) throws java.io.IOException {
        htmlBuffer[0] = '\0';
        int offset = 0;
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);
        return new String(htmlBuffer);
    }

    private String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2 * 1024 * 1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        try {
            URL url_object = new URL(url);
            conn_object = (HttpURLConnection) url_object.openConnection();
            conn_object.setInstanceFollowRedirects(true);

            BufferedReader reader_list = new BufferedReader(new InputStreamReader(conn_object.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_list, htmlBuffer, HTML_BUFFER_SIZE);
            reader_list.close();
            return HTMLSource;
        } catch (Exception e) {
            return "Fail to login";
        } finally {
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }

        private void parse_JSON_String_and_Switch_Activity(String JSONString) {
        try {
            JSONObject rootJSONObj = new JSONObject(JSONString);
            JSONArray jsonArray = rootJSONObj.optJSONArray("leaderboard");
            dbResults.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                dbResults.add(jsonArray.getString(i));
                System.out.println(jsonArray.getString(i));
            }
            sortArray(dbResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        final String url = "https://i.cs.hku.hk/~sekulski/leaderboard.php" + (String.valueOf(ObstacleManager.getScore()).isEmpty() ? "" : "?action=insert&score=" + android.net.Uri.encode(String.valueOf(ObstacleManager.getScore()), "UTF-8"));

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String jsonString;

            @Override
            public String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                jsonString = getJsonPage(url);
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (success) {
                    parse_JSON_String_and_Switch_Activity(jsonString);
                    System.out.println("added");
                } else {
                    System.out.print("Fail to connect");
                }
            }
        }.execute("");
    }

    public void sortArray(ArrayList<String> arrayList) {
        sortedInts.clear();
        for (int i = 0; i < arrayList.size(); i++)
            sortedInts.add(i, Integer.parseInt(arrayList.get(i)));
        arrayList.clear();
        Collections.sort(sortedInts, Collections.reverseOrder());
    }
}
