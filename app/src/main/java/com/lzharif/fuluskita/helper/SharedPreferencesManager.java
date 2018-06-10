package com.lzharif.fuluskita.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lzharif on 20/04/18.
 */

public class SharedPreferencesManager {
    private static final String SHARED_PREFS_NAME = "sp_name";
    private static final String HAS_ADMIN_ACCESS = "has_admin_access";
    private int PRIVATE_MODE = 0;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shEditor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, PRIVATE_MODE);
        shEditor = sharedPreferences.edit();
    }

    public void setHasAdminAccess(boolean hasAdminAccess) {
        shEditor.putBoolean(HAS_ADMIN_ACCESS, hasAdminAccess);
        shEditor.commit();
    }

    public boolean hasAdminAccess() {
        return sharedPreferences.getBoolean(SHARED_PREFS_NAME, true);
    }
}
