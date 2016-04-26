package com.liuke.zhbj.pager;

import android.app.Activity;
import android.view.View;

public abstract class MenuDetailBasePage {
	public Activity mActivity;
	public View mRootView;
	public MenuDetailBasePage(Activity activity){
		mActivity=activity;
		mRootView=initView();
	}
	
	public abstract View initView();
	public void initData(){}
}

