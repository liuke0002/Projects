package liuke.yourpwd;

import android.app.Activity;
import android.content.Intent;

public class BaseActivity extends Activity {
	MyApplication myApplication;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myApplication=(MyApplication)getApplication();
		if(myApplication.isLocked){
			Intent intent = new Intent(this, PasswordActivity.class);  
            startActivity(intent);  
		}
	}

}
