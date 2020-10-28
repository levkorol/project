package com.leokorol.testlove;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import androidx.appcompat.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;

public final class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        this.setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.editTextMainText)).setMovementMethod(new ScrollingMovementMethod());
    }

    @SuppressWarnings("deprecation")
    public void goToNextActivity(View view) {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AuthManager.getInstance().subscribeToSessions();
            if (!AuthManager.getInstance().isInQueue()) {
                AuthManager.getInstance().resetSession();
            }
            Intent intent = new Intent(this, MenuActivity.class);
            this.startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
