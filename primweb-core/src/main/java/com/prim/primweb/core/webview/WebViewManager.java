package com.prim.primweb.core.webview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.utils.PrimWebUtils;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/14 0014
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WebViewManager implements IWebViewManager {
    private View mWebView;

    private FrameLayout mFrameLayout;

    private IAgentWebView agentWebView;

    private ViewGroup mViewGroup;

    private ViewGroup.LayoutParams lp;

    int index;
    boolean needTopProgress;
    View errorView;
    View loadView;
    View topProView;
    Context context;

    /**
     * @param context
     * @param viewGroup
     * @param lp
     * @param index
     * @param needTopProgress
     * @param webView
     * @param errorView
     * @param loadView
     * @param topProView
     * @param agentWebView
     */
    private WebViewManager(@NonNull Context context,
                           @NonNull ViewGroup viewGroup,
                           @NonNull ViewGroup.LayoutParams lp,
                           int index,
                           boolean needTopProgress,
                           View webView,
                           View errorView,
                           View loadView,
                           View topProView,
                           IAgentWebView agentWebView) {
        this.mWebView = webView;
        this.agentWebView = agentWebView;
        this.mViewGroup = viewGroup;
        this.lp = lp;
        this.index = index;
        this.needTopProgress = needTopProgress;
        this.errorView = errorView;
        this.loadView = loadView;
        this.topProView = topProView;
        this.context = context;
    }


    @Override
    public View getWebView() {
        return mWebView;
    }

    @Override
    public FrameLayout getWebParentView() {
        return mFrameLayout;
    }

    @Override
    public IWebViewManager create() {
        ViewGroup mViewGroup = this.mViewGroup;
        if (mViewGroup == null) {
            mViewGroup = mFrameLayout = createLayout();
            PrimWebUtils.scanForActivity(context).setContentView(mViewGroup);
        } else {
            mViewGroup.addView(this.mFrameLayout = createLayout(), index, lp);
        }
        return this;
    }

    private FrameLayout createLayout() {
        WebParentLayout webParentLayout = new WebParentLayout(context);
        webParentLayout.setBackgroundColor(Color.WHITE);
        webParentLayout.addView(createWebView());
        webParentLayout.bindWebView(createWebView());
        return webParentLayout;
    }

    private View createWebView() {
        View webView = null;
        if (this.mWebView != null) {
            webView = mWebView;
        } else {
            webView = new PrimAgentWebView(context);
        }
        return webView;
    }

    @Override
    public IAgentWebView getAgentWeb() {
        return agentWebView;
    }
}
