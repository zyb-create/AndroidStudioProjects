package com.example.zyb15.painttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zyb15 on 2020/5/16.
 */

public class DrawByHand extends View {

    // 定义记录前一个拖动事件发生点的坐标
    float preX;
    float preY;
    private Path path;
    public Paint paint = null;
    // 定义一个内存中的图片，该图片将作为缓冲区
    private Bitmap cacheBitmap = null;
    // 定义cacheBitmap上的Canvas对象
    private Canvas cacheCanvas = null;
    //private float View_width,View_height;

    public DrawByHand(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        // 设置画笔的颜色
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        // 反锯齿
        paint.setAntiAlias(true);  //设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        paint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 获取拖动事件的发生位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint); // ①
                path.reset();
                break;
        }
        invalidate();
        // 返回true表明处理方法已经处理该事件
        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        @SuppressLint("DrawAllocation") Paint bmpPaint = new Paint();
        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        // 沿着path绘制
        canvas.drawPath(path, paint);
    }

}
