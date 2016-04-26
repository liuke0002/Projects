package com.liuke.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.AppInfo;
import com.liuke.mobilesafe.engine.AppInfoParser;

public class AppManagerActivity extends Activity {

	@ViewInject(R.id.lv_apps)
	private ListView lvApps;
	@ViewInject(R.id.ll_loading)
	private LinearLayout llLoad;
	@ViewInject(R.id.tvRam)
	private TextView tvRam;
	@ViewInject(R.id.tvRom)
	private TextView tvRom;
	@ViewInject(R.id.tvNumApp)
	private TextView tvNumApp;
	private List<AppInfo> appInfos;
	AppsAdapter mAdapter;
	private List<AppInfo> userApps;
	private List<AppInfo> systemApps;
	private PopupWindow popupWindow;
	private AppInfo clickAppInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mAdapter = new AppsAdapter();
			llLoad.setVisibility(View.GONE);
			lvApps.setAdapter(mAdapter);
			lvApps.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					popWindowDismiss();
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if (userApps != null && systemApps != null) {
						if (firstVisibleItem < userApps.size()) {
							tvNumApp.setText("用户程序(" + userApps.size() + ")");
						} else {
							tvNumApp.setText("系统程序(" + systemApps.size() + ")");
						}
					}
				}
			});
			lvApps.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					if (position != 0 && position != userApps.size() + 1) {
						View contentView = View.inflate(
								AppManagerActivity.this, R.layout.item_popup,
								null);
						if (position < userApps.size() + 1) {
							clickAppInfo = userApps.get(position - 1);
						} else {
							clickAppInfo = systemApps.get(position
									- userApps.size() - 2);
						}
						TextView tvUninstall = (TextView) contentView
								.findViewById(R.id.tvUninstall);
						TextView tvRun = (TextView) contentView
								.findViewById(R.id.tvRun);
						TextView tvDetail = (TextView) contentView
								.findViewById(R.id.tvDetail);
						tvDetail.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent detailIntent = new Intent();
								detailIntent
										.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
								detailIntent
										.addCategory(Intent.CATEGORY_DEFAULT);
								detailIntent.setData(Uri.parse("package:"
										+ clickAppInfo.getPackageName()));
								startActivity(detailIntent);
								popWindowDismiss();
							}
						});
						tvRun.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = getPackageManager()
										.getLaunchIntentForPackage(
												clickAppInfo.getPackageName());
								startActivity(intent);
								popWindowDismiss();
							}
						});
						tvUninstall.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent uninstall_localIntent = new Intent(
										"android.intent.action.DELETE",
										Uri.parse("package:"
												+ clickAppInfo.getPackageName()));
								startActivity(uninstall_localIntent);
								popWindowDismiss();
							}
						});
						popWindowDismiss();
						popupWindow = new PopupWindow(contentView,
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						popupWindow.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						int location[] = new int[2];
						view.getLocationInWindow(location);
						popupWindow.showAtLocation(parent, Gravity.TOP
								+ Gravity.LEFT, 70, location[1]);
						ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f,
								0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
						AnimationSet as = new AnimationSet(false);
						as.addAnimation(aa);
						as.addAnimation(sa);
						as.setDuration(300);
						contentView.startAnimation(as);
					}

				}
			});
		};
	};

	private void popWindowDismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	private void initData() {
		new Thread() {

			public void run() {
				appInfos = AppInfoParser.getAppInfos(AppManagerActivity.this);
				userApps = new ArrayList<AppInfo>();
				systemApps = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					if (info.isUserApp()) {
						userApps.add(info);
					} else {
						systemApps.add(info);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void initView() {
		setContentView(R.layout.activity_app_manager);
		ViewUtils.inject(this);
		long ram_freeSpace = Environment.getDataDirectory().getFreeSpace();
		long sd_freeSpace = Environment.getExternalStorageDirectory()
				.getFreeSpace();
		tvRam.setText("内存可用：" + Formatter.formatFileSize(this, ram_freeSpace));
		tvRom.setText("sd卡可用：" + Formatter.formatFileSize(this, sd_freeSpace));
	}

	class AppsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			AppInfo appInfo;
			if (position == 0) {
				TextView tv = new TextView(AppManagerActivity.this);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户程序(" + userApps.size() + ")");
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position == userApps.size() + 1) {
				TextView tv = new TextView(AppManagerActivity.this);
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
				convertView = View.inflate(AppManagerActivity.this,
						R.layout.app_manager_item, null);
				holder = new ViewHolder();
				holder.tvApkName = (TextView) convertView
						.findViewById(R.id.tvApkName);
				holder.tvSize = (TextView) convertView
						.findViewById(R.id.tvSize);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.tvMem = (TextView) convertView.findViewById(R.id.tvMem);
				convertView.setTag(holder);
			}
			holder.tvApkName.setText(appInfo.getApkName());
			holder.icon.setBackground(appInfo.getIcon());
			holder.tvSize.setText(Formatter.formatFileSize(
					AppManagerActivity.this, appInfo.getApkSize()));
			if (!appInfo.isRom()) {
				holder.tvMem.setText("手机内存");
			} else {
				holder.tvMem.setText("SD卡存储");
			}
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView icon;
		TextView tvSize;
		TextView tvApkName;
		TextView tvMem;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		popWindowDismiss();
	}

}
