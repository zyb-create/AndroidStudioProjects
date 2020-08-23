package com.example.zyb15.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zyb15.otherapp.IBindlInterface;

public class MainActivity extends AppCompatActivity {

    private IBindlInterface bindlInterface;
    private EditText year,month,day;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        year= findViewById(R.id.year);
        month= findViewById(R.id.month);
        day= findViewById(R.id.day);

        Intent intent=new Intent();
        intent.setAction("zyb.QueryWeekdayService");
        intent.setPackage("com.example.zyb15.otherapp");
        bindService(intent,connection,BIND_AUTO_CREATE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                MainActivity.this.finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unbindService(connection);
            Log.d("TAG","MainActivity--onDestroy--");
        }catch (Exception ignored){}
    }
}
