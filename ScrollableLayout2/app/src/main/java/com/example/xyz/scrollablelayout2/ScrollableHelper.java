package com.example.xyz.scrollablelayout2;


import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ScrollableHelper {

    private View mCurrentScrollableView;

    private ScrollableContainer mCurrentScrollableCainer;

    private int sysVersion = android.os.Build.VERSION.SDK_INT;

    /**
     * a viewgroup whitch contains ScrollView/ListView/RecycelerView..
     */
    public interface ScrollableContainer {
        /**
         * @return ScrollView/ListView/RecycelerView..'s instance
         */
        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer) {
        this.mCurrentScrollableCainer = scrollableContainer;
    }

    public void setCurrentScrollableContainer(View view) {
        this.mCurrentScrollableView = view;
    }

    private View getScrollableView() {
        if (mCurrentScrollableCainer == null) {
            return mCurrentScrollableView;
        }
        return mCurrentScrollableCainer.getScrollableView();
    }

    public boolean isTop() {
        View scrollableView = getScrollableView();
        if (scrollableView == null) {
            return false;
        }
        if (scrollableView instanceof AdapterView) {
            return isAdapterViewTop((AdapterView) scrollableView);
        }
        if (scrollableView instanceof ScrollView) {
            return isScrollViewTop((ScrollView) scrollableView);
        }
        if (scrollableView instanceof RecyclerView) {
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }
        if (scrollableView instanceof WebView) {
            return isWebViewTop((WebView) scrollableView);
        }
        if(scrollableView instanceof ViewPager){
            return isViewPagerTop((ViewPager) scrollableView);
        }

        throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
    }

    private static boolean isViewPagerTop(ViewPager viewPager) {/******/
        if(viewPager != null){

                if (((RelativeLayout) viewPager.getChildAt(viewPager.getCurrentItem())).getChildAt(0) instanceof AdapterView) {
                    return isAdapterViewTop((AdapterView) ((RelativeLayout) viewPager.getChildAt(viewPager.getCurrentItem())).getChildAt(0));
                } else if (((RelativeLayout) viewPager.getChildAt(viewPager.getCurrentItem())).getChildAt(0) instanceof RecyclerView) {
                    return isRecyclerViewTop((RecyclerView) ((RelativeLayout) viewPager.getChildAt(viewPager.getCurrentItem())).getChildAt(0));
                }


        }
        return false;
    }

    private static boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null) {
                    return true;
                }
                if (firstVisibleItemPosition == 0) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    int topMargin = lp.topMargin;
                    int top = childAt.getTop();
                    if (top >= topMargin) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isAdapterViewTop(AdapterView adapterView) {
        if (adapterView != null) {
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);
            if (childAt == null || (firstVisiblePosition == 0 && childAt != null && childAt.getTop() == 0)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isScrollViewTop(ScrollView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    private static boolean isWebViewTop(WebView scrollView) {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public void smoothScrollBy(int velocityY, int distance, int duration) {/******/
        View scrollableView = getScrollableView();
        if (scrollableView instanceof AbsListView
                || scrollableView instanceof  ViewPager) {

            AbsListView absListView = null;

            if(scrollableView instanceof AbsListView) {
                 absListView = (AbsListView) scrollableView;
            }else if (scrollableView instanceof  ViewPager){


                    View view = ((RelativeLayout) ((ViewPager) scrollableView).getChildAt(((ViewPager) scrollableView).getCurrentItem())).getChildAt(0);


                    if (view instanceof AbsListView) {
                        absListView = (AbsListView) view;
                    } else if (view instanceof RecyclerView) {
                        ((RecyclerView) view).fling(0, velocityY);
                    }

            }

            if(absListView!=null) {
                if (sysVersion >= 21) {
                    absListView.fling(velocityY);
                } else {
                    absListView.smoothScrollBy(distance, duration);
                }
            }

        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView)scrollableView).flingScroll(0,velocityY);
        }

    }
}
