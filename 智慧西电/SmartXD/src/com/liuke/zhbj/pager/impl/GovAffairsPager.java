package com.liuke.zhbj.pager.impl;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.liuke.zhbj.R;
import com.liuke.zhbj.activity.LoginActivity;
import com.liuke.zhbj.bean.AppointUsers;
import com.liuke.zhbj.bean.AppointUsers.AppointUser;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.utils.PrefUtils;
import com.liuke.zhbj.view.RadarScanView;
import com.liuke.zhbj.view.RandomTextView;

public class GovAffairsPager extends BasePager {

	View view;
	private RandomTextView randomTextView;
	private Button btn_appoint;
	private RadarScanView radarScanView;
	private LocationManager mLm;
	private double latitude;
	private double longitude;
	protected int count;

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tv_title.setText("一起约吧");
		view = View.inflate(mActivity, R.layout.pagger_appoint, null);
		btn_appoint = (Button) view.findViewById(R.id.btn_appoint);
		randomTextView = (RandomTextView) view
				.findViewById(R.id.random_textview);
		radarScanView = (RadarScanView) view.findViewById(R.id.radarScanView);
		btn_appoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PrefUtils.getBoolean(mActivity, false, "has_login")) {
					radarScanView.scaning(true);
					randomTextView.removeAllViews();
					 getLocationInfo();
					getAppointData();
				} else {
					Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
					mActivity.startActivity(new Intent(mActivity,
							LoginActivity.class));
					mActivity.overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				}
			}

			private void getAppointData() {
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams("utf-8");
				params.addQueryStringParameter("phone_num", PrefUtils
						.getString(mActivity, "18710890605", "phone_num"));
				params.addQueryStringParameter("latitude", latitude+"");
				params.addQueryStringParameter("longitude", longitude+"");
				params.addQueryStringParameter("name",
						PrefUtils.getString(mActivity, "刘珂", "name"));
				httpUtils.send(HttpMethod.POST, GlobalConstants.APPOINT_URL,
						params, new RequestCallBack<String>() {

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								System.out.println(result);
								parseResult(result);
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Toast.makeText(mActivity, "服务器返回数据错误",
										Toast.LENGTH_SHORT).show();
								radarScanView.scaning(false);
							}
						});
			}
		});
		fl_pager.addView(view);

	}

	private void parseResult(String result) {
		Gson gson = new Gson();
		final AppointUsers results = gson.fromJson(result, AppointUsers.class);
		if (results.users.size() > 0) {
			final List<AppointUser> list = results.users;
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (count < list.size()) {
								randomTextView.addKeyWord(list.get(count++)
										.getName());
								randomTextView.setTag(count+"");
								randomTextView.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String i=(String) v.getTag();
										AppointUser user = list.get(Integer.parseInt(i));
										String phone=user.getPhone();
						                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
						                mActivity.startActivity(intent);  
									}
								});
								randomTextView.show();
							}else{
								timer.cancel();
								radarScanView.scaning(false);
							}
						}
					});
				}
			}, 2000, 3000);
		} else {
			Toast.makeText(mActivity, "亲，服务器没有内容呢", Toast.LENGTH_SHORT).show();
			radarScanView.scaning(false);
		}

	}

	protected void getLocationInfo() {
		mLm = (LocationManager) mActivity
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = mLm.getBestProvider(criteria, true);
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}

			@Override
			public void onProviderEnabled(String arg0) {
			}

			@Override
			public void onProviderDisabled(String arg0) {
			}

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
				}
			}
		};
		mLm.requestLocationUpdates(bestProvider, 0, 0, locationListener);
		Location location = mLm.getLastKnownLocation(bestProvider);
		if (null != location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	}

}
