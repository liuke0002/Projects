package com.liuke.zhbj.pager.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.liuke.zhbj.activity.MainActivity;
import com.liuke.zhbj.bean.Categories;
import com.liuke.zhbj.bean.Categories.MenuCategories;
import com.liuke.zhbj.bean.Categories.NewsCenterCategories;
import com.liuke.zhbj.fragment.LeftMenuFragment;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.pager.MenuDetailBasePage;
import com.liuke.zhbj.pager.menu_ipl.InteractDetail;
import com.liuke.zhbj.pager.menu_ipl.NewsDetail;
import com.liuke.zhbj.pager.menu_ipl.PhotoDetail;
import com.liuke.zhbj.pager.menu_ipl.TopicDetail;

public class NewsCenterPager extends BasePager {

	private String mCategoryInfo;
	private ArrayList<MenuCategories> mMenuDetails;
	private ArrayList<MenuDetailBasePage> mNewsPagers;
	private ArrayList<NewsCenterCategories> mNewsTabDetails;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}
	@Override
	public void initView() {
		super.initView();
		ib_menu.setVisibility(View.VISIBLE);
	}
	@Override
	public void initData() {
		super.initData();
		setSlidingMenuState(true);
		getDataFromServer();
		
	}
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.CATEGORIES_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mCategoryInfo = responseInfo.result;
				System.out.println("结果:"+mCategoryInfo);
				parseData();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, "NewsCenterPager请求错误", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parseData() {
		Gson gson = new Gson();
		Categories categories = gson.fromJson(mCategoryInfo, Categories.class);
		if(null!=categories){
			mMenuDetails = categories.data;
			mNewsTabDetails = mMenuDetails.get(0).children;
		}
		MainActivity mainUi=(MainActivity) mActivity;
		LeftMenuFragment menuFragment = mainUi.getLeftMenuContent();
		menuFragment.setLeftMenuData(mMenuDetails);
		mNewsPagers = new ArrayList<MenuDetailBasePage>();
		mNewsPagers.add(new NewsDetail(mActivity,mNewsTabDetails));
		mNewsPagers.add(new TopicDetail(mActivity));
		mNewsPagers.add(new PhotoDetail(mActivity,ib_photo));
		mNewsPagers.add(new InteractDetail(mActivity));
		setCurrentDetailPage(0);
	}
	
	public void setCurrentDetailPage(int position){
		fl_pager.removeAllViews();
		String detailTitle=mMenuDetails.get(position).title;
		tv_title.setText("新闻-"+detailTitle);
		MenuDetailBasePage pager=mNewsPagers.get(position);
		pager.initData();
		if(!(pager instanceof PhotoDetail)){
			ib_photo.setVisibility(View.GONE);
		}
		fl_pager.addView(pager.mRootView);
	}
}
