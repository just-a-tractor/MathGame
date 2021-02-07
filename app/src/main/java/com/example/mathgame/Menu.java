package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Button main_btn;
    Button st_button;
    RadioGroup rg1;
    RadioGroup rg2;
    Typeface font;
    int n = 10;
    int bound = 10;
    boolean m = false;
    private boolean exit_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        //ResultDBHelper resultDBHelper = new ResultDBHelper(getApplicationContext(), new String[] {"easy", "medium", "hard", "maximum"});
        //resultDBHelper.onCreate(resultDBHelper.getReadableDatabase());
        main_btn = findViewById(R.id.main_btn);
        main_btn.setOnClickListener(this);
        st_button = findViewById(R.id.st_button);
        st_button.setOnClickListener(this);
        font = Typeface.createFromAsset(getAssets(), getString(R.string.font));
        main_btn.setTypeface(font);
        st_button.setTypeface(font);
        ((TextView)findViewById(R.id.textView1)).setTypeface(font);
        ((TextView)findViewById(R.id.textView2)).setTypeface(font);

        rg1 = findViewById(R.id.radio1);
        ((RadioButton)findViewById(R.id.radioButton1)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton2)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton3)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton4)).setTypeface(font);
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.exit);
                mediaPlayer.start();
                switch (checkedId) {
                    case R.id.radioButton1:
                        bound = 10;
                        m = false;
                        break;
                    case R.id.radioButton2:
                        bound = 10;
                        m = true;
                        break;
                    case R.id.radioButton3:
                        bound = 20;
                        m = true;
                        break;
                    case R.id.radioButton4:
                        bound = 100;
                        m = true;
                        break;
                    default:
                        break;
                }
            }
        });

        rg2 = findViewById(R.id.radio2);
        ((RadioButton)findViewById(R.id.radioButton1s)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton2s)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton3s)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton4s)).setTypeface(font);
        ((RadioButton)findViewById(R.id.radioButton5s)).setTypeface(font);
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.exit);
                mediaPlayer.start();

                switch (checkedId) {
                    case R.id.radioButton1s:
                        n = 10;
                        break;
                    case R.id.radioButton2s:
                        n = 20;
                        break;
                    case R.id.radioButton3s:
                        n = 30;
                        break;
                    case R.id.radioButton4s:
                        n = 40;
                        break;
                    case R.id.radioButton5s:
                        n = 50;
                        break;

                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btn) {
            Intent intent = new Intent(this, MainActivity.class);

            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.button2);
            mediaPlayer.start();


            Bundle bundle = new Bundle();
            bundle.putInt("stuff1", n);
            bundle.putInt("stuff2", bound);
            bundle.putBoolean("stuff3", m);
            intent.putExtras(bundle);

            startActivity(intent);
        }
        if (v.getId() == R.id.st_button) {
            Intent intent = new Intent(this, Statistic.class);
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed()
    {
        if (!exit_flag) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Are you sure?", Toast.LENGTH_SHORT);
            toast.show();
            exit_flag = true;
        }
        else {
            exit_flag = false;
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
