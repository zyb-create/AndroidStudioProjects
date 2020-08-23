package com.example.zyb15.studentmgr;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by zyb15 on 2020/3/21.
 */

public class FlowLR implements GestureDetector.OnGestureListener {

    private Context context;
    private Class aClass;

    public FlowLR(Context context,Class aClass)
    {
        this.aClass=aClass;
        this.context=context;
    }
    public FlowLR()
    {
    }

    public void setCC(Context context,Class aClass)
    {
        this.aClass=aClass;
        this.context=context;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {    //用户按下并拖动，在松开之前触发的事件
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {   //左右滑动

       // Log.d("TAG",e1.getDownTime()+"-"+e2.getDownTime()+"="+(e2.getEventTime()-e1.getDownTime()));
        //context.getClass().getSimpleName();
//        if((e2.getEventTime()-e1.getDownTime())>600)
//            return false;
        if((e1.getX()-e2.getX())>50&&Math.abs(velocityX)>0) {
            //Log.d("TAG","向左划");
            flowLeft();
            return true;
        }
        if((e2.getX()-e1.getX())>50&&Math.abs(velocityX)>0) {
            //Log.d("TAG","向右划");
            flowRight();
            return true;
        }
        //Log.i("TAG",e1.getX()+"   "+e2.getX());
        return false;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void flowLeft()     //左滑
    {
        if(context.getClass().getSimpleName().equals("ActivityStudent"))
            intentAnother();
    }

    private void flowRight()    //右滑
    {
        if(context.getClass().getSimpleName().equals("ActivityMain"))
            intentAnother();
    }

    public void intentAnother()
    {
        Intent intent=new Intent(context,aClass);
        context.startActivity(intent);
    }

}
