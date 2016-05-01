package com.liuke.zhbj.pager.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.liuke.zhbj.R;
import com.liuke.zhbj.activity.LoginActivity;
import com.liuke.zhbj.global.GlobalConstants;
import com.liuke.zhbj.pager.BasePager;
import com.liuke.zhbj.utils.PrefUtils;
import com.liuke.zhbj.utils.RoundBitmapUtils;

public class SettingPager extends BasePager {

	private View view;
	private ImageView iv_head_icon;
	private TextView tv_name;
	private Button btn_exit;

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tv_title.setText("������ҳ");
		view = View.inflate(mActivity, R.layout.pager_setting, null);
		iv_head_icon = (ImageView) view.findViewById(R.id.iv_head_icon);

		tv_name = (TextView) view.findViewById(R.id.tv_nick_name);
		iv_head_icon.setImageBitmap(RoundBitmapUtils
				.makeRoundCorner(BitmapFactory.decodeResource(
						mActivity.getResources(), R.drawable.ic_launcher128)));

		iv_head_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PrefUtils.getBoolean(mActivity, false, "has_login")) {
					mActivity.startActivity(new Intent(mActivity,
							LoginActivity.class));
				}
			}
		});
		btn_exit = (Button) view.findViewById(R.id.btn_exit);

		if (PrefUtils.getBoolean(mActivity, false, "has_login")) {
			tv_name.setText(PrefUtils.getString(mActivity, "����", "name"));
		} else {
			tv_name.setText("������¼/ע��");
			btn_exit.setText("���˳���ǰ�˺�");
		}
		btn_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (PrefUtils.getBoolean(mActivity, false, "has_login")) {
					HttpUtils httpUtils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addQueryStringParameter("name",
							PrefUtils.getString(mActivity, "����", "name"));
					params.addQueryStringParameter("pwd", PrefUtils.getString(mActivity, "","pwd"));
					params.addQueryStringParameter("phone_num", PrefUtils.getString(
							mActivity, "18710890605", "phone_num"));
					params.addQueryStringParameter("flag", "delete");
					httpUtils.send(HttpMethod.POST, GlobalConstants.LOGIN_URL,
							params, new RequestCallBack<String>() {

								@Override
								public void onSuccess(
										ResponseInfo<String> responseInfo) {
									String result = responseInfo.result;
									setExitStatus(result);
								}

								@Override
								public void onFailure(HttpException error,
										String msg) {
									Toast.makeText(mActivity, "����ʧ��", Toast.LENGTH_SHORT).show();
								}

							});
				}

			}
		});

		if (fl_pager.getParent() instanceof ViewGroup) {
			fl_pager.removeAllViews();
		}

		fl_pager.addView(view);
	}

	private void setExitStatus(String result) {
		// TODO Auto-generated method stub
		if (result.equals("3")) {//�˳���ǰ�˺�
			PrefUtils.putBoolean(mActivity, "has_login", false);
			PrefUtils.putString(mActivity, "name", "");
			btn_exit.setText("���˳���ǰ�˺�");
			tv_name.setText("������¼/ע��");
		}
	}

}
