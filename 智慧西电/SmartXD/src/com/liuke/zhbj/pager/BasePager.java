package com.liuke.zhbj.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.liuke.zhbj.R;
import com.liuke.zhbj.activity.MainActivity;

public class BasePager {
	
	public Activity mActivity;
	public TextView tv_title;
	public ImageButton ib_menu;
	public FrameLayout fl_pager;
	public View mRootView;
	public ImageButton ib_photo;
	public BasePager(Activity activity){
		mActivity=activity;
		initView();
	}
	public void initView() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tv_title=(TextView) mRootView.findViewById(R.id.tv_title);
		ib_menu=(ImageButton) mRootView.findViewById(R.id.ib_menu);
		ib_photo = (ImageButton) mRootView.findViewById(R.id.ib_photo);
		fl_pager=(FrameLayout) mRootView.findViewById(R.id.fl_pager);
		ib_menu.setVisibility(View.GONE);
	}
	public void initData(){
		ib_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
		setSlidingMenuState(false);
	}
	public void toggleSlidingMenu(){
		MainActivity mainUi=(MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();
	}
	public void setSlidingMenuState(boolean enable){
		MainActivity mainUi=(MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
