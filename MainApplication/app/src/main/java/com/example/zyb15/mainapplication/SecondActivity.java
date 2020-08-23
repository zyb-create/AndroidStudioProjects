package com.example.zyb15.mainapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除这个Activity的标题栏
        setContentView(R.layout.activity_second);
    }
}
