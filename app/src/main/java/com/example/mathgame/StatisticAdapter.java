package com.example.mathgame;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class StatisticAdapter extends ArrayAdapter<ModeResult> {
    private Typeface font;
    StatisticAdapter(Context context, ModeResult[] arr) {
        super(context, R.layout.adapter_item, arr);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ModeResult md = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

// Заполняем адаптер
        font = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font));

        TextView[] lst = new TextView[] {(TextView) convertView.findViewById(R.id.text1),
                (TextView) convertView.findViewById(R.id.text2),
                (TextView) convertView.findViewById(R.id.text2s),
                (TextView) convertView.findViewById(R.id.text3),
                (TextView) convertView.findViewById(R.id.text3s),
                (TextView) convertView.findViewById(R.id.text4),
                (TextView) convertView.findViewById(R.id.text4s)};
        lst[0].setText((md.head));
        String a1 = md.q.substring(0, md.q.indexOf(":")+1);
        String a2 = md.q.substring(md.q.indexOf(":")+1);
        lst[1].setText((a1));
        lst[2].setText((" " + a2));
        String b1 = md.speed.substring(0, md.speed.indexOf(":")+1);
        String b2 = md.speed.substring(md.speed.indexOf(":")+1);
        lst[3].setText((b1));
        lst[4].setText((b2));
        String c1 = md.mistakes.substring(0, md.mistakes.indexOf(":")+1);
        String c2 = md.mistakes.substring(md.mistakes.indexOf(":")+1);
        lst[5].setText((c1));
        lst[6].setText((c2));
        for (TextView i: lst) {
            i.setTypeface(font);
            i.setTextSize(20);
            i.setTextColor(Color.rgb(160, 255, 255));
            }
        lst[0].setTextSize(25);
        //lst[2].setTextColor(Color.rgb(0, 0, 120));
        //lst[4].setTextColor(Color.rgb(0, 0, 120));
        //lst[6].setTextColor(Color.rgb(0, 0, 120));

        return convertView;
    }
}
