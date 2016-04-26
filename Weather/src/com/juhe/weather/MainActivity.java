package com.juhe.weather;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.juhe.weather.bean.CityWeather;
import com.juhe.weather.bean.HoursForecast;
import com.juhe.weather.bean.HoursForecast.WeatherInfo;
import com.juhe.weather.swiperefresh.PullToRefreshBase;
import com.juhe.weather.swiperefresh.PullToRefreshBase.OnRefreshListener;
import com.juhe.weather.swiperefresh.PullToRefreshScrollView;
import com.juhe.weather.utils.AppUtils;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

public class MainActivity extends Activity {

	private PullToRefreshScrollView mPullToRefreshScrollView;
	private ScrollView mScrollView;
	private TextView tv_city,// 城市
			tv_release,// 发布时间
			tv_now_weather,// 天气
			tv_today_temp,// 温度
			tv_now_temp,// 当前温度
			tv_aqi,// 空气质量指数
			tv_quality,// 空气质量
			tv_felt_air_temp,// 体感温度
			tv_next_three_temp,// 3小时温度
			tv_next_six_temp,// 6小时温度
			tv_next_nine_temp,// 9小时温度
			tv_next_twelve_temp,// 12小时温度
			tv_next_fifteen_temp,// 15小时温度
			tv_today_temp_a,// 今天温度a
			tv_today_temp_b,// 今天温度b
			tv_exercise_index,// 晨练指数
			tv_wash_index,// 洗车指数
			tv_humidity,// 湿度
			tv_travel_index,// 旅游指数
			tv_wind, tv_uv_index,// 紫外线指数
			tv_dressing_index,// 穿衣指数
			tv_dressing_advice;// 穿衣建议

	private ImageView iv_now_weather,// 现在
			iv_next_three,// 3小时
			iv_next_six,// 6小时
			iv_next_nine,// 9小时
			iv_next_twelve,// 12小时
			iv_next_fifteen,// 15小时
			iv_today_weather;// 今天

	private RelativeLayout rl_city;
	private CityWeather cityWeather;
	private HoursForecast hoursForecast;
	private TextView tv_string;
	private String select_city="西安";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData(select_city);
	}

	private void initData(String city) {

		getDetailWeatherInfo(city);

		getForecast3hInfo(city);

		mPullToRefreshScrollView.onRefreshComplete();
	}

	private void getForecast3hInfo(String city) {
		if (AppUtils.isNetworkOK(this)) {
			Parameters param = new Parameters();
			param.add("cityname", city);
			JuheData.executeWithAPI(MainActivity.this, 39,
					"http://v.juhe.cn/weather/forecast3h", JuheData.GET, param,
					new DataCallBack() {

						@Override
						public void onSuccess(int statusCode,
								String responseString) {
							if (responseString != null) {
								parseFuture3hData(responseString);
								}
						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onFailure(int statusCode,
								String responseString, Throwable throwable) {
							Toast.makeText(MainActivity.this, "读取数据失败",
									Toast.LENGTH_SHORT).show();
						}

					});
		} else {
			Toast.makeText(this, "请检查您的网络", Toast.LENGTH_SHORT).show();
		}
	}

	private void getDetailWeatherInfo(String city) {
		if (AppUtils.isNetworkOK(this)) {
			Parameters param = new Parameters();
			param.add("cityname", city);
			JuheData.executeWithAPI(MainActivity.this, 39,
					"http://v.juhe.cn/weather/index", JuheData.GET, param,
					new DataCallBack() {

						@Override
						public void onSuccess(int statusCode,
								String responseString) {
							if (responseString != null) {
								parseDetailWeatherData(responseString);
							}
						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onFailure(int statusCode,
								String responseString, Throwable throwable) {
							Toast.makeText(MainActivity.this, "读取数据失败",
									Toast.LENGTH_SHORT).show();
						}

					});
		} else {
			Toast.makeText(this, "请检查您的网络", Toast.LENGTH_SHORT).show();
		}
	}

	protected void parseDetailWeatherData(String responseString) {
		Gson gson = new Gson();
		cityWeather = gson.fromJson(responseString, CityWeather.class);

		setDetailInfoView();
	}

	protected void parseFuture3hData(String responseString) {
		Gson gson = new Gson();
		hoursForecast = gson.fromJson(responseString, HoursForecast.class);
		setHoursView(hoursForecast.result);
	}

	public void set3hView(ImageView iv, TextView tvTemp,
			HoursForecast.WeatherInfo bean) {
		String prefixStr = null;
		int time = Integer.parseInt(bean.sh);
		if (time >= 6 && time < 18) {
			prefixStr = "d";
		} else {
			prefixStr = "n";
		}
		iv.setImageResource(getResources().getIdentifier(
				prefixStr + bean.weatherid, "drawable", "com.juhe.weather"));
		int temp = Integer.parseInt(bean.sh) + Integer.parseInt(bean.eh);
		tvTemp.setText(temp / 2 + "°");
	}

	private void setHoursView(List<WeatherInfo> list) {
		set3hView(iv_next_three, tv_next_three_temp, list.get(0));
		set3hView(iv_next_six, tv_next_six_temp, list.get(1));
		set3hView(iv_next_nine, tv_next_nine_temp, list.get(2));
		set3hView(iv_next_twelve, tv_next_twelve_temp, list.get(3));
		set3hView(iv_next_fifteen, tv_next_fifteen_temp, list.get(4));
	}

	private void setDetailInfoView() {
		tv_now_temp.setText(cityWeather.result.sk.temp + "°");
		tv_dressing_index.setText(cityWeather.result.today.dressing_index);
		tv_string.setText(cityWeather.result.today.date_y);

		tv_humidity.setText(cityWeather.result.sk.humidity);
		tv_city.setText(cityWeather.result.today.city);
		tv_uv_index.setText(cityWeather.result.today.uv_index);
		tv_now_weather.setText(cityWeather.result.today.weather);
		tv_release.setText("更新时间  " + cityWeather.result.sk.time);
		tv_dressing_advice.setText(cityWeather.result.today.dressing_advice);
		tv_travel_index.setText(cityWeather.result.today.travel_index);
		tv_exercise_index.setText(cityWeather.result.today.exercise_index);
		tv_wash_index.setText(cityWeather.result.today.wash_index);
		tv_wind.setText(cityWeather.result.today.wind);
		tv_felt_air_temp.setText(cityWeather.result.sk.temp + "°");
		String[] tempArr = cityWeather.result.today.temperature.split("~");
		String temp_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
		String temp_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
		tv_today_temp.setText("↑ " + temp_a + "°  " + "↓ " + temp_b + "°");
		iv_today_weather.setImageResource(this.getResources().getIdentifier(
				"d" + cityWeather.result.today.weather_id.fa, "drawable",
				"com.juhe.weather"));
		tv_today_temp_a.setText(temp_a + "°");
		tv_today_temp_b.setText(temp_b + "°");
		Calendar c = Calendar.getInstance();
		int time = c.get(Calendar.HOUR_OF_DAY);
		String prefixStr = null;
		if (time >= 6 && time < 18) {
			prefixStr = "d";
		} else {
			prefixStr = "n";
		}
		iv_now_weather.setImageResource(getResources().getIdentifier(
				prefixStr + cityWeather.result.today.weather_id.fa, "drawable",
				"com.juhe.weather"));
		Random random = new Random();
		int aqi = random.nextInt(300);
		if (aqi > 250) {
			tv_quality.setText("重度污染");
		} else if (100 < aqi && aqi < 250) {
			tv_quality.setText("中度污染");
		} else {
			tv_quality.setText("轻度污染");
		}
		tv_aqi.setText(aqi + "");
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						initData(select_city);
					}
				});
		mScrollView = mPullToRefreshScrollView.getRefreshableView();

		rl_city = (RelativeLayout) findViewById(R.id.rl_city);
		rl_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,CityList.class);
				startActivityForResult(intent, 100);
			}
		});

		tv_string = (TextView) findViewById(R.id.tv_string);
		tv_dressing_advice = (TextView) findViewById(R.id.tv_dressing_advice);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_travel_index = (TextView) findViewById(R.id.tv_travel_index);
		tv_exercise_index = (TextView) findViewById(R.id.tv_exercise_index);
		tv_release = (TextView) findViewById(R.id.tv_release);
		tv_now_weather = (TextView) findViewById(R.id.tv_now_weather);
		tv_today_temp = (TextView) findViewById(R.id.tv_today_temp);
		tv_now_temp = (TextView) findViewById(R.id.tv_now_temp);
		tv_aqi = (TextView) findViewById(R.id.tv_aqi);
		tv_quality = (TextView) findViewById(R.id.tv_quality);
		tv_next_three_temp = (TextView) findViewById(R.id.tv_next_three_temp);
		tv_next_six_temp = (TextView) findViewById(R.id.tv_next_six_temp);
		tv_next_nine_temp = (TextView) findViewById(R.id.tv_next_nine_temp);
		tv_next_twelve_temp = (TextView) findViewById(R.id.tv_next_twelve_temp);
		tv_next_fifteen_temp = (TextView) findViewById(R.id.tv_next_fifteen_temp);
		tv_today_temp_a = (TextView) findViewById(R.id.tv_today_temp_a);
		tv_today_temp_b = (TextView) findViewById(R.id.tv_today_temp_b);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		tv_wind = (TextView) findViewById(R.id.tv_wind);
		tv_uv_index = (TextView) findViewById(R.id.tv_uv_index);
		tv_dressing_index = (TextView) findViewById(R.id.tv_dressing_index);
		tv_wash_index = (TextView) findViewById(R.id.tv_wash_index);
		iv_now_weather = (ImageView) findViewById(R.id.iv_now_weather);
		iv_next_three = (ImageView) findViewById(R.id.iv_next_three);
		iv_next_six = (ImageView) findViewById(R.id.iv_next_six);
		iv_next_nine = (ImageView) findViewById(R.id.iv_next_nine);
		iv_next_twelve = (ImageView) findViewById(R.id.iv_next_twelve);
		iv_next_fifteen = (ImageView) findViewById(R.id.iv_next_fifteen);
		iv_today_weather = (ImageView) findViewById(R.id.iv_today_weather);
		tv_felt_air_temp = (TextView) findViewById(R.id.tv_felt_air_temp);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==100&&resultCode==RESULT_OK){
			select_city = data.getStringExtra("select_city");
			if(null!=select_city){
				initData(select_city);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
}
