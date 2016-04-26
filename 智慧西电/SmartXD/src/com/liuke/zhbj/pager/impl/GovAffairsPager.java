package com.liuke.zhbj.pager.impl;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.liuke.zhbj.R;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.view.RadarScanView;
import com.liuke.zhbj.view.RandomTextView;

public class GovAffairsPager extends BasePager {

	View view;
	private RandomTextView randomTextView;
	private Button btn_appoint;
	private RadarScanView radarScanView;

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tv_title.setText("һ��Լ��");
		view = View.inflate(mActivity, R.layout.pagger_appoint, null);
		btn_appoint = (Button) view.findViewById(R.id.btn_appoint);
		randomTextView = (RandomTextView) view
				.findViewById(R.id.random_textview);
		radarScanView=(RadarScanView) view.findViewById(R.id.radarScanView);
		randomTextView
				.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
					@Override
					public void onRippleViewClicked(View view) {
						Toast.makeText(mActivity, "�����", Toast.LENGTH_SHORT)
								.show();
					}
				});

		btn_appoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				radarScanView.scaning(true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						randomTextView.addKeyWord("�����");
						randomTextView.addKeyWord("��˧");
						randomTextView.addKeyWord("����");
						randomTextView.addKeyWord("������");
						randomTextView.addKeyWord("ϰ��ƽ");
						randomTextView.show();
					}
				}, 2 * 1000);
			}
		});
		
		fl_pager.addView(view);
	}

}
