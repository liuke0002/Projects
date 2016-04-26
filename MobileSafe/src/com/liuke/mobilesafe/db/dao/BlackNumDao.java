package com.liuke.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.liuke.mobilesafe.bean.BlackNumInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumDao {

	public final BlackNumOpenHelper helper;
	private final static String TABLENAME = "black";

	public BlackNumDao(Context context) {
		helper = new BlackNumOpenHelper(context);
	}

	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("number", number);
		cv.put("mode", mode);
		int insert = (int) db.insert(TABLENAME, null, cv);
		if (insert == -1) {
			return false;
		}
		return true;
	}

	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int delete = db.delete(TABLENAME, "number=?", new String[] { number });
		if (delete == 0) {
			return false;
		}
		return true;
	}

	public boolean changeNumMode(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("mode", mode);
		int update = db.update(TABLENAME, cv, "number=?",
				new String[] { number });
		if (update == 0) {
			return false;
		}
		return true;
	}

	public List<BlackNumInfo> queryAll() {
		List<BlackNumInfo> blackList = new ArrayList<BlackNumInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLENAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String number = cursor.getString(cursor.getColumnIndex("number"));
			String mode = cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumInfo black = new BlackNumInfo(number, mode);
			blackList.add(black);
		}
		cursor.close();
		db.close();
		return blackList;
	}

	public String queryMode(String number) {
		String mode = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLENAME, new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	public List<BlackNumInfo> findPar(int pageNumber, int pageSize) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from " + TABLENAME + "limit ? offset ?",
				new String[] { String.valueOf(pageSize),
						String.valueOf(pageNumber * pageNumber) });
		List<BlackNumInfo>list=new ArrayList<BlackNumInfo>();
		while(cursor.moveToNext()){
			BlackNumInfo info=new BlackNumInfo(cursor.getString(0),cursor.getString(1));
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
	public int getTotalNumber(){
		int count=0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "+ TABLENAME , null);
		cursor.moveToNext();
		count=cursor.getInt(0);
		return count;
	}
}
