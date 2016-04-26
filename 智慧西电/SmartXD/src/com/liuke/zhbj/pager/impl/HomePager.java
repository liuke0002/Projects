package com.liuke.zhbj.pager.impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.liuke.zhbj.R;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.view.MyScrollView;

public class HomePager extends BasePager {

	private MyScrollView scrollView;

	public HomePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		super.initData();
		tv_title.setText("ÖÇ»ÛÎ÷µç");
		View view = View.inflate(mActivity, R.layout.home_page, null);
		scrollView = (MyScrollView) view.findViewById(R.id.my_scroll_view);
		if(fl_pager.getParent() instanceof ViewGroup){
			fl_pager.removeAllViews();
		}
		fl_pager.addView(view);
		int i=0;
		for(i=0;i<scrollView.getChildCount();i++){
			scrollView.getChildAt(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
		}
		
	}
	
	
}
