package com.demien.browser;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by dmitry on 20.04.15.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {
//public class GestureListener extends GestureDetector {

    String TAG="GestureListener";

    int swipe_Min_Distance = 100;
    int swipe_Max_Distance = 350;
    int swipe_Min_Velocity = 100;
    /*
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "[CALLBACK_GL] boolean onDown(e:" + e + ")");
            return super.onDown(e);
        }
    */
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {

        /**
         *
         * Do your stuff
         *
         */

        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(TAG, "[CALLBACK_GL] boolean onSingleTapConfirmed(e:" + e + ")");
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i(TAG, "[CALLBACK_GL] boolean onDoubleTap(e:" + e + ")");


        return super.onDoubleTap(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG, "[CALLBACK_GL] boolean onFling(e1:" + e1 + ", e2:" + e2 + ", velocityX:" + velocityX
                + ", velocityY:" + velocityY + "");

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        if(xDistance > this.swipe_Max_Distance || yDistance > this.swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;

        if(velocityX > this.swipe_Min_Velocity && xDistance > this.swipe_Min_Distance){
            if(e1.getX() > e2.getX()) // right to left
                //this.listener.onSwipe(SWIPE_LEFT);
                Log.i(TAG, "Swipe Left");

            else
                Log.i(TAG, "Swipe Right");
            //this.listener.onSwipe(SWIPE_RIGHT);
        }

        if(velocityY > this.swipe_Min_Velocity && yDistance > this.swipe_Min_Distance){
            if(e1.getY() > e2.getY()) // right to left
                //this.listener.onSwipe(SWIPE_LEFT);
                Log.i(TAG, "Swipe Up");
            else
                Log.i(TAG, "Swipe Down");
            //this.listener.onSwipe(SWIPE_RIGHT);
        }

        //return super.onFling(e1, e2, velocityX, velocityY);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i(TAG, "[CALLBACK_GL] void onShowPress(e:" + e + ")");
        super.onShowPress(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i(TAG, "[CALLBACK_GL] void onLongPress(e:" + e + ")");

        super.onLongPress(e);
    }
}
