package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zyb15 on 2020/4/15.
 */

public class ListPreferenceAdapter extends ArrayAdapter {
    private int resourceId;

    public static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    public ListPreferenceAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceId=resource;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        //Student student=(Student)getItem(position);
        StudentAdapter.ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
            viewHolder=new StudentAdapter.ViewHolder();
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.stu_image);
            viewHolder.textView=(TextView)convertView.findViewById(R.id.stu_message);
            convertView.setTag(viewHolder);
        }else
            viewHolder=(StudentAdapter.ViewHolder)convertView.getTag();

        //assert student != null;
        viewHolder.textView.setText("This is test !");
        viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(position+1));
        viewHolder.imageView.setImageResource(getIconID(position+1));
        return convertView;
    }

    private float getTextSize(int item)
    {
        switch (item)
        {
            case 1:return (float)24;
            case 2:return (float)28;
            case 3:return (float)32;
            case 4:return (float)36;
            case 5:return (float)41;
            default:break;

        }
        return (float)41.0;
    }

    public int getIconID(int item)
    {
        switch (item)
        {
            case 1:return R.mipmap.one;
            case 2:return R.mipmap.two;
            case 3:return R.mipmap.three;
            case 4:return R.mipmap.four;
            case 5:return R.mipmap.five;
            default:break;
        }
        return -1;
    }
}
