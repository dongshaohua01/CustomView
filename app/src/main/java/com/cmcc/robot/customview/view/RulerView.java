package com.cmcc.robot.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.cmcc.robot.customview.R;

/**
 * 直尺
 */
public class RulerView extends View {

     //直尺的高度
     private int rulerHeight;
     private  int RULERHEIGHT = 50;
     //直尺的长度
     private int rulerWidth;
     private int RULERWIDTH = 150;
     //相邻刻度线的距离
     private int distance;
     private int DISTANCE = 5;
     //厘米刻度线的高度
     private int cmHeight;
     private int CMHEIGHT = 30;
     //5毫米刻度线的高度
     private int mmHeight;
     private int MMHEIGHT = 20;
     //毫米刻度线的高度
     private int mHeight;
     private int MHEIGHT = 15;
     //刻度线的颜色
     private int aLineColor;
     private String ALINECOLR = "#000000";
     //刻度线距离左右边线的距离
     private int lrDistance;
     private int LRDISTANCE = 20;

     private Paint mPaint;

    public RulerView(Context context) {
        super(context);
    }

    public RulerView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }


    private void initView(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
        rulerHeight = a.getInteger(R.styleable.RulerView_rulerHeight,RULERHEIGHT);
        rulerWidth = a.getInteger(R.styleable.RulerView_rulerWidth,RULERWIDTH);
        distance = a.getInteger(R.styleable.RulerView_distance,DISTANCE);
        cmHeight = a.getInteger(R.styleable.RulerView_cmHeight,CMHEIGHT);
        mmHeight = a.getInteger(R.styleable.RulerView_mmHeight,MMHEIGHT);
        mHeight = a.getInteger(R.styleable.RulerView_mHeight,MHEIGHT);
        aLineColor = a.getColor(R.styleable.RulerView_aLineColor,Color.parseColor(ALINECOLR));
        lrDistance = a.getInteger(R.styleable.RulerView_lrDistance,LRDISTANCE);
        a.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        drawOutRectangle(canvas,height,width);
        //计算可以绘制多少 mm
        int index = ((width - (lrDistance * 2)) / distance ) / 10;
        canvas.translate(lrDistance,0);
        for(int i = 0; i <= index * 10 ; i++ )
        {
            if(i % 5 == 0 && i % 10 != 0)
            {
                canvas.drawLine(distance,0,distance,mmHeight,mPaint);
            }else if(i % 5 == 0 && i % 10 == 0)
            {
                canvas.drawLine(distance,0,distance,cmHeight,mPaint);
                canvas.drawText(i / 10 +"",distance,cmHeight + 20,mPaint);
            }else {
                canvas.drawLine(distance,0,distance,mHeight,mPaint);
            }
            canvas.translate(distance,0);
        }
    }

    /**
     *  画外层矩形
     * @param canvas
     */
    private void drawOutRectangle(Canvas canvas,int height,int width){
         mPaint.setStyle(Paint.Style.STROKE);
         mPaint.setStrokeWidth(2);
         mPaint.setColor(aLineColor);
         Rect rect = new Rect(0,0,width,height);
        canvas.drawRect(rect,mPaint);
    }



}
