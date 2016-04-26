package com.liuke.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.liuke.mobilesafe.service.KillProcessWidgetService;

public class MyAppWidget extends AppWidgetProvider {
	
	private Intent intent;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		intent = new Intent(context, KillProcessWidgetService.class);
		context.startService(intent);
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		if(intent!=null){
			context.stopService(intent);
		}
	}


}
