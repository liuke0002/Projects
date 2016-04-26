package com.liuke.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuke.zhbj.R;

public class RefleshListView extends ListView implements
		android.widget.AdapterView.OnItemClickListener,
		android.widget.AbsListView.OnScrollListener {

	private int mHeaderHeight;
	private View headerView;
	private int startY;
	public final static int PULL_REFRESH = 0;
	public final static int REFRESHING = 1;
	public final static int RELEASE_REFRESH = 2;
	private RotateAnimation downAnim;
	private RotateAnimation upAnim;
	private int mCurrentState = PULL_REFRESH;
	private TextView tv_time;
	private TextView tv_state;
	private ImageView iv_arrow;
	private ProgressBar pb_rotate;
	private boolean isLoadMore=false;

	public RefleshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeadView();
	}

	public RefleshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadView();
	}

	public RefleshListView(Context context) {
		super(context);
		initHeadView();
	}

	private void initHeadView() {
		headerView = View.inflate(getContext(), R.layout.header_listview, null);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		tv_state = (TextView) headerView.findViewById(R.id.tv_state);
		iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pb_rotate = (ProgressBar) headerView.findViewById(R.id.pb_rotate);
		headerView.measure(0, 0);
		mHeaderHeight = headerView.getMeasuredHeight();
		addHeaderView(headerView);
		initAnim();
		headerView.setPadding(0, -mHeaderHeight, 0, 0);
		initFootView();
	}
	private void initFootView(){
		footView = View.inflate(getContext(), R.layout.footer_listview, null);
		footView.measure(0, 0);
		mFootViewHeight = footView.getMeasuredHeight();
		footView.setPadding(0, -mFootViewHeight, 0, 0);
		addFooterView(footView);
		setOnScrollListener(this);
	}

	private void initAnim() {
		downAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		downAnim.setDuration(300);
		downAnim.setFillAfter(true);
		upAnim = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
				0.5f);
		upAnim.setDuration(300);
		upAnim.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endY = (int) ev.getY();
			int dy = endY - startY;
			if (mCurrentState == REFRESHING) {
				break;
			}
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int paddingTop = dy - mHeaderHeight;
				headerView.setPadding(0, paddingTop, 0, 0);
				if (paddingTop >= 0 && mCurrentState != RELEASE_REFRESH) {
					mCurrentState = RELEASE_REFRESH;
					refreshState();
				} else if (paddingTop < 0 && mCurrentState != PULL_REFRESH) {
					mCurrentState = PULL_REFRESH;
					refreshState();
				}

				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (mCurrentState == RELEASE_REFRESH) {
				headerView.setPadding(0, 0, 0, 0);
				mCurrentState = REFRESHING;
				refreshState();
			} else if (mCurrentState == PULL_REFRESH) {
				headerView.setPadding(0, -mHeaderHeight, 0, 0);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		switch (mCurrentState) {
		case PULL_REFRESH:
			iv_arrow.startAnimation(upAnim);
			tv_state.setText("下拉刷新");
			break;
		case RELEASE_REFRESH:
			tv_state.setText("松开刷新");
			iv_arrow.startAnimation(downAnim);
			break;
		case REFRESHING:
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.INVISIBLE);
			tv_state.setText("正在刷新...");
			pb_rotate.setVisibility(View.VISIBLE);
			if (mOnRefreshListener != null) {
				mOnRefreshListener.onRefresh();
			}
			break;

		default:
			break;
		}
	}

	OnRefreshListener mOnRefreshListener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mOnRefreshListener = listener;
	};

	public interface OnRefreshListener {
		void onRefresh();
		void onLoadMore();
	}

	public void completeRefresh(boolean success) {
		if(!isLoadMore){
			mCurrentState = PULL_REFRESH;
			pb_rotate.setVisibility(View.INVISIBLE);
			iv_arrow.setVisibility(View.VISIBLE);
			headerView.setPadding(0, -mHeaderHeight, 0, 0);
			if (success) {
				tv_time.setText("最后刷新"
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
			}
		}else{
			isLoadMore=false;
			footView.setPadding(0, -mFootViewHeight, 0, 0);
		}
	}

	OnItemClickListener mlistener;
	private View footView;
	private int mFootViewHeight;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		mlistener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mlistener != null) {
			mlistener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
			if(getLastVisiblePosition()==getCount()-1){
				isLoadMore=true;
				footView.setPadding(0, 0, 0, 0);
				setSelection(getCount()-1);
				if(mOnRefreshListener!=null){
					mOnRefreshListener.onLoadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

}
