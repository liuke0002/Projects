package com.liuke.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences mPref;

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] object = (Object[]) intent.getExtras().get("pdus");
		for (Object temp : object) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) temp);
			String messageBody = message.getMessageBody();
			if ("#*alerm*#".equals(messageBody)) {
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.setLooping(true);
				player.start();
				abortBroadcast();
			}else if("location".equals(messageBody)){
				context.startService(new Intent(context, LocationService.class));
				mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = mPref.getString("location", "getting location...");
				System.out.println(location);
				abortBroadcast();
			}
		}
	}

}
