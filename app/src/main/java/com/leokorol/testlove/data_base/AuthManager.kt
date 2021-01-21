package com.leokorol.testlove.data_base

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.base_listeners.IAnswersReceivedListener
import com.leokorol.testlove.base_listeners.ISimpleListener
import com.leokorol.testlove.model.AnswerVariant
import java.util.*

class AuthManager {
    //    private String _deviceId;
    //    private String _code;
    private var _sessionCode: String? = null
    var isInQueue = false
        private set
    var isConnectedToPartner = false
        private set
    private var _currentPart = 0
    private var _isSubscribedToSessions = false
    private var _partnerConnectedListener: ISimpleListener? = null
    private var _answers1ReceivedListener: IAnswersReceivedListener? = null
    private var _answers2ReceivedListener: IAnswersReceivedListener? = null
    private var _answers3ReceivedListener: IAnswersReceivedListener? = null
    fun setPartnerConnectedListener(listener: ISimpleListener?) {
        _partnerConnectedListener = listener
    }

    fun setAnswers1ReceivedListener(listener: IAnswersReceivedListener?) {
        _answers1ReceivedListener = listener
    }

    fun setAnswers2ReceivedListener(listener: IAnswersReceivedListener?) {
        _answers2ReceivedListener = listener
    }

    fun setAnswers3ReceivedListener(listener: IAnswersReceivedListener?) {
        _answers3ReceivedListener = listener
    }

    var currentPart: Int
        get() = _currentPart
        set(currentPart) {
            _currentPart = currentPart
            TestApp.sharedPref?.edit()
                ?.putInt(TestApp.LAST_PART, _currentPart)?.apply()
        }

    fun sendAnswers(answers: Array<Array<AnswerVariant>>, answersBranch: String?) {
        val selfAnswers: MutableList<List<Any>> = ArrayList()
        val database = FirebaseDatabase.getInstance()
        val sessionsRef = database.getReference("sessions")
        val sessionRef = sessionsRef.child(_sessionCode!!)
        val answersRef = sessionRef.child(answersBranch!!)
        val idRef = answersRef.child(deviceId)
        for (iQuestion in answers.indices) {
            selfAnswers.add(getAnswerNumbers(answers[iQuestion]))
        }
        idRef.setValue(selfAnswers)
    }

    private fun getAnswerNumbers(answers: Array<AnswerVariant>): List<Any> {
        val result: MutableList<Any> = ArrayList()
        for (i in answers.indices) {
            if (answers[i].isChecked) {
                result.add(i)
            }
        }
        return result
    }

    fun subscribeToSessions() {
        if (_isSubscribedToSessions) {
            return
        }
        val database = FirebaseDatabase.getInstance()
        val sessionsRef = database.getReference("sessions")
        sessionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists() || isConnectedToPartner) {
                    return
                }
                for (snapshot in dataSnapshot.children) {
                    val sessionCode = snapshot.key
                    if (sessionCode!!.contains(code)) {
                        _sessionCode = sessionCode
                        sessionsRef.child(sessionCode).child("user1").setValue(231)
                        if (_partnerConnectedListener != null) {
                            _partnerConnectedListener!!.eventOccured()
                        }
                        subscribeToSession()
                        isConnectedToPartner = true
                        sessionsRef.child(sessionCode).child("user1").removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        _isSubscribedToSessions = true
    }

    private fun subscribeToSession() {
        val database = FirebaseDatabase.getInstance()
        val sessionsRef = database.getReference("sessions")
        val sessionRef = sessionsRef.child(_sessionCode!!)
        val answers1Ref = sessionRef.child("answers")
        val answers2Ref = sessionRef.child("answers2")
        val answers3Ref = sessionRef.child("answers3")
        answers1Ref.addValueEventListener(AnswersListener("answers", _answers1ReceivedListener))
        answers2Ref.addValueEventListener(AnswersListener("answers2", _answers2ReceivedListener))
        answers3Ref.addValueEventListener(AnswersListener("answers3", _answers3ReceivedListener))
    }

    private inner class AnswersListener(
        private val _answersBranch: String,
        private val _listener: IAnswersReceivedListener?
    ) : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (!dataSnapshot.exists()) {
                return
            }
            val database = FirebaseDatabase.getInstance()
            val sessionsRef = database.getReference("sessions")
            val sessionRef = sessionsRef.child(_sessionCode!!)
            val answersRef = sessionRef.child(_answersBranch)
            val isFinishedRef = sessionRef.child("isFinished")
            val obj = dataSnapshot.value
            if (obj is HashMap<*, *>) {
                val hm = obj as HashMap<String, Any>
                if (hm.size == 2) {
                    isFinishedRef.setValue(true)
                    answersRef.removeValue()
                    isFinishedRef.removeValue()
                    var selfAnswers: List<List<Any>> = ArrayList()
                    var partnerAnswers: List<List<Any>> = ArrayList()
                    for (key in hm.keys) {
                        if (key == deviceId) {
                            selfAnswers = hm[key] as List<List<Any>>
                        } else {
                            partnerAnswers = hm[key] as List<List<Any>>
                        }
                    }
                    _listener?.answersReceived(selfAnswers, partnerAnswers)
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    }

    fun tryMoveToSession(
        partnerCode: String,
        successListener: ISimpleListener?,
        failListener: ISimpleListener?
    ) {
        val database = FirebaseDatabase.getInstance()
        val queueRef = database.getReference("queue")
        val sessionsRef = database.getReference("sessions")
        _sessionCode = null
        isConnectedToPartner = false
        isInQueue = true
        queueRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return
                }
                var found = false
                var foundId: String? = null
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.key
                    val code = snapshot.value as String?
                    if (partnerCode.equals(code, ignoreCase = true)) {
                        found = true
                        foundId = id
                        break
                    }
                }
                if (found) {
                    _sessionCode = code + "_" + partnerCode
                    TestApp.sharedPref?.edit()?.putString(TestApp.SESSiON_CODE, _sessionCode)?.apply()
                    queueRef.child(deviceId).removeValue()
                    queueRef.child(foundId!!).removeValue()
                    sessionsRef.child(_sessionCode!!).child("user2").setValue(123)
                    successListener?.eventOccured()
                    sessionsRef.child(_sessionCode!!).child("user2").removeValue()
                } else {
                    failListener?.eventOccured()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    val deviceId: String
        get() = TestApp.sharedPref?.getString(TestApp.DEVICE_ID, "") ?: ""
    val code: String
        get() = TestApp.sharedPref?.getString(TestApp.CODE, "") ?: ""

    companion object {
        val instance = AuthManager()
    }
}