package com.leokorol.testlove

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.activites.menu.InterestingActivity
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.activites.menu.TogetherEnterNameActivity
import com.leokorol.testlove.activites.menu.connect.ConnectActivity
import com.leokorol.testlove.fire_base.AuthManager

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setContentView(R.layout.activity_menu)
        val goConnectActivity = findViewById<LinearLayout>(R.id.btnGoConnectActivity)
        val goMenuTestsActivity = findViewById<LinearLayout>(R.id.btnGoMenuTestsActivity)
        val goTogetherActivity = findViewById<LinearLayout>(R.id.btnGoTogetherTestsActivity)
        val goInterestingActivity = findViewById<LinearLayout>(R.id.goIntresting)
        goConnectActivity.setOnClickListener { goConnectActivity() }
        goMenuTestsActivity.setOnClickListener { goToTestsActivity() }
        goTogetherActivity.setOnClickListener { goTogetherActivity() }
        goInterestingActivity.setOnClickListener { goInteresting() }
    }

    fun goToTestsActivity() {
        //  if (AuthManager.getInstance().getIsConnectedToPartner()) { //todo закрыть доступ если не подсоеденены
        val intent = Intent(this, MenuTestsActivity::class.java)
        this.startActivity(intent)
        //        } else {
//            showToast("Для прохождения теста необходимо соединиться с партнёром");
        // }
    }

    private fun goConnectActivity() {
        val cm = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            AuthManager.instance.subscribeToSessions()
            //            if (!AuthManager.getInstance().isInQueue()) {
//                AuthManager.getInstance().resetSession();
//            }
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Нет подключения к интернету",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun goInteresting() {
        val intent = Intent(this, InterestingActivity::class.java)
        startActivity(intent)
    }

    fun goTogetherActivity() {
        val intent = Intent(this, TogetherEnterNameActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}