package com.leokorol.testlove.activites.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.results.ResultsActivity
import com.leokorol.testlove.activites.tests.TestOneQuestions
import com.leokorol.testlove.activites.tests.TestThreeQuestions
import com.leokorol.testlove.activites.tests.TestTwoQuestions
import com.leokorol.testlove.base.IAnswersReceivedListener
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.tests.texts.Results
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_tests_menu.*
import java.util.*

class MenuTestsActivity : AppCompatActivity() {
    private var _llPartner: LinearLayout? = null
    private var progressPart1: TextView? = null
    private var deleteProgress: TextView? = null
    private var goMenuActivity: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_tests_menu)
        _llPartner = findViewById(R.id.partner)
        progressPart1 = findViewById(R.id.tvSessionInfoTest1)
        goMenuActivity = findViewById(R.id.goMenuActivity)
        deleteProgress = findViewById(R.id.tvSessionDeleteTest1)
        //    goMenuActivity?.setOnClickListener { goMenu() }


        clickListeners()

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
        checkLastSession()
    }


    private fun checkLastSession() {
        val lastSession = TestApp.sharedPref?.getString(TestApp.SESSiON_CODE, "")
        if (!lastSession!!.isEmpty()) {
            val lastQuestion = TestApp.sharedPref?.getInt(TestApp.LAST_QUESTION, 0)
            tvSessionInfoTest1!!.text = "Текущий прогресс: $lastQuestion/65"
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
        goMenuActivity?.setOnClickListener { replaceActivity(MenuActivity()) }

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

    fun goToTestOneTitle() {
        AuthManager.instance.currentPart = 1
        val intent = Intent(this, TestOneQuestions::class.java)
        startActivity(intent)
    }

    fun goToTestTwoActivity() {
        AuthManager.instance.currentPart = 2
        val intent = Intent(this, TestTwoQuestions::class.java)
        startActivity(intent)
    }

    fun goToTestThreeActivity() {
        AuthManager.instance.currentPart = 3
        val intent = Intent(this, TestThreeQuestions::class.java)
        startActivity(intent)
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

//private fun clickListeners() {
//    goMenuActivity.setOnClickListener { replaceActivity(MenuActivity()) }
//
//    testOneButton.setOnClickListener {
//        goToTestOneTitle()
//    }
//
//    testTwoButton.setOnClickListener {
//        goToTestTwoActivity()
//    }
//
//    testThreeButton.setOnClickListener {
//        goToTestThreeActivity()
//    }
//
//}

//private fun goToTestOneTitle() {
//    AuthManager.instance.currentPart = 1
//    val intent = Intent(this, TestOneQuestions::class.java)
//    startActivity(intent)
//}
//
//private fun goToTestTwoActivity() {
//    AuthManager.instance.currentPart = 2
//    val intent = Intent(this, TestTwoQuestions::class.java)
//    startActivity(intent)
//}
//
//private fun goToTestThreeActivity() {
//    AuthManager.instance.currentPart = 3
//    val intent = Intent(this, TestThreeQuestions::class.java)
//    startActivity(intent)
//}