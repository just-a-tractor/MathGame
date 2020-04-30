package com.example.mathgame;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MathGame extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    Context context;
    Vibrator v;
    int n;
    int bound;
    boolean minus;
    public MathGame(Context context, int n, int bound, boolean minus) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        this.n = n;
        this.bound = bound;
        this.minus = minus;
    }

    public boolean onTouchEvent(MotionEvent event) {
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        drawThread.touch(event, v);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(),getHolder(),n, bound, minus);

        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }
}


