package com.juhe.weather.bean;

import java.util.List;

public class HoursForecast {
	
	public String resultcode;
	public String reason;
	public List<WeatherInfo> result;
	public class WeatherInfo{
		public String weatherid;
		public String weather;
		public String sh;
		public String eh;
		public String date;
		public String temp1;
		public String temp2;
	}

}
