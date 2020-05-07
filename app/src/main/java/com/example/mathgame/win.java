package com.example.mathgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class win extends AppCompatActivity implements View.OnClickListener {
    Button main_btn;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;


    private String readResults(ResultDBHelper resultDBHelper, String table_name){
        SQLiteDatabase database = resultDBHelper.getReadableDatabase();
        Cursor cursor = resultDBHelper.readResults(database, table_name);
        StringBuilder info = new StringBuilder();
        while (cursor.moveToNext()){
            String time = Double.toString(cursor.getDouble(cursor.getColumnIndex("time")));
            String mistakes = Integer.toString(cursor.getInt(cursor.getColumnIndex("mistakes")));
            info.append("\n\nTime: ").append(time).append("\nMistakes: ").append(mistakes);
        }

        return info.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeResults(ResultDBHelper resultDBHelper, double time, int mistakes, int Q, String table_name){
        SQLiteDatabase database = resultDBHelper.getReadableDatabase();
        resultDBHelper.addResult(time, mistakes, Q, database, table_name);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);



        Bundle bundle = getIntent().getExtras();
        int verbs = bundle.getInt("1");
        int mistakes = bundle.getInt("2");
        double time = bundle.getDouble("3");
        String difficult = bundle.getString("4");
        System.out.println(time);


        ResultDBHelper resultDBHelper = new ResultDBHelper(getApplicationContext(), new String[] {"easy_mode", "medium_mode", "hard_mode", "impossible_mode"});

        // resultDBHelper.onUpgrade(resultDBHelper.getReadableDatabase(), 0, 1);

        writeResults(resultDBHelper, time, mistakes, verbs, difficult);

        //resultDBHelper.getReadableDatabase().execSQL("delete from "+ difficult);

        String info = readResults(resultDBHelper, difficult);
        resultDBHelper.close();

        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        text4 = findViewById(R.id.textView4);


        NumberFormat formatter = new DecimalFormat("#0.00");

        text1.setText((formatter.format(time) + " seconds spent.") );
        text2.setText(((mistakes == 0 ? "No" : mistakes) + (mistakes == 1 ? " mistake" : " mistakes") + " made."));
        text3.setText(formatter.format(verbs * 60 / time) + " tasks per minute.");
        text4.setText(formatter.format(60 * resultDBHelper.getStatistics(difficult)) + " - average speed.");

        main_btn = findViewById(R.id.main_btn);
        main_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
