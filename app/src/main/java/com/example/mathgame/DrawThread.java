package com.example.mathgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Calendar;

class DrawThread extends Thread {


    private SurfaceHolder surfaceHolder;

    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();


    private int cheat;


    private boolean flag = true;

    private Ans ans;
    private float xz;
    private float yz;
    private float deltax;
    private float deltay;

    private int verbs = 0;
    private int mistakes = 0;

    private Context mainActivity;
    private int n;
    private int bound;
    private boolean minus;

    private float left;
    private float top;
    private float bottom;
    private float right;

    private Ans[] ans_list;
    private Task[] tasks;
    private Task show_task;
    private ArrayList<Ans> show_ans = new ArrayList<>();
    private java.util.Random random = new java.util.Random();

    private long currentTime;

    private ProgressBar prog;

    private void win(){
        Intent intent = new Intent(mainActivity, win.class);
        Bundle bundle = new Bundle();

        String difficult = !minus ? "easy_mode" : (bound == 10 ? "medium_mode" : (bound == 20 ? "hard_mode" : "impossible_mode"));

        bundle.putInt("1", verbs);
        bundle.putInt("2", mistakes);
        bundle.putDouble("3", (Calendar.getInstance().getTimeInMillis() - currentTime)/ 1000.0);
        bundle.putString("4", difficult);
        intent.putExtras(bundle);
        mainActivity.startActivity(intent);
        this.running = false;
    }

    private String[] task_gen(int n, int bound, boolean minus){

        String[] ans = new String[n];

        for (int i=0; i<n; i++){
            String sign = (minus ? (new String[]{"-", "+"}[random.nextInt(2)]) : "+");
            if (sign.equals("+")) {
                int c = random.nextInt(bound - 1) + 2;
                int a = random.nextInt(c - 1) + 1;
                int b = c - a;
                ans[i] = (a >= b ? a + " " + b + " " + sign : b + " " + a + " " + sign);
            }
            else {
                int a = random.nextInt(bound - 1) + 2;
                int c = random.nextInt(a - 1) + 1;
                int b = a - c;
                ans[i] = (b >= c ? b + " " + c + " " + sign : c + " " + b + " " + sign);
            }
        }
        return ans;
    }

    private void create(Canvas canvas, Paint pb, Paint pb1, int ans_speed) {
        prog = new ProgressBar(canvas, left, right, top, bottom, pb, pb1, 0.0);

        String[] raw = task_gen(n, bound, minus);

        String[] t_l = new String[n];
        String[] test_l = new String[n];

        ArrayList<String> t_l1 = new ArrayList<>();
        for (int a=0; a<n; a++) {
            String first = raw[a].split(" ")[0];
            String second = raw[a].split(" ")[1];
            String sign = raw[a].split(" ")[2];

            String ans1 = sign.equals("+") ? Integer.parseInt(first) + Integer.parseInt(second)+"" :
                    Integer.parseInt(first) - Integer.parseInt(second)+"";

            if (!t_l1.contains(ans1))
                t_l1.add(ans1);
            test_l[a] = ans1;
            t_l[a] = first + (first.length() == 1 ? " " : "") + sign +
                    (second.length() == 1 ? " " : "") + second + " = __";
        }
        tasks = new Task[n];
        String[] t_l1s = new String[t_l1.size()];
        t_l1s = t_l1.toArray(t_l1s);

        for (int i = 0; i<t_l.length; i++){
            tasks[i] = new Task(t_l[i], test_l[i], Color.BLACK,
                    left + canvas.getWidth()/50,
                    bottom + canvas.getHeight()/10 + (canvas.getHeight()/15));
        }

        show_task = new Task(tasks[random.nextInt(tasks.length)]);

        String[] a_l = t_l1s.clone();
        ans_list = new Ans[a_l.length];
        for(int i = 0; i<a_l.length; i++){
            double a = (Math.random()*ans_speed*2)-ans_speed;
            double b = (Math.random()*ans_speed*2)-ans_speed;

            if ((-0.5 > a && a < 0.5) && (-0.5 > b && b < 0.5)) {
                a = (Math.random()*ans_speed/2) + 1;
                b = (Math.random()*ans_speed/2) + 1;
            }
            ans_list[i] = new Ans(a_l[i], (int)(left + Math.random()*(right-100*a_l[i].length()-left)),
                    (int)(top + 150 + Math.random()*(bottom-top-150)), b, a);
        }

        for (Ans i:ans_list) {
            if(i.ans.equals(show_task.ans)) {
                show_ans.add(i);
                break;
            }
        }
        for (int i = 1; i<(ans_list.length > 5 ? 5 : ans_list.length); i++) {
            int r = random.nextInt(ans_list.length);
            if (!ans_list[r].ans.equals(show_task.ans) && !show_ans.contains(ans_list[r])) {
                show_ans.add(ans_list[r]);
            }
            else {
                i--;
            }
        }
        flag = false;
        currentTime = Calendar.getInstance().getTimeInMillis();
    }



    private void hit(Vibrator v, boolean change) {
        long[] mVibratePattern = new long[]{0, 50};
        v.vibrate(mVibratePattern, -1);
        if (change)
        prog.proc += (100.0 / tasks.length);
        verbs++;
    }


    private void miss(Vibrator v, boolean change) {
        long[] mVibratePattern = new long[]{0, 100, 50, 275};
        v.vibrate(mVibratePattern, -1);
        if (change)
        prog.proc -= (100.0 / tasks.length);
        mistakes++;
    }

    private void ret(Ans a, float x, float y) {
        a.sx = a.sx1;
        a.sy = a.sy1;
        a.x = x;
        a.y = y;
    }

    {
        backgroundPaint.setColor(Color.BLUE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }


    DrawThread(Context context, SurfaceHolder surfaceHolder, int n, int bound, boolean minus) {
        this.surfaceHolder = surfaceHolder;
        this.n = n;
        this.bound = bound;
        this.minus = minus;
        this.mainActivity = context;
    }


    void requestStop() {
        running = false;
    }


    void touch(MotionEvent event, Vibrator v) {

        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                if(cheat == 0){
                    if(x <= left && y <= top)
                    cheat = 1;
                }
                else if(cheat == 1){
                    if (x <= left && y <= top)
                        cheat = 2;
                    else
                        cheat=0;
                }
                else if(cheat == 2){
                    if (x >= right && y <= top)
                        cheat = 3;
                    else
                        cheat=0;
                }
                else if(cheat == 3){
                    if (x >= right && y <= top)
                        cheat = 4;
                    else
                        cheat=0;
                }
                else if(cheat == 4){
                    if (x <= left && y <= top) {
                        cheat = 0;
                        prog.proc = (prog.proc < 50 ? 50 : 100);
                        hit(v, false);
                    }
                    else
                        cheat = 0;
                }

                ans = null;

                for (Ans i:show_ans) {
                    if (x >= i.x-50 && x <= i.x+125*i.ans.length() &&
                            y >= i.y-150 && y <= i.y+75)
                    {
                        ans = i;
                        break;
                    }
                }
                if (ans == null || ans.ans.equals("")) {
                    break;
                }
                try {
                    deltax = ans.x - x;
                    deltay = ans.y - y;

                    if (ans.x > left && ans.x < right-100*ans.ans.length() && ans.y < bottom && ans.y > top+150) {
                        xz = ans.x;
                        yz = ans.y;
                    }

                    ans.x = x + deltax;
                    ans.y = y + deltay;
                    if (!ans.spring) {
                        ans.sx1 = ans.sx;
                        ans.sy1 = ans.sy;
                    }
                    ans.sx = 0;
                    ans.sy = 0;
                }
                catch (Exception ignored) {}

                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {

                    if (!ans.ans.equals("") && x >= left && x <= right &&
                            y >= bottom + top / 2 && y <= bottom + top*2.5) {
                        show_task.color = Color.RED;
                    }

                    if (!(!ans.ans.equals("") && x >= left && x <= right &&
                            y >= bottom + top / 2 && y <= bottom + top*2.5)) {
                        show_task.color = Color.BLACK;
                    }


                    ans.x = x + deltax;
                    ans.y = y + deltay;
                }
                catch (Exception ignored) {}
                break;
            case MotionEvent.ACTION_UP: // отпускание

                try {

                    if (ans.x > left && ans.x < right-100*ans.ans.length() && ans.y < bottom && ans.y > top+150) {
                        ans.sx = ans.sx1;
                        ans.sy = ans.sy1;
                    }

                    else {
                        if (x >= left && x <= right &&
                                y >= bottom + top / 2 && y <= bottom + top*2.5) {

                            if (!show_task.lock && show_task.ans.equals(ans.ans)) {
                                hit(v, true);
                                show_task.lock = true;
                                ret(ans, xz, yz);
                                throw new NullPointerException();
                            } else {
                                if (!ans.ans.equals("") && !show_task.lock) {
                                    miss(v, true);
                                    show_task.lock = true;
                                    ret(ans, xz, yz);
                                }
                                else ans.spring(xz, yz);
                                throw new NullPointerException();
                            }
                        }
                        ans.spring(xz, yz);
                    }
                }
                catch (Exception ignored) {}
                break;

            case MotionEvent.ACTION_CANCEL:
                System.out.println("СБОЙ");
                break;
        }
    }


    @Override
    public void run() {

        int ans_speed = 3;

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(175);

        Paint ansPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ansPaint.setTextSize(175);
        ansPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        ansPaint.getStrokeCap();
        ansPaint.setColor(Color.RED);

        Paint rec = new Paint(Paint.ANTI_ALIAS_FLAG);
        rec.setTextSize(100);
        rec.setStyle(Paint.Style.STROKE);
        rec.setColor(Color.GREEN);
        rec.setStrokeWidth(5);

        Paint pb = new Paint(Paint.ANTI_ALIAS_FLAG);
        pb.setStyle(Paint.Style.STROKE);
        pb.setColor(Color.BLACK);
        pb.setStrokeWidth(8);

        Paint pb1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        pb1.setStyle(Paint.Style.FILL);
        pb1.setColor(Color.GREEN);


        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

                    top = canvas.getHeight()/10;
                    bottom = canvas.getHeight()/2+canvas.getHeight()/10;
                    left = canvas.getWidth()/10;
                    right = canvas.getWidth()-canvas.getWidth()/10;


                    if (flag) {
                        create(canvas, pb, pb1, ans_speed);
                    }

                    canvas.drawRect(left, top, right, bottom, rec);

                    boolean w = prog.draw();
                    if(w)win();

                    show_task.draw(canvas, textPaint);
                    int a = random.nextInt(tasks.length);
                    if (tasks[a].solved_task.equals(show_task.solved_task)) {
                        a = random.nextInt(tasks.length);
                    }
                    if (show_task.lock) {
                        show_task = new Task(tasks[a]);
                        show_ans.clear();
                        for (Ans i:ans_list) {
                            if(i.ans.equals(show_task.ans)) {
                                show_ans.add(i);
                                break;
                            }
                        }
                        for (int i = 1; i<(ans_list.length > 5 ? 5 : ans_list.length); i++) {
                            int r = random.nextInt(ans_list.length);
                            if (!ans_list[r].ans.equals(show_task.ans) && !show_ans.contains(ans_list[r])) {
                                show_ans.add(ans_list[r]);
                            }
                            else {
                                i--;
                            }
                        }
                    }

                    for (Ans i: show_ans) {
                        i.draw(canvas, ansPaint, top, bottom, left, right);
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}