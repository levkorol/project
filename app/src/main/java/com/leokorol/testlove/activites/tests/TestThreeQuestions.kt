package com.leokorol.testlove.activites.tests

import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.base_activity.BaseTestQuestionsActivity
import com.leokorol.testlove.tests.texts.Questions

class TestThreeQuestions : BaseTestQuestionsActivity(
    Questions.Part3,
    R.layout.activity_test_three_questions,
    R.drawable.threetitlebg,
    ANSWERS_3,
    2,
    TestApp.LAST_QUESTION_3
)