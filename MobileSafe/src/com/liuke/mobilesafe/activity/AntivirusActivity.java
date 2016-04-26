package com.liuke.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.db.dao.AntivirusDao;
import com.liuke.mobilesafe.utils.MD5Utils;

public class AntivirusActivity extends Activity {

	protected static final int BEGIN = 1;
	protected static final int KILLING = 2;
	protected static final int END = 3;
	private TextView tvAntivirus;
	private RotateAnimation animation;
	private ImageView ivActScanning;
	private PackageManager packageManager;
	private LinearLayout llAnti;
	private ProgressBar pbAnti;
	int mCount=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BEGIN:
				tvAntivirus.setText("初始化8核查杀引擎");
				break;
			case KILLING:
				tvAntivirus.setText("全速查杀中...");
				ScanInfo scanInfo=(ScanInfo) msg.obj;
				System.out.println(scanInfo);
				TextView tv=new TextView(AntivirusActivity.this);
				if(scanInfo!=null){
					if(scanInfo.virus){
						tv.setTextColor(Color.RED);
						tv.setText(scanInfo.appName+"  存在病毒风险");
					}else{
						tv.setTextColor(Color.GRAY);
						tv.setText(scanInfo.appName+"  安全");
					}
					llAnti.addView(tv,0);
				}
				break;
			case END:
				ivActScanning.clearAnimation();
				tvAntivirus.setText(mCount+"个软件存在病毒风险");
				break;

			default:
				break;
			}
		};
	};
	
	private void initData() {
		new Thread() {
			public void run() {
				packageManager = getPackageManager();
				List<PackageInfo> installedPackages = packageManager
						.getInstalledPackages(0);
				pbAnti.setMax(installedPackages.size());
				int progress = 0;
				Message msg = Message.obtain();
				msg.what=BEGIN;
				handler.sendMessage(msg);
				SystemClock.sleep(2000);
				for (PackageInfo info : installedPackages) {
					SystemClock.sleep(50);
					handler.sendEmptyMessage(KILLING);
					ScanInfo scanInfo = new ScanInfo();
					scanInfo.appName = info.applicationInfo.loadLabel(
							packageManager).toString();
					String path = info.applicationInfo.sourceDir;
					String md5 = MD5Utils.getFileMd5(path);
					scanInfo.virus = AntivirusDao.isVirus(md5);
					if(AntivirusDao.isVirus(md5)){
						mCount++;
					}
					Message message = Message.obtain();
					message.what=KILLING;
					message.obj=scanInfo;
					handler.sendMessage(message);
					progress++;
					pbAnti.setProgress(progress);
				}
				Message message = Message.obtain();
				message.what=END;
				handler.sendMessage(message);
			};
		}.start();
	}

	static class ScanInfo {
		String appName;
		boolean virus;
	}

	private void initView() {
		setContentView(R.layout.activity_antivirus);
		animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ivActScanning = (ImageView) findViewById(R.id.iv_act_scanning);
		animation.setDuration(5000);
		animation.setRepeatCount(Animation.INFINITE);
		ivActScanning.startAnimation(animation);
		pbAnti = (ProgressBar) findViewById(R.id.pb_anti);
		tvAntivirus = (TextView) findViewById(R.id.tv_init_antivirus);
		llAnti = (LinearLayout) findViewById(R.id.ll_antivirus);
	}

}
