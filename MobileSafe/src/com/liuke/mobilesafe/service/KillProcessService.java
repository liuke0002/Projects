package com.liuke.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class KillProcessService extends Service {

	BroadcastReceiver mReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver=new killedReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	class killedReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager activityManager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
			for(RunningAppProcessInfo runningInfo:runningAppProcesses){
				if(runningInfo.processName.equals(getPackageName())){
					continue;
				}
				activityManager.killBackgroundProcesses(runningInfo.processName);
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null!=mReceiver){
			unregisterReceiver(mReceiver);
			mReceiver=null;
		}
	}
}

