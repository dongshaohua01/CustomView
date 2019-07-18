package com.cmcc.robot.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.cmcc.robot.customview.R;

/**
 * 进度圆
 */
public class HabitCircularView extends View {


    /**
     * 圆弧的起始角度
     */
    private int startAngle;
    private static final int START_ANGLR = -90;
    /**
     * 圆形内部填充色
     */
    private int centerColor;
    private static final String CENTER_COLOR = "#eeff06";
    /**
     * 进度条的颜色
     */
    private int progressColor;
    private static final String PROGRESS_COLOR = "#FFDA0F0F";
    /**
     * 进度条背景色
     */
    private int ringColor;
    private static final String RING_COLOR = "#FF7281E1";
    /**
     * 文本颜色
     */
    private int textColor;
    private int topTextColor;
    private int bottomTextColor;
    private static final String TEXT_COLOR = "#FF000000";
    private boolean mOneTextColorFlag=true;
    /**
     * 文字大小
     */
    private int topTextSize;
    private int bottomTextSize;
    private static final int TEXT_SIZE = 30;
    /**
     * 文字是否需要显示
     */
    private boolean isTextDisplay;
    /**
     * 圆形内半径
     */
    private int radius;
    private static final int CIRCLE_RADIUS = 20;
    /**
     * 进度条的宽度
     */
    private int ringWidth;
    private static final int RING_WIDTH = 5;
    /**
     * 最外层圆环
      */
    private int ringOutWidth = -1;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 默认进度
     */
    private int mProgress = 0;

    private String topTextContent="";
    private String bottomTextContent="";

    public HabitCircularView(Context context) {
        super(context);
       // initView(context);
    }

    public HabitCircularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public HabitCircularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
        startAngle = a.getInteger(R.styleable.RoundProgressBar_startAngle,START_ANGLR);
        centerColor = a.getColor(R.styleable.RoundProgressBar_centerColor,Color.parseColor(CENTER_COLOR));
        progressColor = a.getColor(R.styleable.RoundProgressBar_progressColor,Color.parseColor(PROGRESS_COLOR));
        ringColor = a.getColor(R.styleable.RoundProgressBar_ringColor,Color.parseColor(RING_COLOR));
        textColor = a.getColor(R.styleable.RoundProgressBar_textColor,Color.parseColor(TEXT_COLOR));
        topTextColor = a.getColor(R.styleable.RoundProgressBar_topTextColor,Color.parseColor(TEXT_COLOR));
        bottomTextColor = a.getColor(R.styleable.RoundProgressBar_bottomTextColor,Color.parseColor(TEXT_COLOR));
        topTextSize = (int) a.getDimension(R.styleable.RoundProgressBar_topTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,TEXT_SIZE,getResources().getDisplayMetrics()));
        bottomTextSize = (int) a.getDimension(R.styleable.RoundProgressBar_bottomTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,TEXT_SIZE,getResources().getDisplayMetrics()));
        isTextDisplay = a.getBoolean(R.styleable.RoundProgressBar_isTextDisplay,true);
        radius = (int) a.getDimension(R.styleable.RoundProgressBar_radiusb,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,CIRCLE_RADIUS,getResources().getDisplayMetrics()));
        ringWidth = (int) a.getDimension(R.styleable.RoundProgressBar_ringWidth,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,RING_WIDTH,getResources().getDisplayMetrics()));
        ringOutWidth = (int) a.getDimension(R.styleable.RoundProgressBar_ringOutWidth,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,RING_WIDTH,getResources().getDisplayMetrics()));
        a.recycle();
        setPaint();
    }

    /**
     * 设置画笔
     */
    private void setPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 添加自定义需求
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取View本身的宽度 / 2 = 圆心的X坐标
        int cx = getWidth() / 2;
        //圆的半径相同 及 圆心的横纵坐标相同
        int cy = cx;
        if(centerColor != 0)
        {
            drawCenterColor(canvas,cx,cy);
        }

        drawOuterCircle(canvas,cx,cy);
        drawProgress(canvas,cx,cy);
        drawTextContent(canvas,cx,cy);
    }

    /**
     * 画内层圆
     * @param canvas
     * @param cx
     * @param cy
     */
    private void drawCenterColor(Canvas canvas, int cx, int cy){
       mPaint.setColor(centerColor);
       mPaint.setStyle(Paint.Style.FILL);
       canvas.drawCircle(cx,cy,radius,mPaint);
    }

    /**
     * 画外层圆
     * @param canvas
     * @param cx
     * @param cy
     */
    private void drawOuterCircle(Canvas canvas, int cx, int cy){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ringColor);
        mPaint.setStrokeWidth(ringWidth);
        canvas.drawCircle(cx,cy,radius,mPaint);

        if(ringOutWidth != -1)
        {
            mPaint.setColor(centerColor);
            mPaint.setStrokeWidth(ringOutWidth);
            canvas.drawCircle(cx,cy,radius+ringWidth/2,mPaint);
        }
    }

    /**
     * 画进度
     * @param canvas
     * @param cx
     * @param cy
     */
     private void drawProgress(Canvas canvas, int cx, int cy){
        mPaint.setColor(progressColor);
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF mRectF = new RectF(cx - radius,cy - radius,cx + radius,cy + radius);
        float sweepAngle = (float) (mProgress * 360.0 / 100);
        canvas.drawArc(mRectF,startAngle,startAngle,false,mPaint);
     }

    /**
     * 画文字内容
     * @param canvas
     * @param cx
     * @param cy
     */
     private void drawTextContent(Canvas canvas, int cx, int cy){
          if(!isTextDisplay)
          {
              return;
          }
          mPaint.setStyle(Paint.Style.FILL);
          if(mOneTextColorFlag)
          {
              mPaint.setColor(textColor);
          }else {
              mPaint.setColor(topTextColor);
          }
          mPaint.setTextSize(topTextSize);
          mPaint.setTypeface(Typeface.DEFAULT_BOLD);
          mPaint.setStrokeWidth(0);
          Rect boundtop = new Rect();
          if(!TextUtils.isEmpty(bottomTextContent))
          {
              //得到文本的宽高
               mPaint.getTextBounds(topTextContent,0,topTextContent.length(),boundtop);
               canvas.drawText(topTextContent,cx - boundtop.width() / 2, cy+20,mPaint);
               if(!mOneTextColorFlag){
                   mPaint.setColor(bottomTextColor);
               }
               mPaint.setTextSize(bottomTextSize);
               mPaint.setTypeface(Typeface.DEFAULT);
               Rect boundbottom = new Rect();
               mPaint.getTextBounds(bottomTextContent,0,bottomTextContent.length(),boundbottom);
               canvas.drawText(bottomTextContent,cx - boundbottom.width() / 2,cy+boundbottom.height()+40,mPaint);
          }else {
               mPaint.getTextBounds(topTextContent,0,topTextContent.length(),boundtop);
               canvas.drawText(topTextContent,cx+boundtop.height()/2,cy+boundtop.height()/2,mPaint);
          }
     }

     public synchronized  int getProgress(){return  mProgress;}

     public synchronized void setProgress(int progress){
         if(progress < 0){
             progress = 0;
         }else if(progress > 100){
             progress = 100;
         }
         mProgress = progress;
         postInvalidate();
     }

     public void setTopTextContent(String topTextContent){
         this.topTextContent = topTextContent;
     }

     public void setBottomTextContent(String bottomTextContent){
         this.bottomTextContent = bottomTextContent;
     }

}
