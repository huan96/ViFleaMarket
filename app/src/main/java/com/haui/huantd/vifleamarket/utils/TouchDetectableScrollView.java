package com.haui.huantd.vifleamarket.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class TouchDetectableScrollView extends ScrollView {

    OnMyScrollChangeListener myScrollChangeListener;

    public TouchDetectableScrollView(Context context) {
        super(context);
    }

    public TouchDetectableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchDetectableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (myScrollChangeListener != null) {
            if (t > oldt) {
                myScrollChangeListener.onScrollDown();
            } else if (t < oldt) {
                myScrollChangeListener.onScrollUp();
            }

        }
    }

    public OnMyScrollChangeListener getMyScrollChangeListener() {
        return myScrollChangeListener;
    }

    public void setMyScrollChangeListener(OnMyScrollChangeListener myScrollChangeListener) {
        this.myScrollChangeListener = myScrollChangeListener;
    }

    public interface OnMyScrollChangeListener {
        public void onScrollUp();

        public void onScrollDown();
    }
}