package com.juhe.weather;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NotiyDetailActivity extends Activity {

	private TextView tv_content;
	private String content;
	int resId[]={R.drawable.lollipop,R.drawable.camera,R.drawable.gift,R.drawable.weather,R.drawable.hourglass};
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		content = intent.getStringExtra("content");
		initView();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notiy_detail);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(content);
		iv = (ImageView) findViewById(R.id.iv);
		findViewById(R.id.btn_read).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		int nextInt = new Random().nextInt(100)%5;
		iv.setImageResource(resId[nextInt]);
	}
}
