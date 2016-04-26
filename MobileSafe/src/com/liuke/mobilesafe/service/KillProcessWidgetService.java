package com.liuke.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.receiver.MyAppWidget;
import com.liuke.mobilesafe.utils.SystemInfoUtils;

public class KillProcessWidgetService extends Service {

	private RemoteViews views;
	private ComponentName provider;
	private Timer timer;
	private TimerTask timerTask;
	private AppWidgetManager appWidgetManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		appWidgetManager = AppWidgetManager.getInstance(this);
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				provider = new ComponentName(getApplicationContext(),
						MyAppWidget.class);
				views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				views.setTextViewText(
						R.id.process_count,
						"正在运行的进程数:"
								+ SystemInfoUtils
										.getRunningProcessCount(getApplicationContext()));
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ Formatter
										.formatFileSize(
												getApplicationContext(),
												SystemInfoUtils
														.getAvailMem(getApplicationContext())));
				Intent intent=new Intent();
				intent.setAction("com.liuke.mobilesafe.receiver.KillProcessReceiver");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				appWidgetManager.updateAppWidget(provider, views);
			}
		};
		
		timer.schedule(timerTask, 0, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
}
