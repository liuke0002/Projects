package liuke.exam.activity;


import liuke.exam.R;
import liuke.exam.biz.ExamBiz;
import liuke.exam.biz.IExamBiz;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity {
	
	IExamBiz mBiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		mBiz=(IExamBiz) getIntent().getSerializableExtra("biz");
		setListener();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		setExamListener();
		setExitListener();
	}

	private void setExitListener() {
		findViewById(R.id.btnExit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View layout=View.inflate(MainMenuActivity.this, R.layout.exit_prompt, null);
				AlertDialog.Builder builder=new AlertDialog.Builder(MainMenuActivity.this);
				builder.setTitle("退出")
				.setView(layout)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setNegativeButton("取消", null);
				builder.create().show();
				
				
			}
		});
	}

	private void setExamListener() {
		findViewById(R.id.btnExam).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog dialog=new ProgressDialog(MainMenuActivity.this);
				dialog.setTitle("加载考题");
				dialog.setMessage("加载中。。。稍候");
				dialog.show();
				new Thread(){
					public void run() {
						mBiz.loadQuestions();
						dialog.dismiss();
						Intent intent=new Intent(MainMenuActivity.this, ExamActivity.class);
						intent.putExtra("biz", (ExamBiz)mBiz);
						startActivity(intent);
					};
				}.start();
			}
		});
	}

	

}
