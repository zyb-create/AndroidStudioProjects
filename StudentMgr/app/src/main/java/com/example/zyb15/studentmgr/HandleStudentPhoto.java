package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zyb15 on 2020/4/25.
 */

public class HandleStudentPhoto implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {

    private Context context;
    private ImageView imageView;
    private Handler handler;
    private GridView gridView;
    private View view;
    private AlertDialog dialog;
    private PhotoAdapter photoAdapter;

    @SuppressLint("HandlerLeak")
    public HandleStudentPhoto(ImageView imageView, Context context)
    {
        this.imageView=imageView;
        this.context=context;
        this.imageView.setOnClickListener(this);
        this.imageView.setOnCreateContextMenuListener(this);
        view= LayoutInflater.from(getContext()).inflate(R.layout.gridview_photos, null);//实例化一个对象
        photoAdapter=new PhotoAdapter(new LinkedList<Bitmap>(),context);
        gridView= view.findViewById(R.id.gridview_photos);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(photoAdapter);
        dialog=new AlertDialog.Builder(context).create();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0x11:
                        Bundle bundle=msg.getData();
                        if(bundle==null)
                            break;
                        //Student student=(Student)bundle.getSerializable("");
                        List<Bitmap> list=(List<Bitmap>) bundle.getSerializable("PhotoList");
                        setPhoto(list);
                        break;
                    default:break;
                }
            }
        };
    }

    private void setDialog(AlertDialog dialog){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height= dm.heightPixels; // 屏幕高度（像素）
//        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width=(int)(width*0.8);
        layoutParams.height=(int)(height/2);
        dialog.getWindow().setAttributes(layoutParams);
        //Log.d("TAG","设置成功");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onClick(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getSystemPhotoList(context);
            }
        }).start();
    }

    private void getSystemPhotoList(Context context)
    {
        List<Bitmap> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0)
            return;
        int num=0;

        while (cursor.moveToNext())
        {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists())//&&tag)
            {
                num++;
                Bitmap bitmap= BitmapFactory.decodeFile(path);
                //Log.d("TAG",path);
                result.add(bitmap);
            }
        }
        Message message=new Message();
        message.what=0x11;
        Bundle bundle=new Bundle();
        bundle.putSerializable("PhotoList", (Serializable) result);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void setPhoto(List<Bitmap> list)
    {
        if(list!=null)
        {
            dialog.show();
            photoAdapter.setList(list);
            photoAdapter.notifyDataSetChanged();
            setDialog(dialog);
            dialog.setContentView(view);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView imageView= view.findViewById(R.id.stu_new_photo);
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//        imageView.setDrawingCacheEnabled(true);
//        Bitmap bitmap=imageView.getDrawingCache();
//        imageView.setDrawingCacheEnabled(false);  //以清空画图缓冲区，否则，下一次从ImageView对象iv_photo中获取的图像，还是原来的图像。
        if(bitmap!=null)
            this.imageView.setImageBitmap(bitmap);
        if(this.dialog!=null)
            this.dialog.dismiss();
            //this.dialog.hide();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "选择图库中图片");
        menu.add(0, 1, 0, "自定义图片");
    }

}
