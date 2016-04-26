package com.liuke.zhbj.pager.menu_ipl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.liuke.zhbj.bean.Photos;
import com.liuke.zhbj.bean.Photos.PhotoNews;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.pager.MenuDetailBasePage;

public class PhotoDetail extends MenuDetailBasePage{

	private ArrayList<PhotoNews> mPhotosList;
	private ListView lvPhoto;
	private GridView gvPhoto;
	private PhotoAdapter mAdapter;
	private boolean isLvDisplay=true;
	private ImageButton ib_photo;
	public PhotoDetail(Activity activity,ImageButton ib) {
		super(activity);
		ib_photo=ib;
		ib.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}

	protected void changeDisplay() {
		isLvDisplay=!isLvDisplay;
		if(isLvDisplay){
			ib_photo.setImageResource(R.drawable.icon_pic_grid_type);
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.INVISIBLE);
		}else{
			ib_photo.setImageResource(R.drawable.icon_pic_list_type);
			lvPhoto.setVisibility(View.INVISIBLE);
			gvPhoto.setVisibility(View.VISIBLE);
		}
		ib_photo.setVisibility(View.VISIBLE);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.photo_pager, null);
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		return view;
	}
	@Override
	public void initData() {
		super.initData();
		ib_photo.setVisibility(View.VISIBLE);
		getDataFromServer();
	}
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "«Î«Û¥ÌŒÛ", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	protected void parseData(String result) {
		Gson gson = new Gson();
		Photos photos = gson.fromJson(result, Photos.class);
		mPhotosList = photos.data.news;
		mAdapter = new PhotoAdapter();
		if(mPhotosList!=null){
			lvPhoto.setAdapter(mAdapter);
			gvPhoto.setAdapter(mAdapter);
		}
	}
	class PhotoAdapter extends BaseAdapter{

		private BitmapUtils bitmapUtils;
		public PhotoAdapter(){
			bitmapUtils=new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		@Override
		public int getCount() {
			return mPhotosList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View view=null;
			if(convertView==null){
				view=View.inflate(mActivity, R.layout.item_photo, null);
				holder=new ViewHolder();
				holder.ivPhoto=(ImageView) view.findViewById(R.id.iv_photo);
				holder.tvTitle=(TextView) view.findViewById(R.id.tv_photo);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			holder.tvTitle.setText(mPhotosList.get(position).title);
			bitmapUtils.display(holder.ivPhoto, mPhotosList.get(position).listimage);
			return view;
		}
		
	}
	static class ViewHolder{
		ImageView ivPhoto;
		TextView tvTitle;
	}

}
