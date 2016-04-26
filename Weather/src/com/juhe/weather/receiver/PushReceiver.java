package com.juhe.weather.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.juhe.weather.NotiyDetailActivity;
import com.juhe.weather.R;

public class PushReceiver extends BroadcastReceiver {

	private static final String TAG = "MyReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			Log.i(TAG, "JPush�û�ע��ɹ�");
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.i(TAG, "���ܵ������������Զ�����Ϣ");
			receivingNotification(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.i(TAG, "���ܵ�����������֪ͨ");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.i(TAG, "�û��������֪ͨ");
			openNotification(context, bundle);
		}
	}

	private void openNotification(Context context, Bundle bundle) {
		String content = bundle.getString(JPushInterface.EXTRA_EXTRA, "��˿����");
		try {
			JSONObject jsonObject = new JSONObject(content);
			String toSomeone = jsonObject.getString("content");
			Intent intent = new Intent(context,NotiyDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("content", toSomeone);
			context.startActivity(intent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receivingNotification(Context context, Bundle bundle) {
		int builderId =1;

		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //����Ϊ�Զ���ʧ
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // ����Ϊ�������𶯶�Ҫ
		JPushInterface.setPushNotificationBuilder(builderId , builder);
	}
	
}
