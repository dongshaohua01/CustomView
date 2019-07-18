package com.cmcc.robot.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cmcc.robot.customview.R;

/**
 * 进度条
 */
public class ProgressBarView extends View {

    //左边背景色
    private static final int BACKGROUP_LEFT = Color.parseColor("#FF4081");
    //右边背景色
    private static final int BACKGROUP_RIGHT = Color.parseColor("#303F9F");
    //进度条默认的高度
    private static final float DEFAULT_PROGRESS_HEIGHT =120f;
    //文字的大小
    private static final float DEFAULT_TEXT_SIZE = 50;
    /**
     * 收益进度条左右两边margin大小
     */
    private static final int MARGIN_SIZE = 20;
    private Context context;

    //左右俩边进度条的距离
    private float marge = 20;
    /**
     * 右边画笔
     */
    private Paint backgroundPaint;
    /**
     * 左边画笔
     */
    private Paint progressPaint;
    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    /**
     * 背景的宽度
     */
    private int view_background_width;
    /**
     * 背景的高度
     */
    private float view_background_height = DEFAULT_PROGRESS_HEIGHT;
    /**
     *  左边百分比
     */
    private String date = "";
    /**
     * 右边百分比
     */
    private String rightData="";

    /**
     * 左边进度
     */
    private float leftProgress ;
    /**
     * 右边进度
     */
    private float rightProgress;
    //背景色
    private int progress_back_color = BACKGROUP_LEFT;
    //字的颜色
    private int text_color = BACKGROUP_RIGHT;
    //字的大小
    private float TEXT_SIZE = DEFAULT_TEXT_SIZE;
    public ProgressBarView(Context context) {
        super(context);
       // initView(context);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = this.context.obtainStyledAttributes(attrs,R.styleable.ProfitProgerssBar);
        progress_back_color = typedArray.getColor(R.styleable.ProfitProgerssBar_progress_backg_color,BACKGROUP_LEFT);
        text_color = typedArray.getColor(R.styleable.ProfitProgerssBar_progress_text_color,BACKGROUP_RIGHT);
        TEXT_SIZE = typedArray.getDimension(R.styleable.ProfitProgerssBar_progress_text_size,DEFAULT_TEXT_SIZE);
        backgroundPaint = new Paint();
        backgroundPaint.setStrokeWidth(10);
        backgroundPaint.setColor(progress_back_color);
        backgroundPaint.setDither(true);
        backgroundPaint.setAntiAlias(true);
        progressPaint = new Paint();
        progressPaint.setStrokeWidth(10);
        progressPaint.setDither(true);
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(BACKGROUP_RIGHT);
        textPaint = new Paint();
        textPaint.setStrokeWidth(10);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TEXT_SIZE);
        /*DisplayMetrics d = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(d);
        view_background_width = d.widthPixels;*/

    }
    private  float  left = 0;//左边绘制的距离
    private  float rights = 0;//右边绘制的距离
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(left < leftProgress || rights < rightProgress)
            {
                left += leftProgress / 8;
                rights += rightProgress / 8;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(500);
                postInvalidate();
            }else {

            }
        }
    };
    /**
     * 初始化 进度条
     * @param date
     * @param leftProgress
     * @param rightProgress
     */
    public void init(String date, float leftProgress, float rightProgress, String rightData){
        this.date = date;
        this.leftProgress = leftProgress;
        this.rightProgress = rightProgress;
        this.rightData = rightData;
        handler.sendEmptyMessage(500);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //得到View的高度  宽度
        view_background_height = this.getMeasuredHeight();
        view_background_width = this.getMeasuredWidth();

        //画左边圆
        float radius = view_background_height / 2;
        canvas.drawCircle(radius,radius,radius,backgroundPaint);
        RectF mRectF = new RectF( radius,radius,view_background_width,view_background_height);
     //   canvas.drawRect(mRectF,-180,-180,backgroundPaint);
       //画左边矩形
        RectF r = new RectF();
        //左边进度条坐标点
        float leftOval = view_background_width * left;
        r.left = radius;
        r.top = 0;
        r.right = leftOval;
        r.bottom = view_background_height  ;////------------------------
        canvas.drawRect(r, backgroundPaint);
        //绘制左边文字
        textPaint.setColor(Color.parseColor("#ffffff"));
        Rect r2 = new Rect();
        textPaint.getTextBounds(date,0,date.length(),r2);
        canvas.drawText(date, leftOval - r2.width() * 2, (view_background_height-r2.top)/2, textPaint);
        //画左边三角
        Path path = new Path();
        path.moveTo(leftOval, 0);// 此点为多边形的起点
        path.lineTo(leftOval + radius / 2, 0);
        path.lineTo(leftOval, view_background_height);
        path.close();
        canvas.drawPath(path,backgroundPaint);

        //画右边圆
        canvas.drawCircle(view_background_width - radius ,radius,radius,progressPaint);
        //画右边矩形
        RectF right = new RectF();
        //矩形左边坐标距离
        float rectLeft = view_background_width - view_background_width * rights ;
        right.left = rectLeft ;
        right.top = 0;
        right.right = view_background_width - radius;
        right.bottom = view_background_height  ;////------------------------
        canvas.drawRect(right, progressPaint);
        //绘制文字
        textPaint.setColor(Color.parseColor("#ffffff"));
        Rect r3 = new Rect();
        textPaint.getTextBounds(rightData,0,rightData.length(),r3);
        canvas.drawText(rightData, rectLeft  + radius , (view_background_height-r2.top)/2, textPaint);//日期
        //画三角
        Path path1 = new Path();
        path1.moveTo(rectLeft, 0);// 此点为多边形的起点
        path1.lineTo(rectLeft - radius / 2, view_background_height);
        path1.lineTo(rectLeft, view_background_height);
        path1.close();
        canvas.drawPath(path1,progressPaint);

        invalidate();
    }
}
