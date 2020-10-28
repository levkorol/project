package com.leokorol.testlove;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WaitForPartner extends AppCompatActivity {
    private ImageView _imageViewBackground;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_partner);
        int backgroundResource = getIntent().getIntExtra("Background", -1);
        _imageViewBackground = findViewById(R.id.imageViewBackground);
        _imageViewBackground.setBackgroundResource(backgroundResource);
    }
}
