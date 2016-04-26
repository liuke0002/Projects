package com.liuke.zhbj.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

@SuppressLint("NewApi") public class ImageLoader {
	
	private static LruCache<String,Bitmap>mMemoryCache;
	private static ImageLoader mImageLoader;
	private ImageLoader(){
		int maxMemory=(int) Runtime.getRuntime().maxMemory();
		int cacheSize=maxMemory/8;
		mMemoryCache=new LruCache<String, Bitmap>(cacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
			
		};
	}
	public static ImageLoader getInstance(){
		if(null==mImageLoader){
			mImageLoader=new ImageLoader();
		}
		return mImageLoader;
	}
	public void addBitmapToMemoryCache(String key,Bitmap bitmap){
		if(getBitmapFromMemoryCache(key)==null){
			mMemoryCache.put(key, bitmap);
		}
	}
	public Bitmap getBitmapFromMemoryCache(String key){
		return mMemoryCache.get(key);
	}

	public static int caculateInSampleSize(BitmapFactory.Options options,int reqWidth){
		int outWidth = options.outWidth;
		int widthRatio=Math.round(outWidth/reqWidth);
		
		return widthRatio;
	}
	public static Bitmap decodeSampledBitmapFromResource(String pathName,int reqWidth){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(pathName,options);
		options.inSampleSize=caculateInSampleSize(options, reqWidth);
		options.inJustDecodeBounds=false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;  
		return BitmapFactory.decodeFile(pathName, options);
	}
	
	
}
