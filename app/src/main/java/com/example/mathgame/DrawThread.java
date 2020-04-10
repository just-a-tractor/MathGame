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
    private double sxz;
    private double syz;
    private float xz;
    private float yz;

    private float left;
    private float top;
    private float bottom;
    private float right;

    private Ans[] ans_list;
    private Task[] tasks;

    private double procent = 0.0;

    private void hit(Vibrator v) {
        v.vibrate(275);
    }


    private void miss(Vibrator v) {
        v.vibrate(50);
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
                    sxz = ans.sx;
                    syz = ans.sy;
                    xz = ans.x;
                    yz = ans.y;

                    ans.x = x;
                    ans.y = y;
                    ans.sx = 0;
                    ans.sy = 0;
                }
                catch (Exception ignored) {}

                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {
                    ans.x = x;
                    ans.y = y;
                }
                catch (Exception ignored) {}
                break;
            case MotionEvent.ACTION_UP: // отпускание

                try {

                    if (x > left && x < right-100 && y < bottom && y > top+150) {
                        ans.sx = sxz;
                        ans.sy = syz;
                    }

                    else {
                        for (int l = 0; l < tasks.length; l++) {

                            if (x >= tasks[l].posx - 50 && x <= tasks[l].posx + 400 &&
                                    y >= tasks[l].posy - 50 && y <= tasks[l].posy + 100) {

                                if (tasks[l].color == Color.RED && tasks[l].ans.equals(ans.ans)) {
                                    hit(v);
                                    tasks[l].task = tasks[l].task.substring(0, tasks[l].task.length() - 2) + tasks[l].ans;
                                    tasks[l].lock = true;
                                    tasks[l].color = Color.BLACK;

                                    for (int m = l + 1; m < tasks.length; m++) {
                                        if (!tasks[m].lock) {
                                            tasks[m].color = Color.RED;
                                            break;
                                        }
                                    }
                                    procent += (100.0 / tasks.length);
                                    break;
                                } else {
                                    if (tasks[l].color == Color.RED)
                                        if (!ans.ans.equals("")) miss(v);
                                    break;
                                }

                            }

                        }

                        ans.sx = sxz;
                        ans.sy = syz;
                        ans.x = xz;
                        ans.y = yz;

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


                    //canvas.drawBitmap(bitmap, smileX, smileY, backgroundPaint);

                    if (flag) {

                        String[] t_l = new String[]{"4 + 5 = __", "5 + 3 = __", "6 - 4 = __",
                                "9 - 2 = __", "2 + 6 = __", "7 - 4 = __"};
                        String[] t_l1 = new String[]{"9", "8", "2", "7", "8", "3"};
                        tasks = new Task[t_l.length];

                        if (t_l.length <= 4){for(int i = 0; i<t_l.length; i++){tasks[i] = new Task(t_l[i],
                                t_l1[i], left + canvas.getWidth()/5+canvas.getWidth()/200,
                                bottom + canvas.getHeight()/10 + (canvas.getHeight()/15)*i, Color.BLACK);}}

                        else if (t_l.length <= 8) {for(int i = 0; i<4; i++){tasks[i] = new Task(t_l[i],
                                t_l1[i], left,
                                bottom + canvas.getHeight()/10 + (canvas.getHeight()/15)*i, Color.BLACK);}
                            for(int j = 4; j<t_l.length; j++){tasks[j] = new Task(t_l[j],
                                    t_l1[j], left + canvas.getWidth()/3 + canvas.getWidth()/10,
                                    bottom + canvas.getHeight()/10 + (canvas.getHeight()/15)*(j-4), Color.BLACK);}}

                        tasks[0].color = Color.RED;


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

                    canvas.drawRect(left, top, right, bottom, rec);

                    canvas.drawRect(left,canvas.getHeight()-canvas.getHeight()/15 ,
                            (float)(left + ((right-left)/100*procent)),
                            canvas.getHeight()-canvas.getHeight()/50, pb1);

                    canvas.drawRect(left,canvas.getHeight()-canvas.getHeight()/15 ,right,
                            canvas.getHeight()-canvas.getHeight()/50, pb);

                    for (Task i:tasks) {

                        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        fontPaint.setTextSize(100);
                        fontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                        fontPaint.setColor(i.color);

                        canvas.drawText(i.task, i.posx, i.posy, fontPaint);
                    }

                    for (Ans i:ans_list) {
                        i.x += i.sx;
                        i.y += i.sy;

                        if (i.x > right-100 || i.x < left) {
                            i.sx = -i.sx;
                        }

                        if (i.y > bottom || i.y < top+150) {
                            i.sy = -i.sy;
                        }
                        canvas.drawText(i.ans, i.x, i.y, ansPaint);
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}