package liuke.exam.activity;

import android.app.Activity;
import android.view.View;

public class BaseActivity extends Activity {
	
	<T extends View> T findViewById_(int id){
		return (T)findViewById(id);
	}

}
