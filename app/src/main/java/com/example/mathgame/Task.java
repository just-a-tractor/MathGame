package com.example.mathgame;

public class Task {
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
