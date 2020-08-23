package com.example.zyb15.mainapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    final String TAG="TAG";
    Button finish,startDialogActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"------OnCreate------");

        finish=(Button)findViewById(R.id.finish);
        startDialogActivity=(Button)findViewById(R.id.startDialogActivity);
        //////////////////////////////////////////
        startDialogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
        /////////////////////////////////////////////
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
        ////////////////////////////////////////////////
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"------onStart------");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"------onRestart------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"------onResume------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"------onPause------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"------onStop------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"------onDestory------");
    }
}
