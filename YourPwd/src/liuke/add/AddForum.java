package liuke.add;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import liuke.sqlite_e.E_OpenHelper;
import liuke.yourpwd.R;

public class AddForum extends Activity{
	private ImageButton forumSave;
	private ImageView forumImage;
	private TextView forumText;
	private EditText forumEditText1;
	private EditText forumEditText2;
	private String selected2;
	public static final String DB_NAME="DBTest";
	E_OpenHelper OpenHelper;
	SQLiteDatabase db=null;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_forum);
		OpenHelper=new E_OpenHelper(this, DB_NAME, null, 1);
		forumText=(TextView)findViewById(R.id.forumText);
		forumSave=(ImageButton)findViewById(R.id.forumSave);
		forumSave.setOnClickListener(new imageButtonListener());
		forumImage=(ImageView)findViewById(R.id.forumImage);
		forumEditText1=(EditText) findViewById(R.id.forumEditText1);
		forumEditText2=(EditText) findViewById(R.id.forumEditText2);
		Intent intent=getIntent();
		selected2=intent.getStringExtra("item2");
		if(selected2.equals("CSDN")){
			forumText.setText(selected2);
		}
		else if(selected2.equals("天涯论坛")){
			forumText.setText(selected2);
		}
		else if(selected2.equals("网易论坛")){
			forumText.setText(selected2);
		}
		else if(selected2.equals("新浪电脑")){
			forumText.setText(selected2);
		}
	}
	class imageButtonListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Animation animation=AnimationUtils.loadAnimation(AddForum.this, R.anim.rotate);
			forumImage.startAnimation(animation);
			String s1=forumEditText1.getText().toString();
			String s2=forumEditText2.getText().toString();
			String s3=selected2;
			InsertTb(s1,s2,s3);
		}
		
	}
	public void InsertTb(String userName,String password,String name){
		int flag=-1;
		db=OpenHelper.getWritableDatabase();
		String strUName=userName;
		String strPwd=password;
		String strName=name;
		String sql="Insert into Users(username,password,name) values('"+strUName+"','"+strPwd+"','"+strName+"')";
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("err", " insert failed");
			flag=0;
			Toast.makeText(AddForum.this, "保存失败", Toast.LENGTH_SHORT).show();
		}
		db.close();
		if(flag==-1){
			Toast.makeText(AddForum.this, "已保存", Toast.LENGTH_SHORT).show();
		}
	}
}
