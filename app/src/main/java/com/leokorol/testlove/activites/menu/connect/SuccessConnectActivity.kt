package com.leokorol.testlove.activites.menu.connect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuTestsActivity

class SuccessConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_connect)
        val goMenuTestActivity = findViewById<TextView>(R.id.buttonGoTests)
        val goMenuActivity = findViewById<ImageView>(R.id.igoMenuActivity)
        goMenuActivity.setOnClickListener { goMenu() }
        goMenuTestActivity.setOnClickListener { goMenuTest() }
    }

    private fun goMenuTest() {
        val intent = Intent(this, MenuTestsActivity::class.java)
        startActivity(intent)
    }

    private fun goMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}