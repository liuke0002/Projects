package com.liuke.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	public final static String PATH = "data/data/com.liuke.mobilesafe/files/antivirus.db";
	public static boolean  isVirus(String md5){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select _id from datable where md5=?", new String[]{md5});
		if(cursor.moveToNext()){
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}
}
