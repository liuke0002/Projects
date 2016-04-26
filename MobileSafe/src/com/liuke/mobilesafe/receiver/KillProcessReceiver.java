package com.liuke.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo runningInfo : runningAppProcesses) {
			activityManager.killBackgroundProcesses(runningInfo.processName);
		}
		Toast.makeText(context, "«Â¿ÌÕÍ±œ", Toast.LENGTH_SHORT).show();
	}

}
