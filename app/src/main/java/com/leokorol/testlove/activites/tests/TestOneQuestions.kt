package com.leokorol.testlove.activites.tests

import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.base_activity.BaseTestQuestionsActivity
import com.leokorol.testlove.tests.texts.Questions

class TestOneQuestions : BaseTestQuestionsActivity(
    Questions.Part1,
    R.layout.activity_test_one,
    R.drawable.onetitlebg,
    ANSWERS,
    1,
    TestApp.LAST_QUESTION_1
)