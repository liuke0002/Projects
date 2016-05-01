package com.liuke.zhbj.receiver;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyPushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
			
		}else if(action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
			
		}else if(action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)){
			
		}
	}

}
