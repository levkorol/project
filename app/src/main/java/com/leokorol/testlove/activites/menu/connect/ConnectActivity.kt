package com.leokorol.testlove.activites.menu.connect

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.activites.menu.MenuLauncherActivity
import com.leokorol.testlove.base_listeners.ISimpleListener
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.utils.isConnectedToInternet
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast
import kotlinx.android.synthetic.main.activity_conect.*

class ConnectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conect)

        clickListeners()

        connect_textViewSelfCode.text = AuthManager.instance.code

        AuthManager.instance.setPartnerConnectedListener(object : ISimpleListener {
            override fun eventOccured() {
                textViewInfo.text = "Вы соединены с партнёром, можете проходить тест"
                showToast("Код подтверждён")
                replaceActivity(SuccessConnectActivity())
            }

        })
        if (AuthManager.instance.isConnectedToPartner) {
            textViewInfo.text = "Вы соединены с партнёром, можете проходить тест"
            replaceActivity(SuccessConnectActivity())
        }
    }

    private fun clickListeners() {
        cgoMenuActivity.setOnClickListener { replaceActivity(MenuLauncherActivity()) }

        sendCodeOnClick.setOnClickListener { sendTextByApp(AuthManager.instance.code) }

        buttonOk.setOnClickListener {
            okBtnOnClick()
        }
    }


    private fun okBtnOnClick() {
        if (isConnectedToInternet()) {
            AuthManager2.connectToPartner(connect_editTextPartnerCode.text.toString())
            AuthManager.instance.tryMoveToSession(
                connect_editTextPartnerCode.text.toString(),
                object : ISimpleListener {
                    override fun eventOccured() {
                        hideVirtualKeyboard()
                    }
                },
                object : ISimpleListener {
                    override fun eventOccured() {
                        showToast("Партнера с таким кодом не существует")
                    }
                }
            )
        } else {
            showToast("Отсутствует подключение к интернету")
        }
    }

    private fun sendTextByApp(text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Код партнёра")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(sharingIntent, "Отправить код партнёру"))
    }


    private fun hideVirtualKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}