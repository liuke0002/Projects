package com.liuke.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String encode(String password){
		try {
			StringBuffer sb=new StringBuffer();
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());
			for(byte b:digest){
				int i=b&0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length()<2){
					hexString="0"+hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getFileMd5(String path){
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			int len=0;
			byte[] flush=new byte[8192];
			MessageDigest digester = MessageDigest.getInstance("MD5");
			while((len=fis.read(flush))!=-1){
				digester.update(flush, 0, len);
			}
			byte[] digest = digester.digest();
			StringBuffer sb=new StringBuffer();
			for(byte b:digest){
				int i=b&0xff;
				String hexString = Integer.toHexString(i);
				if(hexString.length()<2){
					hexString="0"+hexString;
				}
				sb.append(hexString);
			}
			fis.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
