package com.liuke.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY=(int) ev.getY();
			if(Math.abs(endY-startY)>=Math.abs(endX-startX)){
				getParent().requestDisallowInterceptTouchEvent(false);
			}else{
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
