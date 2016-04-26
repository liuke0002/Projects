package com.liuke.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private MyListener mListener;
	private LocationManager mLm;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLm = (LocationManager) getSystemService(LOCATION_SERVICE);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		Criteria criteria=new Criteria();
		criteria.isCostAllowed();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = mLm.getBestProvider(criteria, true);
		mListener = new MyListener();
		mLm.requestLocationUpdates(bestProvider, 0, 0, mListener);
	}
	
	class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			mPref.edit().putString("location", longitude+","+latitude).commit();
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	} 
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mLm.removeUpdates(mListener);//Í£Ö¹¸üÐÂ
	}

}
