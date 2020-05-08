package com.example.mathgame;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class StatisticAdapter extends ArrayAdapter<ModeResult> {

    StatisticAdapter(Context context, ModeResult[] arr) {
        super(context, R.layout.adapter_item, arr);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ModeResult md = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.text1)).setText(md.head);
        ((TextView) convertView.findViewById(R.id.text2)).setText(md.q);
        ((TextView) convertView.findViewById(R.id.text3)).setText(md.speed);
        ((TextView) convertView.findViewById(R.id.text4)).setText(md.mistakes);
        return convertView;
    }
}
