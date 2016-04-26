package liuke.yourpwd;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MyApplication extends Application {
	public boolean isLocked=true;
	public String password="2015";
	IntentFilter filter;
	LockScreenReceiver receiver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		receiver=new LockScreenReceiver();
		filter=new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		this.registerReceiver(receiver, filter);
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		this.unregisterReceiver(receiver);
	}
	class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
				isLocked=true;
			}
		}
		
	}

}
