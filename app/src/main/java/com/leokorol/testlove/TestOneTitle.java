package com.leokorol.testlove;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public final class TestOneTitle extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_one_title);
    }

    public void goToMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void goTestOne(View view) {
        Intent intent = new Intent(this, TestOne.class);
        startActivity(intent);
    }
}