package com.liuke.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.liuke.zhbj.R;
import com.liuke.zhbj.fragment.ContentFragment;
import com.liuke.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

	private static final String CONTENT_FRAGMENT = "content_fragment";
	private static final String MENU_FRAGMENT = "menu_fragment";
	private FragmentManager mFm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.fragment_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(200);
		initFragment();
	}

	private void initFragment() {
		mFm = getSupportFragmentManager();
		FragmentTransaction transaction = mFm.beginTransaction();
		transaction.replace(R.id.fl_menu, new LeftMenuFragment(),MENU_FRAGMENT);
		transaction.replace(R.id.fl_main, new ContentFragment(),
				CONTENT_FRAGMENT);
		transaction.commit();
	}
	public ContentFragment getContentFragmentFromActivity(){
		ContentFragment contentFragment = (ContentFragment) mFm.findFragmentByTag(CONTENT_FRAGMENT);
		return contentFragment;
	}
	public LeftMenuFragment getLeftMenuContent(){
		LeftMenuFragment leftMenuFragment=(LeftMenuFragment) mFm.findFragmentByTag(MENU_FRAGMENT);
		return leftMenuFragment;
	}
}
