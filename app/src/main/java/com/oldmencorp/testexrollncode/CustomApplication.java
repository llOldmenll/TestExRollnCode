package com.oldmencorp.testexrollncode;

import android.app.Application;
import android.content.Context;


public class CustomApplication extends Application {

    private volatile static CustomApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
