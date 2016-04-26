package com.liuke.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.liuke.mobilesafe.bean.AppInfo;

public class AppInfoParser {
	
	public static List<AppInfo> getAppInfos(Context context){
		List<AppInfo>packageInfos=new ArrayList<AppInfo>();
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> packages = manager.getInstalledPackages(0);
		for(PackageInfo info:packages){
			AppInfo appInfo=new AppInfo();
			String packageName = info.packageName;
			appInfo.setPackageName(packageName);
			Drawable icon = info.applicationInfo.loadIcon(manager);
			appInfo.setIcon(icon);
			String apkName = (String) info.applicationInfo.loadLabel(manager);
			appInfo.setApkName(apkName);
			String sourceDir=info.applicationInfo.sourceDir;
			File file=new File(sourceDir);
			long size=file.length();
			appInfo.setApkSize(size);
			int flag=info.applicationInfo.flags;
			if((flag&ApplicationInfo.FLAG_SYSTEM)!=0){
				appInfo.setUserApp(false);
			}else{
				appInfo.setUserApp(true);
			}
			if((flag&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				appInfo.setRom(true);
			}else{
				appInfo.setRom(false);
			}
			packageInfos.add(appInfo);
		}
		
		return packageInfos;
	}

}
