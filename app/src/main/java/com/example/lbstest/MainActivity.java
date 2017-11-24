package com.example.lbstest;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_menu,return1,return2,start;
    private LinearLayout main,user,rank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        ActionBar actionbar=getSupportActionBar();//取消标题栏
        if (actionbar!=null){
            actionbar.hide();
        }
        setContentView(R.layout.activity_main);
        user=(LinearLayout)findViewById(R.id.user);
        user.setVisibility(View.INVISIBLE);
        rank=(LinearLayout)findViewById(R.id.rank);
        rank.setVisibility(View.INVISIBLE);
        main=(LinearLayout)findViewById(R.id.main);
        main.setVisibility(View.VISIBLE);
        btn_menu=(Button)findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(btn_menu);
            }
        });
        return1=(Button)findViewById(R.id.return1);
        return2=(Button)findViewById(R.id.return2);
        start=(Button)findViewById(R.id.start);
        return1.setOnClickListener(this);
        return2.setOnClickListener(this);
        start.setOnClickListener(this);
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu1, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.user:
                        user=(LinearLayout)findViewById(R.id.user);
                        user.setVisibility(View.VISIBLE);
                        rank=(LinearLayout)findViewById(R.id.rank);
                        rank.setVisibility(View.INVISIBLE);
                        main=(LinearLayout)findViewById(R.id.main);
                        main.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rank:
                        main=(LinearLayout)findViewById(R.id.main);
                        main.setVisibility(View.INVISIBLE);
                        user=(LinearLayout)findViewById(R.id.user);
                        user.setVisibility(View.INVISIBLE);
                        rank=(LinearLayout)findViewById(R.id.rank);
                        rank.setVisibility(View.VISIBLE);
                        break;
                    case R.id.Exit:
                        finish();
                        break;
                }
                return false;
            }});

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu)
            {}}); popupMenu.show();}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return1:
            case R.id.return2:
                user=(LinearLayout)findViewById(R.id.user);
                user.setVisibility(View.INVISIBLE);
                rank=(LinearLayout)findViewById(R.id.rank);
                rank.setVisibility(View.INVISIBLE);
                main=(LinearLayout)findViewById(R.id.main);
                main.setVisibility(View.VISIBLE);
                break;
            case R.id.start:
                Intent intent=new Intent(MainActivity.this,Mainview.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
