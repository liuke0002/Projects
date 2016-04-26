package com.liuke.zhbj.bean;

import java.util.ArrayList;

public class Photos {
	public int retcode;
	public PhotoData data;
	public class PhotoData{
		public String more;
		public String title;
		public ArrayList<PhotoNews>news;
	}
	public class PhotoNews{
		public String id;
		public String listimage;
		public String title;
	}
}
