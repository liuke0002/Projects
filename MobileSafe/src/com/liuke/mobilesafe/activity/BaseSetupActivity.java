package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDector;

	SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		mDector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						float distanceX = e2.getRawX() - e1.getRawX();
						float distanceY = e2.getRawY() - e1.getRawY();
						if (distanceY < 100 && distanceY > -100) {//上滑幅度过大
							if (distanceX < -200) {
								showNextPage();
							} else if (distanceX > 200) {// 向左滑
								showPreviousPage();
							}
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
		super.onCreate(savedInstanceState);
	}

	public abstract void showNextPage();

	public abstract void showPreviousPage();

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDector.onTouchEvent(event);// 委托手势识别器处理触摸事件
		return super.onTouchEvent(event);
	}

	public void next(View v) {
		showNextPage();
	}

	public void previous(View v) {
		showPreviousPage();
	}

}
