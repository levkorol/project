package com.leokorol.testlove;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.UUID;

public class TestApp extends Application {

    final static String APP_PREFS = "app_prefs";

    public static String getAPP_PREFS() {
        return APP_PREFS;
    }

    public static String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public static String getCODE() {
        return CODE;
    }

    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String CODE = "CODE";
    public static final String SESSiON_CODE = "SESSiON_CODE";
    public static final String LAST_QUESTION = "LAST_QUESTION";
    public static final String LAST_PART = "LAST_PART";

    private static Context context;
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    public static SharedPreferences getSharedPref() {
        return sharedPreferences;
    }

    private void init() {
        sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        String deviceId = UUID.randomUUID().toString().toUpperCase();
        String code = generateCode();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference deviceIdRef = database.getReference("queue").child(deviceId);
        deviceIdRef.setValue(code);

        if (sharedPreferences.getString(DEVICE_ID, "").isEmpty()) {
            sharedPreferences.edit().putString(DEVICE_ID, deviceId).apply();
        }

        if (sharedPreferences.getString(CODE, "").isEmpty()) {
            sharedPreferences.edit().putString(CODE, code).apply();
        }

    }

    private static String generateCode() {
        String result = "";
        String str = "abcdefghijklmnopqrstuvwxyz1234567890";
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < 12; i++) {
            result += str.charAt(r.nextInt(str.length()));
        }
        return result;
    }

}
