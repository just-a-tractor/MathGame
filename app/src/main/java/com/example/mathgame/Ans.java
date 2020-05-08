package com.example.mathgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ans {
    String ans;
    private double width_one;
    private double height_one;
    float x, y; // normal cords
    float s_x, s_y; // spring cords
    double sx, sy; // present speed
    double sx1, sy1; // default speed
    boolean spring;
    public Ans(String a, float x, float y, double sx, double sy, double width_one, double height_one) {
        this.ans = a;
        this.y = y;
        this.x = x;
        this.sx = sx;
        this.sy = sy;
        this.sx1 = sx;
        this.sy1 = sy;
        this.height_one = height_one;
        this.width_one = width_one;
        this.spring = false;
    }

    void spring(float x, float y){
        spring = true;
        s_x = x;
        s_y = y;
        sx = width_one*30*(s_x-this.x)/Math.sqrt(Math.pow(s_x-this.x, 2) + Math.pow(s_y-this.y, 2));
        sy = width_one*30*(s_y-this.y)/Math.sqrt(Math.pow(s_x-this.x, 2) + Math.pow(s_y-this.y, 2));
    }

    void draw(Canvas canvas, Paint paint, float top, float bottom, float left, float right) {
        if (spring)  {
            if (x >= s_x-20*width_one && y >= s_y-20*height_one && x <= s_x+20*width_one && y <= s_y+20*height_one) {
                spring = false;
                s_x = 0;
                s_y = 0;
                sx = sx1;
                sy = sy1;
            }
         }

        if (!spring) {
            if (x > right - 100*width_one*ans.length() || x < left)
                sx = -sx;
            if (y > bottom || y < top + 150*height_one)
                sy = -sy;
        }
            x += sx;
            y += sy;
        canvas.drawText(ans, x, y, paint);
    }
    @Override
    public String toString() {
        return "ans: " + ans;
    }
}
