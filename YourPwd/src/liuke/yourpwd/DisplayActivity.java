package liuke.yourpwd;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import liuke.sqlite_e.E_OpenHelper;

public class DisplayActivity extends BaseActivity {
	E_OpenHelper OpenHelper;
	SQLiteDatabase db=null;
	private SimpleCursorAdapter adapter;
	public static final String DB_NAME="DBTest";
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		OpenHelper=new E_OpenHelper(this, DB_NAME, null, 1);
		listView=(ListView) findViewById(R.id.listView);
		db=OpenHelper.getReadableDatabase();
		Cursor c=db.query("Users", null, null, null, null, null, null);
//		while(c.moveToNext()){
//			String name=c.getString(c.getColumnIndex("name"));
//			System.out.println(name);
//		}
		adapter=new SimpleCursorAdapter(this, R.layout.display_list_item, c, new String[]{"username","password"}, new int[]{R.id.display_username,R.id.display_password});
		
		listView.setAdapter(adapter);
	}
}
