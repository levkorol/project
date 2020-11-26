package com.leokorol.testlove.activites.menu

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.MenuActivity
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.activites.results.ResultsActivity
import com.leokorol.testlove.activites.tests.TestOneQuestions
import com.leokorol.testlove.activites.tests.TestThreeQuestions
import com.leokorol.testlove.activites.tests.TestTwoQuestions
import com.leokorol.testlove.base.IAnswersReceivedListener
import com.leokorol.testlove.fire_base.AuthManager
import com.leokorol.testlove.tests.texts.Results
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
        goMenuActivity?.setOnClickListener { goMenu() }


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
            progressPart1!!.text = "Текущий прогресс: $lastQuestion/65"
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

    fun goToTestOneTitle(view: View?) {
        AuthManager.instance.currentPart = 1
        val intent = Intent(this, TestOneQuestions::class.java)
        startActivity(intent)
    }

    fun goToTestTwoActivity(view: View?) {
        AuthManager.instance.currentPart = 2
        val intent = Intent(this, TestTwoQuestions::class.java)
        startActivity(intent)
    }

    fun goToTestThreeActivity(view: View?) {
        AuthManager.instance.currentPart = 3
        val intent = Intent(this, TestThreeQuestions::class.java)
        startActivity(intent)
    }

    private fun goMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
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