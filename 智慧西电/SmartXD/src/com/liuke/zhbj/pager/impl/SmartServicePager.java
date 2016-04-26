package com.liuke.zhbj.pager.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;

import com.liuke.zhbj.R;
import com.liuke.zhbj.adapter.FollowAdapter;
import com.liuke.zhbj.bean.RecommandCardBean;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.view.RefleshListView;

public class SmartServicePager extends BasePager {

	private View view;
	private RefleshListView lv;

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tv_title.setText("�óԺ���");
		view = View.inflate(mActivity, R.layout.follow_pager, null);
		lv = (RefleshListView) view.findViewById(R.id.lv_follow);
		if (null != lv.getParent()) {
			// (ViewGroup)lv.getParent()
		}

		lv.setAdapter(new FollowAdapter(loadLvDataFromNet(), mActivity));
		fl_pager.removeAllViews();
		fl_pager.addView(view);
	}

	private List<RecommandCardBean> loadLvDataFromNet() {
		List<RecommandCardBean> list = new ArrayList<RecommandCardBean>();
		List<String> image_urls = new ArrayList<String>();
		List<String> article_tag = new ArrayList<String>();
		image_urls.add("image_url1");
		image_urls.add("imageUrl2");
		image_urls.add("imageUrl2");
		for (int i = 0; i < 5; i++) {
			list.add(new RecommandCardBean(
					"0",
					"���� 22:29",
					"article_id",
					"author_id",
					"image_url",
					"��������",
					"�����Ǹ����︴�յļ��ڣ�Ҳ�ǰ�����ѿ�ļ���,�����ס�������е���ѿ��Ҫ��һ���˵�����,��ֻ������һ��,�ͻᷢ����(��)�Ĵ��ڡ�ת���˲�䣬Ե�����㣬�������磬�������",
					article_tag, image_urls, "24", "10", "0", "0"));
			list.add(new RecommandCardBean("1", "1����ǰ", "activity_id", "activity_image_url", "100", "�ǳ�����"));
		}

		lv.completeRefresh(true);
		return list;
	}

}
