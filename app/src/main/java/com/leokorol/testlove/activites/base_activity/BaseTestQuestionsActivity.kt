package com.leokorol.testlove.activites.base_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.activites.menu.MenuLauncherActivity
import com.leokorol.testlove.activites.menu.MenuTestsActivity
import com.leokorol.testlove.data_base.AuthManager2
import com.leokorol.testlove.model.AnswerVariant
import com.leokorol.testlove.tests.recycler_view.AnswerVariantAdapter
import com.leokorol.testlove.tests.texts.QuestionWithVariants
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast


open class BaseTestQuestionsActivity(
    private val _questions: Array<QuestionWithVariants>,
    private val _layoutId: Int,
    private val _backgroundResource: Int,
    private val _answersBranch: String,
    private val numberTest: Int,
    private val lastQuestion: String,
) : AppCompatActivity() {
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _textViewQuestionText: TextView
    private lateinit var _textViewNumberQuestion: TextView
    private lateinit var _allAnswerVariants: Array<Array<AnswerVariant>>
    private var _currentQuestionIndex = 0

    private val countOfCheckedAnswers: Int
        get() {
            var result = 0
            for (element in _allAnswerVariants[_currentQuestionIndex]) {
                if (element.isChecked) {
                    result++
                }
            }
            return result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layoutId)
        _recyclerView = findViewById(R.id.recyclerView)
        _textViewQuestionText = findViewById(R.id.textViewQuestionText)
        _textViewNumberQuestion = findViewById(R.id.textViewNumberQuestion)
        val llm = LinearLayoutManager(this)
        _recyclerView.layoutManager = llm


        _currentQuestionIndex = TestApp.sharedPref?.getInt(lastQuestion, 0) ?: 0

        goToQuestion(0)


    }

    fun goToMenuActivity(view: View?) {
        replaceActivity(MenuTestsActivity())
    }



    fun goToNextQuestion(view: View?) {
        // if( ) {       //todo если вопрос последний по клику перебрасывать в MenuTestsActivity()
        if (countOfCheckedAnswers == 0) {
            showToast("Выберите 1-2 варианта ответа")
        } else {
            val answerSet = mutableSetOf<String>()
            for (i in _allAnswerVariants[_currentQuestionIndex].indices) {
                if (_allAnswerVariants[_currentQuestionIndex][i].isChecked) {
                    answerSet.add(i.toString())
                }
            }
            AuthManager2.saveAnswer(numberTest, _currentQuestionIndex, answerSet)
            if (_currentQuestionIndex == _questions.size - 1) {
                //AuthManager.instance.sendAnswers(_allAnswerVariants, _answersBranch)
                AuthManager2.copyAnswersFromPrefsToDatabase(numberTest, _questions.size)
                val intent = Intent(this, MenuLauncherActivity::class.java)
                intent.putExtra("Background", _backgroundResource)
                startActivity(intent) //todo активити с информацией после завершения

                //todo database.addCompleteListeners{ }  слушать ответы
            } else {
                _currentQuestionIndex++
                TestApp.sharedPref?.edit()?.putInt(lastQuestion, _currentQuestionIndex)?.apply()
                goToQuestion(_currentQuestionIndex)
            }
        }
//    else {
//            replaceActivity(MenuTestsActivity())
//            showToast("Вы прошли тест. Когда ваш партнер закончит результаты появятся в категории Результаты под тестом")
//        }
    }

    private fun goToQuestion(numberQuestion: Int) {
        val ava = AnswerVariantAdapter(_allAnswerVariants[_currentQuestionIndex])
        _recyclerView.adapter = ava
        ava.notifyDataSetChanged()
        _textViewQuestionText.text = _questions[numberQuestion].question
        _textViewNumberQuestion.text =
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