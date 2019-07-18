package com.cmcc.robot.customview.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cmcc.robot.customview.R;
import com.cmcc.robot.customview.view.ClockView;
import com.cmcc.robot.customview.view.HabitCircularView;
import com.cmcc.robot.customview.view.ProgressBarView;


public class MActivity extends AppCompatActivity {

    /**
     * 进度条
     */
    private ProgressBarView mPro;

    /**
     * 进度圆
     */
    private HabitCircularView habitCircularView;

    ClockView clockView;

    private Button psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        mPro = findViewById(R.id.pro);
        mPro.init("30%",0.30f,0.40f,"40%");
        habitCircularView = findViewById(R.id.round_pro);
        habitCircularView.setBottomTextContent("40");
        habitCircularView.setTopTextContent("50%");
        habitCircularView.setProgress(80);

        clockView = findViewById(R.id.clockview);

        View view = View.inflate(this,R.layout.dialog_paypsd,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        psd = findViewById(R.id.psd);
        psd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.show();
            }
        });

    }

}
