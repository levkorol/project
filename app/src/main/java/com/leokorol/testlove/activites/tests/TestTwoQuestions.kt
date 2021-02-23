package com.leokorol.testlove.activites.tests

import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.base_activity.BaseTestQuestionsActivity
import com.leokorol.testlove.tests.texts.Questions

class TestTwoQuestions : BaseTestQuestionsActivity(
    Questions.Part2,
    R.layout.activity_test_two_questions,
    R.drawable.twotitlebg,
    ANSWERS_2,
    3,
    TestApp.LAST_QUESTION_2
)