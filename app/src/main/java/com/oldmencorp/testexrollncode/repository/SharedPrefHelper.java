package com.oldmencorp.testexrollncode.repository;


import android.content.Context;
import android.content.SharedPreferences;

import com.oldmencorp.testexrollncode.utils.Constants;
import com.oldmencorp.testexrollncode.CustomApplication;

import static com.oldmencorp.testexrollncode.utils.Constants.BUNDLE_CHANGE_CONFIG_STATE;
import static com.oldmencorp.testexrollncode.utils.Constants.BUNDLE_COUNT;
import static com.oldmencorp.testexrollncode.utils.Constants.BUNDLE_SERVICE_START_TIME;

public class SharedPrefHelper {

    public static int readCount() {
        SharedPreferences sharedPref = getSharedPref();
        return sharedPref.getInt(BUNDLE_COUNT, 0);
    }

    public static long readTime() {
        SharedPreferences sharedPref = getSharedPref();
        return sharedPref.getLong(Constants.BUNDLE_SERVICE_START_TIME, 0);
    }

    public static boolean readChangeConfigState(){
        SharedPreferences sharedPref = getSharedPref();
        return sharedPref.getBoolean(Constants.BUNDLE_CHANGE_CONFIG_STATE, false);
    }

    public static void writeCount(int countValue){
        getSharedPref().edit().putInt(BUNDLE_COUNT, countValue).apply();
    }

    public static void writeTime(long timeValue){
        getSharedPref().edit().putLong(BUNDLE_SERVICE_START_TIME, timeValue).apply();
    }

    public static void writeChangeConfigState(boolean isChange){
        getSharedPref().edit().putBoolean(BUNDLE_CHANGE_CONFIG_STATE, isChange).apply();
    }

    private static SharedPreferences getSharedPref() {
        return CustomApplication.getAppContext()
                .getSharedPreferences(Constants.SHARED_SESSION, Context.MODE_PRIVATE);
    }
}
