package com.leokorol.testlove.data_base

import com.google.firebase.database.FirebaseDatabase
import com.leokorol.testlove.TestApp

object AuthManager2 {

    private val database = FirebaseDatabase.getInstance()

    fun connectToPartner(partnerCode: String) {
        val myCode = TestApp.sharedPref?.getString(TestApp.CODE, "")
        val deviceIdRef = myCode?.let { database.getReference(it).child("partner") }
        deviceIdRef?.setValue(partnerCode)
    }

    fun saveAnswer(testNumber: Int, questionNumber: Int, answerNumbers: Set<String>) {
        TestApp.sharedPref?.edit()
            ?.putStringSet("answer_${testNumber}_${questionNumber}", answerNumbers)?.apply()
    }

    fun copyAnswersFromPrefsToDatabase(testNumber: Int, questionsCount: Int) {
        val answersRef = database.getReference(TestApp.getUserCode()).child("test_${testNumber}")
        for (i in 0 until questionsCount) {
            val answers = TestApp.sharedPref?.getStringSet("answer_${testNumber}_${i}", null)
            if (answers != null) {
                answersRef.child(i.toString()).setValue(answers.map { it.toInt() })
            }
        }
    }
}