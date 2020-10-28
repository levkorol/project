package com.leokorol.testlove;

public class QuestionWithVariants {
    private String _question;
    private String[] _answerVariants;
    private String _answer;

    public QuestionWithVariants(String question, String[] answerVariants)
    {
        _question = question;
        _answerVariants = answerVariants;
    }

    public String getQuestion() {
        return _question;
    }

    public String[] getAnswerVariants() {
        return _answerVariants;
    }

    public String getAnswer() {
        return _answer;
    }

    public void setAnswer(String answer) {
        _answer = answer;
    }
}
