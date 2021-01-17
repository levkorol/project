package com.leokorol.testlove.activites.base_activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.activites.results.WaitForPartner
import com.leokorol.testlove.data_base.AuthManager
import com.leokorol.testlove.model.AnswerVariant
import com.leokorol.testlove.tests.recycler_view.AnswerVariantAdapter
import com.leokorol.testlove.tests.texts.QuestionWithVariants


open class BaseTestQuestionsActivity(
    private val _questions: Array<QuestionWithVariants>,
    private val _layoutId: Int,
    private val _backgroundResource: Int,
    private val _answersBranch: String
) : AppCompatActivity() {
    private var _recyclerView: RecyclerView? = null
    private var _textViewQuestionText: TextView? = null
    private var _textViewNumberQuestion: TextView? = null
    private lateinit var _allAnswerVariants: Array<Array<AnswerVariant>>
    private var _currentQuestionIndex = 0
    private val _currentPart = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layoutId)

//        _allAnswerVariants = arrayOfNulls(_questions.size)
//        for (iQuestion in 0 until _questions.size) {
//            val answerVariants: Array<String> = _questions[iQuestion].answerVariants
//            _allAnswerVariants[iQuestion] = arrayOfNulls(answerVariants.size)
//
//            for (iAnswer in answerVariants.indices) {
//                _allAnswerVariants[iQuestion]?.set(iAnswer, AnswerVariant(answerVariants[iAnswer]))
//            }
//        }

        _recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        _textViewQuestionText = findViewById<View>(R.id.textViewQuestionText) as TextView
        _textViewNumberQuestion = findViewById<View>(R.id.textViewNumberQuestion) as TextView
        val llm = LinearLayoutManager(this)
        _recyclerView!!.layoutManager = llm
        goToQuestion(0)

    }

    fun goToMenuActivity(view: View?) {
        val intent = Intent(this, MenuTestsActivity::class.java)
        startActivity(intent)
    }

    private val countOfCheckedAnswers: Int
        get() {
            var result = 0
            for (element in _allAnswerVariants[_currentQuestionIndex]!!) {
                if (element?.isChecked == true) {
                    result++
                }
            }
            return result
        }

    fun goToNextQuestion(view: View?) {
        if (countOfCheckedAnswers == 0) {
            val toast = Toast.makeText(
                applicationContext,
                "Выберите 1-2 варианта ответа",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else if (_currentQuestionIndex == _questions.size - 1) {
            AuthManager.instance.sendAnswers(_allAnswerVariants, _answersBranch)
            val intent = Intent(this, WaitForPartner::class.java)
            intent.putExtra("Background", _backgroundResource)
            startActivity(intent)
        } else {
            _currentQuestionIndex++
            TestApp.sharedPref?.edit()?.putInt(TestApp.LAST_QUESTION, _currentQuestionIndex)
                ?.apply()
            goToQuestion(_currentQuestionIndex)
        }
    }

    private fun goToQuestion(numberQuestion: Int) {
        val ava = AnswerVariantAdapter(_allAnswerVariants[_currentQuestionIndex])
        _recyclerView!!.adapter = ava
        ava.notifyDataSetChanged()
        _textViewQuestionText!!.text = _questions[numberQuestion].question
        _textViewNumberQuestion!!.text =
            (_currentQuestionIndex + 1).toString() + "/" + _questions.size
    }

    companion object {
        const val ANSWERS = "answers"
        const val ANSWERS_2 = "answers2"
        const val ANSWERS_3 = "answers3"
    }

    init {
        _allAnswerVariants = Array(_questions.size) { emptyArray() }
        for (iQuestion in _questions.indices) {
            val answerVariants = _questions[iQuestion].answerVariants
            _allAnswerVariants[iQuestion] =
                Array(answerVariants.size) { i -> AnswerVariant(answerVariants[i]) }
        }
    }
}