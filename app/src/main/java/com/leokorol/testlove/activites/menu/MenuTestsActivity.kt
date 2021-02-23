package com.leokorol.testlove.activites.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.tests.TestOneQuestions
import com.leokorol.testlove.activites.tests.TestThreeQuestions
import com.leokorol.testlove.activites.tests.TestTwoQuestions
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.utils.replaceActivity
import kotlinx.android.synthetic.main.activity_tests_menu.*

class MenuTestsActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_tests_menu)
        deleteProgressListeners()
        clickListeners()
        checkLastSession()

    }

    private fun deleteProgressListeners() {
        tvSessionDeleteTest1.setOnClickListener {
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_1, 0)?.apply()
            tvSessionInfoTest1.text = "Текущий прогресс: 0/60"
        }

        tvSessionDeleteTest2.setOnClickListener {
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_2, 0)?.apply()
            tvSessionInfoTest2.text = "Текущий прогресс: 0/45"
        }

        tvSessionDeleteTest3.setOnClickListener {
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION_3, 0)?.apply()
            tvSessionInfoTest3.text = "Текущий прогресс: 0/40"
        }
    }

    private fun checkLastSession() {

        val lastSession = TestApp.sharedPref?.getString(TestApp.SESSiON_CODE, "")

        if (lastSession!!.isNotEmpty()) {

            val lastQuestion1 = TestApp.sharedPref?.getInt(TestApp.LAST_QUESTION_1, 0)
            val lastQuestion2 = TestApp.sharedPref?.getInt(TestApp.LAST_QUESTION_2, 0)
            val lastQuestion3 = TestApp.sharedPref?.getInt(TestApp.LAST_QUESTION_3, 0)

            tvSessionInfoTest1.text = "Текущий прогресс: $lastQuestion1/60"
            tvSessionInfoTest2.text = "Текущий прогресс: $lastQuestion2/45"
            tvSessionInfoTest3.text = "Текущий прогресс: $lastQuestion3/40"

            val database = FirebaseDatabase.getInstance()
            database.reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //                  val database = FirebaseDatabase.getInstance()
//                    val sessionsRef = database.getReference("sessions")

                    // TODO забирать данные по ответам тут

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

}

