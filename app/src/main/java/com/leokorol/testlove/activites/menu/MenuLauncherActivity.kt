package com.leokorol.testlove.activites.menu

import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.menu.connect.ConnectActivity
import com.leokorol.testlove.activites.menu.intresting.InterestingActivity
import com.leokorol.testlove.activites.menu.togetherTests.TogetherEnterNameActivity
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuLauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        this.setContentView(R.layout.activity_menu)

        initClick()
        whatMyName()
        setupDisconnectButton()

        visiblePartner(true) // todo сделать видимым если партнер подключен
    }


    private fun setupDisconnectButton() {    //todo отсоеденить партнера и удалить все сессии в тестах

        btnDisconnect.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val sessionsRef = database.getReference("sessions")

            visiblePartner(false)

            sessionsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        return
                    }
                    for (snapshot in dataSnapshot.children) {
                        val sessionCode = snapshot.key
                        if (sessionCode!!.contains(TestApp.getUserCode())) {
                            sessionsRef.removeValue()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun whatMyName() {  //todo сохранить имя свое в базу и что бы оно передавалось партнеру при подключении
        val myNameEditText = findViewById<EditText>(R.id.name_user)
        myNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                TestApp.sharedPref?.edit()?.putString(TestApp.USER_NAME, s.toString())?.apply()
            }
        })
    }

    private fun goToTestsActivity() {
        //    if (AuthManager.getInstance().getIsConnectedToPartner()) {  //todo закрыть доступ если не подсоеденены
        replaceActivity(MenuTestsActivity())
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
            replaceActivity(ConnectActivity())
        } else {
            showToast("Нет подключения к интернету")
        }
    }

    private fun visiblePartner(isVisible: Boolean) {
        if (isVisible) {
            partner.visibility = View.VISIBLE
        } else {
            partner.visibility = View.GONE
        }
    }

    private fun initClick() {
        btnGoConnectActivity.setOnClickListener { goConnectActivity() }
        btnGoMenuTestsActivity.setOnClickListener { goToTestsActivity() }
        btnGoTogetherTestsActivity.setOnClickListener { replaceActivity(TogetherEnterNameActivity()) }
        goIntresting.setOnClickListener { replaceActivity(InterestingActivity()) }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}