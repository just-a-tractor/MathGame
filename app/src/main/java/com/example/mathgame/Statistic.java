package com.example.mathgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Statistic extends AppCompatActivity implements View.OnClickListener {

    ResultDBHelper resultDBHelper;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        resultDBHelper = new ResultDBHelper(getApplicationContext(), new String[] {"easy_mode", "medium_mode", "hard_mode", "impossible_mode"});
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(this);
        ListView lv = findViewById(R.id.ListView);
        StatisticAdapter adapter = new StatisticAdapter(this, makeResult());
        lv.setAdapter(adapter);
    }

    private String readResults(ResultDBHelper resultDBHelper, String table_name, String column){
        SQLiteDatabase database = resultDBHelper.getReadableDatabase();
        Cursor cursor = resultDBHelper.readResults(database, table_name);
        StringBuilder info = new StringBuilder();
        while (cursor.moveToNext()){
            String foo = Double.toString(cursor.getDouble(cursor.getColumnIndex(column)));
            info.append(foo).append(" ");
        }

        return info.toString();
    }

    public static double sum(String[] array) {

        double[] array1 = new double[array.length];
        for (int i = 0; i < array.length; i++) {

            try {
                array1[i]=(int)Double.parseDouble(array[i]);
            }
            catch (java.lang.NumberFormatException ignored) {}
        }


        double s = 0;
        for (double i: array1){
            s += i;
        }
        return s;
    }

    ModeResult[] makeResult() {

        ModeResult[] arr = new ModeResult[4];



        String[] headArr = {"Easy_mode", "Medium_mode", "Hard_mode", "Impossible_mode"};
        String[] qArr = new String[4];
        String[] mistakesArr = new String[4];

        NumberFormat formatter = new DecimalFormat("#0.00");

        for(int i = 0; i < 4; i++) {
            String a = headArr[i].toLowerCase();
            double param = sum(readResults(resultDBHelper, a, "Q").split(" "));
            qArr[i] = String.valueOf((int)param);
            mistakesArr[i] = formatter.format((sum(readResults(resultDBHelper, a, "mistakes").split(" "))/param)*100);
        }

        for (int i = 0; i < arr.length; i++) {
            ModeResult res = new ModeResult();
            res.head = headArr[i];
            res.q = "Correctly resolved tasks: " + qArr[i];
            res.mistakes = "Mistakes percent: " + ((mistakesArr[i].equals("не число") || mistakesArr[i].equals("NaN")) ? "0,00" : mistakesArr[i]) + "%";
            res.speed = "Average speed: " + formatter.format(60 * resultDBHelper.getStatistics(headArr[i].toLowerCase())) + " tasks per minute.";
            arr[i] = res;
        }
        for (ModeResult i: arr)
        System.out.println(i);
        return arr;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.clear) {

            new AlertDialog.Builder(this)
                    .setTitle("Permanently delete")
                    .setMessage("Are you sure? This is irreversibly.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            resultDBHelper.onUpgrade(resultDBHelper.getReadableDatabase(), 0, 1);
                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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
