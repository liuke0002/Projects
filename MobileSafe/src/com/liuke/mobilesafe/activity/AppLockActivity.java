package com.liuke.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.fragment.LockedFragment;
import com.liuke.mobilesafe.fragment.UnLockFragment;

public class AppLockActivity extends FragmentActivity implements
		OnClickListener {

	private FragmentManager fragmentManager;
	private TextView tvLocked;
	private TextView tvUnlock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_app_lock);
		tvLocked = (TextView) findViewById(R.id.tv_locked);
		tvUnlock = (TextView) findViewById(R.id.tv_unlock);
		tvLocked.setOnClickListener(this);
		tvUnlock.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		UnLockFragment unLockFragment = new UnLockFragment();
		transaction.replace(R.id.fl_content, unLockFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_locked:
			LockedFragment lockedFragment = new LockedFragment();
			transaction.replace(R.id.fl_content, lockedFragment);
			tvUnlock.setBackgroundResource(R.drawable.tab_left_default);
			tvLocked.setBackgroundResource(R.drawable.tab_right_pressed);
			break;
		case R.id.tv_unlock:
			UnLockFragment unLockFragment = new UnLockFragment();
			transaction.replace(R.id.fl_content, unLockFragment);
			tvUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tvLocked.setBackgroundResource(R.drawable.tab_right_default);
			break;
		default:
			break;
		}
		transaction.commit();
	}

}
