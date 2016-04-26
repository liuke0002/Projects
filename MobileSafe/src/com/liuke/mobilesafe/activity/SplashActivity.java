package com.liuke.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.utils.StreamUtils;

public class SplashActivity extends Activity {

	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_JSON_ERROR = 1;
	protected static final int CODE_URL_ERROR = 2;
	protected static final int CODE_NET_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;

	private RelativeLayout ll_root;

	private TextView tvVersion;
	private TextView tvProgress;

	private String mVersionName;
	private int mVersionCode;
	private String mDownloadUrl;
	private String mDescription;

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			}
		};
	};
	private SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ll_root = (RelativeLayout) findViewById(R.id.ll_root);
		AlphaAnimation animation=new AlphaAnimation(0.3f, 1);
		animation.setDuration(2000);
		tvVersion = (TextView) findViewById(R.id.tvVersion);
		tvProgress=(TextView) findViewById(R.id.tvProgress);
		String versionName = getVersionName();
		tvVersion.setText("版本号："+versionName);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		copyDb("address.db");
		copyDb("antivirus.db");
		if(autoUpdate){
			checkVersion();
		}else{
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		ll_root.startAnimation(animation);
	}

	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private String checkVersion(){
		final long startTime = System.currentTimeMillis();
		new Thread(){
			public void run() {
				Message msg=Message.obtain();
				HttpURLConnection conn=null;
				try {
					URL url=new URL("http://10.0.2.2:8080/update.json");
					conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.connect();
					if(conn.getResponseCode()==200){
						InputStream is = conn.getInputStream();
						String result = StreamUtils.readFromStream(is);
						//解析json
						try {
							JSONObject jo=new JSONObject(result);
							mVersionName = jo.getString("versionName");
							mVersionCode = jo.getInt("versionCode");
							mDownloadUrl = jo.getString("downloadUrl");
							mDescription = jo.getString("description");
							if(mVersionCode>getVersionCode()){
								msg.what=CODE_UPDATE_DIALOG;
							}else{
								msg.what=CODE_ENTER_HOME;
							}
						} catch (JSONException e) {
							msg.what=CODE_JSON_ERROR;
						}
					}
				} catch (MalformedURLException e) {
					msg.what=CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what=CODE_NET_ERROR;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					long timeUsed=endTime-startTime;
					if(timeUsed<2000){
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
					if(conn!=null){
						conn.disconnect();
					}
				}
			}

		}.start();
		return "";
	}
	//http://10.177.10.170:8080/MoblieSafe2.0.apk
	protected void showUpdateDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("最新版本："+mVersionName)
		.setMessage(mDescription)
		.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				downLoad();
			}
		});
		builder.setNegativeButton("以后再说",new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				enterHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				enterHome();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	protected void downLoad() {
		HttpUtils utils=new HttpUtils();
		tvProgress.setVisibility(View.VISIBLE);
		String target=Environment.getExternalStorageDirectory()+"/update.apk";
		utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("失败");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				tvProgress.setText("下载进度:"+current*100/total+"%");
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setDataAndType(Uri.fromFile(arg0.result),"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			}

		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void enterHome(){
		Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void copyDb(String DbName){
		File file=new File(getFilesDir(), DbName);
		InputStream is=null;
		FileOutputStream fos=null;
		if(file.exists()){
			return;
		}
		try {
			is = getAssets().open(DbName);
			fos=new FileOutputStream(file);
			int len=0;
			byte []buffer=new byte[1024];
			while((len=is.read(buffer))!=-1){
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
