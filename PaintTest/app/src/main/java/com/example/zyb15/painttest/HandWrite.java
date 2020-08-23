package com.example.zyb15.painttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zyb15 on 2020/5/16.
 */

public class HandWrite extends View {

    Paint paint = null;
    Bitmap originalBitmap = null;
    Bitmap new1_Bitmap = null;
    Bitmap new2_Bitmap = null;
    float startX = 0,startY = 0;
    float clickX = 0,clickY = 0;
    boolean isMove = true;
    boolean isClear = false;
    int color=Color.BLUE;
    float strokeWidth=10.0f;

    public HandWrite(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher).copy(Bitmap.Config.ARGB_8888,true);
        new1_Bitmap = Bitmap.createBitmap(originalBitmap);
    }
    public void clear(){
        isClear = true;
        new2_Bitmap = Bitmap.createBitmap(originalBitmap);
        invalidate();
    }

    public void red(){
        isClear=false;
        color=Color.RED;
    }

    public void blue(){
        isClear=false;
        color= Color.BLUE;
    }
    public void brush(){
        strokeWidth=20.0f;
    }
    public void eraser(){
        color=Color.WHITE;
        strokeWidth=80.0f;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawBitmap(HandWriting(new1_Bitmap), 0, 0,null);
    }

    public Bitmap HandWriting(Bitmap o_Bitmap)
    {
        Canvas canvas = null;
        if(isClear) {
            canvas = new Canvas(new2_Bitmap);
        }
        else{
            canvas = new Canvas(o_Bitmap);
        }
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        if(isMove)
        {
            canvas.drawLine(startX, startY, clickX, clickY, paint);
        }
        startX = clickX;
        startY = clickY;
        if(isClear)
        {
            return new2_Bitmap;
        }
        return o_Bitmap;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        clickX = event.getX();
        clickY = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            isMove = false;
            invalidate();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            isMove = true;
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }


}
