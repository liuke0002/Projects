package com.liuke.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	public static String readFromStream(InputStream is){
		int len=0;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte [] buffer=new byte[1024];
		try {
			while((len=is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
