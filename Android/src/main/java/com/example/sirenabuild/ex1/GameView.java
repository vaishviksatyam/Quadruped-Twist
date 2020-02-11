package com.example.sirenabuild.ex1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sirenabuild on 6/1/18.
 */

public class GameView extends SurfaceView {

    public Bitmap bmp;

    private SurfaceHolder holder;



    public GameView(Context context) {

        super(context);
        initialize();


    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize(){
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {



            @Override

            public void surfaceDestroyed(SurfaceHolder holder) {

            }



            @Override

            public void surfaceCreated(SurfaceHolder holder) {

                Canvas c = holder.lockCanvas(null);

                onDraw(c);

                holder.unlockCanvasAndPost(c);

            }



            @Override

            public void surfaceChanged(SurfaceHolder holder, int format,

                                       int width, int height) {

            }

        });

        bmp = BitmapFactory.decodeResource(getResources(), com.example.sirenabuild.ex1.R.drawable.white);
    }

    @Override

    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(scaleBitmap(bmp,this.getWidth(),this.getHeight()), 0, 0, null);

    }
    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if(bitmapToScale == null)
            return null;
//get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
// create a matrix for the manipulation
        Matrix matrix = new Matrix();

// resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

// recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);  }
}


