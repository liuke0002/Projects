package liuke.yourpwd;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import liuke.add.AddBank;
import liuke.add.AddOther;

public class AddActivity extends TabActivity {
	private TabHost myTabHost;
	private Button btn_bankSure;
	private Button btn_otherSure;
	private String selected1;
	private String selected2;
	private String radioId;
	private RadioGroup other;
	private RadioGroup bank;
	int []layRes=new int[]{R.id.tab_bank,R.id.tab_forum,R.id.tab_other};
	String []addItem=new String[]{"银行","论坛","其他"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myTabHost=getTabHost();
		LayoutInflater.from(this).inflate(R.layout.tab, myTabHost.getTabContentView(),true);
		other=(RadioGroup) findViewById(R.id.other);
		bank=(RadioGroup) findViewById(R.id.bank);
		bank.setOnCheckedChangeListener(new bankRadioListener());
		other.setOnCheckedChangeListener(new otherRadioListener());
		btn_bankSure=(Button) findViewById(R.id.btn_bankSure);
		btn_bankSure.setOnClickListener(new bankListener());
		btn_otherSure=(Button) findViewById(R.id.btn_otherSure);
		btn_otherSure.setOnClickListener(new otherListener());
		for(int x=0;x<this.layRes.length;x++){
			TabSpec myTab=this.myTabHost.newTabSpec("tab"+x);
			myTab.setIndicator(addItem[x]);
			myTab.setContent(this.layRes[x]);
			this.myTabHost.addTab(myTab);
		}
	}
	class bankRadioListener implements OnCheckedChangeListener{

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			selected1=checkedId+"";
			System.out.println(selected1);
		}
		
	}

	class bankListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(AddActivity.this , AddBank.class);
			intent.putExtra("item1", selected1);
			AddActivity.this.startActivity(intent);
		}
		
	}
	class otherRadioListener implements OnCheckedChangeListener{

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			radioId=checkedId+"";
		}
		
	}
	class otherListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();		
			intent.setClass(AddActivity.this, AddOther.class);
			intent.putExtra("item3", radioId);
			AddActivity.this.startActivity(intent);
		}
		
	}
}






