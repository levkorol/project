package com.leokorol.testlove.base_listeners

interface IAnswersReceivedListener {
    fun answersReceived(selfAnswers: List<List<Any>>, partnerAnswers: List<List<Any>>)
}