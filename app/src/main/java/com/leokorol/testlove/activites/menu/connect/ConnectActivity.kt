package com.leokorol.testlove.activites.menu.connect

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.base.ISimpleListener
import com.leokorol.testlove.fire_base.AuthManager

class ConnectActivity : AppCompatActivity() {
    private lateinit var _textViewSelfCode: TextView
    private lateinit var _editTextPartnerCode: EditText
    private lateinit var _textViewInfo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conect)
        _textViewSelfCode = findViewById(R.id.connect_textViewSelfCode)
        _editTextPartnerCode = findViewById(R.id.connect_editTextPartnerCode)
        _textViewInfo = findViewById(R.id.textViewInfo)
        _textViewSelfCode.setText(AuthManager.instance.code)
        val goMenuActivity = findViewById<ImageView>(R.id.cgoMenuActivity)
        goMenuActivity.setOnClickListener { goMenu() }

        AuthManager.instance.setPartnerConnectedListener(object : ISimpleListener {
            override fun eventOccured() {
                _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест")
                showToast("Код подтверждён")
                goSuccess()
            }

        })
        if (AuthManager.instance.isConnectedToPartner) {
            _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест")
            goSuccess()
        }
    }

    private fun goSuccess() {
        val intent = Intent(this, SuccessConnectActivity::class.java)
        startActivity(intent)
    }

    fun okBtnOnClick(view: View?) {
        AuthManager.instance.tryMoveToSession(
            _editTextPartnerCode!!.text.toString(),
            object : ISimpleListener {
                override fun eventOccured() {
                    hideVirtualKeyboard()
                }
            },
            null
        )
    }

    private fun hideVirtualKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun sendCodeOnClick(view: View?) {
        sendTextByApp(AuthManager.instance.code)
    }

    private fun sendTextByApp(text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Код партнёра")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(sharingIntent, "Отправить код партнёру"))
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun goMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}