package com.leokorol.testlove.activites.menu.togetherTests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuLauncherActivity
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_together_enter_name.*

class TogetherEnterNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_together_enter_name)

        clickListeners()
    }

    private fun clickListeners() {
        tgoMenuActivity.setOnClickListener { replaceActivity(MenuLauncherActivity()) }
    }
}