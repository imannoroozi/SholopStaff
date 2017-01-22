package com.sholop.sholopstaff.utilities;

/**
 * Created by Pooyan on 10/22/2015. used again
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SQLiteHandler db;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_IMAGE_URL = "userImageURL";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        db = new SQLiteHandler(context);
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setCurrentUser(String uid, String email, String name) {
        editor.putString(KEY_USER_ID, uid);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, name);

        // commit changes
        editor.commit();
    }

    public int getCurrentUserID(){
        return Integer.parseInt(pref.getString(KEY_USER_ID, "sdfs"));
    }
    public String getCurrentUserImageURL(){
        return pref.getString(KEY_USER_IMAGE_URL, "sdfs");
    }
    public String getCurrentUserName(){
        return pref.getString(KEY_USER_NAME, "sdfs");
    }
    public String getCurrentUserEmail(){
        return pref.getString(KEY_USER_EMAIL, "sdfs");
    }

    public void updateCurrentUserURL( String url) {
        editor.putString(KEY_USER_IMAGE_URL, url);
        editor.commit();
        db.updateUser(this.getCurrentUserName(),this.getCurrentUserEmail(), String.valueOf(this.getCurrentUserID()),url);
    }

    public void updateCurrentUserName( String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.commit();

        db.updateUser(name,this.getCurrentUserEmail(), String.valueOf(this.getCurrentUserID()),this.getCurrentUserImageURL());
    }
}
