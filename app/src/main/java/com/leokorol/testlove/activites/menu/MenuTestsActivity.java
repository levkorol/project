package com.leokorol.testlove.activites.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leokorol.testlove.MenuActivity;
import com.leokorol.testlove.R;
import com.leokorol.testlove.TestApp;
import com.leokorol.testlove.activites.results.ResultsActivity;
import com.leokorol.testlove.activites.tests.TestOneQuestions;
import com.leokorol.testlove.activites.tests.TestThreeQuestions;
import com.leokorol.testlove.activites.tests.TestTwoQuestions;
import com.leokorol.testlove.base.IAnswersReceivedListener;
import com.leokorol.testlove.fire_base.AuthManager;
import com.leokorol.testlove.tests.texts.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MenuTestsActivity extends AppCompatActivity {
    private LinearLayout _llIntresting;
    private TextView progressPart1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_tests_menu);
        _llIntresting = findViewById(R.id.partner);
        progressPart1 = findViewById(R.id.tvSessionInfoTest1);

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

        checkLastSession();

    }

    private void checkLastSession() {
        final String lastSession = TestApp.sharedPreferences.getString(TestApp.SESSiON_CODE, "");
        if (!lastSession.isEmpty()) {
            int lastQuestion = TestApp.sharedPreferences.getInt(TestApp.LAST_QUESTION, 0);
            progressPart1.setText(String.valueOf(lastQuestion));


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference sessionsRef = database.getReference("sessions");

                    // TODO забирать данные по ответам тут
//                    DatabaseReference sessionRef = sessionsRef.child(_sessionCode);
//                    DatabaseReference answersRef = sessionRef.child(_answersBranch);
//                    DatabaseReference isFinishedRef = sessionRef.child("isFinished");
                    Object obj = snapshot.getValue();
                    if (obj instanceof HashMap) {
                        HashMap<String, Object> hm = (HashMap<String, Object>)obj;
                        if (hm.size() == 2) {
//                            isFinishedRef.setValue(true);
//                            answersRef.removeValue();
//                            isFinishedRef.removeValue();
                            List<List<Object>> selfAnswers = new ArrayList<>();
                            List<List<Object>> partnerAnswers = new ArrayList<>();
                            for (Object key : hm.keySet()) {
//                                if (key.toString().equals(getDeviceId())) {
//                                    selfAnswers = (List<List<Object>>)hm.get(key);
//                                } else {
//                                    partnerAnswers = (List<List<Object>>)hm.get(key);
//                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
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

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
