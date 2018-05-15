package com.prim.primweb.core.webview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/14 0014
 * 描    述：webview 的父类 主要是 顶部的进度条  加载中的view  加载错误的view
 * 修订历史：
 * ================================================
 */
public class WebParentLayout extends FrameLayout {
    private View mWebView, mErrorView, mLoadView;

    public WebParentLayout(@NonNull Context context) {
        this(context, null);
    }

    public WebParentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public WebParentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void bindWebView(View view) {
        if (mWebView == null) {
            this.mWebView = view;
        }
        addView(mWebView);
    }

    void bindPageError(View view) {
        if (null == mErrorView) {
            mErrorView = view;
        }
    }

    void bindPageLoad(View view) {
        if (null == mLoadView) {
            mLoadView = view;
        }
    }

    void showPageError(View view) {
        if (null == mErrorView) {
            mErrorView = view;
            addView(mErrorView, 1);
        }
        mErrorView.setVisibility(VISIBLE);
    }

    void hidePageError() {
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
    }

    void showPageLoad(View view) {
        if (null == mLoadView) {
            mLoadView = view;
            addView(mLoadView, 1);
        }
        mLoadView.setVisibility(VISIBLE);
    }

    void hidePageLoad() {
        if (mLoadView != null) {
            mLoadView.setVisibility(GONE);
        }
    }


}
