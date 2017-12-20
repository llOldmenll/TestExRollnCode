package com.oldmencorp.testexrollncode.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.oldmencorp.testexrollncode.utils.Constants;
import com.oldmencorp.testexrollncode.service.CounterService;
import com.oldmencorp.testexrollncode.R;
import com.oldmencorp.testexrollncode.repository.SharedPrefHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.counter_main)
    TextView mCounter;
    @BindView(R.id.last_start_time_main)
    TextView mLastStartTime;
    @BindView(R.id.btn_start_main)
    Button mBtnStart;
    @BindView(R.id.btn_stop_main)
    Button mBtnStop;

    private CountReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        registerReceiver();
        initUi();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!SharedPrefHelper.readChangeConfigState())
            sendBroadcast(new Intent(Constants.FILTER_ACTIVITY_RESUME));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        SharedPrefHelper.writeChangeConfigState(isChangingConfigurations());
    }


    private void registerReceiver() {
        mReceiver = new CountReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.FILTER_NEW_COUNT);
        intentFilter.addAction(Constants.FILTER_SERVICE_STARTED);
        registerReceiver(mReceiver, intentFilter);
    }

    private void initUi() {
        mBtnStart.setOnClickListener(v ->
                startService(new Intent(this, CounterService.class)));
        mBtnStop.setOnClickListener(v ->
                stopService(new Intent(this, CounterService.class)));
        updateCount();
        updateTime();
    }

    private void updateCount() {
        mCounter.setText(String.valueOf(SharedPrefHelper.readCount()));
    }

    private void updateTime() {
        if (SharedPrefHelper.readTime() > 0)
            mLastStartTime.setText(getFormatedTime(SharedPrefHelper.readTime()));
        else
            mLastStartTime.setText(String.valueOf(SharedPrefHelper.readTime()));
    }

    private String getFormatedTime(long dateInMillis) {
        DateFormat mDateForm = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.getDefault());
        return mDateForm.format(new Date(dateInMillis));
    }

    private class CountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null)
                switch (intent.getAction()) {
                    case Constants.FILTER_SERVICE_STARTED:
                        SharedPrefHelper.writeTime(intent.getLongExtra(Constants.BUNDLE_SERVICE_START_TIME, 0));
                        updateTime();
                        break;
                    case Constants.FILTER_NEW_COUNT:
                        SharedPrefHelper.writeCount(intent.getIntExtra(Constants.BUNDLE_COUNT, 0));
                        updateCount();
                        break;
                }
        }
    }
}
