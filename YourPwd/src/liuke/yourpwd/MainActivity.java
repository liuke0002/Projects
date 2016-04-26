package liuke.yourpwd;

import java.lang.reflect.Method;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private Button add;
	private Button delete;
	private Button change;
	private Button display;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(new addListener());
        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new deleteListener());
        change=(Button)findViewById(R.id.change);
        change.setOnClickListener(new changeListener());
        display=(Button)findViewById(R.id.display);
        display.setOnClickListener(new displayListener());
    }
   private void consult(){
	   Intent intent=new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("tel:18710890605"));
		startActivity(intent);
	   
   }
    class addListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, AddActivity.class);
			MainActivity.this.startActivity(intent);
		}
    	
    }
    class deleteListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, DeleteActivity.class);
			MainActivity.this.startActivity(intent);
		}
    	
    }
    class changeListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, UpdateActivity.class);
			MainActivity.this.startActivity(intent); 
		}
    	
    }
    class displayListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, DisplayActivity.class);
			MainActivity.this.startActivity(intent); 
			
		}
    	
    }
    private long lastClickTime=0;
    @Override
    public void onBackPressed() {
    	if(lastClickTime<=0){
    		Toast.makeText(this, "再按一次后退键退出", Toast.LENGTH_SHORT).show();
    		lastClickTime=System.currentTimeMillis();
    	}else{
    		long currentClickTime=System.currentTimeMillis();
    		if(currentClickTime-lastClickTime<1000){
    			finish();
    		}else{
    			Toast.makeText(this, "再按一次后退键退出", Toast.LENGTH_SHORT).show();
        		lastClickTime=System.currentTimeMillis();
    		}
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	setIconEnable(menu, true);  
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    private void setIconEnable(Menu menu, boolean enable)  
    {  
        try   
        {  
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");  
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);  
            m.setAccessible(true);  
            m.invoke(menu, enable);  
              
        } catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
    }  
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
		case R.id.menu_consult:
			consult();
			break;
		case R.id.menu_play:
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
}
