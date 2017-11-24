package com.example.lbstest;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class GroupSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        ActionBar actionbar=getSupportActionBar();//取消标题栏
        if (actionbar!=null){
            actionbar.hide();
        }
        setContentView(R.layout.activity_group_select);
    }
}
