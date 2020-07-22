package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MathGame mg;
    int length = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int n = bundle.getInt("stuff1");
        int bound = bundle.getInt("stuff2");
        boolean minus = bundle.getBoolean("stuff3");
        length = bundle.getInt("stuff4");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.relaxing1);
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        mg = new MathGame(this, n, bound, minus, mediaPlayer);
        setContentView(mg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
    }

    @Override
    public void onBackPressed()
    {
        mg.drawThread.requestStop();
        super.onBackPressed();
        Bundle bundle = new Bundle();
        length = mediaPlayer.getCurrentPosition();
        bundle.putInt("stuff", length);
        Intent intent = new Intent(this, Menu.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
