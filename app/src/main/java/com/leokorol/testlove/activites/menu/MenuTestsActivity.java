package com.leokorol.testlove.activites.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;
import com.leokorol.testlove.activites.results.ResultsActivity;
import com.leokorol.testlove.activites.tests.TestOneQuestions;
import com.leokorol.testlove.activites.tests.TestThreeQuestions;
import com.leokorol.testlove.activites.tests.TestTwoQuestions;
import com.leokorol.testlove.base.IAnswersReceivedListener;
import com.leokorol.testlove.fire_base.AuthManager;
import com.leokorol.testlove.tests.texts.Results;

import java.util.ArrayList;
import java.util.List;

public final class MenuTestsActivity extends AppCompatActivity {
    private LinearLayout _llIntresting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_tests_menu);
        _llIntresting = findViewById(R.id.partner);

        AuthManager.getInstance().setAnswers1ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuTestsActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart1(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.onetitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuTestsActivity.this.startActivity(resultsIntent);
            }
        });
        AuthManager.getInstance().setAnswers2ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuTestsActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart2(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.twotitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuTestsActivity.this.startActivity(resultsIntent);
            }
        });
        AuthManager.getInstance().setAnswers3ReceivedListener(new IAnswersReceivedListener() {
            @Override
            public void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers) {
                Intent resultsIntent = new Intent(MenuTestsActivity.this, ResultsActivity.class);
                int equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers);
                String result = Results.getResultsPart3(equalAnswersCount);
                resultsIntent.putExtra("Background", R.drawable.threetitlebg);
                resultsIntent.putExtra("TestResults", result);
                MenuTestsActivity.this.startActivity(resultsIntent);
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
        AuthManager.getInstance().setCurrentPart(1);
        Intent intent = new Intent(this, TestOneQuestions.class);
        startActivity(intent);
    }

    public void goToTestTwoActivity(View view) {
        AuthManager.getInstance().setCurrentPart(2);
        Intent intent = new Intent(this, TestTwoQuestions.class);
        startActivity(intent);

    }


    public void goToTestThreeActivity(View view) {
        AuthManager.getInstance().setCurrentPart(3);
        Intent intent = new Intent(this, TestThreeQuestions.class);
        startActivity(intent);
    }

    public void goMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
