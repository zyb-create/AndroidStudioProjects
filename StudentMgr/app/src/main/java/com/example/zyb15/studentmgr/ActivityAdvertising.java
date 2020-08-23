package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;


public class ActivityAdvertising extends Activity {

    private Handler handler;
    private Timer timer;
    public int time=4;
    Button button;
    public float textSize=(float)1.15;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences!=null)
            textSize=Float.parseFloat(sharedPreferences.getString("fontsize",String.valueOf(1.15)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        getActionBar().hide();
        button=(Button)findViewById(R.id.adv_button);
        button.setTextColor(Color.WHITE);
////////////////////////////////////////////////////////
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null)
                    timer.cancel();
                intentAnother();
            }
        });
///////////////////////////////////////////////////
        handler=new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if(time>=0&&time<5)
                            button.setText("跳过（"+time+"s）");
                        else {
                            if(timer!=null)
                                timer.cancel();
                            intentAnother();
                        }
                        break;
//                    case 2:
//                        getWindow().setBackgroundDrawableResource(R.mipmap.advertising);
//                        break;
                    default:
                        break;
                }
            }
        };
        ///////////////////////////////////////////////////////////////////////////////
        setTimer();
        //setBackGround();
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = resources.getConfiguration();
            config.fontScale = textSize;//MyApplication.getTextSize();
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
        return resources;
    }

    public void intentAnother()
    {
        Intent intent=new Intent(ActivityAdvertising.this,ActivityMain.class);
        startActivity(intent);
    }

    public void setTimer()
    {
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                //message.obj=time--;
                synchronized(this){time--;}
                handler.sendMessage(message);
            }
        },1000,1000);
    }

    public void setBackGround()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

}
