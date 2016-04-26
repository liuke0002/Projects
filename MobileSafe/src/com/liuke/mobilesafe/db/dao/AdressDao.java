package com.liuke.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AdressDao {
	public final static String PATH = "data/data/com.liuke.mobilesafe/files/address.db";

	public static String getAdress(String phoneNumber) {
		String address = "δ֪����";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		if(phoneNumber.matches("^1[3-8]\\d{9}$")){
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { phoneNumber.substring(0, 7) });
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		}else if(phoneNumber.matches("^\\d+$")){
			switch (phoneNumber.length()) {
			case 3:
				address="�����绰";
				break;
			case 4:
				address="ģ����";
				break;
			case 5:
				address="�ͷ��绰";
				break;
			case 7:
			case 8:
				address="���ص绰";
				break;
			default:
				if(phoneNumber.startsWith("0")&& phoneNumber.length()>10){
					Cursor cursor = database.rawQuery("select location from data2 where area=?",
							new String[]{phoneNumber.substring(1, 4)});
					if(cursor.moveToNext()){
						address=cursor.getString(0);
					}else{
						cursor.close();
						cursor = database.rawQuery("select location from data2 where area=?",
								new String[]{phoneNumber.substring(1, 3)});
						if(cursor.moveToNext()){
							address=cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		database.close();
		return address;
	}
}
