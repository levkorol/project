package com.leokorol.testlove.tests.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leokorol.testlove.R;
import com.leokorol.testlove.model.AnswerVariant;

import java.util.LinkedList;

public class AnswerVariantAdapter extends RecyclerView.Adapter<AnswerVariantAdapter.AnswerVariantViewHolder> {
    private AnswerVariant[] _answerVariants;
    private LinkedList<AnswerVariant> _lastCheckedVariants = new LinkedList<>();

    public AnswerVariantAdapter(AnswerVariant[] answerVariants) {
        _answerVariants = answerVariants;
    }

    @NonNull
    @Override
    public AnswerVariantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_answer_variant, viewGroup, false);
        AnswerVariantViewHolder avvh = new AnswerVariantViewHolder(v);
        return avvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerVariantViewHolder answerVariantViewHolder, int answerVariantIndex) {
        final AnswerVariant av = _answerVariants[answerVariantIndex];
        answerVariantViewHolder.checkBoxVariant.setText(av.getAnswerText());
        answerVariantViewHolder.checkBoxVariant.setChecked(av.getIsChecked());
        answerVariantViewHolder.checkBoxVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.setIsChecked(answerVariantViewHolder.checkBoxVariant.isChecked());
                if (av.getIsChecked()) {
                    _lastCheckedVariants.addLast(av);
                } else {
                    _lastCheckedVariants.remove(av);
                }
                while (_lastCheckedVariants.size() > 2) {
                    _lastCheckedVariants.removeFirst();
                }
                for (int i = 0; i < _answerVariants.length; i++) {
                    AnswerVariant a = _answerVariants[i];
                    if (!_lastCheckedVariants.contains(a)) {
                        a.setIsChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _answerVariants.length;
    }

    public static class AnswerVariantViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxVariant;
        AnswerVariantViewHolder(View itemView) {
            super(itemView);
            checkBoxVariant = (CheckBox)itemView.findViewById(R.id.checkBoxVariant);
        }
    }
}
