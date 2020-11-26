package com.leokorol.testlove.activites.menu.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;
import com.leokorol.testlove.activites.menu.MenuTestsActivity;

public class SuccessConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_connect);
        TextView goMenuTestActivity = findViewById(R.id.buttonGoTests);
        ImageView goMenuActivity = findViewById(R.id.igoMenuActivity);

        goMenuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMenu();
            }
        });

        goMenuTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMenuTest();
            }
        });
    }

    private void goMenuTest() {
        Intent intent = new Intent(this, MenuTestsActivity.class);
        startActivity(intent);
    }

    private void goMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
