package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuke.mobilesafe.R;

public class DragViewActivity extends Activity {
	private int startX;
	private int startY;

	private SharedPreferences mPref;
	private TextView tvBottom, tvTop;
	private ImageView ivDrag;
	private WindowManager mWM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		tvTop = (TextView) findViewById(R.id.tv_top);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		int leftX = mPref.getInt("leftX", 0);
		int topY = mPref.getInt("topY", 0);
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		final int winWidth = mWM.getDefaultDisplay().getWidth();
		final int winHeight = mWM.getDefaultDisplay().getHeight();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();
		params.leftMargin = leftX;
		params.topMargin = topY;
		ivDrag.setLayoutParams(params);
		if (topY > winHeight / 2) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvBottom.setVisibility(View.VISIBLE);
			tvTop.setVisibility(View.INVISIBLE);
		}
		final long mHits[]=new long[2];
		ivDrag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				mHits[mHits.length-1]=SystemClock.uptimeMillis();
				if(mHits[mHits.length-1]-mHits[0]>=1000){
					ivDrag.layout(winWidth/2-ivDrag.getWidth()/2, ivDrag.getTop(), winWidth/2+ivDrag.getWidth()/2, ivDrag.getBottom());
				}
			}
		});
		ivDrag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx = endX - startX;
					int dy = endY - startY;
					int l = ivDrag.getLeft() + dx;
					int t = ivDrag.getTop() + dy;
					int r = ivDrag.getRight() + dx;
					int b = ivDrag.getBottom() + dy;
					if (t < 0 || b > winHeight - 20 || l < 0 || r > winWidth) {
						break;
					}
					if (t > winHeight / 2) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						tvBottom.setVisibility(View.VISIBLE);
						tvTop.setVisibility(View.INVISIBLE);
					}
					ivDrag.layout(l, t, r, b);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					Editor editor = mPref.edit();
					editor.putInt("leftX", ivDrag.getLeft());
					editor.putInt("topY", ivDrag.getTop());
					editor.commit();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drag_view, menu);
		return true;
	}

}
