package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.utils.MD5Utils;
import com.liuke.mobilesafe.utils.UIUtils;

public class EnterPwdActivity extends Activity {

	private EditText etPwd;
	private Button btnSure;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pwd = etPwd.getText().toString();
				if (TextUtils.isEmpty(pwd)) {
					etPwd.setError("密码为空！");
				} else {
					mPref = getSharedPreferences("config", MODE_PRIVATE);
					String savedPwd = mPref.getString("password", "");
					if (savedPwd.equals(MD5Utils.encode(pwd))) {
						Intent i = getIntent();
						String packageName = null;
						if (i != null) {
							packageName = i.getStringExtra("packageName");
						}
						Intent intent = new Intent();
						intent.putExtra("packageName", packageName);
						intent.setAction("com.liuke.mobilesafe.activity.EnterPwdActivity");
						sendBroadcast(intent);
						finish();
					} else {
						UIUtils.showToast("密码错误", EnterPwdActivity.this);
					}
				}
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_enter_pwd);
		etPwd = (EditText) findViewById(R.id.et_password);
		btnSure = (Button) findViewById(R.id.btn_sure);
	}

	@Override
	public void onBackPressed() {
		// 当用户输入后退健 的时候。我们进入到桌面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
	}

}
