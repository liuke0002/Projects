package liuke.exam.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import liuke.exam.R;
import liuke.exam.biz.IExamBiz;
import liuke.exam.entity.ExamInfo;
import liuke.exam.entity.Question;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ExamActivity extends Activity implements OnClickListener{

	IExamBiz mBiz;
	ExamInfo mExamInfo;
	Gallery mGallery;
	QuestionAdapter adapter;
	TextView mtvExamInfo,mtvLeftTime;
	EditText metQuestion;
	CheckBox[]mchkOptions;
	int mPosition;
	Timer mTimer;
	ArrayList<Integer>mSelectedItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);
		initData();
		initView();
		beginExam();
		setListener();
	}


	private void setListener() {
		// TODO Auto-generated method stub
		setOnItemListener();
		for(CheckBox chk:mchkOptions){
			chk.setOnClickListener(this);
		}
	}


	private void setOnItemListener() {
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mBiz.saveUserAnswers(mPosition, getUserAnswers());
				setSelectedItem();
				mPosition=position%mExamInfo.getQuestionCount();
				showQuestion();//显示新选题目
			}
		});
	}


	protected void setSelectedItem() {
		// TODO Auto-generated method stub
		ArrayList<String>userAnswers=mBiz.getQuestion(mPosition).getUserAnswers();
		if(mSelectedItems.contains(mPosition)){
			if(userAnswers.isEmpty()){
				adapter.removeSelectedItem(mPosition);
			}
		}else{//考题未做过
			if(!userAnswers.isEmpty()){
				adapter.addSelectedItem(mPosition);
			}
		}
	}


	protected void showQuestion() {
		// TODO Auto-generated method stub
		for(CheckBox chk:mchkOptions){
			chk.setChecked(false);
		}
		Question q=mBiz.getQuestion(mPosition);
		metQuestion.setText(q.toString());
		ArrayList<String>userAnswers=q.getUserAnswers();
		if(userAnswers.isEmpty()){
			return;
		}
		for(int i=0;i<userAnswers.size();i++){
			char answer=userAnswers.get(i).charAt(0);
			mchkOptions[answer-65].setChecked(true);
		}
	}


	protected ArrayList<String> getUserAnswers() {
		ArrayList<String>userAnswers=new ArrayList<String>();
		for(int i=0;i<mchkOptions.length;i++){
			if(mchkOptions[i].isChecked()){
				userAnswers.add(mchkOptions[i].getText().toString());
			}
		}
		return userAnswers;
	}


	private void beginExam() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				mExamInfo  = mBiz.beginExam();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mtvExamInfo.setText(mExamInfo.toString());
						adapter=new QuestionAdapter(ExamActivity.this, mSelectedItems, mExamInfo);;
						mGallery.setAdapter(adapter);

					}
				});
				startTime();
			};
		}.start();
	}

	protected void startTime() {
		long start=System.currentTimeMillis();
		final long end=start+mExamInfo.getLimittTime()*60*1000;
		mTimer=new Timer();
		mTimer.schedule(new TimerTask() {

			long minute,second;
			@Override
			public void run() {
				long leftTime=end-System.currentTimeMillis();
				minute=leftTime/1000/60;
				second=leftTime/1000%60;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mtvLeftTime.setText("剩余时间："+minute+":"+second);
					}
				});
			}
		}, 0, 1000);
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				mTimer.cancel();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						commit();
					}
				});
			}
		}, end);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mtvExamInfo=(TextView) findViewById(R.id.tvExamInfo);
		mtvLeftTime=(TextView) findViewById(R.id.tvLeftTime);
		metQuestion=(EditText) findViewById(R.id.etQuestion);
		mchkOptions=new CheckBox[4];
		mchkOptions[0]=(CheckBox) findViewById(R.id.chkA);
		mchkOptions[1]=(CheckBox) findViewById(R.id.chkB);
		mchkOptions[2]=(CheckBox) findViewById(R.id.chkC);
		mchkOptions[3]=(CheckBox) findViewById(R.id.chkD);
		mGallery=(Gallery) findViewById(R.id.gallery);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mBiz=(IExamBiz) getIntent().getSerializableExtra("biz");
		mSelectedItems=new ArrayList<Integer>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.exam, menu);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.mi_item){
			commit();
		}
		return super.onOptionsItemSelected(item);
	}

	private void commit() {
		// TODO Auto-generated method stub
		int score=mBiz.over();
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.exam_commit32x32)
		.setTitle("交卷")
		.setMessage(mBiz.getUser().getName()+"成绩："+score+"分")
		.setPositiveButton("返回", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.create().show();
	}

	class QuestionAdapter extends BaseAdapter{

		Context context;
		ArrayList<Integer>selectedItems;
		ExamInfo examInfo;



		public QuestionAdapter(Context context,
				ArrayList<Integer> selectedItems, ExamInfo examInfo) {
			super();
			this.context = context;
			this.selectedItems = selectedItems;
			this.examInfo = examInfo;
		}

		public ArrayList<Integer>getSelectedItem(){
			return selectedItems;
		}

		public void removeSelectedItem(Integer position){
			selectedItems.remove(position);
			notifyDataSetChanged();
		}
		public void addSelectedItem(int position){
			selectedItems.add(position);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView=View.inflate(context, R.layout.item_question, null);
				holder=new ViewHolder();
				holder.ivThumb=(ImageView) convertView.findViewById(R.id.ivThumb);
				holder.tvQuestion=(TextView) convertView.findViewById(R.id.tvQuestion);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			position=position%examInfo.getQuestionCount();
			holder.tvQuestion.setText("题"+(position+1));
			if(selectedItems.contains(position)){
				holder.ivThumb.setImageResource(R.drawable.answer24x24);
			}else{
				holder.ivThumb.setImageResource(R.drawable.ques24x24);
			}
			return convertView;
		}
		final class ViewHolder{
			ImageView ivThumb;
			TextView tvQuestion;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v instanceof CheckBox){
			if(mSelectedItems.contains(mPosition)){
				for(CheckBox chk:mchkOptions){
					if(chk.isChecked()){
						return;
					}
				}
				adapter.removeSelectedItem(mPosition);
			}else{
				for(CheckBox chk:mchkOptions){
					if(chk.isChecked()){
						adapter.addSelectedItem(mPosition);
						break;
					}
				}
			}
		}
	}
}

