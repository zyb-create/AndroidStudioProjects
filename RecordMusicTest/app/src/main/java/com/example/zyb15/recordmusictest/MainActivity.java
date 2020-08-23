package com.example.zyb15.recordmusictest;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ar, dr, sr, pa, pd,as,ds;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ar = findViewById(R.id.addRecord);
        dr = findViewById(R.id.deleteRecord);
        sr = findViewById(R.id.stopRecord);
        pa = findViewById(R.id.playAdd);
        pd = findViewById(R.id.playDelete);
        as=findViewById(R.id.addSave);
        ds=findViewById(R.id.deleteSave);
        ar.setOnClickListener(this);
        dr.setOnClickListener(this);
        sr.setOnClickListener(this);
        pa.setOnClickListener(this);
        pd.setOnClickListener(this);
        as.setOnClickListener(this);
        ds.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try{
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }catch (Exception e){}
        mediaPlayer=null;
        switch (view.getId()) {
            case R.id.addRecord:
                start("add");
                break;
            case R.id.deleteRecord:
                start("delete");
                break;
            case R.id.stopRecord:
                stop();
                break;
            case R.id.playAdd:
                doPlay("add");
                break;
            case R.id.playDelete:
                doPlay("delete");
                break;
            case R.id.addSave:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        copyFile("add");
                    }
                }).start();
                break;
            case R.id.deleteSave:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        copyFile("delete");
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    public void initMediaRecorder(String name) {
        mediaRecorder = new MediaRecorder();

        // 设置音频来源     MIC == 麦克
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置默认音频输出格式   .mpeg4 格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 设置默认音频编码方式 增强型低延迟AAC（AAC-ELD）音频编解码器 编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //指定音频输出文件路径
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/MyMusics/"+name+".m4a");
        //设置设置录制会话的最长持续时间（以ms为单位）
        mediaRecorder.setMaxDuration(10*1000);
    }

    //开始录音
    public void start(String name) {
        if (mediaRecorder == null) {
            initMediaRecorder(name);
        }
        if (!isRecording) {
            try {
                isRecording = true;
                mediaRecorder.prepare();
                mediaRecorder.start();
                //开始录制
            } catch (IOException e) {
                e.printStackTrace();
                isRecording = false;
            }
        } else {
            Log.d("TAG", "正在录制中。。。");
        }
    }

    //停止录音
    public void stop() {
        if (mediaRecorder != null && isRecording) {
            isRecording = false;
            try{
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }catch (Exception e){System.out.println("mediaRecorder.stop();失败");}

        } else {
            Log.d("TAG", "已停止");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            try {
                isRecording = false;
                mediaRecorder.stop();
                mediaRecorder.release();
                //mPathList.clear();
            } catch (Exception e) {
            }
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //hadDestroy = true;
            mediaPlayer.release();
        }
    }

    private void doPlay(String name) {
        mediaPlayer=new MediaPlayer();

        //设置监听回调 播放完毕
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(MainActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        try {
            //设置声音文件
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/MyMusics/"+name+".m4a");
            mediaPlayer.prepare();

            //配置音量,中等音量
            mediaPlayer.setVolume(1, 1);
            //播放是否循环
            mediaPlayer.setLooping(false);

            //mediaPlayer.prepareAsync();
            mediaPlayer.start();

        } catch (IOException e1) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
            e1.printStackTrace();
        }

    }

    public synchronized void copyFile(String oldPathName) {
        String newPathName=Environment.getExternalStorageDirectory().getPath() + "/MyMusics/"+oldPathName+".m4a";
        oldPathName=Environment.getExternalStorageDirectory().getPath() + "/Music/"+oldPathName+".mp3";
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                Log.d("TAG", "copyFile:  oldFile not exists.");
                return;
            } else if (!oldFile.isFile()) {
                Log.d("TAG", "copyFile:  oldFile not file.");
                return;
            } else if (!oldFile.canRead()) {
                Log.d("TAG", "copyFile:  oldFile cannot read.");
                return;
            }

            File file=new File(newPathName);
            if(file.exists())
                file.delete();

            FileInputStream fileInputStream = new FileInputStream(oldFile);
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
