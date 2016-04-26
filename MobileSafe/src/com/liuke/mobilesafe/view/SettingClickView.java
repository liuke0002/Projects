package com.liuke.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuke.mobilesafe.R;

public class SettingClickView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/rescom.liuke.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private String mTiltle;
	
	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTiltle = attrs.getAttributeValue(NAMESPACE, "title");
		initView();
	}

	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvDesc = (TextView) findViewById(R.id.tvDesc);
		setTitle(mTiltle);
	}
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	public void setDesc(String desc){
		tvDesc.setText(desc);
	}
}
