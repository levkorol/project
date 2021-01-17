package com.leokorol.testlove.activites.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import java.util.*

class SplashActivity : AppCompatActivity() {
    var timer: Timer? = null
    var mTimerTask: TimerTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        timer = Timer()
        mTimerTask = MyTimerTask()
        timer!!.schedule(mTimerTask, 2000)

    }

    internal inner class MyTimerTask : TimerTask() {
        override fun run() {
            val intent = Intent(this@SplashActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}