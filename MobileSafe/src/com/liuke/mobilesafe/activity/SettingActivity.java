package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.service.AddressService;
import com.liuke.mobilesafe.service.CallsafeService;
import com.liuke.mobilesafe.service.WatchDogService;
import com.liuke.mobilesafe.utils.SystemInfoUtils;
import com.liuke.mobilesafe.view.SettingClickView;
import com.liuke.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SettingItemView sivCallsafe;
	private SettingItemView sivAddress;
	private SettingItemView sivApplock;
	private SettingClickView scvAdressStyle;
	private SettingClickView scvAdressLocation;
	private SharedPreferences mPref;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initAddressView();
		initUpdateView();
		initAddessStyleView();
		initAddressLocation();
		initCallsafeView();
		initApplockView();
	}


	private void initApplockView() {
		sivApplock = (SettingItemView) findViewById(R.id.siv_applock);
		boolean running = SystemInfoUtils.isServiceRunning(this, "com.liuke.mobilesafe.service.WatchDogService");
		sivApplock.setChecked(running);
		sivApplock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,WatchDogService.class);
				sivApplock.setChecked(!sivApplock.isChecked());
				if(sivApplock.isChecked()){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
	}


	private void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean checked = mPref.getBoolean("auto_update", false);
		if(checked){
			sivUpdate.setChecked(checked);
		}else{
			sivUpdate.setChecked(checked);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					mPref.edit().putBoolean("auto_update", false).commit();
				}else{
					sivUpdate.setChecked(true);
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}


	private void initAddressView() {
		// TODO Auto-generated method stub
		sivAddress=(SettingItemView) findViewById(R.id.siv_address);
		boolean running = SystemInfoUtils.isServiceRunning(this, "com.liuke.mobilesafe.service.AddressService");
		sivAddress.setChecked(running);
		sivAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,AddressService.class);
				sivAddress.setChecked(!sivAddress.isChecked());
				if(sivAddress.isChecked()){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
	}
	private void initCallsafeView() {
		// TODO Auto-generated method stub
		sivCallsafe=(SettingItemView) findViewById(R.id.siv_callsafe);
		boolean running = SystemInfoUtils.isServiceRunning(this, "com.liuke.mobilesafe.service.CallsafeService");
		sivCallsafe.setChecked(running);
		sivCallsafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,CallsafeService.class);
				sivCallsafe.setChecked(!sivCallsafe.isChecked());
				if(sivCallsafe.isChecked()){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
	}
	private void initAddessStyleView(){
		scvAdressStyle=(SettingClickView) findViewById(R.id.scv_address_style);
		scvAdressStyle.setDesc("活力橙");
		scvAdressStyle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showAddressDialog();
			}
		});
	}


	final String []items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	protected void showAddressDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");
		int style = mPref.getInt("address_style", 1);
		scvAdressStyle.setDesc(items[style]);
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mPref.edit().putInt("address_style", which).commit();
				dialog.dismiss();
				scvAdressStyle.setDesc(items[which]);
			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	private void initAddressLocation(){
		scvAdressLocation=(SettingClickView) findViewById(R.id.scv_address_location);
		scvAdressLocation.setDesc("设置归属地提示框显示位置");
		scvAdressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
	}

}
