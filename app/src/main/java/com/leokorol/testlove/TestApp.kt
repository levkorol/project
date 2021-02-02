package com.leokorol.testlove

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.utils.showToast
import java.util.*

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        init()

        AuthManager2.isConnectedPartner(getUserCode(), {
            AuthManager2.subscribePartnerTestResults(getPartnerCode())
            AuthManager2.subscribeMyTestResults(getUserCode())
        }, {})

        AuthManager2.setTest1Listener { my, partner ->
            showToast("Вы оба пришли тест 1. Можете посмотреть результаты в результатах")
        }

        AuthManager2.setTest2Listener { my, partner ->
            showToast("Вы оба пришли тест 2. Можете посмотреть результаты в результатах")
        }

        AuthManager2.setTest3Listener { my, partner ->
            showToast("Вы оба пришли тест 3. Можете посмотреть результаты в результатах")
        }
    }

    private fun init() {
        sharedPref = getSharedPreferences(APP_PREFS, MODE_PRIVATE)
        val database = FirebaseDatabase.getInstance()
        var code = sharedPref?.getString(MY_CODE, "")!!
        var deviceId = sharedPref?.getString(DEVICE_ID, "")!!
        if (code.isEmpty() || deviceId.isEmpty()) {
            code = generateCode()
            deviceId = UUID.randomUUID().toString().toUpperCase()
            sharedPref?.edit()?.putString(MY_CODE, code)?.apply()
            sharedPref?.edit()?.putString(DEVICE_ID, deviceId)?.apply()
        }
        val deviceIdRef = database.getReference("queue").child(deviceId)
        deviceIdRef.setValue(code)
        //database.getReference(code).child("partner").setValue("")
        AuthManager2.initPartnerConnectedListener(code)
    }


    companion object {
        const val APP_PREFS = "app_prefs"
        const val DEVICE_ID = "DEVICE_ID"
        const val USER_NAME = "USER_NAME"
        const val PARTNER_NAME = "PARTNER_NAME"
        const val MY_CODE = "CODE"
        const val PARTNER_CODE = "PARTNER_CODE"
        const val SESSiON_CODE = "SESSiON_CODE"
        const val LAST_QUESTION_1 = "LAST_QUESTION_1"
        const val LAST_QUESTION_2 = "LAST_QUESTION_2"
        const val LAST_QUESTION_3 = "LAST_QUESTION_3"
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

        fun getPartnerCode(): String {
            return sharedPref?.getString(PARTNER_CODE, "") ?: ""
        }

        fun getUserCode(): String {
            return sharedPref?.getString(MY_CODE, "") ?: ""
        }

        fun savePartnerCode(partnerCode: String) {
            sharedPref?.edit()?.putString(PARTNER_CODE, partnerCode)?.apply()
        }
    }
}