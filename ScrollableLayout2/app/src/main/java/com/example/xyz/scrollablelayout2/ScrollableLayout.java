package com.example.xyz.scrollablelayout2;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;


public class ScrollableLayout extends LinearLayout {

    private Context context;
    private Scroller mScroller;
    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private boolean mIsHorizontalScrolling;
    private float x_down;
    private float y_down;
    private float x_move;
    private float y_move;
    private float moveDistanceX;
    private float moveDistanceY;

    private View mHeadView;
    private View mTabLayout;
    private ViewPager childViewPager;
    private WebView childWebView;
    private ListView childListView;

    private int deltaWebViewY;
    private int lastChildWebViewScrollY;

    private int deltaTouchScrollY;
    private int lasttouchScrollY;
    private boolean childWebViewNotToScroll = false;
    private boolean isChildWebViewBottom = false;
    private boolean isWebViewOnHead = false;

    private DIRECTION mDirection;
    private int mHeadHeight;
    private int mTabLayoutHeight;
    private int mScrollY;
    private int sysVersion;
    private boolean flag1, flag2;
    private int mLastScrollerY;
    private boolean mDisallowIntercept;

    private int minY = 0;
    private int maxY = 0;

    private int mCurY;
    private int lastCury;
    private boolean isClickHead;
    private int mScrollMinY = 10;

    enum DIRECTION {
        UP,
        DOWN
    }

    public interface OnScrollListener {
        void onScroll(int currentY, int maxY);
    }

    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private ScrollableHelper mHelper;

    public ScrollableHelper getHelper() {
        return mHelper;
    }

    public ScrollableLayout(Context context) { this(context, null); }

    public ScrollableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mHelper = new ScrollableHelper();
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        sysVersion = Build.VERSION.SDK_INT;
        setOrientation(VERTICAL);
        setMotionEventSplittingEnabled(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHeadView = getChildAt(0);
        mTabLayout = getChildAt(1);

        if(mTabLayout != null && mTabLayout instanceof TabLayout){
            measureChildWithMargins(mTabLayout, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
            mTabLayoutHeight = mTabLayout.getMeasuredHeight();
        }else {
            mTabLayoutHeight=0;
        }

        if(mHeadView != null){
            measureChildWithMargins(mHeadView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0);
            maxY = mHeadView.getMeasuredHeight()+mTabLayoutHeight;
            mHeadHeight = mHeadView.getMeasuredHeight()+mTabLayoutHeight;
        }


        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + maxY, MeasureSpec.EXACTLY));
    }



    @Override
    protected void onFinishInflate() {
        if (mHeadView != null && !mHeadView.isClickable()) {
            mHeadView.setClickable(true);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt instanceof ViewPager) {
                childViewPager = (ViewPager) childAt;
            }
            if (childAt != null && childAt instanceof WebView && i==0) {
                childWebView = (WebView) childAt;
                isWebViewOnHead=true;
            }
            if (childAt != null && childAt instanceof ListView && i==1) {
                childListView = (ListView) childAt;
            }

            if(childAt != null && childAt instanceof ScrollableLayout && i==0){
                View grandChildAt = ((ScrollableLayout)(childAt)).getChildAt(1);
                if(grandChildAt != null && grandChildAt instanceof WebView){
                    childWebView = (WebView) grandChildAt;
                    isWebViewOnHead=true;
                }
            }

        }

        super.onFinishInflate();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        float deltaY;
        int shiftX;
        int shiftY;
        shiftX = (int) Math.abs(currentX - mDownX);
        shiftY = (int) Math.abs(currentY - mDownY);



        int touchScrollY = (int)ev.getRawY();
        deltaTouchScrollY =  lasttouchScrollY -touchScrollY;
        lasttouchScrollY = touchScrollY;



        judgeWebViewScrollState(mCurY, deltaTouchScrollY);



        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDisallowIntercept = false;
                mIsHorizontalScrolling = false;
                x_down = ev.getRawX();
                y_down = ev.getRawY();
                flag1 = true;
                flag2 = true;
                mDownX = currentX;
                mDownY = currentY;
                mLastX = currentX;
                mLastY = currentY;
                mScrollY = getScrollY();
                checkIsClickHead((int) currentY, mHeadHeight, getScrollY());
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDisallowIntercept) {
                    break;
                }
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                deltaY = mLastY - currentY;
                if (flag1) {
                    if (shiftX > mTouchSlop && shiftX > shiftY) {
                        flag1 = false;
                        flag2 = false;
                    } else if (shiftY > mTouchSlop && shiftY > shiftX) {
                        flag1 = false;
                        flag2 = true;
                    }
                }

                if (flag2 && shiftY > mTouchSlop && shiftY > shiftX && (!isSticked() || mHelper.isTop())) {
                    if (childViewPager != null) {
                        childViewPager.requestDisallowInterceptTouchEvent(true);
                    }
                    if(isWebViewOnHead?childWebViewNotToScroll:true) {
                        scrollBy(0, (int) (deltaY + 0.5));
                    }

                }
                mLastX = currentX;
                mLastY = currentY;
                x_move = ev.getRawX();
                y_move = ev.getRawY();
                moveDistanceX = (int) (x_move - x_down);
                moveDistanceY = (int) (y_move - y_down);
                if (Math.abs(moveDistanceY) > mScrollMinY && (Math.abs(moveDistanceY) * 0.1 > Math.abs(moveDistanceX))) {
                    mIsHorizontalScrolling = false;
                } else {
                    mIsHorizontalScrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (flag2 && shiftY > shiftX && shiftY > mTouchSlop) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    float yVelocity = -mVelocityTracker.getYVelocity();
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        if (mDirection == DIRECTION.UP && isSticked()) {
                        } else {
                            mScroller.fling(0, getScrollY(), 0, (int) yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                            mLastScrollerY = getScrollY();
                            invalidate();
                        }
                    }


                    if ((isClickHead || !isSticked()) && (isWebViewOnHead?childWebViewNotToScroll:true)) {
                        int action = ev.getAction();
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        boolean dd = super.dispatchTouchEvent(ev);
                        ev.setAction(action);
                        return dd;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (flag2 && isClickHead && (shiftX > mTouchSlop || shiftY > mTouchSlop)) {
                    int action = ev.getAction();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    boolean dd = super.dispatchTouchEvent(ev);
                    ev.setAction(action);
                    return dd;
                }
                break;
            default:
                break;
        }


        super.dispatchTouchEvent(ev);
        return true;
    }



    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (sysVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance / duration;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();

            /**全局惯性滑动事件**/
            judgeWebViewScrollState(mCurY, deltaTouchScrollY);
            Log.e("webScroll",deltaWebViewY+"  "+(isChildWebViewBottom?"T":"F")+ " "+mCurY);



            /**scrollableLayout 整体惯性滑动事件**/
            //惯性上滑
            if (mDirection == DIRECTION.UP) {
                if (isSticked()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                    mHelper.smoothScrollBy(getScrollerVelocity(distance, duration), distance, duration);
                    mScroller.forceFinished(true);

                    return;
                } else {

                    Log.e("aaa",deltaWebViewY+" "+isChildWebViewBottom+" "+mCurY);

                    if(isWebViewOnHead?(isChildWebViewBottom || (isChildWebViewBottom && ((deltaTouchScrollY > 0)  || (deltaWebViewY < 0 )))):false ){

                        int deltaY = (currY - mLastScrollerY);
                        scrollBy(0,deltaY);

                    }else {
                        scrollTo(0, currY);
                    }

                }
                //惯性下滑
            } else {
                if (mHelper.isTop()) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;

                    scrollTo(0, toY);


                    //下滑到达scrollableLayout顶部时将末速度传给webview，以作惯性运动
                    if(isWebViewOnHead?(mCurY<=0):false){
                        int distance = mScroller.getFinalY() - currY;
                        int duration = calcDuration(mScroller.getDuration(), mScroller.timePassed());
                        if(childWebView!=null) {
                            childWebView.flingScroll(0, -getScrollerVelocity(distance, duration));
                        }
                    }


                    Log.e("ac",currY+" "+toY+" "+deltaY);

                    if (mCurY <= minY) {
                        mScroller.forceFinished(true);
                        return;
                    }
                }
                invalidate();
            }
            mLastScrollerY = currY;
        }
    }





    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= maxY) {
            toY = maxY;
        } else if (toY <= minY) {
            toY = minY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if(isWebViewOnHead?childWebViewNotToScroll:true) {

            if (y >= maxY) {
                y = maxY;
            } else if (y <= minY) {
                y = minY;
            }
            lastCury = mCurY;
            mCurY = y;

            if (onScrollListener != null) {
                onScrollListener.onScroll(y, maxY);
            }
            super.scrollTo(x, y);
        }
    }




    public void scrollToSecondPage(){
        if(childWebView!=null){
            childWebView.scrollTo(0, (int)(childWebView.getContentHeight() * childWebView.getScale()));
            super.scrollTo(0,mHeadHeight);
            mCurY=mHeadHeight;
            isChildWebViewBottom=true;
            childWebViewNotToScroll=true;
        }
    }


    private void judgeWebViewScrollState(int mCurY, int deltaTouchScrollY){

        if(childWebView!=null) {
            int childWebViewScrollY = childWebView.getScrollY();
            deltaWebViewY =  lastChildWebViewScrollY - childWebViewScrollY;
            lastChildWebViewScrollY = childWebViewScrollY;

            isChildWebViewBottom = ((int) ((childWebView.getContentHeight() * childWebView.getScale()) - (childWebView.getHeight() + childWebView.getScrollY()))) == 0;
            if ((mCurY > 0 ) || (isChildWebViewBottom && ((deltaTouchScrollY > 0)  || (deltaWebViewY < 0 ) ))) {
                childWebViewNotToScroll = true;
            } else {
                childWebViewNotToScroll = false;
            }

            if((isChildWebViewBottom && deltaTouchScrollY <0) && mCurY <= 0){
                childWebViewNotToScroll = false;
            }


            Log.e("WebViewScrollState1",mCurY+"  "+lastCury+" "+(mCurY - lastCury));
            Log.e("judgeWebViewScrollState",mCurY+"  "+(isChildWebViewBottom?"T":"F")+" "+deltaTouchScrollY +"  "+ (childWebViewNotToScroll?"T":"F")+ "  " + (int)(childWebView.getContentHeight() * childWebView.getScale())+"  "+childWebView.getScrollY());

//            childWebView.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return childWebViewNotToScroll;
//                }
//            });

//            childWebView.requestDisallowInterceptTouchEvent(!childWebViewNotToScroll);

            if(childListView!=null) {
                childListView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return !isSticked();
                    }
                });
            }

            if(childViewPager!=null){

                for(int i=0;i<childViewPager.getChildCount();i++){
                    RelativeLayout page = (RelativeLayout) childViewPager.getChildAt(i);
                    for(int j=0;j<page.getChildCount();j++){
                        View view = page.getChildAt(j);
                        if(view instanceof AbsListView || view instanceof RecyclerView){
                            view.setOnTouchListener(new OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    return !isSticked();
                                }
                            });
                        }
                    }
                }

//                childViewPager.setOnTouchListener(new OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return !isSticked();
//                    }
//                });

            }



//            childListView.requestDisallowInterceptTouchEvent(!childWebViewNotToScroll);


        }
    }


    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void checkIsClickHead(int downY, int headHeight, int scrollY) {
        isClickHead = downY + scrollY <= headHeight;
    }

    private int calcDuration(int duration, int timepass) {
        return duration - timepass;
    }

    public void requestScrollableLayoutDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        mDisallowIntercept = disallowIntercept;
    }

    public boolean isSticked() {
        return mCurY == maxY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setScrollMinY(int y) {
        mScrollMinY = y;
    }

    public boolean isCanPullToRefresh() {
        if (getScrollY() <= 0 && mHelper.isTop() && !mIsHorizontalScrolling) {
            return true;
        }
        return false;
    }
}
