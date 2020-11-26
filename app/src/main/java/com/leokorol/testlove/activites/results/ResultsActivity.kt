package com.leokorol.testlove.activites.results

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuTestsActivity

class ResultsActivity : AppCompatActivity() {
    private lateinit var _textViewResults: TextView
    private lateinit var _imageViewBackground: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        _textViewResults = findViewById(R.id.textViewResults)
        _textViewResults.setMovementMethod(ScrollingMovementMethod())
        val results = intent.getStringExtra("TestResults")
        val backgroundResource = intent.getIntExtra("Background", -1)
        _textViewResults.setText(results)
        _imageViewBackground = findViewById(R.id.imageViewBackground)
        _imageViewBackground.setBackgroundResource(backgroundResource)
    }

    fun okClick(view: View?) {
        val resultsIntent = Intent(this, MenuTestsActivity::class.java)
        startActivity(resultsIntent)
    }
}