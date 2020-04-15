package com.example.mathgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

class DrawThread extends Thread {


    private SurfaceHolder surfaceHolder;

    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();

    private boolean flag = true;

    private Ans ans;
    private float xz;
    private float yz;
    float deltax;
    float deltay;

    private float left;
    private float top;
    private float bottom;
    private float right;

    private Ans[] ans_list;
    private Task[] tasks;
    private Task show_task;
    java.util.Random random = new java.util.Random();

    ProgressBar prog;



    void create(Canvas canvas, Paint pb, Paint pb1, int ans_speed) {
        prog = new ProgressBar(canvas, left, right, top, bottom, pb, pb1, 0.0);

        String[] t_l = new String[]{"4 + 5 = __", "5 + 3 = __", "6 - 4 = __",
                "9 - 2 = __", "2 + 6 = __", "7 - 4 = __"};
        String[] t_l1 = new String[]{"9", "8", "2", "7", "8", "3"};
        tasks = new Task[t_l.length];


        for (int i = 0; i<t_l.length; i++){
            tasks[i] = new Task(t_l[i], t_l1[i], Color.BLACK,
                    left + canvas.getWidth()/50,
                    bottom + canvas.getHeight()/10 + (canvas.getHeight()/15));
        }

        show_task = new Task(tasks[random.nextInt(tasks.length)]);

        String[] a_l = new String[]{"9", "2", "7", "8", "3"};
        ans_list = new Ans[a_l.length];
        for(int i = 0; i<a_l.length; i++){
            double a = (Math.random()*ans_speed*2)-ans_speed;
            double b = (Math.random()*ans_speed*2)-ans_speed;

            if ((-0.5 > a && a < 0.5) && (-0.5 > b && b < 0.5)) {
                a = (Math.random()*ans_speed/2) + 1;
                b = (Math.random()*ans_speed/2) + 1;
            }
            ans_list[i] = new Ans(a_l[i], (int)(left + Math.random()*(right-100-left)),
                    (int)(top + 150 + Math.random()*(bottom-top-150)), b, a);
        }
        flag = false;
    }



    private void hit(Vibrator v) {
        v.vibrate(275);
        prog.proc += (100.0 / tasks.length);
    }


    private void miss(Vibrator v) {
        v.vibrate(50);
        prog.proc -= (100.0 / tasks.length);
    }

    void ret(Ans a, float x, float y) {
        a.sx = a.sx1;
        a.sy = a.sy1;
        a.x = x;
        a.y = y;
    }

    {
        backgroundPaint.setColor(Color.BLUE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }


    DrawThread(Context context, SurfaceHolder surfaceHolder) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.smile);
        this.surfaceHolder = surfaceHolder;
    }


    void requestStop() {
        running = false;
    }


    void touch(MotionEvent event, Vibrator v) {

        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие

                ans = null;

                for (Ans i:ans_list) {
                    if (x >= i.x-50 && x <= i.x+125 &&
                            y >= i.y-150 && y <= i.y+75)
                    {
                        ans = i;
                        break;
                    }
                }

                try {
                    deltax = ans.x - x;
                    deltay = ans.y - y;

                    xz = ans.x + deltax;
                    yz = ans.y + deltay;

                    ans.x = x + deltax;
                    ans.y = y + deltay;
                    ans.sx = 0;
                    ans.sy = 0;
                }
                catch (Exception ignored) {}

                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {
                    ans.x = x + deltax;
                    ans.y = y + deltay;
                }
                catch (Exception ignored) {}
                break;
            case MotionEvent.ACTION_UP: // отпускание

                try {

                    if (x > left && x < right-100 && y < bottom && y > top+150) {
                        ans.sx = ans.sx1;
                        ans.sy = ans.sy1;
                    }

                    else {
                        if (x >= right-3*left && x <= right-left &&
                                    y >= show_task.y - 150 && y <= show_task.y + 75) {

                                if (!show_task.lock && show_task.ans.equals(ans.ans)) {
                                    hit(v);
                                    show_task.lock = true;
                                    ret(ans, xz, yz);
                                    throw new NullPointerException();
                                } else {
                                    if (!ans.ans.equals("") && !show_task.lock) {
                                        miss(v);
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
        textPaint.setTextSize(200);

        Paint ansPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ansPaint.setTextSize(200);
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

                    prog.draw();

                    show_task.draw(canvas, textPaint);
                    int a = random.nextInt(tasks.length);
                    if (tasks[a].solved_task.equals(show_task.solved_task)) {
                        a = random.nextInt(tasks.length);
                    }
                    if (show_task.lock)
                        show_task = new Task(tasks[a]);

                    for (Ans i:ans_list) {
                        i.draw(canvas, ansPaint, top, bottom, left, right);
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}