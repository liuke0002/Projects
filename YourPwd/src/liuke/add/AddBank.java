package liuke.add;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import liuke.sqlite_e.E_OpenHelper;
import liuke.yourpwd.R;

public class AddBank extends Activity{
	private ImageView bankImage;
	private ImageButton bankSave;
	private EditText bankEditText1;
	private EditText bankEditText2;
	private String selected1;
	public static final String DB_NAME="DBTest";
	E_OpenHelper OpenHelper;
	SQLiteDatabase db=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bank);
		OpenHelper=new E_OpenHelper(this, DB_NAME, null, 1);
		Intent intent=getIntent();
		selected1=intent.getStringExtra("item1");
		bankImage=(ImageView)findViewById(R.id.bankImage);
		bankSave=(ImageButton)findViewById(R.id.bankSave);
		bankSave.setOnClickListener(new imageButtonListener());
		bankEditText1=(EditText) findViewById(R.id.bankEditText1);
		bankEditText2=(EditText) findViewById(R.id.bankEditText2);
		if(selected1.equals("2131230746")){
			bankImage.setImageResource(R.drawable.abc);
		}
		else if(selected1.equals("2131230747")){
			bankImage.setImageResource(R.drawable.cbc);
		}
		else if(selected1.equals("2131230749")){
			bankImage.setImageResource(R.drawable.bbc);
		}
		else if(selected1.equals("2131230748")){
			bankImage.setImageResource(R.drawable.com);
		}
	}
	class imageButtonListener implements OnClickListener{

		public void onClick(View v) {
			String s1=bankEditText1.getText().toString();
			String s2=bankEditText2.getText().toString();
			String s3=selected1;
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
			Toast.makeText(AddBank.this, "±£¥Ê ß∞‹", Toast.LENGTH_SHORT).show();
		}
		db.close();
		if(flag==-1){
			Toast.makeText(AddBank.this, "“—±£¥Ê", Toast.LENGTH_SHORT).show();
		}
	}
}
