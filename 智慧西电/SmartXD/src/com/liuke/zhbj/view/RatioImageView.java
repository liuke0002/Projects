package com.liuke.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/18.
 */
public class RatioImageView extends ImageView {

    private int ratio = 1;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); 
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = widthSize - getPaddingLeft() - getPaddingRight();

        int heightMode = MeasureSpec.getMode(heightMeasureSpec); 
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = heightSize - getPaddingTop() - getPaddingBottom();

        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY) {
            height = (int) (width / ratio + 0.5f); // 淇濊瘉4鑸嶄簲鍏�
        } else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY) {
            width = (int) ((height * ratio) + 0.5f);
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                width + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
