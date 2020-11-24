package com.leokorol.testlove.activites.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Timer timer;
    TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask, 2000);

//        mStop.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {

//            }
//        });

    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }
}