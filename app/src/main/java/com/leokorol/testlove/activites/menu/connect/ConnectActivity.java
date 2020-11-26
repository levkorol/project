package com.leokorol.testlove.activites.menu.connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;
import com.leokorol.testlove.base.ISimpleListener;
import com.leokorol.testlove.fire_base.AuthManager;

public class ConnectActivity extends AppCompatActivity {
    private TextView _textViewSelfCode;
    private EditText _editTextPartnerCode;
    private TextView _textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conect);

        _textViewSelfCode = findViewById(R.id.connect_textViewSelfCode);
        _editTextPartnerCode = findViewById(R.id.connect_editTextPartnerCode);
        _textViewInfo = findViewById(R.id.textViewInfo);
        _textViewSelfCode.setText(AuthManager.getInstance().getCode());
        final ImageView goMenuActivity = findViewById(R.id.cgoMenuActivity);

        goMenuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMenu();
            }
        });

        AuthManager.getInstance().setPartnerConnectedListener(new ISimpleListener() {

            @Override
            public void eventOccured() {
                _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест");
                showToast("Код подтверждён");
                goSuccess();
            }
        });
        if (AuthManager.getInstance().getIsConnectedToPartner()) {
            _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест");
            goSuccess();
        }
    }

    private void goSuccess() {
        Intent intent = new Intent(this, SuccessConnectActivity.class);
        startActivity(intent);
    }

    public void okBtnOnClick(View view) {
        AuthManager.getInstance().tryMoveToSession(_editTextPartnerCode.getText().toString(),
                new ISimpleListener() {
                    @Override
                    public void eventOccured() {
                        hideVirtualKeyboard();
                    }
                },
                null);
    }

    private void hideVirtualKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void sendCodeOnClick(View view) {
        sendTextByApp(AuthManager.getInstance().getCode());
    }

    private void sendTextByApp(String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Код партнёра");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, "Отправить код партнёру"));
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void goMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}