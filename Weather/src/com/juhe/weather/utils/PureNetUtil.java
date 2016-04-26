package com.juhe.weather.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PureNetUtil {

	public static String get(String url) {
		return post(url, null);
	}

	public static String post(String url, Map<String, String> param) {
		HttpsURLConnection conn = null;
		try {
			URL u = new URL(url);
			conn = (HttpsURLConnection) u.openConnection();
			StringBuffer sb = null;
			if (null != param) {
				sb = new StringBuffer();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				OutputStream outputStream = conn.getOutputStream();
				BufferedWriter bufferedWriter = new BufferedWriter(
						new OutputStreamWriter(outputStream));
				for (Map.Entry s : param.entrySet()) {
					sb.append(s.getKey()).append("=").append(s.getValue())
							.append("&");
				}
				bufferedWriter.write(sb
						.deleteCharAt(sb.toString().length() - 1).toString());
				bufferedWriter.close();
				sb = null;
			}
			conn.connect();
			sb = new StringBuffer();
			int responseCode = conn.getResponseCode();
			BufferedReader br = null;
			if (200 == responseCode) {
				InputStream inputStream = conn.getInputStream();
				br = new BufferedReader(new InputStreamReader(inputStream));
				String str = null;
				sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str).append(System.getProperty("line.separator"));
					;
				}
				br.close();
				if (sb.toString().length() == 0) {
					return null;
				}
				return sb.toString()
						.substring(
								0,
								sb.toString().length()
										- System.getProperty("line.separator")
												.length());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
		}
		return null;
	}
}
