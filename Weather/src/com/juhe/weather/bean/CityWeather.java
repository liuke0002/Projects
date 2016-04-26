package com.juhe.weather.bean;


public class CityWeather {
	
	public String resultcode;
	public String reason;
	public WeatherInfo result;
	public class WeatherInfo{
		public SK sk;
		public TODAY today;
		public FUTURE future;
	}
	
	public class FutureWeather{
		public String temperature;
		public String weather;
		public String week;
		public String date;
		public String wind;
		public WEATHER_ID weather_id;
	}
	
	public class FUTURE{
		public FutureWeather day_20160411;
	}
	
	
	
	public class SK{
		public String temp;
		public String wind_direction;
		public String wind_strength;
		public String humidity;
		public String time;
	}
	public class TODAY{
		public String wind;
		public String city;
		public String dressing_advice;
		public String dressing_index;
		public String travel_index;
		public String exercise_index;
		public String date_y;
		public String uv_index;
		public String week;
		public String wash_index;
		public String temperature;
		public String weather;
		public WEATHER_ID weather_id;
	}
	public class WEATHER_ID{
		public String fa;
		public String fb;
	}
	
	
}
