package com.cmcc.robot.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import com.cmcc.robot.customview.activity.ChoiceActivity;
import com.cmcc.robot.customview.activity.DiagonalCircleActivity;
import com.cmcc.robot.customview.activity.MActivity;
import com.cmcc.robot.customview.activity.RoundActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 进度条
     */
    private Button mProgress;
    /**
     * 进度圆
     */
    private Button mOvalPro;
    /**
     * 对勾圆
     */
    private Button diagonalCircle;
    /**
     * 选择器
     */
    private Button choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mProgress = (Button) findViewById(R.id.progress);
        mProgress.setOnClickListener(this);
        mOvalPro = (Button) findViewById(R.id.oval_pro);
        mOvalPro.setOnClickListener(this);
        diagonalCircle = findViewById(R.id.duigou);
        diagonalCircle.setOnClickListener(this);
        choice = findViewById(R.id.choice);
        choice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.progress:
                startActivity(new Intent(MainActivity.this,MActivity.class));
                break;
            case R.id.oval_pro:
                startActivity(new Intent(MainActivity.this,RoundActivity.class));
                break;
            case R.id.duigou:
               start();
                break;
            case R.id.choice:
                startActivity(new Intent(MainActivity.this, ChoiceActivity.class));
                break;
        }
    }

    private void start(){
        //组合动画
        AnimationSet animationSet = new AnimationSet(true);
        float x = diagonalCircle.getWidth() / 2 ;
        float y = diagonalCircle.getHeight() / 2;
        RotateAnimation rotateAnimation = new RotateAnimation(0,90,x,y);
        rotateAnimation.setDuration(1000);
        animationSet.addAnimation(rotateAnimation);
        diagonalCircle.startAnimation(rotateAnimation);
        //属性动画
        /*ObjectAnimator rotation =  ObjectAnimator.ofFloat(diagonalCircle,"rotation",90f,0f,90f,180f,90);
        AnimatorSet set = new AnimatorSet();
        set.play(rotation);
        set.setDuration(9000);
        set.start();*/
    /*    set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(MainActivity.this, DiagonalCircleActivity.class));
                overridePendingTransition(R.anim.open_start,R.anim.open_close);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(MainActivity.this, DiagonalCircleActivity.class));
                overridePendingTransition(R.anim.open_start,R.anim.open_close);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
