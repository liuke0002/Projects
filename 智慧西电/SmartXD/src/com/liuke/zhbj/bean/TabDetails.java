package com.liuke.zhbj.bean;

import java.util.ArrayList;

public class TabDetails {
	
	public int retcode;
	public TabDetail data;
	public class TabDetail{
		public String more;
		public String title;
		public ArrayList<TopNewsData> topnews;
		public ArrayList<NewsData> news;
	}
	
	public class NewsData{
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}
	
	public class TopNewsData{
		public String pubdate;
		public String title;
		public String topimage;
		public String url;
		public String id;
		public String news;
	}
	

}
