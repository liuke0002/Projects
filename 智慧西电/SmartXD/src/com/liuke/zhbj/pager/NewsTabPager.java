package com.liuke.zhbj.pager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.liuke.zhbj.R;
import com.liuke.zhbj.activity.NewsItemActivity;
import com.liuke.zhbj.bean.Categories.NewsCenterCategories;
import com.liuke.zhbj.bean.TabDetails;
import com.liuke.zhbj.bean.TabDetails.NewsData;
import com.liuke.zhbj.bean.TabDetails.TopNewsData;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.utils.PrefUtils;
import com.liuke.zhbj.view.RefleshListView;
import com.liuke.zhbj.view.RefleshListView.OnRefreshListener;
import com.viewpagerindicator.CirclePageIndicator;

public class NewsTabPager extends MenuDetailBasePage {

	NewsCenterCategories mTabData;
	private RefleshListView lv_news;
	private ViewPager vp_top_news;
	private View headView;
	private String mUrl;
	private ArrayList<TopNewsData> mTopnewsList;
	private ArrayList<NewsData> mNewsList;
	private TextView tv_top_title;
	private CirclePageIndicator mIndicator;
	private String mMoreUrl;
	private ListNewsAdapter mNewsAdapter;

	public NewsTabPager(Activity activity, NewsCenterCategories tabData) {
		super(activity);
		mTabData = tabData;
		mUrl = GlobalConstants.preUrl + mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_list, null);
		headView = View.inflate(mActivity, R.layout.news_top, null);
		lv_news = (RefleshListView) view.findViewById(R.id.lv_news);
		vp_top_news = (ViewPager) headView.findViewById(R.id.vp_top_news);
		tv_top_title = (TextView) headView.findViewById(R.id.tv_top_title);
		lv_news.addHeaderView(headView);
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
		lv_news.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if(!TextUtils.isEmpty(mMoreUrl)){
					getMoreDataFromServer();
				}else{
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
					lv_news.completeRefresh(false);
				}
			}
		});
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity,NewsItemActivity.class);
				intent.putExtra("jumpUrl", mNewsList.get(position).url);
				String hasReadId = PrefUtils.getString(mActivity, "", "readId");
				if(hasReadId.contains(mNewsList.get(position).id)){
					TextView tvTitle=(TextView) view.findViewById(R.id.tv_news_title);
					tvTitle.setTextColor(Color.GRAY);
				}else{
					PrefUtils.putString(mActivity, "readId", mNewsList.get(position).id+",");
				}
				
				mActivity.startActivity(intent);
				
			}
		});
	}
	
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				lv_news.completeRefresh(true);
				parseData(result,false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				lv_news.completeRefresh(false);
				error.printStackTrace();
				Toast.makeText(mActivity, "请求错误", Toast.LENGTH_SHORT).show();
			}

		});
	}

	private void getMoreDataFromServer(){
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				lv_news.completeRefresh(true);
				parseData(result,true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				lv_news.completeRefresh(false);
				error.printStackTrace();
				Toast.makeText(mActivity, "请求错误", Toast.LENGTH_SHORT).show();
			}

		});
	}
	protected void parseData(String result,boolean hasMore) {
		Gson gson = new Gson();
		TabDetails tabDetails = gson.fromJson(result, TabDetails.class);
		String moreUrl=tabDetails.data.more;
		if(!TextUtils.isEmpty(moreUrl)){
			mMoreUrl = GlobalConstants.preUrl+moreUrl;
		}else{
			mMoreUrl=null;
		}
		if(!hasMore){
			mNewsList = tabDetails.data.news;
		}
		mTopnewsList = tabDetails.data.topnews;
		if (null != mTopnewsList) {
			vp_top_news.setAdapter(new TopNewsAdapter());
			mIndicator = (CirclePageIndicator)headView.findViewById(R.id.indicator);
	        mIndicator.setViewPager(vp_top_news);
	        mIndicator.setSnap(true);
	        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					tv_top_title.setText(mTopnewsList.get(position).title);
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					
				}
			});
	        mIndicator.onPageSelected(0);
	        tv_top_title.setText(mTopnewsList.get(0).title);
	        new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							int currentItem=vp_top_news.getCurrentItem();
							mIndicator.setCurrentItem((++currentItem)%mTopnewsList.size());
						}
					});
				}
			}, 4000, 4000);
		}
		if (null != mNewsList && !hasMore) {
			mNewsAdapter = new ListNewsAdapter();
			lv_news.setAdapter(mNewsAdapter);
		}else if(hasMore){
			mNewsList.addAll(tabDetails.data.news);
			mNewsAdapter.notifyDataSetChanged();
		}

	}
	class TopNewsAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mTopnewsList.size();
			
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
			bitmapUtils.display(imageView, mTopnewsList.get(position%mTopnewsList.size()).topimage);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class ListNewsAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;
		public ListNewsAdapter(){
			bitmapUtils=new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		
		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View view=null;
			if (null == convertView) {
				view = View.inflate(mActivity, R.layout.item_news_list, null);
				holder=new ViewHolder();
				holder.tv_news_title=(TextView) view.findViewById(R.id.tv_news_title);
				holder.tv_pubdate=(TextView) view.findViewById(R.id.tv_pubdate);
				holder.iv_thumb=(ImageView) view.findViewById(R.id.iv_thumb);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			holder.tv_news_title.setText(getItem(position).title);
			holder.tv_pubdate.setText(getItem(position).pubdate);
			bitmapUtils.display(holder.iv_thumb, getItem(position).listimage);
			return view;
		}

	}

	static class ViewHolder {
		TextView tv_news_title;
		TextView tv_pubdate;
		ImageView iv_thumb;
	}
	
}
