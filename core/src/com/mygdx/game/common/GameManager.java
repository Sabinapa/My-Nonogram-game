package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();
    private static final String PREFERENCES_NAME = "settings";
    private static final String TIMER_KEY = "isTimerOn";
    private static final String GAMEMODE_KEY = "gameMode";
    private static final String SOUND_KEY = "isSoundOn";

    private static final String MUSIC_KEY = "isMusicOn";

    private static Preferences preferences;

    private static List<Levels> levels;

    public static void initialize() {

        if(preferences == null)
        {
            preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        }
        if (levels == null) {
            levels = new ArrayList<>();
        }

    }

    public void saveLevels(Levels level) {
        levels.add(level);
        saveLevelsToJson();
    }

    public List<Levels> loadLevels() {
        String scoresJson = Gdx.files.local("levels.json").exists() ?
                Gdx.files.local("levels.json").readString() :
                "[]";

        Json json = new Json();
        levels = json.fromJson(ArrayList.class, Levels.class, scoresJson);
        return levels;
    }

    private void saveLevelsToJson() {
        Json json = new Json();
        String levelJson = json.prettyPrint(levels);
        Gdx.files.local("levels.json").writeString(levelJson, false);
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

    public static void setGameMode(String gameMode) {
        preferences.putString(GAMEMODE_KEY, gameMode);
    }

    public static String getGameMode() {
        return preferences.getString(GAMEMODE_KEY, "Easy");
    }

    public static void flush() {

        if (preferences != null) {
            preferences.flush();
        }
    }
}
