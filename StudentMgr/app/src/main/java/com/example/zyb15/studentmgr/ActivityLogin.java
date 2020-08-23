package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends Activity {

    private static final String TAG ="TAG" ;
    private Handler handler;
    public float textSize=(float)1.15;
    public String user_no="";
    public String user_password="";
    public AutoCompleteTextView editText1;
    public EditText editText2;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=getSharedPreferences("com.example.zyb15.studentmgr_preferences",MODE_PRIVATE);
        if(sharedPreferences!=null) {
            textSize = Float.parseFloat(sharedPreferences.getString("fontsize", String.valueOf(1.15)));
            user_no=sharedPreferences.getString("user_no","");
            user_password = sharedPreferences.getString("user_password","");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActionBar().hide();
        editText1=(AutoCompleteTextView)findViewById(R.id.email);
        editText2=(EditText)findViewById(R.id.password);
        editText1.setText(user_no);
        editText2.setText(user_password);

        ((Button)findViewById(R.id.email_sign_in_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yanZheng();
            }

        });
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if(editText1.getText().toString().equals(user_no)&&editText2.getText().toString().equals(user_password))
                        {
                            //Intent intent=new Intent(ActivityLogin.this,ActivityAdvertising.class);
                            Intent intent=new Intent(ActivityLogin.this,ActivityMain.class);
                            startActivity(intent);
                        }
                        else {
                            //((ProgressBar)findViewById(R.id.login_progress)).setVisibility(View.INVISIBLE);
                            //((TextView)findViewById(R.id.textView11)).setVisibility(View.INVISIBLE);
                            Toast.makeText(ActivityLogin.this, "登录失败，请重试",Toast.LENGTH_SHORT).show();
                            //return;
                        }
                        break;
                    default:
                        break;
                }
            }
        };

    }

    private void yanZheng()
    {
        //((ProgressBar)findViewById(R.id.login_progress)).setVisibility(View.VISIBLE);
        //((TextView)findViewById(R.id.textView11)).setVisibility(View.VISIBLE);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        },0);
        /*try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }

    @Override
    public Resources getResources() {
        Resources resources=super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = resources.getConfiguration();
            config.fontScale=textSize;//MyApplication.getTextSize();
            resources.updateConfiguration(config,resources.getDisplayMetrics());
        }
        return resources;
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
//            final Resources res = newBase.getResources();
//            final Configuration config = res.getConfiguration();
//            config.fontScale =(float) 1.5;// MyApplication.getFontSize();//设置正常字体大小的倍数
//            final Context newContext = newBase.createConfigurationContext(config);
//            super.attachBaseContext(newContext);
//        }else{
//            super.attachBaseContext(newBase);
//        }
//
//    }

}