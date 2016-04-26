package com.liuke.zhbj.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.liuke.zhbj.R;
import com.liuke.zhbj.utils.PrefUtils;

public class GuideActivity extends Activity {

	private ViewPager guide_viewpager;
	private final static int[] imagesId = { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private List<ImageView> list;
	private LinearLayout ll_dots;
	private int mWidth;
	private ImageView iv_red_dot;
	private Button btn_end_guide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		setListener();
	}

	private void setListener() {
		guide_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if(position==list.size()-1){
					btn_end_guide.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int len = (int) (mWidth * positionOffset)+position*mWidth;
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.leftMargin=len;
				iv_red_dot.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		btn_end_guide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				PrefUtils.putBoolean(GuideActivity.this, "guide_page_show",
						true);
				finish();
			}
		});
	}

	private void initData() {
		list = new ArrayList<ImageView>();
		for (int i = 0; i < imagesId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imagesId[i]);
			list.add(imageView);

		}
		for (int i = 0; i < imagesId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.dot_normal);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				params.leftMargin = 10;
			}
			imageView.setLayoutParams(params);
			ll_dots.addView(imageView);
		}
		guide_viewpager.setAdapter(new GuideViewPager());
		ll_dots.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					// 当layout执行结束后回调此方法
					@Override
					public void onGlobalLayout() {
						ll_dots.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mWidth = ll_dots.getChildAt(1).getLeft()
								- ll_dots.getChildAt(0).getLeft();
					}
				});
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
		ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
		iv_red_dot = (ImageView) findViewById(R.id.iv_red_dot);
		btn_end_guide = (Button) findViewById(R.id.btn_end_guide);
	}

	class GuideViewPager extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = list.get(position);
			container.addView(imageView);
			
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}
	}

}
