package com.liuke.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.ProcessInfo;
import com.liuke.mobilesafe.engine.ProcessInfoParser;

public class TaskActivity extends Activity {

	@ViewInject(R.id.tvRuningProcesses)
	private TextView tvRuningProcesses;
	@ViewInject(R.id.tvAvailMem)
	private TextView tvAvailMem;
	@ViewInject(R.id.lvTask)
	private ListView lvTask;
	@ViewInject(R.id.btnSelectAll)
	private Button btnSelectAll;
	@ViewInject(R.id.btnSelectOther)
	private Button btnSelectOther;
	@ViewInject(R.id.btnClear)
	private Button btnClear;
	@ViewInject(R.id.btnSetting)
	private Button btnSetting;
	@ViewInject(R.id.ll_loading)
	private LinearLayout llLoad;
	private List<ProcessInfo> userApps;
	private List<ProcessInfo> systemApps;
	private List<ProcessInfo> appInfos;
	private TaskAdapter mAdapter;
	private long availMem;
	private int runningProcessCount;
	private long totalMem;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		initData();
		setClickListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setClickListener() {
		lvTask.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProcessInfo info = (ProcessInfo) lvTask
						.getItemAtPosition(position);
				if (info != null) {
					info.setChecked(!info.isChecked());
					mAdapter.notifyDataSetChanged();
				}
			}

		});
		btnSelectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (ProcessInfo info : appInfos) {
					info.setChecked(true);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		btnSelectOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ProcessInfo info : appInfos) {
					info.setChecked(!info.isChecked());
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				int killedCount = 0;
				long killedMem = 0;
				for (int i = 0; i < userApps.size(); i++) {
					if (userApps.get(i).isChecked()
							&& !userApps.get(i).getPackageName()
									.equals(getPackageName())) {
						killedMem += userApps.get(i).getMemUse();
						activityManager.killBackgroundProcesses(userApps.get(i)
								.getPackageName());
						userApps.remove(i);
						killedCount++;
					}
				}
				for (int i = 0; i < systemApps.size(); i++) {
					if (systemApps.get(i).isChecked()) {
						killedMem += systemApps.get(i).getMemUse();
						activityManager.killBackgroundProcesses(systemApps.get(
								i).getPackageName());
						systemApps.remove(i);
						killedCount++;
					}
				}
				runningProcessCount -= killedCount;
				tvRuningProcesses.setText("运行中进程:" + runningProcessCount + "个");
				availMem += killedMem;
				tvAvailMem.setText("剩余/总内存:"
						+ Formatter.formatFileSize(TaskActivity.this, availMem)
						+ "/"
						+ Formatter.formatFileSize(TaskActivity.this, totalMem));
				mAdapter.notifyDataSetChanged();
			}
		});
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskActivity.this,
						TaskSettingActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		runningProcessCount = runningAppProcesses.size();
		tvRuningProcesses.setText("运行中进程:" + runningProcessCount + "个");
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		availMem = outInfo.availMem;
		totalMem = outInfo.totalMem;
		tvAvailMem.setText("剩余/总内存:" + Formatter.formatFileSize(this, availMem)
				+ "/" + Formatter.formatFileSize(this, totalMem));
		ProcessInfoParser.getProcessInfos(this);
		new Thread() {
			public void run() {
				mAdapter = new TaskAdapter();
				appInfos = ProcessInfoParser.getProcessInfos(TaskActivity.this);
				userApps = new ArrayList<ProcessInfo>();
				systemApps = new ArrayList<ProcessInfo>();
				for (ProcessInfo info : appInfos) {
					if (info.isUserApp()) {
						userApps.add(info);
					} else {
						systemApps.add(info);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						llLoad.setVisibility(View.GONE);
						lvTask.setAdapter(mAdapter);
					}
				});
			};
		}.start();
	}

	private void initView() {
		setContentView(R.layout.activity_task);
		ViewUtils.inject(this);
	}

	class TaskAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			boolean result = mPref.getBoolean("showSystemProcesses", true);
			if (result) {
				return userApps.size() + 2 + systemApps.size();
			} else {
				return userApps.size() + 1;
			}
		}

		@Override
		public ProcessInfo getItem(int position) {
			if (position < userApps.size() + 1) {
				return userApps.get(position - 1);
			} else {
				return systemApps.get(position - userApps.size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			ProcessInfo appInfo;
			if (position == 0) {
				TextView tv = new TextView(TaskActivity.this);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户程序(" + userApps.size() + ")");
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position == userApps.size() + 1) {
				TextView tv = new TextView(TaskActivity.this);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统程序(" + systemApps.size() + ")");
				tv.setTextColor(Color.WHITE);
				return tv;
			}
			if (position < userApps.size() + 1) {
				appInfo = userApps.get(position - 1);
			} else {
				appInfo = systemApps.get(position - userApps.size() - 2);
			}
			if (convertView != null && convertView instanceof LinearLayout) {
				holder = new ViewHolder();
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(TaskActivity.this,
						R.layout.app_task_item, null);
				holder = new ViewHolder();
				holder.tvApkName = (TextView) convertView
						.findViewById(R.id.tvApkName);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.tvMem = (TextView) convertView.findViewById(R.id.tvMem);
				holder.cbStatus = (CheckBox) convertView
						.findViewById(R.id.cbAppStatus);
				convertView.setTag(holder);
			}
			holder.tvApkName.setText(appInfo.getAppName());
			holder.tvMem.setText("内存占用:"
					+ Formatter.formatFileSize(TaskActivity.this,
							appInfo.getMemUse()));
			holder.icon.setBackground(appInfo.getIcon());
			holder.cbStatus.setChecked(appInfo.isChecked());
			if(appInfo.getPackageName().equals(getPackageName())){
				holder.cbStatus.setVisibility(View.INVISIBLE);
			}else{
				holder.cbStatus.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

	}

	static class ViewHolder {
		ImageView icon;
		TextView tvApkName;
		TextView tvMem;
		CheckBox cbStatus;
	}

}
