package com.example.mathgame;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

class ProgressBar {
    Canvas canvas;
    double proc;
    float l;
    float r;
    float t;
    float b;
    Paint pb1;
    Paint pb2;
    public ProgressBar(Canvas c,float l, float r, float t, float b, Paint pb1, Paint pb2 ,double pr) {
        this.canvas = c;
        this.proc = pr;
        this.l = l;
        this.r = r;
        this.b = b;
        this.t = t;
        this.pb1 = pb1;
        this.pb2 = pb2;
    }
    boolean draw() {
        if(proc < 0)proc=0;
        canvas.drawRect(l,canvas.getHeight()-canvas.getHeight()/15 ,
                (float)(l + ((r-l)/100*proc)),
                canvas.getHeight()-canvas.getHeight()/50, pb2);

        canvas.drawRect(l,canvas.getHeight()-canvas.getHeight()/15 ,r,
                canvas.getHeight()-canvas.getHeight()/50, pb1);
        return proc >= 100;
    }
}
