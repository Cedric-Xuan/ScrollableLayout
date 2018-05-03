package com.example.xyz.scrollablelayout2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y on 2017/11/30.
 */

public class PageTwo extends RelativeLayout{
    private List<String> data;
    private MyListViewAdapter listViewAdapter;
    private ListView listView;
    private int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    private LinearLayoutManager layoutManager;
    private MyRecycleViewAdapter myRecycleViewAdapter;
    private RecyclerView myRecycleView;


    public PageTwo(Context context, AttributeSet attributes, int defStyle){
        super(context,attributes,defStyle);
        initView();
    }

    public PageTwo(Context context, AttributeSet attributes){
        super(context,attributes);
        initView();
    }

    public PageTwo(Context context){
        super(context);
        initView();
    }


    public void initView() {

//        LayoutParams params = new LayoutParams(MP,MP);
//        listView = new ListView(getContext());
//        addView(listView,params);
//
        data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        data.add("9");
        data.add("10");
        data.add("11");
        data.add("12");
        data.add("13");
        data.add("14");
        data.add("15");
        data.add("16");
        data.add("17");
        data.add("18");
        data.add("19");
        data.add("20");
        data.add("21");
        data.add("22");
        data.add("23");
        data.add("24");
        data.add("25");
        data.add("26");
        data.add("27");
        data.add("28");
        data.add("29");
        data.add("30");
        data.add("31");
//
//        listViewAdapter = new MyListViewAdapter(getContext(),data);
//        listView.setAdapter(listViewAdapter);


        LayoutParams params = new LayoutParams(MP,MP);
        myRecycleView = new RecyclerView(getContext());
        addView(myRecycleView,params);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecycleView.setLayoutManager(layoutManager);

        myRecycleViewAdapter = new MyRecycleViewAdapter(getContext(),data);
        myRecycleView.setAdapter(myRecycleViewAdapter);


    }


}
