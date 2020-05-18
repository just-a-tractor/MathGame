package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MathGame mg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int n = bundle.getInt("stuff1");
        int bound = bundle.getInt("stuff2");
        boolean minus = bundle.getBoolean("stuff3");
        mg = new MathGame(this, n, bound, minus);
        setContentView(mg);
    }
    @Override
    public void onBackPressed()
    {
        mg.drawThread.requestStop();
        super.onBackPressed();
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
