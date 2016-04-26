package com.liuke.mobilesafe.service;

import java.util.List;

import com.liuke.mobilesafe.activity.EnterPwdActivity;
import com.liuke.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WatchDogService extends Service {

	private AppLockDao dao;
	private ActivityManager activityManager;
	private String extra;
	private boolean flag;
	private screenReceiver mReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		flag = true;
		startWatch();
		mReceiver = new screenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction("com.liuke.mobilesafe.activity.EnterPwdActivity");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mReceiver, filter);
	}

	class screenReceiver extends BroadcastReceiver{



		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_SCREEN_OFF)){
				flag=false;
				
			}else if(action.equals(Intent.ACTION_SCREEN_ON)){
				flag=true;
			}else if(action.equals("com.liuke.mobilesafe.activity.EnterPwdActivity")){
				extra = intent.getStringExtra("packageName");
			}
		}
		
	}
	private void startWatch() {
		new Thread(){
			public void run() {
				activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				while(flag){
					List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
					String packageName = runningTasks.get(0).topActivity.getPackageName();
					dao = new AppLockDao(WatchDogService.this);
					if(dao.isLocked(packageName)&&!packageName.equals(extra)){
						Intent intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("packageName",packageName);
						startActivity(intent);
					}
				}
			};
		}.start();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		flag=false;
	}
}
