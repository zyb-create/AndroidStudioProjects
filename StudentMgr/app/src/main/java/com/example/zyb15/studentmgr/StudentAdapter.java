package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zyb15 on 2020/3/2.
 */

public class StudentAdapter extends ArrayAdapter {

    private final int resourceId;

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public StudentAdapter(Context context, int textViewResourceId, List<Map<String,Object>> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Map<String,Object> map=(Map<String, Object>)getItem(position);
        assert map != null;
        Bitmap bitmap=(Bitmap) map.get("photo");
        Student student = (Student) map.get("student");
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.stu_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.stu_message);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        assert student != null;
        viewHolder.textView.setText(student.getStu_name());
        if(bitmap!=null)
            viewHolder.imageView.setImageBitmap(bitmap);
        else if(student.getStu_id()!=0)
            viewHolder.imageView.setImageResource(student.getStu_id());
        return convertView;
    }
}





