package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class win extends AppCompatActivity implements View.OnClickListener {
    Button main_btn;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Bundle bundle = getIntent().getExtras();
        int verbs = bundle.getInt("1");
        int mistakes = bundle.getInt("2");
        long time = bundle.getLong("3");

        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);

        text1.setText((verbs + " correctly resolved tasks."));
        text2.setText((mistakes + " mistakes made."));
        text3.setText((time / 1000.0 + " seconds spent"));
        text4.setText("you cool");

        main_btn = (Button) findViewById(R.id.main_btn);
        main_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
