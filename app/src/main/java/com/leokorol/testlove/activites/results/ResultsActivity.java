package com.leokorol.testlove.activites.results;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.R;
import com.leokorol.testlove.activites.menu.MenuTestsActivity;

public class ResultsActivity extends AppCompatActivity {
    private TextView _textViewResults;
    private ImageView _imageViewBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        _textViewResults = findViewById(R.id.textViewResults);
        _textViewResults.setMovementMethod(new ScrollingMovementMethod());
        String results = getIntent().getStringExtra("TestResults");
        int backgroundResource = getIntent().getIntExtra("Background", -1);
        _textViewResults.setText(results);
        _imageViewBackground = findViewById(R.id.imageViewBackground);
        _imageViewBackground.setBackgroundResource(backgroundResource);
    }

    public void okClick(View view) {
        Intent resultsIntent = new Intent(this, MenuTestsActivity.class);
        startActivity(resultsIntent);
    }
}
