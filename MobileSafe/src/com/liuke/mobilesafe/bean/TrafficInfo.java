package com.liuke.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class TrafficInfo {

	private String apkName;
	private Drawable icon;
	private long upTraffics;
	private long downTraffics;
	private long totalTraffics;

	public long getTotalTraffics() {
		return totalTraffics;
	}

	public void setTotalTraffics(long totalTraffics) {
		this.totalTraffics = totalTraffics;
	}

	public long getUpTraffics() {
		return upTraffics;
	}

	public void setUpTraffics(long upTraffics) {
		this.upTraffics = upTraffics;
	}

	public long getDownTraffics() {
		return downTraffics;
	}

	public void setDownTraffics(long downTraffics) {
		this.downTraffics = downTraffics;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
}
