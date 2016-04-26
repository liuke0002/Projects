package com.liuke.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

public class SmsUtils {
	
	public static boolean backupSms(Activity context,ProgressDialog pd) {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			XmlPullParserFactory factory;
			FileOutputStream os=null;
			ContentResolver resolver = context.getContentResolver();
			Uri uri=Uri.parse("content://sms/");
			String []projection=new String[]{"address","type","date","body"};
			Cursor cursor = resolver.query(uri, projection, null, null, null);
			int sum = cursor.getCount();
			pd.setMax(sum);
			try {
				File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
				os=new FileOutputStream(file);
				factory = XmlPullParserFactory.newInstance();
				XmlSerializer serializer = factory.newSerializer();
				serializer.setOutput(os, "utf-8");
				serializer.startDocument("utf-8", true);
				int count=0;
				serializer.startTag(null, "smss");
				serializer.attribute(null, "sum", sum+"");
				while(cursor.moveToNext()){
					pd.setProgress(count++);
					
					serializer.startTag(null, "sms");
					
					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");
					
					serializer.startTag(null, "type");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "type");
					
					serializer.startTag(null, "date");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "date");
					
					serializer.startTag(null, "body");
					serializer.text(cursor.getString(3));
					serializer.endTag(null, "body");
					
					serializer.endTag(null, "sms");
					
				}
				serializer.endTag(null, "smss");
				serializer.endDocument();
				os.flush();
				os.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else{
			UIUtils.showToast("SDø®Œ¥π“‘ÿ", context);
		}
		return false;
	}

}
