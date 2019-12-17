package com.example.mobilegame;

public class External_booleans {

    /*-----------RESPONSIBLE FOR BUTTON CLICKS-----------*/
    public static Boolean button_vibration_effects = true;
    public static Boolean getButton_vibration_effects(){
        return External_booleans.button_vibration_effects;
    }
    public static void setButton_vibration_effects(Boolean button_vibration_effects) {
        External_booleans.button_vibration_effects = button_vibration_effects;
    }

    /*------------RESPONSIBLE FOR MENU MUSIC-------------*/
    public static Boolean menu_music_switch = true;
    public static Boolean getMenu_music_switch() {
        return External_booleans.menu_music_switch;
    }
    public static void setMenu_music_switch(Boolean menu_music_switch) {
        External_booleans.menu_music_switch = menu_music_switch;
    }

    /*------------RESPONSIBLE FOR GAME MUSIC-------------*/
    public static Boolean game_music_switch = true;
    public static Boolean getGame_music_switch() {
        return game_music_switch;
    }
    public static void setGame_music_switch(Boolean game_music_switch) {
        External_booleans.game_music_switch = game_music_switch;
    }


}
