package com.example.zyb15.studentmgr;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by zyb15 on 2020/5/17.
 */

public class PlayRecord {

    private MediaPlayer mediaPlayer;
    private Context context;

    public PlayRecord(Context context)
    {
        this.context=context;
    }

    public void setContext(Context context)
    {
        this.context=context;
    }

    public void doPlay(String path,String name) {
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
                Toast.makeText(context, "播放失败", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        try {
            //设置声音文件
            File file=new File(Environment.getExternalStorageDirectory().getPath() + "/"+path+"/"+name+".m4a");
            if (file.exists())
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/"+path+"/"+name+".m4a");
            else
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/"+path+"/"+name+".mp3");
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

}
