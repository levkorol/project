package com.leokorol.testlove.activites.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;

public class InterestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting);
        ImageView goMenuActivity = findViewById(R.id.igoMenuActivity);

        goMenuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMenu();
            }
        });
    }

    private void goMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}