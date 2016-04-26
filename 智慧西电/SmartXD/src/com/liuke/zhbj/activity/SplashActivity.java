package com.liuke.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.liuke.zhbj.R;
import com.liuke.zhbj.utils.PrefUtils;

public class SplashActivity extends Activity {

	private RelativeLayout rl_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		AnimationSet animationSet = new AnimationSet(false);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(1000);
		rotateAnimation.setDuration(1000);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(rotateAnimation);
		rl_splash.setAnimation(animationSet);
		animationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextPage();
			}
		});
	}

	protected void jumpNextPage() {
		boolean guide_page_show = PrefUtils.getBoolean(this, false, "guide_page_show");
		if(guide_page_show){
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}else{
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		}
		finish();
	}

}
