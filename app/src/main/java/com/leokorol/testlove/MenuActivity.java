package com.leokorol.testlove;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.activites.menu.ConnectActivity;
import com.leokorol.testlove.activites.menu.InterestingActivity;
import com.leokorol.testlove.activites.menu.MenuTestsActivity;
import com.leokorol.testlove.activites.menu.TogetherEnterNameActivity;
import com.leokorol.testlove.fire_base.AuthManager;


public final class MenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        this.setContentView(R.layout.activity_menu);
        LinearLayout goConnectActivity = findViewById(R.id.btnGoConnectActivity);
        final LinearLayout goMenuTestsActivity = findViewById(R.id.btnGoMenuTestsActivity);
        LinearLayout goTogetherActivity = findViewById(R.id.btnGoTogetherTestsActivity);
        final LinearLayout goInterestingActivity = findViewById(R.id.goIntresting);

        goConnectActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goConnectActivity();
            }
        });

        goMenuTestsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTestsActivity();
            }
        });

        goTogetherActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTogetherActivity();
            }
        });

        goInterestingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goInteresting();
            }
        });

    }

    public void goToTestsActivity() {
        //  if (AuthManager.getInstance().getIsConnectedToPartner()) { //todo закрыть доступ если не подсоеденены
        Intent intent = new Intent(this, MenuTestsActivity.class);
        this.startActivity(intent);
//        } else {
//            showToast("Для прохождения теста необходимо соединиться с партнёром");
        // }
    }

    private void goConnectActivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            AuthManager.getInstance().subscribeToSessions();
//            if (!AuthManager.getInstance().isInQueue()) {
//                AuthManager.getInstance().resetSession();
//            }

            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Нет подключения к интернету", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void goInteresting() {
        Intent intent = new Intent(this, InterestingActivity.class);
        startActivity(intent);
    }

    public void goTogetherActivity() {
        Intent intent = new Intent(this, TogetherEnterNameActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
