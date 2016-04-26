package com.juhe.weather.bean;

import java.util.List;

public class Cities {
	public String resultcode;
	public String reason;
	public List<CityInfo> result;
	public class CityInfo{
		public String id;
		public String province;
		public String city;
		public String district;
	}
}
