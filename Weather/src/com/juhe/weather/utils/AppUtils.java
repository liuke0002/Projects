package com.juhe.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {

	public static boolean isNetworkOK(Context context){
		if(context==null){
			return false;
		}
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info!=null){
			return info.isAvailable();
		}
		return false;
	}
}
