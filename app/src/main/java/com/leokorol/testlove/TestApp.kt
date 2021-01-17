package com.leokorol.testlove

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        init()
    }

    private fun init() {
        sharedPref = getSharedPreferences(APP_PREFS, MODE_PRIVATE)
        val database = FirebaseDatabase.getInstance()
        var code = sharedPref?.getString(CODE, "")!!
        var deviceId = sharedPref?.getString(DEVICE_ID, "")!!
        if (code.isEmpty() || deviceId.isEmpty()) {
            code = generateCode()
            deviceId = UUID.randomUUID().toString().toUpperCase()
            sharedPref?.edit()?.putString(CODE, code)?.apply()
            sharedPref?.edit()?.putString(DEVICE_ID, deviceId)?.apply()
        }
        val deviceIdRef = database.getReference("queue").child(deviceId)
        deviceIdRef.setValue(code)
    }



    companion object {
        const val APP_PREFS = "app_prefs"
        const val DEVICE_ID = "DEVICE_ID"
        const val USER_NAME = "USER_NAME"
        const val PARTNER_NAME = "PARTNER_NAME"
        const val CODE = "CODE"
        const val SESSiON_CODE = "SESSiON_CODE"
        const val LAST_QUESTION = "LAST_QUESTION"
        const val LAST_PART = "LAST_PART"
        private var context: Context? = null
        var sharedPref: SharedPreferences? = null
        private fun generateCode(): String {
            var result = ""
            val str = "abcdefghijklmnopqrstuvwxyz1234567890"
            val r = Random(System.currentTimeMillis())
            for (i in 0..11) {
                result += str[r.nextInt(str.length)]
            }
            return result
        }


        fun getUserName(): String {
           return sharedPref?.getString(USER_NAME, "") ?: ""
        }

        fun getUserCode(): String {
            return sharedPref?.getString(CODE, "") ?: ""
        }
    }
}