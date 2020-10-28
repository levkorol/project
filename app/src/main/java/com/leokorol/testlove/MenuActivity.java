package com.leokorol.testlove;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class MenuActivity extends AppCompatActivity {
    private TextView _textViewSelfCode;
    private EditText _editTextPartnerCode;
    private TextView _textViewInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        _textViewSelfCode = findViewById(R.id.textViewSelfCode);
        _editTextPartnerCode = findViewById(R.id.editTextPartnerCode);
        _textViewInfo = findViewById(R.id.textViewInfo);
        _textViewSelfCode.setText("Ваш код: " + AuthManager.getInstance().getCode());
        AuthManager.getInstance().setPartnerConnectedListener(new ISimpleListener() {
            @Override
            public void eventOccured() {
            _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест");
            showToast("Код подтверждён");
            }
        });
        if (AuthManager.getInstance().getIsConnectedToPartner()) {
            _textViewInfo.setText("Вы соединены с партнёром, можете проходить тест");
        }

        AuthManager.getInstance().setAnswers1ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart1(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.onetitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuActivity.this.startActivity(resultsIntent);
            }
        });
        AuthManager.getInstance().setAnswers2ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart2(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.twotitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuActivity.this.startActivity(resultsIntent);
            }
        });
        AuthManager.getInstance().setAnswers3ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart3(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.threetitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuActivity.this.startActivity(resultsIntent);
            }
        });
    }

    private static int getEqualAnswersCount(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
        int result = 0;
        for (int i = 0; i < selfAnswers.size(); i++) {
            List<Object> self = selfAnswers.get(i);
            List<Object> partner = partnerAnswers.get(i);
            List<Object> intersection = new ArrayList<>(self);
            intersection.retainAll(partner);
            result += intersection.size();
        }
        return result;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void goToTestOneTitle(View view) {
        if (AuthManager.getInstance().getIsConnectedToPartner()) {
            AuthManager.getInstance().setCurrentPart(1);
            Intent intent = new Intent(this, TestOneTitle.class);
            startActivity(intent);
        } else {
            showToast("Для прохождения теста необходимо соединиться с партнёром");
        }
    }

    public void goToTestTwoActivity(View view) {
        if (AuthManager.getInstance().getIsConnectedToPartner()) {
            AuthManager.getInstance().setCurrentPart(2);
            Intent intent = new Intent(this, TestTwoActivity.class);
            startActivity(intent);
        } else {
            showToast("Для прохождения теста необходимо соединиться с партнёром");
        }
    }


    public void goToTestThreeActivity(View view) {
        if (AuthManager.getInstance().getIsConnectedToPartner()) {
            AuthManager.getInstance().setCurrentPart(3);
            Intent intent = new Intent(this, TestThreeActivity.class);
            startActivity(intent);
        } else {
            showToast("Для прохождения теста необходимо соединиться с партнёром");
        }
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

    //https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    private void hideVirtualKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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

    public void goToHoroscope(View view) {
        Intent intent = new Intent(this, HoroscopeActivity.class);
        startActivity(intent);
    }
}
