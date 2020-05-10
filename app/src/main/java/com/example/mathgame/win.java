package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class win extends AppCompatActivity implements View.OnClickListener {
    Button main_btn;
    Button send;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;

    int verbs;
    int mistakes;
    double time;
    String difficult;
    NumberFormat formatter;
    double avg;

    String info;


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


    private void writeResults(ResultDBHelper resultDBHelper, double time, int mistakes, int Q, String table_name){
        SQLiteDatabase database = resultDBHelper.getReadableDatabase();
        resultDBHelper.addResult(time, mistakes, Q, database, table_name);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);



        Bundle bundle = getIntent().getExtras();
        verbs = bundle.getInt("1");
        mistakes = bundle.getInt("2");
        time = bundle.getDouble("3");
        difficult = bundle.getString("4");


        ResultDBHelper resultDBHelper = new ResultDBHelper(getApplicationContext(), new String[] {"easy_mode", "medium_mode", "hard_mode", "impossible_mode"});

        // resultDBHelper.onUpgrade(resultDBHelper.getReadableDatabase(), 0, 1);

        writeResults(resultDBHelper, time, mistakes, verbs, difficult);

        //resultDBHelper.getReadableDatabase().execSQL("delete from "+ difficult);

        info = readResults(resultDBHelper, difficult);
        avg = resultDBHelper.getStatistics(difficult);
        resultDBHelper.close();

        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        text4 = findViewById(R.id.textView4);


        formatter = new DecimalFormat("#0.00");

        text1.setText((formatter.format(time) + " seconds spent."));
        text2.setText(((mistakes == 0 ? "No" : mistakes) + (mistakes == 1 ? " mistake" : " mistakes") + " made."));
        text3.setText(formatter.format(verbs * 60 / time) + " tasks per minute.");
        text4.setText(formatter.format(60 * avg) + " - average speed.");

        main_btn = findViewById(R.id.main_btn);
        main_btn.setOnClickListener(this);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btn) {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.send) {

            String send_text = difficult + "\n" + verbs + " correctly resolved tasks." + "\n" +
                    (formatter.format(time) + " seconds spent.") + "\n"
                    + (mistakes == 0 ? "No" : mistakes) + (mistakes == 1 ? " mistake" : " mistakes") + " made." + "\n" +
                    formatter.format(verbs * 60 / time) + " tasks per minute." + "\n" +
                    formatter.format(60 * avg) + " - average speed.";

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"alex.gif2003@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Last try result:");
            i.putExtra(Intent.EXTRA_TEXT, send_text);
            try {
                startActivity(Intent.createChooser(i, "Send result..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(win.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
