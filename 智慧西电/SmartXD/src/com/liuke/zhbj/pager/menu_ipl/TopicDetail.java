package com.liuke.zhbj.pager.menu_ipl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.liuke.zhbj.pager.MenuDetailBasePage;

public class TopicDetail extends MenuDetailBasePage{

	public TopicDetail(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView text = new TextView(mActivity);
		text.setText("�˵�����ҳ-ר��");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
