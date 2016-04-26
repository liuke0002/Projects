package com.liuke.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.liuke.zhbj.R;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.pager.impl.GovAffairsPager;
import com.liuke.zhbj.pager.impl.HomePager;
import com.liuke.zhbj.pager.impl.NewsCenterPager;
import com.liuke.zhbj.pager.impl.SettingPager;
import com.liuke.zhbj.pager.impl.SmartServicePager;

public class ContentFragment extends BaseFragment {

	private ViewPager vp_content;
	private RadioGroup rg_bottom;
	private List<BasePager> mPagers;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		vp_content = (ViewPager) view.findViewById(R.id.vp_content);
		rg_bottom = (RadioGroup) view.findViewById(R.id.rg_bottom);
		return view;
	}

	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsCenterPager(mActivity));
		mPagers.add(new SmartServicePager(mActivity));
		mPagers.add(new GovAffairsPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		vp_content.setAdapter(new ContentAdapter());
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mPagers.get(position).initData();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		rg_bottom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.bottom_tab_home:
					vp_content.setCurrentItem(0, false);
					break;
				case R.id.bottom_tab_news:
					vp_content.setCurrentItem(1, false);
					break;
				case R.id.bottom_tab_smart:
					vp_content.setCurrentItem(2, false);
					break;
				case R.id.bottom_tab_gov:
					vp_content.setCurrentItem(3, false);
					break;
				case R.id.bottom_tab_setting:
					vp_content.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mPagers.get(position).mRootView);
			if(position==0){
				mPagers.get(position).initData();
			}
			return mPagers.get(position).mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
	public NewsCenterPager getNewsCenterPager(){
		return (NewsCenterPager) mPagers.get(1);
	}

}
