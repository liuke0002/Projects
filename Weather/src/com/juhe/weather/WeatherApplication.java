package com.juhe.weather;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.thinkland.sdk.android.JuheSDKInitializer;

public class WeatherApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		JuheSDKInitializer.initialize(getApplicationContext());
		JPushInterface.init(this);
	}
}
