package com.example.mass.ui.main.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsProvider {

    private static final String SHARED_PREFS_FILE = "SHARED_PREFS_FILE";

    public static SharedPreferences get(Context c) {
        return c.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

}
