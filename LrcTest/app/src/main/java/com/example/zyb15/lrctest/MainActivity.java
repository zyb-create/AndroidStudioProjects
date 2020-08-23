package com.example.zyb15.lrctest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    public LrcView lrcView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lrcView=findViewById(R.id.lrc);
    }

    public void update(View view) {
        lrcView.setFile("/storage/emulated/0/Music/石岩-永夜(电视剧原声版).txt");
        //startActivity(new Intent(MainActivity.this,MainActivity.class));
    }
}
