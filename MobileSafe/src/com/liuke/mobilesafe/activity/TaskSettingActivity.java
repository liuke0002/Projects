package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.service.KillProcessService;
import com.liuke.mobilesafe.utils.SystemInfoUtils;
import com.liuke.mobilesafe.view.SettingItemView;

public class TaskSettingActivity extends Activity {

	@ViewInject(R.id.siv_ShowSysProcesses)
	private SettingItemView sivShowSysProcesses;
	private SharedPreferences mPref;
	@ViewInject(R.id.siv_TimeToClear)
	private SettingItemView sivTimeToClear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		initShowSystemProcess();
		initTimeToClear();
	}

	private void initTimeToClear() {
		boolean running = SystemInfoUtils.isServiceRunning(this, "com.liuke.mobilesafe.service.KillProcessService");
		sivTimeToClear.setChecked(running);
		sivTimeToClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(TaskSettingActivity.this, KillProcessService.class);
				sivTimeToClear.setChecked(!sivTimeToClear.isChecked());
				if(sivTimeToClear.isChecked()){
					startService(intent);
				}else{
					stopService(intent);
				}

			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_task_setting);
		ViewUtils.inject(this);
	}

	private void initShowSystemProcess() {
		boolean result = mPref.getBoolean("showSystemProcesses", true);
		sivShowSysProcesses.setChecked(result);
		sivShowSysProcesses.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = sivShowSysProcesses.isChecked();
				sivShowSysProcesses.setChecked(!checked);
				mPref.edit().putBoolean("showSystemProcesses", !checked).commit();
			}
		});
	}
	

}
