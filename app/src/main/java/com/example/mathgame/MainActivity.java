package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int n = bundle.getInt("stuff1");
        int bound = bundle.getInt("stuff2");
        boolean minus = bundle.getBoolean("stuff3");
        setContentView(new MathGame(this, n, bound, minus));
    }
}
