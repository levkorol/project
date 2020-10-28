package com.leokorol.testlove;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HoroscopeItemAdapter  extends RecyclerView.Adapter<HoroscopeItemAdapter.HoroscopeItemViewHolder> {
    private HoroscopeItem[] _horoscopeItems;

    public HoroscopeItemAdapter(HoroscopeItem[] horoscopeItems) {
        _horoscopeItems = horoscopeItems;
    }

    @NonNull
    @Override
    public HoroscopeItemAdapter.HoroscopeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_horoscope_item, viewGroup, false);
        HoroscopeItemAdapter.HoroscopeItemViewHolder hivh = new HoroscopeItemViewHolder(view);
        return hivh;
    }

    @Override
    public void onBindViewHolder(@NonNull final HoroscopeItemAdapter.HoroscopeItemViewHolder horoscopeItemViewHolder, int horoscopeItemIndex) {
        HoroscopeItem hi = _horoscopeItems[horoscopeItemIndex];
        horoscopeItemViewHolder._textViewZodiacName.setText(hi.getZodiacName());
        horoscopeItemViewHolder._textViewPrediction.setText(hi.getPrediction());
    }

    @Override
    public int getItemCount() {
        return _horoscopeItems.length;
    }

    public static class HoroscopeItemViewHolder extends RecyclerView.ViewHolder {
        TextView _textViewZodiacName;
        TextView _textViewPrediction;

        HoroscopeItemViewHolder(View itemView) {
            super(itemView);
            _textViewZodiacName = itemView.findViewById(R.id.textViewZodiacName);
            _textViewPrediction = itemView.findViewById(R.id.textViewPrediction);
        }
    }
}