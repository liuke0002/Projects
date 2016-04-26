package liuke.exam.activity;

import liuke.exam.R;
import liuke.exam.biz.ExamBiz;
import liuke.exam.biz.IExamBiz;
import liuke.exam.biz.IdOrPwdException;
import liuke.exam.entity.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;

public class LoginActivity extends BaseActivity {
	
	EditText metId,metPwd;
	RadioButton mrbSaveAll,mrbSaveId,mrbNotSave;
	IExamBiz mBiz;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

	private void initData() {
		SharedPreferences sp=getSharedPreferences("login", MODE_PRIVATE);
		int id = sp.getInt("id", -1);
		if(-1!=id){
			metId.setText(""+id);
		}
		String pwd = sp.getString("password", "");
		metPwd.setText(pwd);
		new Thread(){
			@Override
			public void run() {
				mBiz=new ExamBiz(LoginActivity.this);
			}
		}.start();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		setLoginListener();
		setExitListener();
	}

	private void setExitListener() {
		findViewById(R.id.btnExit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View layout=View.inflate(LoginActivity.this, R.layout.exit_prompt, null);
				AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("ÍË³ö")
				.setView(layout)
				.setPositiveButton("ÍË³ö", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setNegativeButton("È¡Ïû", null);
				builder.create().show();
				
				
			}
		});
	}

	private void setLoginListener() {
		findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id=metId.getText().toString();
				String pwd=metPwd.getText().toString();
				if(TextUtils.isEmpty(id)){
					metId.setError("±àºÅ²»ÄÜÎª¿Õ");
					return;
				}
				int uid=Integer.parseInt(id);
				if(TextUtils.isEmpty(pwd)){
					metPwd.setError("ÃÜÂë²»ÄÜÎª¿Õ");
					return;
				}
				try {
					User user=mBiz.login(uid, pwd);
					saveLoginInfo(uid,pwd);
					Intent intent=new Intent(LoginActivity.this, MainMenuActivity.class);
					intent.putExtra("biz", (ExamBiz)mBiz);
					startActivity(intent);
				} catch (IdOrPwdException e) {
					if(e.getMessage().equals("ÇëÏÈ×¢²á")){
						metId.setError("ÇëÏÈ×¢²á");
					}else if(e.getMessage().equals("ÃÜÂë´íÎó")){
						metPwd.setError("ÃÜÂë´íÎó");
					}
				}
			}
		});
	}

	protected void saveLoginInfo(int id,String pwd) {
		// TODO Auto-generated method stub
		SharedPreferences sp=getSharedPreferences("login", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		if(mrbSaveAll.isChecked()){
			editor.putInt("id",id);
			editor.putString("password",pwd);
			editor.commit();
		}else if(mrbSaveId.isChecked()){
			editor.putInt("id", id);
			editor.commit();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		metId=findViewById_(R.id.etId);
		metPwd=findViewById_(R.id.etPwd);
		mrbNotSave=findViewById_(R.id.rbNotSave);
		mrbSaveAll=findViewById_(R.id.rbSaveAll);
		mrbSaveId=findViewById_(R.id.rbSaveId);
	}


   
    
}
