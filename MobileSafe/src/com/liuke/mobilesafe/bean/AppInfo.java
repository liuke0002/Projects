package com.liuke.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	private String apkName;
	private long apkSize;
	private boolean userApp;
	private Drawable icon;
	private String packageName;
	private boolean rom;
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public long getApkSize() {
		return apkSize;
	}
	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isRom() {
		return rom;
	}
	public void setRom(boolean rom) {
		this.rom = rom;
	}
	@Override
	public String toString() {
		return "AppInfo [apkName=" + apkName + ", apkSize=" + apkSize
				+ ", userApp=" + userApp + ", icon=" + icon + ", packageName="
				+ packageName + ", rom=" + rom + "]";
	}
	

}
