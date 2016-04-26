package com.liuke.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.ProcessInfo;

public class ProcessInfoParser {

	public static List<ProcessInfo> getProcessInfos(Context context) {
		int totalPrivateDirty = 0;
		PackageManager packageManager = context.getPackageManager();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();
		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo info = new ProcessInfo();
			String processName = runningAppProcessInfo.processName;
			info.setPackageName(processName);
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(
						processName, 0);
				Drawable icon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				info.setIcon(icon);
				CharSequence label = packageInfo.applicationInfo
						.loadLabel(packageManager);
				info.setAppName(label.toString());
				int flags = packageInfo.applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					info.setUserApp(false);
				} else {
					info.setUserApp(true);
				}
				MemoryInfo[] memoryInfo = activityManager
						.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid });
				totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty();
				if (totalPrivateDirty != 0) {
					info.setMemUse(totalPrivateDirty * 1024);
				}
			} catch (NameNotFoundException e) {
				info.setAppName("系统进程");
				info.setIcon(context.getResources().getDrawable(
						R.drawable.thumb_me));
				e.printStackTrace();
			}
			processInfos.add(info);
		}

		return processInfos;
	}

}
