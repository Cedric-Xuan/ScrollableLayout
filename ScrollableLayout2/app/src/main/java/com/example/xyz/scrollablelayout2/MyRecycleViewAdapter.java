package com.example.xyz.scrollablelayout2;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xyz on 2017/8/10.
 */


public class MyRecycleViewAdapter extends RecyclerView.Adapter
{

	private Context context;
	public List<String> data = new ArrayList<>();
	private LayoutInflater inflater;

	public MyRecycleViewAdapter(Context context, List<String> data)
	{
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.list_item, parent, false);

		return new CircleNoteItemViewHolder(layout);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		CircleNoteItemViewHolder viewHolder = (CircleNoteItemViewHolder)holder;
		setData(viewHolder, data.get(position));
	}

	@Override
	public int getItemCount()
	{
		if(data != null)
		{
			return data.size();
		}
		return 0;
	}


	class CircleNoteItemViewHolder extends RecyclerView.ViewHolder
	{

		private TextView textView;

		public CircleNoteItemViewHolder(final View itemView)
		{
			super(itemView);

			textView=itemView.findViewById(R.id.textView);

		}

	}


	@Override
	public long getItemId(int position)
	{
		return position;
	}



	/**
	 * 设置数据
	 *
	 * @param text
	 */

	public void setData(final CircleNoteItemViewHolder viewHolder, String text) {
		if(!TextUtils.isEmpty(text)){
			viewHolder.textView.setText(text);
		}
	}



}
