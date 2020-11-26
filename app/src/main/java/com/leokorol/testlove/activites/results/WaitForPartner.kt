package com.leokorol.testlove.activites.results

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R

class WaitForPartner : AppCompatActivity() {
    private lateinit var _imageViewBackground: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_for_partner)
        val backgroundResource = intent.getIntExtra("Background", -1)
        _imageViewBackground = findViewById(R.id.imageViewBackground)
        _imageViewBackground.setBackgroundResource(backgroundResource)
    }
}