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
    public MathGame(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        drawThread.touch(event, v);

        int x = (int)event.getX();
        int y = (int)event.getY();

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(),getHolder());

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


class Ans {
    String ans;
    float x;
    float y;
    double sx, sy;
    public Ans(String a, float x, float y, double sx, double sy) {
        this.ans = a;
        this.y = y;
        this.x = x;
        this.sx = sx;
        this.sy = sy;
    }
    public Ans(Ans another) {
        this.ans = another.ans;
        this.y = another.y;
        this.x = another.x;
        this.sx = another.sx;
        this.sy = another.sy;
    }
}

class Task {
    String task;
    String ans;
    float posx;
    float posy;
    int color;
    boolean lock;
    public Task(String task, String ans, float posx, float posy, int color) {
        this.ans = ans;
        this.task = task;
        this.posy = posy;
        this.posx = posx;
        this.color = color;
        this.lock = false;
    }
    public Task(Task another) {
        this.ans = another.ans;
        this.task = another.task;
        this.posy = another.posy;
        this.posx = another.posx;
        this.color = another.color;
        this.lock = another.lock;
    }
}