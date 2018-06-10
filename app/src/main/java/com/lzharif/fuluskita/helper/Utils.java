package com.lzharif.fuluskita.helper;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by lzharif on 20/04/18.
 */

public class Utils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
