package com.oldmencorp.testexrollncode.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.oldmencorp.testexrollncode.utils.Constants;
import com.oldmencorp.testexrollncode.repository.SharedPrefHelper;

import java.util.Timer;
import java.util.TimerTask;

public class CounterService extends Service {

    private int count;
    private boolean mHasStarted = false;
    private final String TAG = "CounterService";
    private Timer mTimer = new Timer();
    private ResumeReceiver mResumeReceiver;

    public CounterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mResumeReceiver = new ResumeReceiver();
        registerReceiver(mResumeReceiver, new IntentFilter(Constants.FILTER_ACTIVITY_RESUME));
        Toast.makeText(this, "Service was started!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = SharedPrefHelper.readCount();
        Log.i(TAG, "CounterService onStartCommand");
        if (!mHasStarted) {
            updateTime();
            mTimer.schedule(new Counter(), 0, Constants.COUNT_UPDATE_PERIOD);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        unregisterReceiver(mResumeReceiver);
        Log.i(TAG, "CounterService onDestroy");
        Toast.makeText(this, "Service was stoped!", Toast.LENGTH_SHORT).show();
    }

    private void updateCount() {
        Intent intent = new Intent(Constants.FILTER_NEW_COUNT);
        intent.putExtra(Constants.BUNDLE_COUNT, count);
        sendBroadcast(intent);
        Log.i(TAG, "CounterService updateCount, count = " + count);
    }

    private void updateTime() {
        Intent intent = new Intent(Constants.FILTER_SERVICE_STARTED);
        intent.putExtra(Constants.BUNDLE_SERVICE_START_TIME, System.currentTimeMillis());
        sendBroadcast(intent);
        Log.i(TAG, "CounterService updateTime");
    }

    private class Counter extends TimerTask {
        @Override
        public void run() {
            Log.i(TAG, "CounterService - Counter run");
            if (!mHasStarted) {
                mHasStarted = true;
            } else {
                count++;
                updateCount();
            }
        }
    }

    private class ResumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateCount();
        }
    }
}
