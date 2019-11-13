package com.cmcc.robot.customview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.cmcc.robot.customview.R;
import com.cmcc.robot.customview.view.DiagonalCircleView;

public class DiagonalCircleActivity extends AppCompatActivity {

    private DiagonalCircleView diagonalCircleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagonal_circle);
        diagonalCircleView = findViewById(R.id.diagonal);
        diagonalCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diagonalCircleView.setChecked(true);
            }
        });
    }
}
