package com.leokorol.testlove.tests.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.leokorol.testlove.R
import com.leokorol.testlove.model.AnswerVariant
import com.leokorol.testlove.tests.recycler_view.AnswerVariantAdapter.AnswerVariantViewHolder
import java.util.*

class AnswerVariantAdapter(private val _answerVariants: Array<AnswerVariant>) :
    RecyclerView.Adapter<AnswerVariantViewHolder>() {
    private val _lastCheckedVariants = LinkedList<AnswerVariant>()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AnswerVariantViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_answer_variant, viewGroup, false)
        return AnswerVariantViewHolder(v)
    }

    override fun onBindViewHolder(
        answerVariantViewHolder: AnswerVariantViewHolder,
        answerVariantIndex: Int
    ) {
        val av = _answerVariants[answerVariantIndex]
        answerVariantViewHolder.checkBoxVariant.text = av.answerText
        answerVariantViewHolder.checkBoxVariant.isChecked = av.isChecked
        answerVariantViewHolder.checkBoxVariant.setOnClickListener {
            av.isChecked = answerVariantViewHolder.checkBoxVariant.isChecked
            if (av.isChecked) {
                _lastCheckedVariants.addLast(av)
            } else {
                _lastCheckedVariants.remove(av)
            }
            while (_lastCheckedVariants.size > 2) {
                _lastCheckedVariants.removeFirst()
            }
            for (i in _answerVariants.indices) {
                val a = _answerVariants[i]
                if (!_lastCheckedVariants.contains(a)) {
                    a.isChecked = false
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return _answerVariants.size
    }

    class AnswerVariantViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var checkBoxVariant: CheckBox =
            itemView.findViewById<View>(R.id.checkBoxVariant) as CheckBox
    }
}