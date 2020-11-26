package com.leokorol.testlove.activites.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R

class InterestingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interesting)
        val goMenuActivity = findViewById<ImageView>(R.id.igoMenuActivity)
        goMenuActivity.setOnClickListener { goMenu() }
    }

    private fun goMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}