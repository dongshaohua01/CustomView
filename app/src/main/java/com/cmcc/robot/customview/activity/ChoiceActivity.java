package com.cmcc.robot.customview.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cmcc.robot.customview.R;
import com.cmcc.robot.customview.view.PickerScrollView;

import java.util.ArrayList;
import java.util.List;

public class ChoiceActivity extends AppCompatActivity {

    PickerScrollView pickerScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        pickerScrollView = findViewById(R.id.pick);
        List<String> list = new ArrayList<>();
        list.add("我");
        list.add("ni");
        list.add("ta");
        pickerScrollView.setData(list);
    }

}
