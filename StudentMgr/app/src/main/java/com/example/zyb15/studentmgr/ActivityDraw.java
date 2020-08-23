package com.example.zyb15.studentmgr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static java.lang.System.out;

public class ActivityDraw extends Activity {

    public Button red,blue,clear,brush,eraser;
    private Bezier bezier;
    private Button button;
    public static Bitmap bitmap;
    public static boolean TAG=false;  //是否设置为图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        bezier = findViewById(R.id.bezier);
        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setProgress(5);
        button=findViewById(R.id.curveYes);
        button.setVisibility(View.INVISIBLE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bezier.setSize(seekBar.getProgress());
            }
        });
    }

    public void yellow(View view) {
        bezier.setColor(Color.rgb(255,255,0));
    }

    public void red(View view) {
        bezier.setColor(Color.rgb(255,0,0));
    }

    public void blue(View view) {
        bezier.setColor(Color.rgb(30,144,255));
    }

    public void green(View view) {
        bezier.setColor(Color.rgb(0,255,0));
    }

    public void curveYes(View view) {
        bezier.tag2=true;
        synchronized (this){
            bezier.saveCueve();
            bezier.invalidate();
            bezier.tag2=false;
            bezier.tag3=false;
            bezier.tag4=false;
        }

    }

    public void line(View view) {
        button.setVisibility(View.INVISIBLE);
        bezier.operate=2;
    }

    public void curve(View view) {
        button.setVisibility(View.VISIBLE);
        bezier.operate=1;
    }

    public void circle(View view) {
        button.setVisibility(View.INVISIBLE);
        bezier.operate=3;
    }

    public void rect(View view) {
        button.setVisibility(View.INVISIBLE);
        bezier.operate=4;
    }

    public void clear(View view) {
        bezier.clearCacheCanvas();
        bezier.invalidate();
    }

    public void saveAndSet(View view) {

        bitmap=bezier.getCacheBitmap();
        TAG=true;
        saveBitmapToFile(bitmap);
        setResult(1002);
        bezier.clearCacheCanvas();
        bezier.invalidate();
        this.finish();
    }

    public void saveBitmapToFile(Bitmap bm) {

        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/Pictures/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, System.currentTimeMillis() + ".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名
            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            out.flush();
            out.close();
            Toast.makeText(ActivityDraw.this, "保存已经至" + Environment.getExternalStorageDirectory() + "/Pictures/" + "目录文件夹下", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancle(View view) {
        TAG=false;
        setResult(1002);
        bezier.clearCacheCanvas();
        bezier.invalidate();
        this.finish();
    }

    public void setButBotSave(View view) {
        bitmap=bezier.getCacheBitmap();
        TAG=true;
        setResult(1002);
        bezier.clearCacheCanvas();
        bezier.invalidate();
        this.finish();
    }
}
