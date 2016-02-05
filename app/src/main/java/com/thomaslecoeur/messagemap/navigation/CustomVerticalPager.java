package com.thomaslecoeur.messagemap.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import pro.alexzaitsev.freepager.library.view.core.VerticalPager;

public class CustomVerticalPager extends VerticalPager {

    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    int width = displayMetrics.widthPixels;
    int height = displayMetrics.heightPixels;
    int threshold = 300;

    public CustomVerticalPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVerticalPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            if(ev.getY() >= threshold && ev.getY() <= height - threshold){
                setPagingEnabled(false);
            }else{
                setPagingEnabled(true);
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

}
