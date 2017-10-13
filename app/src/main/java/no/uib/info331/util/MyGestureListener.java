package no.uib.info331.util;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by EddiStat on 13.10.2017.
 */

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {



    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        if(velocityY > 500){
            Log.d("TAG", "onFling: "+ velocityY);
            return true;
        }else{
            return false;
        }
    }
}

