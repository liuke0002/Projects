package com.liuke.zhbj.bean;

import java.util.ArrayList;

public class Categories {
	public int retcode;
	public ArrayList<MenuCategories>data;
	
	
	
	public class MenuCategories{
		public String id;
		public String title;
		public int type;
		public String url;
		public ArrayList<NewsCenterCategories>children;
	}
	
	public class NewsCenterCategories{
		public String id;
		public String title;
		public int type;
		public String url;
	}
}
