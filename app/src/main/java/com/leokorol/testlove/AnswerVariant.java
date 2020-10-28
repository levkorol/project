package com.leokorol.testlove;

public class AnswerVariant {
    private String _answerText;
    private boolean _isChecked;

    public AnswerVariant(String answerText) {
        _answerText = answerText;
        _isChecked = false;
    }

    public String getAnswerText() {
        return _answerText;
    }

    public boolean getIsChecked() {
        return _isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        _isChecked = isChecked;
    }
}
