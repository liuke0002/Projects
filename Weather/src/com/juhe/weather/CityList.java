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
		cityList.add("����");
		cityList.add("����");
		cityList.add("�Ϻ�");
		cityList.add("���");
		cityList.add("����");
		cityList.add("������");
		cityList.add("�������");
		cityList.add("̫ԭ");
		cityList.add("����");
		cityList.add("�ػʵ�");
		cityList.add("����");
		cityList.add("����");
		cityList.add("�Ӱ�");
		cityList.add("����");
		cityList.add("μ��");
		cityList.add("����");
		cityList.add("����");
		cityList.add("����");
		cityList.add("ͭ��");
		cityList.add("����");
		cityList.add("��³ľ��");
		cityList.add("�ɶ�");
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_city);
		lv=(ListView) findViewById(R.id.lv_city);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList));
	}
}
