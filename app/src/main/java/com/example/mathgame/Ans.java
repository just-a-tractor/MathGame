package com.example.mathgame;

public class Ans {
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
