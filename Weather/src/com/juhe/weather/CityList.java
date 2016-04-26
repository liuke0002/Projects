package com.juhe.weather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class CityList extends Activity {

	List<String> cityList=new ArrayList<String>();
	ListView lv;
	private ImageView iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		setListener();
	}

	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = getIntent();
				intent.putExtra("select_city", cityList.get(position));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void initData() {
		cityList.add("北京");
		cityList.add("西安");
		cityList.add("上海");
		cityList.add("天津");
		cityList.add("重庆");
		cityList.add("哈尔滨");
		cityList.add("齐齐哈尔");
		cityList.add("太原");
		cityList.add("沧州");
		cityList.add("秦皇岛");
		cityList.add("咸阳");
		cityList.add("宝鸡");
		cityList.add("延安");
		cityList.add("榆林");
		cityList.add("渭南");
		cityList.add("商洛");
		cityList.add("安康");
		cityList.add("汉中");
		cityList.add("铜川");
		cityList.add("济南");
		cityList.add("乌鲁木齐");
		cityList.add("成都");
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_city);
		lv=(ListView) findViewById(R.id.lv_city);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList));
	}
}
