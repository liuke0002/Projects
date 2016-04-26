package com.liuke.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
	public final static String PREF_NAME = "config";

	public static boolean getBoolean(Context context, boolean defaultValue,
			String key) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static void putString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context, String defaultValue,
			String key){
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

}
