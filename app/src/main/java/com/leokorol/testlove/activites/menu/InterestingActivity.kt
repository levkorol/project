package com.leokorol.testlove.activites.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_interesting.*

class InterestingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interesting)

        clickListeners()
    }

    private fun clickListeners() {
        igoMenuActivity.setOnClickListener { replaceActivity(MenuActivity()) }
    }
}