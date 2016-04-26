package com.liuke.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	
	private boolean userApp;
	private String packageName;
	private String appName;
	private long memUse;
	private Drawable icon;
	private boolean checked;
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getMemUse() {
		return memUse;
	}
	public void setMemUse(long memUse) {
		this.memUse = memUse;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	

}
