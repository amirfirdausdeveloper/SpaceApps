package com.space.spaceapps.Utils;


import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManagerSplashScreenLogin {
    private static final String PREF_NAME = "login_screen";
    private static final String IS_FIRST_TIME_LAUNCH = "ISSecondTime";
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    Context context;
    //shared pref mode
    int PRIVATE_MODE = 0;

    public PreferenceManagerSplashScreenLogin(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTime) {
        edit.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        edit.commit();
    }

    public boolean isFirstTimeLaunching() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
