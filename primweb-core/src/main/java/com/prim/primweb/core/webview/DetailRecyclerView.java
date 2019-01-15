package com.prim.primweb.core.webview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.OverScroller;

import java.lang.reflect.Field;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019/1/14 - 2:32 PM
 */
public class DetailRecyclerView extends RecyclerView implements IDetailListView {

    private ListViewTouchHelper helper;

    private ScrollerCompat scrollerCompat;//下次就不用再反射了

    private OverScroller overScroller;

    public DetailRecyclerView(Context context) {
        super(context);
        init();
    }

    public DetailRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetailRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                helper.onScrollStateChanged(newState == RecyclerView.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (listener != null) {//滚动时显示滚动条
                    listener.onShow();
                }
            }
        });
    }

    @Override
    public void setScrollView(PrimScrollView scrollView) {
        helper = new ListViewTouchHelper(scrollView, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (helper == null) {
            return super.onTouchEvent(e);
        }
        if (!helper.onTouchEvent(e)) {
            return false;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void customScrollBy(int dy) {
        scrollBy(0, dy);
    }

    @Override
    public boolean startFling(int vy) {
        if (getVisibility() == GONE)
            return false;
        return fling(0, vy);
    }

    private PrimScrollView.OnScrollBarShowListener listener;

    @Override
    public void setOnScrollBarShowListener(PrimScrollView.OnScrollBarShowListener listener) {
        this.listener = listener;
    }

    @Override
    public void scrollToFirst() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, 0);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(0, 0);
        }
    }

    /**
     * 获取RecyclerView滑动速度
     *
     * @return
     */
    public int getCurrVelocity() {
        if (scrollerCompat != null) {
            return (int) scrollerCompat.getCurrVelocity();
        } else if (overScroller != null) {
            return (int) overScroller.getCurrVelocity();
        }
        Class clazz = RecyclerView.class;
        try {
            Field viewFlingerField = clazz.getDeclaredField("mViewFlinger");
            viewFlingerField.setAccessible(true);
            Object viewFlinger = viewFlingerField.get(this);
            Field scrollerField = viewFlinger.getClass().getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            //在RecyclerView api 26 已经发生改变
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                overScroller = (OverScroller) scrollerField.get(viewFlinger);
                return (int) overScroller.getCurrVelocity();
            } else {//26以下
                scrollerCompat = (ScrollerCompat) scrollerField.get(viewFlinger);
                return (int) scrollerCompat.getCurrVelocity();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }
}
