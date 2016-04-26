package com.liuke.mobilesafe.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumOpenHelper extends SQLiteOpenHelper {

	private final static String NAME = "blackNum.db";

	public BlackNumOpenHelper(Context context) {
		super(context, NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists black(_id integer primary key autoincrement,number varchar(15),mode varchar(4))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
