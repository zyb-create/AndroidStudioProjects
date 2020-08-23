package com.example.zyb15.studentmgr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zyb15 on 2020/4/25.
 */

public class PhotoAdapter extends BaseAdapter {

    public void setList(List<Bitmap> list) {
        this.list = list;
    }

    static class Holder
    {
        ImageView imageView;
    }

    private List<Bitmap> list;
    public Context context;

    public PhotoAdapter(List<Bitmap> list,Context context)
    {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap=(Bitmap) getItem(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.stu_photo, null);//实例化一个对象
            holder=new Holder();
            holder.imageView=convertView.findViewById(R.id.stu_new_photo);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
        assert bitmap != null;
        holder.imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
