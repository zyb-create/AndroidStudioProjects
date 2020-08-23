package com.example.zyb15.otherapp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IBindlInterface bindlInterface;
    private EditText year,month,day;
    private NetworkMonitorService.NetworkBind networkBind;
    private boolean NetworkStatus=false;
    private TextView textView;
    private boolean tag=true;

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bindlInterface=IBindlInterface.Stub.asInterface(iBinder);
            Log.d("TAG","获取到bindlInterface");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("TAG","onServiceDisconnected");
        }
    };

    private ServiceConnection connection2=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            networkBind=(NetworkMonitorService.NetworkBind)iBinder;
            Log.d("TAG","获取到networkBind");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @SuppressLint("HandlerLeak")
    private Handler  handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(NetworkStatus)
                        textView.setVisibility(View.INVISIBLE);
                        //textView.setText("网络已连接");
                    else
                    {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("网络未连接");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        year= findViewById(R.id.year);
        month= findViewById(R.id.month);
        day= findViewById(R.id.day);
        textView= findViewById(R.id.status);

        Intent intent=new Intent();
        intent.setAction("zyb.QueryWeekdayService");
        intent.setPackage("com.example.zyb15.otherapp");
        bindService(intent,connection,BIND_AUTO_CREATE);

        Intent intent2=new Intent();
        intent.setAction("zyb.NetworkMonitorService");
        intent.setPackage("com.example.zyb15.otherapp");
        bindService(intent,connection2,BIND_AUTO_CREATE);

        autoCheck();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int y,m,d;
                try{
                    y=Integer.parseInt(year.getText().toString());
                    m=Integer.parseInt(month.getText().toString())-1;
                    d=Integer.parseInt(day.getText().toString());
                }catch (Exception e){Log.d("TAG","转换失败");return;}
                try {
                    ((TextView)findViewById(R.id.textView)).setText(bindlInterface.getWeekDay(y,m,d));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.unBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///unbindService(connection);
                tag=false;
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            networkBind.openCheck();
            Log.d("TAg","打开");
        }catch (Exception ignored){}

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            networkBind.closeCheck();
            Log.d("TAg","关闭");
        }catch (Exception ignored){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            tag=false;
            unbindService(connection);
            unbindService(connection2);
            Log.d("TAG","MainActivity--onDestroy--");
        }catch (Exception ignored){}
    }

    public void autoCheck()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tag)
                {
                    try{
                        synchronized (this){NetworkStatus=networkBind.getNetworkStatus();}
                        handler.sendEmptyMessage(1);
                    }catch (Exception e){e.printStackTrace();}
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("TAG","autoCheck()我被关闭了");
            }
        }).start();
    }

}
