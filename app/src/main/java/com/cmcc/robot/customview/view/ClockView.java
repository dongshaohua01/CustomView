package com.cmcc.robot.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Calendar;

public class ClockView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final static int DEFAULT_RADIUS = 200;

    /**
     * 半径
     */
    private int radius = DEFAULT_RADIUS;

    /**
     * 当前时间的时、分、秒
     */
    private int hour, minute, second;
    /**
     * holder对象
     */
    private SurfaceHolder holder;
    /**
     * 是否开始绘制
     */
    private boolean flag;
    /**
     * 圆和刻度的画笔
     */
    private Paint paint;
    /**
     * 指针的画笔
     */
    private Paint pointerPaint;
    /**
     * 画布的宽和高
     */
    private int canvasWidth, canvasHeight;

    /**
     * 时、分刻度的长度
     */
    private int hourDegreeLength, secondDegreeLength;
    /**
     * 时分秒指针的长度
     */
    private int minutePointerLength, secondPointerLength, hourPointerLength;

    private OnTimeChangeListener onTimeChangeListener;
    private Canvas canvas;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取当前时分秒
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minute = Calendar.getInstance().get(Calendar.MINUTE);
        second = Calendar.getInstance().get(Calendar.SECOND);

        //获取holder对象，并设置回调
        holder = getHolder();
        holder.addCallback(this);
        //初始化画笔
        paint = new Paint();
        pointerPaint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        pointerPaint.setColor(Color.BLACK);
        pointerPaint.setAntiAlias(true);
        pointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointerPaint.setTextSize(22);
        pointerPaint.setTextAlign(Paint.Align.CENTER);

        //设置不可获取焦点
        setFocusable(false);
        setFocusableInTouchMode(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth, desiredHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize;
        } else {
            desiredWidth = radius * 2 + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(widthSize, desiredWidth);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize;
        } else {
            desiredHeight = radius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight);
            }
        }

        // +4是为了设置默认的2px的内边距，因为绘制时钟的圆的画笔设置的宽度是2px
        setMeasuredDimension(canvasWidth = desiredWidth + 4, canvasHeight = desiredHeight + 4);

        radius = (int) (Math.min(desiredWidth - getPaddingLeft() - getPaddingRight(), desiredHeight - getPaddingTop() - getPaddingBottom()) * 1.0f / 2);
        calculateLengths();
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        flag = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        flag = false;
    }

    @Override
    public void run() {
        long start, end;
        while (flag) {
            start = System.currentTimeMillis();
            draw();
            logic();
            end = System.currentTimeMillis();

            try {
                if (end - start < 1000) {
                    Thread.sleep(1000 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 时间进度的逻辑
     */
    private void logic() {
        second++;
        if (second == 60) {
            second = 0;
            minute++;
            if (minute == 60) {
                minute = 0;
                hour++;
                if (hour == 24) {
                    hour = 0;
                }
            }
        }
        handler.sendEmptyMessage(0);

    }

    /**
     * 绘制
     */
    private void draw() {
        try {
            //获取画布，并锁定
            canvas = holder.lockCanvas();
            if (canvas != null) {
                //开始绘制,刷屏
                canvas.drawColor(Color.WHITE);
                //将坐标原点移动到去掉内边距的画布中心
                canvas.translate(canvasWidth * 1.0f / 2 + getPaddingLeft() - getPaddingRight(), canvasHeight * 1.0f / 2 + getPaddingTop() - getPaddingBottom());
                //设置画笔为2个宽度，绘制圆形
                paint.setStrokeWidth(2f);
                canvas.drawCircle(0, 0, radius, paint);
                //绘制时刻度，0-12个时刻，圆为360度，那么每个为30度
                for (int i = 0; i < 12; i++) {
                    canvas.drawLine(0, radius, 0, radius - hourDegreeLength, paint);
                    canvas.rotate(30);
                }
                //绘制秒刻度，一共60秒，那么没秒6度，并且时刻度已经绘制过了就不需要再绘制了
                paint.setStrokeWidth(1.5f);
                for (int i = 0; i < 60; i++) {
                    if (i % 5 != 0) {
                        canvas.drawLine(0, radius, 0, radius - secondDegreeLength, paint);
                    }
                    canvas.rotate(6);
                }
                //绘制数字
                pointerPaint.setColor(Color.BLACK);
                for (int i = 0; i < 12; i++) {
                    String number = (6 + i) < 12 ? String.valueOf(6 + i) : (6 + i) > 12 ? String.valueOf(i - 6) : "12";
                    canvas.drawText(number, 0, radius * 5.5f / 7, pointerPaint);
                    canvas.rotate(30);
                }
                //绘制上下午,判断当前时间是否大于小于12
                canvas.drawText(hour < 12 ? "AM" : "PM", 0, radius * 1.5f / 4, pointerPaint);
                //绘制指针
                Path path = new Path();
                path.moveTo(0, 0);
                int[] hourPointerCoordinates = getPointerCoordinates(hourPointerLength);
                path.lineTo(hourPointerCoordinates[0], hourPointerCoordinates[1]);
                path.lineTo(hourPointerCoordinates[2], hourPointerCoordinates[3]);
                path.lineTo(hourPointerCoordinates[4], hourPointerCoordinates[5]);
                path.close();
                canvas.save();
                canvas.rotate(180 + hour % 12 * 30 + minute * 1.0f / 60 * 30);
                canvas.drawPath(path, pointerPaint);
                canvas.restore();
                //绘制分针
                path.reset();
                path.moveTo(0, 0);
                int[] minutePointerCoordinates = getPointerCoordinates(minutePointerLength);
                path.lineTo(minutePointerCoordinates[0], minutePointerCoordinates[1]);
                path.lineTo(minutePointerCoordinates[2], minutePointerCoordinates[3]);
                path.lineTo(minutePointerCoordinates[4], minutePointerCoordinates[5]);
                path.close();
                canvas.save();
                canvas.rotate(180 + minute * 6);
                canvas.drawPath(path, pointerPaint);
                canvas.restore();
                //绘制秒针
                pointerPaint.setColor(Color.RED);
                path.reset();
                path.moveTo(0, 0);
                int[] secondPointerCoordinates = getPointerCoordinates(secondPointerLength);
                path.lineTo(secondPointerCoordinates[0], secondPointerCoordinates[1]);
                path.lineTo(secondPointerCoordinates[2], secondPointerCoordinates[3]);
                path.lineTo(secondPointerCoordinates[4], secondPointerCoordinates[5]);
                path.close();
                canvas.save();
                canvas.rotate(180 + (second+1) * 6);
                canvas.drawPath(path, pointerPaint);
                canvas.restore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://改变时间
                    if (onTimeChangeListener != null) {

                        onTimeChangeListener.onTimeChange(ClockView.this, hour, minute, second);
                    }
                    break;
            }
        }
    };

    /**
     * 获取指针坐标
     *
     * @param pointerLength 指针长度
     * @return int[]{x1,y1,x2,y2,x3,y3}
     */
    private int[] getPointerCoordinates(int pointerLength) {
        int y = (int) (pointerLength * 3.0f / 4);
        int x = (int) (y * Math.tan(Math.PI / 180 * 5));
        return new int[]{-x, y, 0, pointerLength, x, y};
    }

    /**
     * 计算指针和刻度的长度
     */
    private void calculateLengths() {
        hourDegreeLength = (int) (radius * 1.0f / 7);
        secondDegreeLength = (int) (hourDegreeLength * 1.0f / 2);

        // hour : minute : second = 1 : 1.25 : 1.5
        hourPointerLength = (int) (radius * 1.0 / 2);
        minutePointerLength = (int) (hourPointerLength * 1.25f);
        secondPointerLength = (int) (hourPointerLength * 1.5f);
    }


    //-----------------Setter and Getter start-----------------//
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = Math.abs(hour) % 24;
        if (onTimeChangeListener != null) {
            onTimeChangeListener.onTimeChange(this, hour, minute, second);
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = Math.abs(minute) % 60;
        if (onTimeChangeListener != null) {
            onTimeChangeListener.onTimeChange(this,  hour, minute, second);
        }
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = Math.abs(second) % 60;
        if (onTimeChangeListener != null) {
            onTimeChangeListener.onTimeChange(this,  hour, minute, second);
        }
    }

    public void setTime(Integer... time) {
        if (time.length > 3) {
            throw new IllegalArgumentException("the length of argument should bo less than 3");
        }
        if (time.length > 2)
            setSecond(time[2]);
        if (time.length > 1)
            setMinute(time[1]);
        if (time.length > 0)
            setHour(time[0]);
    }
    //-----------------Setter and Getter end-------------------//

    //************* interface *************

    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    /**
     * 当时间改变的时候提供回调的接口
     */
    public interface OnTimeChangeListener {
        /**
         * 时间发生改变时调用
         *
         * @param view   时间正在改变的view
         * @param hour   改变后的小时时刻
         * @param minute 改变后的分钟时刻
         * @param second 改变后的秒时刻
         */
        void onTimeChange(View view, int hour, int minute, int second);
    }
}
