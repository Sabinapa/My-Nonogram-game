package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {
    private static final String PREFERENCES_NAME = "settings";
    private static final String TIMER_KEY = "isTimerOn";
    private static final String SOUND_KEY = "isSoundOn";

    private static final String MUSIC_KEY = "isMusicOn";

    private static Preferences preferences;

    public static void initialize() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public static boolean isTimerOn() {
        return preferences.getBoolean(TIMER_KEY, true);
    }

    public static void setTimerState(boolean isTimerOn) {
        preferences.putBoolean(TIMER_KEY, isTimerOn);
    }

    public static boolean isSoundOn() {
        return preferences.getBoolean(SOUND_KEY, true);
    }

    public static void setSoundState(boolean isSoundOn) {
        preferences.putBoolean(SOUND_KEY, isSoundOn);
    }

    public static boolean isMusicOn() {
        return preferences.getBoolean(MUSIC_KEY, true);
    }

    public static void setMusicState(boolean isMusicOn) {
        preferences.putBoolean(MUSIC_KEY, isMusicOn);
    }



    public static void flush() {
        preferences.flush();
    }
}
