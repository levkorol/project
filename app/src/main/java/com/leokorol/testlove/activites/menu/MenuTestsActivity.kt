package com.leokorol.testlove.activites.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.results.ResultsActivity
import com.leokorol.testlove.activites.tests.TestOneQuestions
import com.leokorol.testlove.activites.tests.TestThreeQuestions
import com.leokorol.testlove.activites.tests.TestTwoQuestions
import com.leokorol.testlove.base_listeners.IAnswersReceivedListener
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.tests.texts.Results
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast
import kotlinx.android.synthetic.main.activity_tests_menu.*
import java.util.*

class MenuTestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_tests_menu)

        clickListeners()
        checkLastSession()

        AuthManager.instance.setAnswers1ReceivedListener(object : IAnswersReceivedListener {
            override fun answersReceived(
                selfAnswers: List<List<Any>>,
                partnerAnswers: List<List<Any>>
            ) {
                val resultsIntent = Intent(this@MenuTestsActivity, ResultsActivity::class.java)
                val equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers)
                val result = Results.getResultsPart1(equalAnswersCount)
                resultsIntent.putExtra("Background", R.drawable.onetitlebg)
                resultsIntent.putExtra("TestResults", result)
                this@MenuTestsActivity.startActivity(resultsIntent)
            }
        })

        AuthManager.instance.setAnswers2ReceivedListener(object : IAnswersReceivedListener {
            override fun answersReceived(
                selfAnswers: List<List<Any>>,
                partnerAnswers: List<List<Any>>
            ) {
                val resultsIntent = Intent(this@MenuTestsActivity, ResultsActivity::class.java)
                val equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers)
                val result = Results.getResultsPart2(equalAnswersCount)
                resultsIntent.putExtra("Background", R.drawable.twotitlebg)
                resultsIntent.putExtra("TestResults", result)
                this@MenuTestsActivity.startActivity(resultsIntent)
            }
        })
        AuthManager.instance.setAnswers3ReceivedListener(object : IAnswersReceivedListener {
            override fun answersReceived(
                selfAnswers: List<List<Any>>,
                partnerAnswers: List<List<Any>>
            ) {
                val resultsIntent = Intent(this@MenuTestsActivity, ResultsActivity::class.java)
                val equalAnswersCount = getEqualAnswersCount(selfAnswers, partnerAnswers)
                val result = Results.getResultsPart3(equalAnswersCount)
                resultsIntent.putExtra("Background", R.drawable.threetitlebg)
                resultsIntent.putExtra("TestResults", result)
                this@MenuTestsActivity.startActivity(resultsIntent)
            }
        })


    }

    private fun deleteProgress(lastQuestion: String) {
        tvSessionDeleteTest1.setOnClickListener {
            if (lastQuestion.isEmpty()) {
                showToast("Прогресс пустой")
            } else {

            }
        }
    }

    private fun checkLastSession() {

        val lastSession = TestApp.sharedPref?.getString(TestApp.SESSiON_CODE, "")

        if (lastSession!!.isNotEmpty()) {

            val lastQuestion = TestApp.sharedPref?.getInt(TestApp.LAST_QUESTION, 0)

            tvSessionInfoTest1.text = "Текущий прогресс: $lastQuestion/60"
            tvSessionInfoTest2.text =
                "Текущий прогресс: $lastQuestion/45"  //todo записывает общее значение
            tvSessionInfoTest3.text = "Текущий прогресс: $lastQuestion/40"

            tvSessionDeleteTest1.setOnClickListener { }

            val database = FirebaseDatabase.getInstance()
            database.reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val database = FirebaseDatabase.getInstance()
                    val sessionsRef = database.getReference("sessions")

                    // TODO забирать данные по ответам тут
//                    DatabaseReference sessionRef = sessionsRef.child(_sessionCode);
//                    DatabaseReference answersRef = sessionRef.child(_answersBranch);
//                    DatabaseReference isFinishedRef = sessionRef.child("isFinished");
                    val obj = snapshot.value
                    if (obj is HashMap<*, *>) {
                        val hm = obj as HashMap<String, Any>
                        if (hm.size == 2) {
//                            isFinishedRef.setValue(true);
//                            answersRef.removeValue();
//                            isFinishedRef.removeValue();
                            val selfAnswers: List<List<Any>> = ArrayList()
                            val partnerAnswers: List<List<Any>> = ArrayList()
                            for (key in hm.keys) {
//                                if (key.toString().equals(getDeviceId())) {
//                                    selfAnswers = (List<List<Object>>)hm.get(key);
//                                } else {
//                                    partnerAnswers = (List<List<Object>>)hm.get(key);
//                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun clickListeners() {
        goMenuActivity?.setOnClickListener { replaceActivity(MenuLauncherActivity()) }

        testOneButton.setOnClickListener {
            goToTestOneTitle()
        }

        testTwoButton.setOnClickListener {
            goToTestTwoActivity()
        }

        testThreeButton.setOnClickListener {
            goToTestThreeActivity()
        }
    }

    private fun goToTestOneTitle() {
        AuthManager.instance.currentPart = 1
        replaceActivity(TestOneQuestions())
    }

    private fun goToTestTwoActivity() {
        AuthManager.instance.currentPart = 2
        replaceActivity(TestTwoQuestions())
    }

    private fun goToTestThreeActivity() {
        AuthManager.instance.currentPart = 3
        replaceActivity(TestThreeQuestions())
    }

    companion object {
        private fun getEqualAnswersCount(
            selfAnswers: List<List<Any>>,
            partnerAnswers: List<List<Any>>
        ): Int {
            var result = 0
            for (i in selfAnswers.indices) {
                val self = selfAnswers[i]
                val partner = partnerAnswers[i]
                val intersection: MutableList<Any> = ArrayList(self)
                intersection.retainAll(partner)
                result += intersection.size
            }
            return result
        }
    }
}

