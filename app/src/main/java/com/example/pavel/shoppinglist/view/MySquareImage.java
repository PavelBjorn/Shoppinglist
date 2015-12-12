package com.example.pavel.shoppinglist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MySquareImage extends ImageView {


    public MySquareImage(Context context) {
        super(context);
    }

    public MySquareImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySquareImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = this.getMeasuredWidth();
        setMeasuredDimension(w,w);
    }
}
