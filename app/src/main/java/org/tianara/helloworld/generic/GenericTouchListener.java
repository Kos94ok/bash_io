package org.tianara.helloworld.generic;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GenericTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public static final int SWIPE_NONE = 0;
    public static final int SWIPE_LEFT = 1;
    public static final int SWIPE_RIGHT = 2;
    public static final int SWIPE_TOP = 4;
    public static final int SWIPE_BOTTOM = 8;
    public static final int SWIPE_ALL = 15;
    public static final int NO_INTERRUPT = 16;

    private int swipeFlags;

    public GenericTouchListener(Context ctx, int swipeFlags){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.swipeFlags = swipeFlags;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //super.onFling(e1, e2, velocityX, velocityY);
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0 && (swipeFlags & SWIPE_RIGHT) == SWIPE_RIGHT) {
                            onSwipeRight();
                            result = true;
                        } else if ((swipeFlags & SWIPE_LEFT) == SWIPE_LEFT ){
                            onSwipeLeft();
                            result = true;
                        }
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0 && (swipeFlags & SWIPE_BOTTOM) == SWIPE_BOTTOM) {
                        onSwipeBottom();
                        result = true;
                    } else if ((swipeFlags & SWIPE_TOP) == SWIPE_TOP) {
                        onSwipeTop();
                        result = true;
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if ((swipeFlags & NO_INTERRUPT) == NO_INTERRUPT) { result = false; }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
