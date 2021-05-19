package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class prefs {
    private static final String HIGHEST = "Highest Score";
    private SharedPreferences preferences;

    public prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }
    public void higestscore(int score)
    {
        int cs = score;
        int ls = preferences.getInt(HIGHEST,0);
        if(cs>ls)
            preferences.edit().putInt(HIGHEST,cs).apply();
    }
    public int getHighestScore()
    {
        return preferences.getInt(HIGHEST,0);
    }
}
