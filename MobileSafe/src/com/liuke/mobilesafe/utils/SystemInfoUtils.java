package com.liuke.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class SystemInfoUtils {
	
	
	public static boolean isServiceRunning(Context context,String serviceName){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for(RunningServiceInfo serviceInfo:runningServices){
			String className = serviceInfo.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
		}
		return false;
	}
	
	public static int getRunningProcessCount(Context context){
		ActivityManager am = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
}
