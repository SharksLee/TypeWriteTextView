package com.example.administrator.typewritetextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TypeWriteTextView.OnTypeListener {

    private TypeWriteTextView typeWriteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeWriteTextView = findViewById(R.id.ttv);
        typeWriteTextView.setTypeListener(this);

    }

    @Override
    public void onTypeStart() {
        Toast.makeText(this, "打字开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTypeStop() {
        Toast.makeText(this, "打字结束", Toast.LENGTH_SHORT).show();
    }

    public void type(View view) {
        typeWriteTextView.bindText("监控到房管局扣除价格肯定几十个没尽快发结果看到发几个\n接口了房间跟快递机构空间了房间跟快递较高的风景进\n口发动机盖看到过进口到福建高考进度");
    }
}
