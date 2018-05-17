package com.prim.primweb.core.webclient;

import android.graphics.Bitmap;
import android.view.KeyEvent;

import com.prim.primweb.core.webview.IAgentWebView;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：代理WebViewClient 这里只代理常用的几个方法
 * 修订历史：
 * ================================================
 */
public interface IAgentWebViewClient {
    void onPageStarted(IAgentWebView view, String url, Bitmap favicon);

    boolean shouldOverrideUrlLoading(IAgentWebView view, String url);

    void onPageFinished(IAgentWebView view, String url);

    void onReceivedError(IAgentWebView view, int errorCode, String description, String url);

    void shouldInterceptRequest(IAgentWebView view, String url);

    boolean shouldOverrideKeyEvent(IAgentWebView view, KeyEvent event);

    void onLoadResource(IAgentWebView view, String url);

    void onPageCommitVisible(IAgentWebView view, String url);

    void doUpdateVisitedHistory(IAgentWebView view, String url, boolean isReload);

    void onUnhandledKeyEvent(IAgentWebView webView, KeyEvent keyEvent);
}
