package liuke.add;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import liuke.sqlite_e.E_OpenHelper;
import liuke.yourpwd.R;

public class AddOther extends Activity{
	E_OpenHelper OpenHelper;
	SQLiteDatabase db=null;
	private Button otherSave;
	private EditText otherEditText1;
	private EditText otherEditText2;
	public static final String DB_NAME="DBTest";
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_other);
		OpenHelper=new E_OpenHelper(this, DB_NAME, null, 1);
		Intent intent=getIntent();
		String radioId=intent.getStringExtra("item3");
		otherSave=(Button) findViewById(R.id.otherSave);
		otherEditText1=(EditText)findViewById(R.id.otherEditText1);
		otherEditText2=(EditText)findViewById(R.id.otherEditText2);
		if(radioId.equals("2131230753")){
			this.getWindow().setBackgroundDrawableResource(R.drawable.load);
			otherSave.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String s1=otherEditText1.getText().toString();
					String s2=otherEditText2.getText().toString();
					String s3="qq";
					InsertTb(s1,s2,s3);
				}
			});
		}




		else if(radioId.equals("2131230754")){
			this.getWindow().setBackgroundDrawableResource(R.drawable.load1);
			otherSave.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String s1=otherEditText1.getText().toString();
					String s2=otherEditText2.getText().toString();
					String s3="weixin";
					InsertTb(s1,s2,s3);
				}
			});
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
			Toast.makeText(AddOther.this, "±£¥Ê ß∞‹", Toast.LENGTH_SHORT).show();
		}
		db.close();
		if(flag==-1){
			Toast.makeText(AddOther.this, "“—±£¥Ê", Toast.LENGTH_SHORT).show();
		}
	}

}
