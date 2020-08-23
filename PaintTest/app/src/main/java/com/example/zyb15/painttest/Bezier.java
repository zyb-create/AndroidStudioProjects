package com.example.zyb15.painttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;

/**
 * Created by zyb15 on 2020/5/16.
 */

public class Bezier extends View {

    private int width,height;
    private Paint mPaint;
    // 定义一个内存中的图片，该图片将作为缓冲区
    private Bitmap cacheBitmap = null;
    // 定义cacheBitmap上的Canvas对象
    private Canvas cacheCanvas = null;
    private boolean tag=false;  //新建曲线时不画后两个点
    private PointF start, end, control;
    private Path path;
    private int StrokeWidthsize=5;
    private int color=Color.RED;
    public int operate=2;
    public boolean tag2=false;  //曲线是否已画好
    public boolean tag3=false;  //曲线的顶点
    public boolean tag4=false;  //曲线的末点
    private float prex=0;        //直线的前一点
    private float prey=0;        //直线的后一点
    private float circleX,circleY,circleR;   //圆心坐标及半径

    public Bezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path=new Path();
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStrokeWidth(StrokeWidthsize);
        //mPaint.setColor(Color.rgb(255,255,0));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);//60
        start = new PointF(0,0);
        end = new PointF(0,0);
        control = new PointF(0,0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width=w;
        this.height=h;
        cacheBitmap = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        //设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mPaint.setColor(color);
        mPaint.setStrokeWidth(StrokeWidthsize);
        //mPaint.setStyle(Paint.Style.STROKE);
        // 获取拖动事件的发生位置
        float x = event.getX();
        float y = event.getY();;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                switch (operate)
                {
                    case 1:
                        if(!tag3)
                        {
                            start.x=event.getX();
                            start.y=event.getY();
                            tag=true;
                        }
                        if(!tag2&&tag3)
                            tag4=true;
                        break;
                    case 2:
                        path.moveTo(x,y);
                        prex=x;
                        prey=y;
                        break;
                    case 3:
                        circleX=x;
                        circleY=y;
                        break;
                    case 4:
                        circleX=x;
                        circleY=y;
                        break;
                    default:break;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                switch (operate)
                {
                    case 1:
                        if(!tag4)
                        {
                            end.x=x;
                            end.y=y;
                        }
                        break;
                    case 2:
                        path.quadTo(prex,prey,x,y);
                        prex=x;
                        prey=y;
                        break;
                    case 3:
                        path.reset();
                        circleR= (float) sqrt((x-circleX)*(x-circleX)+(y-circleY)*(y-circleY));
                        path.addCircle(circleX,circleY,circleR,Path.Direction.CCW);
                        //path.fillType = Path.FillType.WINDING;
                        break;
                    case 4:
//                        path.addRoundRect(RectF(circleX,circleY,x,y),Path.Direction.CCW);
                        path.addRect(new RectF(circleX,circleY,x,y),Path.Direction.CCW);
                        break;
                    default:break;
                }

                break;
            case MotionEvent.ACTION_UP:
                switch (operate)
                {
                    case 1:
                        if(!tag4)
                        {
                            end.x=x;
                            end.y=y;
                            tag3=true;
                        }
                        break;
                    case 2:
                        saveLine(cacheCanvas);
                        break;
                    case 3:
                        path.rewind();
                        circleR= (float) sqrt((x-circleX)*(x-circleX)+(y-circleY)*(y-circleY));
                        path.addCircle(circleX,circleY,circleR,Path.Direction.CW);
                        saveLine(cacheCanvas);
                        break;
                    case 4:
                        path.rewind();
                        path.addRect(new RectF(circleX,circleY,x,y),Path.Direction.CCW);
                        saveLine(cacheCanvas);
                        break;
                    default:break;
                }

                break;
        }

        control.x=abs(end.x+start.x)/2;
        control.y=(end.x+end.y)/2+20;
        if(tag4&&tag3&&!tag2)
        {
            control.x=x;
            control.y=y;
        }

        //提醒更新
        invalidate();
        // 返回true表明处理方法已经处理该事件
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Paint bmpPaint = new Paint();

        switch (operate)
        {
            case 1:
                if(!tag2)
                    saveCacheCanvas(canvas);
                break;
            case 2:
                //saveLine(canvas);
                break;
            case 3://saveLine(canvas);
            break;
            case 4://saveLine(canvas);
            break;
            default:break;
        }

        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        // 沿着path绘制
        canvas.drawPath(path, mPaint);

    }

    public void saveCacheCanvas(Canvas cacheCanvas)  //画曲线
    {
        if(!tag2)
        {
            // 绘制数据点和控制点
            mPaint.setColor(Color.GRAY);
            mPaint.setStrokeWidth(8);
            cacheCanvas.drawPoint(start.x,start.y,mPaint);
            if(tag)
            {
                tag=false;
                return;
            }
            cacheCanvas.drawPoint(end.x,end.y,mPaint);
            cacheCanvas.drawPoint(control.x,control.y,mPaint);

            // 绘制辅助线
            mPaint.setStrokeWidth(1);
            cacheCanvas.drawLine(start.x,start.y,control.x,control.y,mPaint);
            cacheCanvas.drawLine(end.x,end.y,control.x,control.y,mPaint);
        }


        // 绘制贝塞尔曲线
        mPaint.setColor(color);
        mPaint.setStrokeWidth(StrokeWidthsize);

        path.rewind();
        path.moveTo(start.x,start.y);
        path.quadTo(control.x,control.y,end.x,end.y);

        cacheCanvas.drawPath(path, mPaint);
    }

    public void saveLine(Canvas cacheCanvas)  //画直线、原、矩形
    {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(StrokeWidthsize);
        cacheCanvas.drawPath(path,mPaint);
        path.rewind();
    }


    public void setSize(int size)
    {
        this.StrokeWidthsize=size;
    }
    public void setColor(int color)
    {
        this.color=color;
    }
    public void saveCueve()
    {
        synchronized (this)
        {
            saveCacheCanvas(cacheCanvas);
            invalidate();
        }
    }
    public void clearCacheCanvas()
    {
        cacheBitmap=Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        cacheCanvas.setBitmap(cacheBitmap);
    }

    public Bitmap getCacheBitmap()
    {
        return cacheBitmap;
    }

}
