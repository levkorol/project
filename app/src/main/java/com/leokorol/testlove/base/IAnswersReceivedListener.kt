package com.leokorol.testlove.base

interface IAnswersReceivedListener {
    fun answersReceived(selfAnswers: List<List<Any>>, partnerAnswers: List<List<Any>>)
}