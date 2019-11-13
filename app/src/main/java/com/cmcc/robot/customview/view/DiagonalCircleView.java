package com.cmcc.robot.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cmcc.robot.customview.R;

/**
 * 对勾圆
 */
public class DiagonalCircleView extends View {


    //没有选中的基调颜色
    private int uncheck_base_color;
    private String uncheckColor = "#CCCCCC";
    //选中后的基调颜色
    private int check_base_color;
    private String checkColor = "#FFDB14";
    //选中后勾的颜色
    private int check_tick_color;
    private String checkTickColor = "#009900";
    //白的
    private String white = "#FFFFFF";
    //圆的半径
    private float radius;
    private float tickRadiusOffset;
    //中心点坐标X、Y
    private float centerX;
    private float centerY;
    //画背景圆
    RectF rect = null;
    //圆环宽度
    private float circleW;
    //画笔 --未选中状态下
    private Paint mPaint;
    //画圆的画笔
    private Paint mPaintCircle;
    private float[] mPoints;
    //是否被点亮
    private boolean isChecked = false;
    //对勾颜色透明度
    private int alphaCount = 0;

    //计数器
    private int circleCounter = 0;
    private int ringCounter = 0;
    private int scaleCounter = 45;

    public DiagonalCircleView(Context context) {
        super(context);
    }

    public DiagonalCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public DiagonalCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TickView);
        uncheck_base_color = a.getColor(R.styleable.TickView_uncheck_base_color,Color.parseColor(uncheckColor));
        check_base_color = a.getColor(R.styleable.TickView_check_base_color,Color.parseColor(checkColor));
        check_tick_color = a.getColor(R.styleable.TickView_check_tick_color,Color.parseColor(checkTickColor));
        radius = a.getDimension(R.styleable.TickView_radiust,25f);
        a.recycle();

        mPaint = new Paint();
        circleW = 10f;
        mPaint.setColor(uncheck_base_color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circleW);

        mPaintCircle = new Paint();
        mPaintCircle.setStyle(Paint.Style.FILL);

        mPoints = new float[8];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        radius = width / 2 - circleW / 2;
        //得到圆心的坐标点
        centerX =getWidth() / 2;
        centerY =getHeight() / 2;
        tickRadiusOffset = radius / 4;
        //对勾的坐标点
        mPoints[0] = centerX - radius / 3;
        mPoints[1] = centerY;
        mPoints[2] = centerX;
        mPoints[3] = centerY + radius / 3;
        mPoints[4] = centerX ;
        mPoints[5] = centerY + radius / 3;
        mPoints[6] = centerX + radius / 2 ;
        mPoints[7] = centerY - radius / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isChecked)//未选中状态
        {
            //画背景圆
            rect = new RectF(centerX - radius,centerY - radius,centerX + radius,centerY + radius);
            canvas.drawArc(rect,90,360,false,mPaint);
            //画对勾
            canvas.drawLines(mPoints,mPaint);
            return;
        }

        //画勾选后的圆 每次+12
        ringCounter += 12;
        if(ringCounter>=360)
        {
            ringCounter = 360;
        }
          mPaint.setColor(check_base_color);
          canvas.drawArc(rect,90,ringCounter,false,mPaint);
          //当圆环绘制达到100%时开始绘制缩放圆
          if(ringCounter == 360){
              //先绘制背景的圆
              mPaintCircle.setColor(check_base_color);
              canvas.drawCircle(centerX,centerY,radius,mPaintCircle);

              //绘制白色圆每次缩放半径为6
              circleCounter += 6;
              mPaintCircle.setColor(Color.parseColor(white));
              canvas.drawCircle(centerX,centerY,radius - circleCounter,mPaintCircle);
              //当白色圆的半径等于圆半径是就该将背景圆展示出来了
              //+40加一个延迟
              if(circleCounter >= radius + 40)
              {
                  mPaint.setColor(Color.parseColor(white));
                  alphaCount += 20;
                  if(alphaCount >= 225)
                  {
                      alphaCount = 225;
                       //当对勾及颜色透明度绘制完成后，加一个放大效果
                       if(radius - circleCounter <= -100) {
                           scaleCounter -= 5;
                           if (scaleCounter <= -45) {
                               scaleCounter = -45;
                           }
                           //增长半径有一个放大的效果
                           float strokeWidth = scaleCounter > 0 ? radius + circleW : radius;
                           canvas.drawCircle(centerX, centerY, strokeWidth, mPaint);
                       }
                  }
                  //绘制勾选后的对勾
                  mPaint.setAlpha(alphaCount);
                  canvas.drawLines(mPoints,mPaint);
              }
          }
          invalidate();
    }

    //暴露外部接口，改变绘制状态
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        reset();
    }
    /**
     *  重置，并重绘
     */
    private void reset() {
        //计数器重置
        ringCounter = 0;
        circleCounter = 0;
        scaleCounter = 45;
        alphaCount = 0;
        invalidate();
    }
}
