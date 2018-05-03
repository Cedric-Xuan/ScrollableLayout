package com.example.xyz.scrollablelayout2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyz on 2017/10/9.
 */

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> data = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyListViewAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            convertView = mInflater.inflate(R.layout.list_item , null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(data.get(i));

        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    class ViewHolder {
        public TextView textView;
    }

}
