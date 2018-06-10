package com.lzharif.fuluskita.model;

import com.google.firebase.database.Exclude;
import com.lzharif.fuluskita.helper.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzharif on 20/04/18.
 */

public class User {
    public String userId;
    public String username;
    public String email;
    public boolean hasAdminAccess;
    public int userTheme;
    public int userProfPic;

    public User() {}

    public User(String username, String email, boolean hasAdminAccess, int userTheme, int userProfPic) {
        this.username = username;
        this.email = email;
        this.hasAdminAccess = hasAdminAccess;
        this.userTheme = userTheme;
        this.userProfPic = userProfPic;
    }

    @Exclude
    public Map<String, Object> toMapRegister() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constant.EMAIL_USER, email);
        result.put(Constant.USERNAME, username);
        result.put(Constant.HAS_ADMIN_ACCESS, hasAdminAccess);
        result.put(Constant.USER_THEME, userTheme);
        result.put(Constant.USER_PROF_PIC, userProfPic);
        return result;
    }
}
