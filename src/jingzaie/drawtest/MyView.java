package jingzaie.drawtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jingzaie on 14-6-25.
 */
public class MyView extends View { 
    private Paint paint = new Paint();
    public float startX=0,startY=0;
    public int flagStop=0;
    public ChartRect chartRect =new ChartRect();
    public GestureDetector   mGestureDetector;
    private float vx=0;
    private float vy=1;
    private float dx=0;
    private float ax=0;
    private float dy=0;
    private float ay=0;
    public float targetleft=430; //目标x轴左距离
    public float targetright;//目标x轴右距离
    private float targety=430; //目标Y距离
    private double sping=0.01; //弹动系数
    private double sping1=0.4;
//    private double friction=0.05; //摩擦力系数
    private double friction=0.95; //摩擦力系数 原始
    private float springLength=100;//弹簧的长度
    private RectF rt=null;
    private float margin=10;
    private int flag=0;
    private float downX;
    private float upX;
	private int saveCount;
    public MyView(Context context) {

        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);





    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        if(flag==0) {
            //初始化场景
            rt = new RectF(margin, margin, getWidth() - margin, 400);
            canvas.drawRect(rt, paint);

            RectF rt0 = new RectF(rt.left + springLength, rt.height() - chartRect.getHeight(), rt.left + springLength + chartRect.getWidth(), rt.height());
            canvas.drawRect(rt0, paint);
            chartRect.setLeft(rt0.left);
            chartRect.setRight(rt0.right);
            chartRect.setX(rt0.left);
            targetleft=rt0.left;
            targetright=getWidth()-margin-springLength;
            int num =(int)chartRect.getWidth()/100;
            for(int i=1;i<num;i++){
                float x=chartRect.getX()+i*100;
                canvas.drawLine(x,400-40-10,x,400-10,paint);
            }
            canvas.drawLine(targetleft,400-40-20,targetleft,400,paint); //左弹簧固定点
            canvas.drawLine(targetright,400-40-20,targetright,400,paint);//右弹簧固定点
            canvas.drawLine(rt.left, rt.height() - (chartRect.getHeight() / 2), targetleft, rt.height() - (chartRect.getHeight() / 2), paint);//左弹簧线
            canvas.drawLine(rt.right, rt.height() - (chartRect.getHeight() / 2), chartRect.getRight(), rt.height() - (chartRect.getHeight() / 2), paint);//左弹簧线
            mGestureDetector = new GestureDetector(getContext(), new MyGestureListener(this));
            setLongClickable(true);
//            this.setOnTouchListener(new OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    return mGestureDetector.onTouchEvent(event);
//                }
//
//            });

            flag=1;

        }else{
            //canvas.restoreToCount(0);
            rt = new RectF(margin, margin, getWidth()-margin, 400);
            canvas.drawRect(rt,paint);

            //canvas.restoreToCount(1);

            RectF rt0 = new RectF(0,rt.height()-chartRect.getHeight(),chartRect.getWidth(),rt.height());
//            saveCount=canvas.save();
            saveCount= canvas.save(1);
            
            canvas.translate(chartRect.getX(),0);

            //canvas.drawOval(rt0, paint);
            canvas.drawRect(rt0,paint);
            int num =(int)chartRect.getWidth()/100;

//            canvas.restoreToCount(saveCount);
            canvas.restoreToCount(saveCount);
            canvas.drawLine(rt.left, rt.height() - (chartRect.getHeight() / 2), chartRect.getX(), rt.height() - (chartRect.getHeight() / 2), paint);
            canvas.drawLine(rt.right, rt.height() - (chartRect.getHeight() / 2), chartRect.getX()+chartRect.getWidth() , rt.height() - (chartRect.getHeight() / 2), paint);//左弹簧线
            canvas.drawLine(targetleft,400-40-20,targetleft,400,paint); //左弹簧固定点
            canvas.drawLine(targetright,400-40-20,targetright,400,paint);//右弹簧固定点

            for(int i=1;i<num;i++){
                float x=chartRect.getX()+i*100;
                canvas.drawLine(x,400-40-10,x,400-10,paint);
            }
            //canvas.save(1);
        }


    }






    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //flagStop=1;
                //downX=event.getX();
                return mGestureDetector.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE:

//                upX=event.getX();
//                if(chartRect.getLeft()>targetleft||chartRect.getRight()<targetright) {
//                    chartRect.setX(chartRect.getX() + (upX - downX) * (float) sping1);
//                }else {
//                    chartRect.setX(chartRect.getX() + (upX - downX));
//                }
//                downX=upX;
//                chartRect.setLeft(chartRect.getX());
//                chartRect.setRight(chartRect.getX()+chartRect.getWidth());
//                invalidate();
                return mGestureDetector.onTouchEvent(event);
                 //break;
            case MotionEvent.ACTION_UP:
                flagStop=0;
                if(chartRect.getX()>targetleft) {
                    vx = 0;
                    DrawImage dr = new DrawImage(0);
                    dr.start();
                }else if((chartRect.getX()+chartRect.getWidth())<targetright){
                    vx=0;
                    DrawImage dr = new DrawImage(1);
                    dr.start();
                }else {
                    return mGestureDetector.onTouchEvent(event);
                }
            default:
                break;
        }
        return true;
    }

    public  class DrawImage extends Thread {
        private double speed=0.5;
        private int flag1;
        public DrawImage(int flag){
            this.flag1=flag;
        }
        @Override
        public  void run() {

            if(flag1==0) {
                while (true) {
                    if (flagStop == 1) {
                        break;
                    }
                    dx = targetleft - chartRect.getX();

                    ax = dx * (float) sping;
                    //dy=targety-chartRect.getY();
                    //ay=dy*(float)sping;
                    //if(Math.abs(vx)>0.001){
                    vx += ax;
                    vx *= (float) friction;
                    if(dx>=0){
                        dx=0;
                        vx=0;
                        chartRect.setX(targetleft);
                    }else{
                        chartRect.setX(chartRect.getX() + vx);
                    }

                    //vy+=ay;
                    // vy*=(float)friction;
                    // chartRect.setY(chartRect.getY()+vy);

//                }
//                else{
//                    break;
//                }
                    chartRect.setLeft(chartRect.getX());
                    chartRect.setRight(chartRect.getX()+chartRect.getWidth());
                    MyView.this.postInvalidate();
                    if(dx==0)break;
                    try {
                        Thread.sleep(17);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }else{
                while (true) {
                    if (flagStop == 1) {
                        break;
                    }
                    dx = targetright - (chartRect.getX()+chartRect.getWidth());
                    ax = dx * (float) sping;
                    vx += ax;
                    vx *= (float) friction;
                    if(dx<=0){
                        dx=0;
                        chartRect.setX(0-(chartRect.getWidth()-(getWidth()-springLength-margin)));
                    }else {
                        chartRect.setX(chartRect.getX() + vx);
                    }
                    chartRect.setLeft(chartRect.getX());
                    chartRect.setRight(chartRect.getX()+chartRect.getWidth());
                    MyView.this.postInvalidate();
                    if(dx==0)break;
                    try {
                        Thread.sleep(17);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
}