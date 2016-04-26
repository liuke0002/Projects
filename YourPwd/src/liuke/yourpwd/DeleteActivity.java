package liuke.yourpwd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import liuke.sqlite_e.E_OpenHelper;

public class DeleteActivity extends BaseActivity {
	private E_OpenHelper OpenHelper;
	private SQLiteDatabase dbRead=null,dbWrite=null;
	private SimpleCursorAdapter adapter;
	public static final String DB_NAME="DBTest";
	private  OnItemClickListener ListViewClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(DeleteActivity.this).setIcon(R.drawable.delete).setTitle("温馨提醒").setMessage("hi,您确定删除该项吗？删除后将不再出现在您的列表中哦!").setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Cursor c=adapter.getCursor();
					c.moveToPosition(position);
					int itemId=c.getInt(c.getColumnIndex("_id"));
					dbWrite.delete("Users", "_id=?", new String[]{itemId+""});
					refreshListView();
					
				}
			}).show();
			
		}
	};
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		OpenHelper=new E_OpenHelper(this, DB_NAME, null, 1);
		listView=(ListView) findViewById(R.id.listView);
		dbRead=OpenHelper.getReadableDatabase();
		dbWrite=OpenHelper.getWritableDatabase();
		adapter=new SimpleCursorAdapter(this, R.layout.display_list_item, null, new String[]{"username","password"}, new int[]{R.id.display_username,R.id.display_password});
		listView.setAdapter(adapter);
		refreshListView();
		listView.setOnItemClickListener(ListViewClickListener);
	}
	private void refreshListView(){
		Cursor c=dbRead.query("Users", null, null, null, null, null, null);
		adapter.changeCursor(c);
	}

}
