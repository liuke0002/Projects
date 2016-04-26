package com.liuke.zhbj.pager.impl;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liuke.zhbj.R;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.utils.RoundBitmapUtils;

public class SettingPager extends BasePager {

	private View view;
	private ImageView iv_head_icon;

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tv_title.setText("������ҳ");
		view = View.inflate(mActivity, R.layout.pager_setting, null);
		iv_head_icon = (ImageView) view.findViewById(R.id.iv_head_icon);
		iv_head_icon.setImageBitmap(RoundBitmapUtils
				.makeRoundCorner(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.icon_150)));
		if(fl_pager.getParent() instanceof ViewGroup){
			fl_pager.removeAllViews();
		}
		
		
		fl_pager.addView(view);
	}

}
