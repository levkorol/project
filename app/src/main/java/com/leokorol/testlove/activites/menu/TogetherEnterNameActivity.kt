package com.leokorol.testlove.activites.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R

class TogetherEnterNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_together_enter_name)
        val goMenuActivity = findViewById<ImageView>(R.id.tgoMenuActivity)
        goMenuActivity.setOnClickListener { goMenu() }
    }

    private fun goMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}