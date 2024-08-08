package com.example.zadatak2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class AnalogClockView extends View {
    private Paint paint;
    private int width, height;
    private int padding = 50;
    private int numeralSpacing = 20;
    private int handTruncation, hourHandTruncation;
    private int radius;
    private boolean isInit; // za inicijalizaciju sata
    private Handler handler;

    public AnalogClockView(Context context) {
        super(context);
        initClock();
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock();
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClock();
    }

    private void initClock() {
        paint = new Paint();
        handler = new Handler();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        int min = Math.min(width, height);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            return;
        }

        canvas.drawColor(Color.WHITE);

        int centerX = width / 2;
        int centerY = height / 2;

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        // Crtanje sata
        canvas.drawCircle(centerX, centerY, radius + padding - 10, paint);

        //Kazaljke na satu
        paint.setStrokeWidth(8);
        for (int i = 0; i < 12; i++) {
            int angle = i * 30;
            double angleRad = Math.toRadians(angle);
            int startX = (int) (centerX + Math.cos(angleRad) * radius);
            int startY = (int) (centerY + Math.sin(angleRad) * radius);
            int stopX = (int) (centerX + Math.cos(angleRad) * (radius - numeralSpacing));
            int stopY = (int) (centerY + Math.sin(angleRad) * (radius - numeralSpacing));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // Postavljanje vremena
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        drawHandLine(canvas, centerX, centerY, (hour + minute / 60.0) * 5f, true, false); // hour hand
        drawHandLine(canvas, centerX, centerY, minute, false, false); // minute hand
        drawHandLine(canvas, centerX, centerY, second, false, true); // second hand

        // Odstupanje
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 1000);
    }

    private void drawHandLine(Canvas canvas, int centerX, int centerY, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        if (isSecond) {
            paint.setColor(Color.RED);
            paint.setStrokeWidth(4);
        } else {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8);
        }
        int endX = (int) (centerX + Math.cos(angle) * handRadius);
        int endY = (int) (centerY + Math.sin(angle) * handRadius);
        canvas.drawLine(centerX, centerY, endX, endY, paint);
    }
}
