package liuke.yourpwd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {
	MyApplication myApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);  
        myApplication = (MyApplication) getApplication();  
          
        final EditText editTextPassword = (EditText) findViewById(R.id.etPassword);  
          
        findViewById(R.id.btnSure).setOnClickListener(new OnClickListener() {  
    
            public void onClick(View v) {  
  
                String password = editTextPassword.getText().toString();  
                if (password != null && password.equals(myApplication.password)) {  
                    myApplication.isLocked = false;  
                    PasswordActivity.this.finish();  
                } else {  
                    Toast.makeText(PasswordActivity.this, "√‹¬Î¥ÌŒÛ£°", Toast.LENGTH_SHORT).show();  
                    editTextPassword.setText("");  
                }  
            }  
        });  
	}
}
