package com.cmcc.robot.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
        }
    }
}
