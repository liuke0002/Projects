package com.liuke.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

import com.liuke.mobilesafe.bean.TrafficInfo;

public class TrafficInfoParser {
	
	
	public static List<TrafficInfo> getTrafficInfos(Context context){
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
		List<TrafficInfo>infos=new ArrayList<TrafficInfo>();
		for(ApplicationInfo appInfo:installedApplications){
			long rxBytes = TrafficStats.getUidRxBytes(appInfo.uid);
			long txBytes = TrafficStats.getUidTxBytes(appInfo.uid);
			System.out.println(appInfo.loadLabel(packageManager)+"--"+appInfo.uid);
			long totalBytes=rxBytes+txBytes;
			if(totalBytes==0){
				continue;
			}
			TrafficInfo trafficInfo = new TrafficInfo();
			trafficInfo.setUpTraffics(txBytes);
			trafficInfo.setTotalTraffics(totalBytes);
			trafficInfo.setDownTraffics(rxBytes);
			Drawable icon = appInfo.loadIcon(packageManager);
			trafficInfo.setIcon(icon);
			String apkName = appInfo.loadLabel(packageManager).toString();
			trafficInfo.setApkName(apkName);
			infos.add(trafficInfo);
		}
		return infos;
	}

}
