package com.example.mathgame;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Task {
    String task, solved_task, ans;
    int color;
    boolean lock;
    float x, y;
    public Task(String task, String ans, int color, float x, float y) {
        this.ans = ans;
        this.task = task;
        this.color = color;
        this.lock = false;
        this.solved_task = task.substring(0, task.length() - 2) + ans;
        this.x = x;
        this.y = y;
    }
    public Task(Task another) {
        this.ans = another.ans;
        this.task = another.task;
        this.y = another.y;
        this.x = another.x;
        this.color = another.color;
        this.solved_task = task.substring(0, task.length() - 2) + ans;
        this.lock = another.lock;
    }
    void draw(Canvas canvas, Paint paint) {
        paint.setColor(this.color);
        if (!this.lock)
            canvas.drawText(this.task, x, y, paint);
        else
            canvas.drawText(this.solved_task, x, y, paint);
    }
}
