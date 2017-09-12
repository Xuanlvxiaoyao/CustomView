package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/9/11.
 */

public class CircleView extends View {

    private Paint paint,paintBlue;
    private int rwidth;
    private float radius;

    //标志
    private boolean flag=true;

    private float mNum;

    private float getNum;


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化画笔
    public void init(){
        paint=new Paint();
        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.colorAccent));
        //设置画笔粗度
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.paint));
        //设置画笔模式 只有边没有填充色
        paint.setStyle(Paint.Style.STROKE);
        //再给一个属性就是  抗锯齿  让你的画笔更加圆滑一些
        paint.setAntiAlias(true);

        paintBlue=new Paint();
        paintBlue.setColor(getResources().getColor(R.color.colorPrimary));
        paintBlue.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.paint));
        paintBlue.setStyle(Paint.Style.STROKE);
        paintBlue.setAntiAlias(true);
        paintBlue.setTextSize(50.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //view的宽高模式是wrap_content，match_parent
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
//            int width = (int) (2 * radius + 2 * paint.getStrokeWidth());
//            int height = (int) (2 * radius + 2 * paint.getStrokeWidth());
//            setMeasuredDimension(width, height);
        } else {
            //自己指定宽高
            int wSize = MeasureSpec.getSize(widthMeasureSpec);
            rwidth = wSize;
            radius = (rwidth - 2 * paint.getStrokeWidth()) / 2;
            //让ondraw（）再调用一次
            postInvalidate();
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            int wm = MeasureSpec.makeMeasureSpec(wSize, MeasureSpec.EXACTLY);
            int hm = MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.EXACTLY);
            super.onMeasure(wm, hm);
        }
    }

    //在ondraw 中千万不能干什么？  千万不能初始化画笔等其他控件
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: " );
        //画东西就可以了
        /**
         * 参数1：x轴圆心坐标
         * 参数2：y轴圆心坐标
         * 参数3：半径
         * 参数4：画笔
         */
        float strokeWidth = paint.getStrokeWidth();

        //绘制半圆弧度
        canvas.drawArc(strokeWidth,strokeWidth,2.0f*radius+strokeWidth,
                       2.0f*radius+strokeWidth,-180.0f,180.0f,false,paint);


        /**
         * 绘制蓝色弧度
         *
         * mNum*180  因为半圆
         */
        canvas.drawArc(strokeWidth,strokeWidth,2.0f*radius+strokeWidth,
                2.0f*radius+strokeWidth,-180.0f,mNum*180,false,paintBlue);

        
        if(mNum>=getNum){
            canvas.drawText((int)(getNum*100)+"%",radius-35.0f,radius+strokeWidth,paintBlue);
        }

    }



    /**
     * 绘制蓝色弧度
     * @param num  绘制蓝色弧度的度数的百分比
     */
    public void drawBlueArc(final float num){
        getNum=num;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    try {
                        Thread.sleep(50);
                        if(mNum>=num){
                            flag=false;
                        }

                        mNum+=5.0f/360;

                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
