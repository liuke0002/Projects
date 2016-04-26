package com.liuke.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.liuke.mobilesafe.R;

public class ContactActivity extends Activity {

	private ListView lvContacts;
	private Map<String, String> mContact;
	private List<Map<String, String>> mContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		lvContacts = (ListView) findViewById(R.id.lvContacts);
		String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };
		Cursor cursor = getContentResolver().query(Phone.CONTENT_URI,
				projection, null, null, null);
		mContacts = new ArrayList<Map<String, String>>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(Phone.DISPLAY_NAME));
			String number = cursor.getString(cursor
					.getColumnIndex(Phone.NUMBER));
			mContact = new HashMap<String, String>();
			mContact.put("name", name);
			mContact.put("number", number);
			mContacts.add(mContact);
		}
		cursor.close();
		lvContacts.setAdapter(new SimpleAdapter(this, mContacts,
				R.layout.contact_list_item, new String[] { "name", "number" },
				new int[] {R.id.tvName,R.id.tvNum}));
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> contact = mContacts.get(position);
				String number = contact.get("number");
				Intent intent=new Intent();
				intent.putExtra("number", number);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}
		
}
