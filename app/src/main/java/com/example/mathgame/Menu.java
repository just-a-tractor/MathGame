package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Button main_btn;
    RadioGroup rg1;
    RadioGroup rg2;
    int n = 10;
    int bound = 10;
    boolean m = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        main_btn = (Button) findViewById(R.id.main_btn);
        main_btn.setOnClickListener(this);

        rg1 = (RadioGroup) findViewById(R.id.radio1);
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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

        rg2 = (RadioGroup) findViewById(R.id.radio2);
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            //Create the bundle
            Bundle bundle = new Bundle();

            bundle.putInt("stuff1", n);
            bundle.putInt("stuff2", bound);
            bundle.putBoolean("stuff3", m);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
