package com.liuke.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.zhbj.R;
import com.liuke.zhbj.activity.MainActivity;
import com.liuke.zhbj.bean.Categories.MenuCategories;
import com.liuke.zhbj.pager.impl.NewsCenterPager;

public class LeftMenuFragment extends BaseFragment {

	private List<MenuCategories> mList;
	@ViewInject(R.id.lv_menu)
	private ListView lv_menu;
	private MenuDetailAdapter mAdapter;
	private int mPos;
	private View view;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.left_menu, null);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		lv_menu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPos = position;
				mAdapter.notifyDataSetChanged();
				setCurrentMenuDetailPager(mPos);
			}
		});
	}

	protected void setCurrentMenuDetailPager(int position) {
		ContentFragment contentFragment = ((MainActivity) getActivity()).getContentFragmentFromActivity();
		NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
		newsCenterPager.setCurrentDetailPage(position);
		((MainActivity) getActivity()).getSlidingMenu().toggle();
	}

	public void setLeftMenuData(ArrayList<MenuCategories> menuList) {
		mList = menuList;
		mAdapter = new MenuDetailAdapter();
		if(null!=lv_menu){
			lv_menu.setAdapter(mAdapter);
		}
	}

	class MenuDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public MenuCategories getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.item_menu, null);
			TextView tv_menu_item=(TextView) view.findViewById(R.id.tv_menu_item);
			tv_menu_item.setText(getItem(position).title);
			if(mPos==position){
				tv_menu_item.setEnabled(true);
			}else{
				tv_menu_item.setEnabled(false);
			}
			return view;
		}
	}
}
