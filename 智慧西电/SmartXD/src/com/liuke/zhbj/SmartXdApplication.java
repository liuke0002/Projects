package com.liuke.zhbj;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class SmartXdApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.init(this);
	}

}
