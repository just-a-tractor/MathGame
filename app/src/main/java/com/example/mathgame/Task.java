package com.example.mathgame;

import android.graphics.Canvas;
import android.graphics.Paint;

class Task {
    private String task;
    String solved_task;
    String ans;
    int color;
    boolean lock;
    private float x, y;
    double x1, x2; // for miss
    double h_x, h_y; // for hit

    void setPaint(Paint paint) {
        this.paint = paint;
    }

    private Paint paint;
    boolean miss = false;
    boolean hit = false;
    boolean direction = false; // true is right
    float def_x, def_y; // default position
    int times = 0;
    boolean should_draw = true;

    Task(String task, String ans, Paint paint, int color, float x, float y) {
        this.ans = ans;
        this.task = task;
        this.color = color;
        this.paint = paint;
        this.lock = false;
        this.solved_task = task.substring(0, task.length() - 1) + ans;
        this.x = x;
        this.y = y;
        def_x = x;
        def_y = y;
    }

    Task(Task another) {
        this.ans = another.ans;
        this.task = another.task;
        this.y = another.y;
        this.x = another.x;
        this.color = another.color;
        this.solved_task = task.substring(0, task.length() - 1) + ans;
        this.lock = another.lock;
        this.paint = another.paint;
        this.hit = another.hit;
        this.miss = another.miss;
        this.def_x = another.def_x;
        this.def_y = another.def_y;
        this.x1 = another.x1;
        this.x2 = another.x2;
        this.h_x = another.h_x;
        this.h_y = another.h_y;
        this.direction = another.direction;
        this.times = another.times;
    }

    void draw(Canvas canvas) {
        if (miss) {
            if (x == def_x && y == def_y) {
                if (!direction)
                    x = (float) (x1/2);
                else
                    x = (float) (x2/2);
            } else if (x == (float) x1/2) {
                if (!direction)
                    x = (float) x1;
                else
                    x = def_x;
            } else if (x == (float) x1) {
                x = (float) x1/2;
                direction = true;
            } else if (x == (float) x2/2) {
                if (!direction) {
                    x = def_x;
                    times += 1;
                }
                else
                    x = (float) x2;
            } else if (x == (float) x2) {
                x = (float) x2/2;
                direction = false;
            }
            if (times >= 2) {
                miss = false;
                should_draw = false;
            }
        }

        if (hit) {
            h_y = canvas.getHeight()-canvas.getHeight()/50 - def_y;
            int n = (int) (15*(canvas.getHeight()/10)/226.0);
            float speed_cnst = (float) (h_y/n);
            float size_cnst = 2;

            if (Math.round(y) == Math.round(def_y + h_y)) {
                hit = false;
                should_draw = false;
            }
            else for (int i = 1; i<n; i++) {
                if (Math.round(y) == Math.round(def_y + (float)i*speed_cnst)){
                    y += speed_cnst;
                    paint.setTextSize(paint.getTextSize()-size_cnst);
                    break;
                }
            }
            if (y == def_y) {
                y = def_y + speed_cnst;
            }
        }

        paint.setColor(this.color);
        if (!this.lock)
            canvas.drawText(this.task, x, y, paint);
        else
            canvas.drawText(this.solved_task, x, y, paint);
    }
}
