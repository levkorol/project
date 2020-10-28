package com.leokorol.testlove;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestQuestions extends AppCompatActivity {
    private RecyclerView _recyclerView;
    private TextView _textViewQuestionText;
    private TextView _textViewNumberQuestion;
    private AnswerVariant[][] _allAnswerVariants;
    private int _currentQuestionIndex = 0;
    private QuestionWithVariants[] _questions;
    private int _layoutId;
    private int _backgroundResource;
    private String _answersBranch;

    public TestQuestions(QuestionWithVariants[] questions, int layoutId, int backgroundResource, String answersBranch) {
        _questions = questions;
        _layoutId = layoutId;
        _backgroundResource = backgroundResource;
        _answersBranch = answersBranch;
        _allAnswerVariants = new AnswerVariant[_questions.length][];
        for (int iQuestion = 0; iQuestion < _questions.length; iQuestion++) {
            String[] answerVariants = _questions[iQuestion].getAnswerVariants();
            _allAnswerVariants[iQuestion] = new AnswerVariant[answerVariants.length];
            for (int iAnswer = 0; iAnswer < answerVariants.length; iAnswer++) {
                _allAnswerVariants[iQuestion][iAnswer] = new AnswerVariant(answerVariants[iAnswer]);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(_layoutId);

        _recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        _textViewQuestionText = (TextView) findViewById(R.id.textViewQuestionText);
        _textViewNumberQuestion = (TextView) findViewById(R.id.textViewNumberQuestion);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(llm);
        goToQuestion(0);
    }

    public void goToMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    private int getCountOfCheckedAnswers() {
        int result = 0;
        for (int i = 0; i < _allAnswerVariants[_currentQuestionIndex].length; i++) {
            if (_allAnswerVariants[_currentQuestionIndex][i].getIsChecked()) {
                result++;
            }
        }
        return result;
    }

    public void goToNextQuestion(View view) {
        if (getCountOfCheckedAnswers() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Выберите 1-2 варианта ответа", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (_currentQuestionIndex == _questions.length - 1) {
            AuthManager.getInstance().sendAnswers(_allAnswerVariants, _answersBranch);
            Intent intent = new Intent(this, WaitForPartner.class);
            intent.putExtra("Background", _backgroundResource);
            startActivity(intent);
        } else {
            _currentQuestionIndex++;
            goToQuestion(_currentQuestionIndex);
        }
    }

    private void goToQuestion(int numberQuestion) {
        AnswerVariantAdapter ava = new AnswerVariantAdapter(_allAnswerVariants[_currentQuestionIndex]);
        _recyclerView.setAdapter(ava);
        ava.notifyDataSetChanged();

        _textViewQuestionText.setText(_questions[numberQuestion].getQuestion());
        _textViewNumberQuestion.setText((_currentQuestionIndex + 1) + "/" + _questions.length);
    }
}
