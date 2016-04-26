package com.liuke.zhbj.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;



public class CacheUtils {

	public static void setCache(Context context,String result,String cacheName) {
		File cacheDir = context.getCacheDir();
		File file = new File(cacheDir, cacheName);
		if(!file.exists()){
			try {
				BufferedWriter bw=new BufferedWriter(new FileWriter(file));
				bw.write(result);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
