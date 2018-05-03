package com.example.xyz.scrollablelayout2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private MyListViewAdapter listViewAdapter;
    private ScrollableLayout scrollableLayout;
    private ScrollableLayout scrollableInnerLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private List<View> pageViews = new ArrayList<>();
    private List<String> data;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web_view);
        scrollableLayout = (ScrollableLayout) findViewById(R.id.scrollable_layout);
        scrollableInnerLayout = (ScrollableLayout) findViewById(R.id.scrollable_inner_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.toolbar_tab);



//        webView.loadUrl("http://www.cnblogs.com/chaizp/p/5627515.html");
        webView.loadDataWithBaseURL("file:///android_asset/",
                "<html>\n" +
                        "\n" +
                        "<head><script src=\"zepto.js\"></script>\n" +
                        "<style>body{margin:0 auto;padding:0;font-family:'lucida grande','lucida sans unicode',lucida,helvetica,'Hiragino Sans GB','Microsoft YaHei','WenQuanYi Micro Hei',sans-serif;box-sizing:border-box}.container{margin:0 auto;padding:0px 14px 0 14px}.container a:hover{color:#96a8d0}.container a{color:#96a8d0}a:focus,a:hover{text-decoration:none;outline:0}a{color:#96a8d0;text-decoration:none}.content{position:relative;font-size:15px;width:100%;line-height:24px;word-wrap:break-all}.content blockquote{margin:0 0 25px 0;padding:20px;word-break:break-all;border-left:4px solid #999;background-color:#f6f6f6}.content p{color:#2f2e2e;margin:17px 0 17px 0;word-break:break-word;white-space:pre-wrap;line-height:22px;text-align:justify;text-justify:inter-ideograph;}.content blockquote p:last-child{margin-bottom:0}b{}i{font-style:italic}.image-package{margin:10px 0px 10px 0px;text-align:center}.image-package img{max-width:100%;height:auto;vertical-align:middle;border:0;-ms-interpolation-mode:bicubic}a{\n" +
                        "color: #96a8d0}.image-package.vw_loading{position: relative;padding-bottom: 50%;overflow: hidden;background: #c3c3c3;} .image-package.vw_loading img{position: absolute;left:0;top:0;}isandroid{}</style>\n" +
                        "\n" +
                        "\n" +
                        " <script language=\"javascript\"> $(function(){$('img').click(function(){ var pos = $(this).data('pos'); window.clickOnAndroid.clickImage(pos);});}) </script>\n" +
                        " <script language=\"javascript\"> $(function(){$('a').click(function(){ var protocol = $(this).attr('href'); window.clickOnAndroid.clickLink(protocol);return false;})}) </script>\n" +
                        " </head>\n" +
                        "\n" +
                        "\n" +
                        " <body><div class=\"container\">\n" +
                        " <div class=\"content\"><p>樱花飘落的速度是秒速5cm</p>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfe612b09.jpg_l640-hd\" data-oh=\"277\" data-ow=\"500\" data-pos=\"0\" ></div>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfe9af6fe.gif_l640-hd\" data-oh=\"281\" data-ow=\"500\" data-pos=\"1\" ></div>\n" +
                        " <br/>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfeab0c13.gif_l640-hd\" data-oh=\"225\" data-ow=\"400\" data-pos=\"2\" ></div>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfea51b6b.gif_l640-hd\" data-oh=\"264\" data-ow=\"500\" data-pos=\"3\" ></div>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfedb1012.gif_l640-hd\" data-oh=\"240\" data-ow=\"427\" data-pos=\"4\" ></div>\n" +
                        " <div class=\"image-package\"><img src=\"http://xsns-alioss.circle520.com/api/20170925/06/59c8dfecbd1f4.gif_l640-hd\" data-oh=\"250\" data-ow=\"500\" data-pos=\"5\" ></div>\n" +
                        " </div></div></body>\n" +
                        " </html>\n" +
                        " \n" +
                        " \n" +
                        " <script>var W=document.body.clientWidth;var imgs=document.getElementsByTagName(\"img\");for(var n=0;n<imgs.length;n++){var cc_item=imgs[n];if(cc_item&&cc_item.getAttribute){var src=cc_item.getAttribute(\"src\");var cc_w=cc_item.getAttribute(\"data-ow\")||500;var cc_h=cc_item.getAttribute(\"data-oh\")||312;if(cc_w&&cc_h&&src){var cc_p=cc_h/cc_w;if(cc_w<W){cc_p=cc_h/W}cc_item.parentElement.className+=\" vw_loading\";cc_item.parentElement.style.paddingBottom=Math.round(cc_p*100)+\"%\";(function(cc_item){var img=new Image();img.onload=function(){cc_item.parentElement.style.paddingBottom=0;cc_item.parentElement.className=cc_item.parentElement.className.replace(\"vw_loading\",\"\")};img.src=src})(cc_item)}}};</script>"
                , "text/html", "utf-8", "");


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");


        PageOne pageOne = new PageOne(getApplicationContext());
        pageViews.add(pageOne);

        PageTwo pageTwo = new PageTwo(getApplicationContext());
        pageViews.add(pageTwo);

        myViewPagerAdapter = new MyViewPagerAdapter(pageViews);
        viewPager.setAdapter(myViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initTabToolBar(getApplicationContext());


        scrollableLayout.getHelper().setCurrentScrollableContainer(viewPager);
        scrollableInnerLayout.getHelper().setCurrentScrollableContainer(webView);





    }


    private ToolBarItemView toolBarItemThread;
    private ToolBarItemView toolBarItemActivity;
    /**初始化顶部导航栏**/
    private void initTabToolBar(Context context) {

            tabLayout.setTabTextColors(0xff808080, 0xff809fd8);


        ColorStateList colorStateList = tabLayout.getTabTextColors();
        TabLayout.Tab item1 = tabLayout.getTabAt(0);
        TabLayout.Tab item2 = tabLayout.getTabAt(1);

        //帖子
        if (item1 != null) {
            toolBarItemThread = new ToolBarItemView(context);
            toolBarItemThread.setText("最新");
            toolBarItemThread.setContentTextColor(tabLayout.getTabTextColors());
            item1.setCustomView(toolBarItemThread);
        }

        //活动
        if (item2 != null) {
            toolBarItemActivity = new ToolBarItemView(context);
            toolBarItemActivity.setText("精选");
            toolBarItemActivity.setContentTextColor(colorStateList);
            item2.setCustomView(toolBarItemActivity);
        }

    }


    //单个tab的View
    class ToolBarItemView extends FrameLayout {
        private LayoutInflater inflater;
        private TextView textView;

        public ToolBarItemView(Context context){
            super(context);
            initView(context);
        }

        public ToolBarItemView(Context context,AttributeSet attributeSet){
            super(context,attributeSet);
            initView(context);
        }

        public ToolBarItemView(Context context,AttributeSet attributeSet,int defStyle){
            super(context,attributeSet,defStyle);
            initView(context);
        }

        private void initView(Context context){
            inflater=LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.circle_detail_info_tab,null);
            addView(view);

            textView=(TextView) view.findViewById(R.id.circle_tv_toolbar);
        }

        public void setContentTextColor(ColorStateList color)
        {
            textView.setTextColor(color);
        }

        public void setContentTextColor(int color)
        {
            textView.setTextColor(color);
        }

        public void setFousable()
        {
            textView.requestFocus();
        }

        public void setText(String text)
        {
            textView.setText(text);
        }

    }






    /**
     * ViewPager Adapter
     **/
    class MyViewPagerAdapter extends PagerAdapter
    {

        private List<View> mLists;

        public MyViewPagerAdapter(List<View> lists)
        {
            this.mLists = lists;
        }

        @Override
        public int getCount()
        {
            return mLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            container.addView(mLists.get(position));
            return mLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView(mLists.get(position));
        }
    }



}
