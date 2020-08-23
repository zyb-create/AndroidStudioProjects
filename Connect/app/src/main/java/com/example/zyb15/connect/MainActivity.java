package com.example.zyb15.connect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

import DbFactory.DnUtil;

public class MainActivity extends Activity {

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0x11:
                    String s = (String) msg.obj;
                    ((TextView)findViewById(R.id.textView)).setText(s);
                    break;
                    default:((TextView)findViewById(R.id.textView)).setText("失败");break;
            }

        }
    };
    private String message="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message1=handler.obtainMessage();
                        String sql="select * from User_table";
                        //Log.i("TAG","开始");
                        System.out.println("1");
                        ResultSet resultSet= DnUtil.Search(sql);
                        //Log.i("TAG","继续");
                        System.out.println("2");
                        try {
                            assert resultSet != null;
                            while (resultSet.next())
                                message = resultSet.getString("User_name");
                            System.out.println("3");
                            DnUtil.Close();
                            message1.what=0x11;
                            message1.obj=message;
                            System.out.println("4");
                            handler.sendMessage(message1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



            }
        });
    }
}
