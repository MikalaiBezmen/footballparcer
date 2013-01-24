package com.argando.parcersample.instruments;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.argando.parcersample.IFragmentTransaction;

public class GestureDetectorImpl implements GestureDetector.OnGestureListener {
    private IFragmentTransaction mFragmentTransaction;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public GestureDetectorImpl(IFragmentTransaction fragmentTransaction) {
        mFragmentTransaction = fragmentTransaction;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.i(LOG_TAG, "onDown");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.i(LOG_TAG, "onShowPress");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.i(LOG_TAG, "onSingleTapUp");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.i(LOG_TAG, "onScroll");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.i(LOG_TAG, "onLongPress");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.i(LOG_TAG, "onFlink");
        try {
            //if (Math.abs(motionEvent.getY() - motionEvent1.getY()) > SWIPE_MAX_OFF_PATH)
            //    return false;
            if (motionEvent.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(v) > SWIPE_THRESHOLD_VELOCITY) {
                mFragmentTransaction.launchMenuFragment();
                Log.i(LOG_TAG, "slide right");
                return true;
            } else if (motionEvent1.getX() - motionEvent.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(v) > SWIPE_THRESHOLD_VELOCITY) {
                mFragmentTransaction.launchMatchFragment();
                Log.i(LOG_TAG, "slide left");
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    private static final String LOG_TAG = GestureDetectorImpl.class.getSimpleName();
}
