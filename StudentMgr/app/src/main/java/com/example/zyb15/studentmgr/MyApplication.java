package com.example.zyb15.studentmgr;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by zyb15 on 2020/4/14.
 */

public class MyApplication extends Application {

//    private static float textSize=(float)1.15;
//
//    public static float getTextSize() {
//        return textSize;
//    }
//
//    public static void setTextSize(float textSize) {
//        MyApplication.textSize = textSize;
//    }

    @Override
    public void onCreate() {
//        SharedPreferences sharedPreferences=getSharedPreferences("com.example.zyb15.studentmgr_preferences",MODE_PRIVATE);
//        if(sharedPreferences!=null)
//        {
//            Log.d("TAG","I am First");
//            textSize= Float.parseFloat(sharedPreferences.getString("fontsize",String.valueOf(1.15)));
//        }
        super.onCreate();
    }
}
