package com.liuke.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {
	
	private AppLockHelper helper;

	public AppLockDao(Context context){
		helper = new AppLockHelper(context);
	}
	
	public void addLock(String packageName){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		db.insert("applock", null, values);
		db.close();
	}
	
	public void deleteLock(String packageName){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packagename=?", new String[]{packageName});
		db.close();
	}
	
	public boolean isLocked(String packageName){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", new String[]{"_id"}, "packagename=?", new String[]{packageName}, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			db.close();
			return true;
		}else{
			cursor.close();
			db.close();
			return false;
		}
	}

}
