package com.example.layouttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

public class PaintBoard extends View {
    int width, height;
    int lastX = -1;
    int lastY = -1;
    Canvas mCanvas,outputCanvas;
    Paint mPaint;
    Context mContext;
    Bitmap mBitmap,background,output,copyBitmap;

    public PaintBoard(Context context) {
        super(context);
        mContext = context;
    }

    public PaintBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setBackground(Bitmap bitmap){
        background = bitmap;
        width = background.getWidth();
        height= background.getHeight();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //output = Bitmap.createBitmap(background.getWidth(),background.getHeight(), Bitmap.Config.ARGB_8888);
        //outputCanvas = new Canvas(output);
        //Paint paint = new Paint();
        //outputCanvas.drawBitmap(background,(this.getWidth()-width)/2,(this.getHeight()-height)/2,null);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5.0F);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //copyBitmap=Bitmap.createBitmap(background).copy(Bitmap.Config.ARGB_8888,true);
        mCanvas=new Canvas();
        mCanvas.setBitmap(mBitmap); //비트맵 객체에 캔버스 달아 그림 그릴 수 있도록 하기위한 설정
        /* 이후부터는 mCanvas 위에 그리는 그림은 mBitmap에 적용된다. */
        //outputCanvas = new Canvas(mBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction(); // 액션 정보 가져오기
        int x = (int) event.getX(); // X좌표
        int y = (int) event.getY(); // Y좌표

        switch(action){

            /* 이동중에 이전 좌표값과 현재 좌표값 연결해서 선 그리기*/
            case MotionEvent.ACTION_MOVE:
                if(lastX != -1){
                    //outputCanvas.drawLine(lastX, lastY, x, y, mPaint);
                    mCanvas.drawLine(lastX, lastY, x, y, mPaint);
                }
                lastX = x;
                lastY = y;
                break;
            /* 손을 떼면 최종 좌표값 (-1, -1)로 리셋*/
            case MotionEvent.ACTION_UP:
                lastX = -1;
                lastY = -1;

                break;
        }
        invalidate(); // 다시 onDraw() 메소드 호출하여 그리기

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* 그림이 그려진 비트맵을 화면에 표시하기 */
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    /* 페인트 색 설정을 위한 메소드 */
    public void setPaintColor(@ColorInt int color){
        mPaint.setColor(color);
    }
}