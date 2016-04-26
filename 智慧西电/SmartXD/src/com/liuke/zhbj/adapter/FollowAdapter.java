package com.liuke.zhbj.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.liuke.zhbj.R;
import com.liuke.zhbj.bean.RecommandCardBean;
import com.liuke.zhbj.view.RatioImageView;


public class FollowAdapter extends BaseAdapter {
	private List<RecommandCardBean> dataList;
	private Context context;

	private BitmapUtils bitmapUtils;

	public FollowAdapter(List<RecommandCardBean> dataList, Context context) {
		this.dataList = dataList;
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		switch (type) {
		case 0:
			final ViewHolder viewHolder;
			final RecommandCardBean info = dataList.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_follow, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.iv1 = (RatioImageView) convertView
						.findViewById(R.id.iv1);
				viewHolder.iv_like = (ImageView) convertView
						.findViewById(R.id.iv_like);
				viewHolder.iv2 = (RatioImageView) convertView
						.findViewById(R.id.iv2);
				viewHolder.iv3 = (RatioImageView) convertView
						.findViewById(R.id.iv3);
				viewHolder.tv_like_count = (TextView) convertView
						.findViewById(R.id.tv_like_count);
				viewHolder.iv_author = (ImageView) convertView
						.findViewById(R.id.iv_author);
				viewHolder.tv_author_name = (TextView) convertView
						.findViewById(R.id.tv_author_name);
				viewHolder.tv_pub_time = (TextView) convertView
						.findViewById(R.id.tv_pub_time);
				viewHolder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			List<String> image_urls = info.getImage_url();
			switch (image_urls.size()) {
			case 0:
				viewHolder.iv1.setVisibility(View.GONE);
				viewHolder.iv2.setVisibility(View.GONE);
				viewHolder.iv3.setVisibility(View.GONE);
				break;
			case 1:
				bitmapUtils.display(viewHolder.iv1, image_urls.get(0));
				viewHolder.iv2.setVisibility(View.GONE);
				viewHolder.iv3.setVisibility(View.GONE);
				break;
			case 2:
				bitmapUtils.display(viewHolder.iv1, image_urls.get(0));
				bitmapUtils.display(viewHolder.iv2, image_urls.get(1));
				viewHolder.iv3.setVisibility(View.GONE);
				break;
			case 3:
				bitmapUtils.display(viewHolder.iv1, image_urls.get(0));
				bitmapUtils.display(viewHolder.iv2, image_urls.get(1));
				bitmapUtils.display(viewHolder.iv3, image_urls.get(2));
				break;
			default:
				bitmapUtils.display(viewHolder.iv1, image_urls.get(0));
				bitmapUtils.display(viewHolder.iv2, image_urls.get(1));
				bitmapUtils.display(viewHolder.iv3, image_urls.get(2));
				break;

			}
			bitmapUtils.display(viewHolder.iv_author, info.getAuthor_image());
			viewHolder.tv_like_count.setText(info.getLike_count());
			if (info.getHas_praised().equals("1")) {
				viewHolder.iv_like
						.setImageResource(R.drawable.icon_like_detail);
			} else {
				viewHolder.iv_like
						.setImageResource(R.drawable.icon_unlike_detail);
			}
			viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (info.getHas_praised().equals("1")) {
						info.setHas_praised("0");
						int count = Integer.parseInt(info.getLike_count()) - 1;
						info.setLike_count(count + "");
					} else {
						info.setHas_praised("1");
						int count = Integer.parseInt(info.getLike_count()) + 1;
						info.setLike_count(count + "");
					}
					notifyDataSetChanged();
				}
			});
			viewHolder.tv_content.setText(info.getText());
			viewHolder.tv_author_name.setText(info.getAuthor_nickname());
			viewHolder.tv_pub_time.setText(info.getTimestamp());
			break;
		case 1:
			ViewHolder2 viewHolder2;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_fragment_bargin_lv, parent, false);
				viewHolder2 = new ViewHolder2();
				viewHolder2.item_bargin_iv_img = (ImageView) convertView
						.findViewById(R.id.item_bargin_iv_img);
				viewHolder2.item_bargin_tv_sign_up = (TextView) convertView
						.findViewById(R.id.item_bargin_tv_sign_up);
				viewHolder2.item_bargin_tv_tag = (TextView) convertView
						.findViewById(R.id.item_bargin_tv_tag);
				viewHolder2.tv_pubtime=(TextView) convertView.findViewById(R.id.tv_pubtime);
				convertView.setTag(viewHolder2);
			} else {
				viewHolder2 = (ViewHolder2) convertView.getTag();
			}
			viewHolder2.item_bargin_tv_sign_up.setText(dataList.get(position)
					.getActivity_book_num());
			viewHolder2.item_bargin_tv_tag.setText(dataList.get(position)
					.getActivity_title());
			viewHolder2.tv_pubtime.setText(dataList.get(position).getTimestamp());
			bitmapUtils.display(viewHolder2.item_bargin_iv_img, dataList.get(position).getActivity_image_url());
			break;
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		String type = dataList.get(position).getType();
		if (type.equals("0")) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	static class ViewHolder {
		ImageView iv_author;
		TextView tv_author_name;
		TextView tv_pub_time;
		ImageView iv_follow;
		TextView tv_content;
		RatioImageView iv1, iv2, iv3;
		TextView tv_like_count;
		ImageView iv_like;
	}

	static class ViewHolder2 {
		TextView item_bargin_tv_tag;
		ImageView item_bargin_iv_img;
		TextView item_bargin_tv_sign_up;
		TextView tv_pubtime;
	}
}
