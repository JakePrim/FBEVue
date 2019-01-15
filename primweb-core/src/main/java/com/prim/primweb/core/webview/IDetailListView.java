package com.prim.primweb.core.webview;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019/1/14 - 2:12 PM
 */
public interface IDetailListView {
    void setScrollView(PrimScrollView scrollView);

    boolean canScrollVertically(int direction);

    void customScrollBy(int dy);

    boolean startFling(int vy);

    void setOnScrollBarShowListener(PrimScrollView.OnScrollBarShowListener listener);

    /**
     * 滑到第一项
     */
    void scrollToFirst();
}
