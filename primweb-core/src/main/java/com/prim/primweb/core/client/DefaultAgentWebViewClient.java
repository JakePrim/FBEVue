package com.prim.primweb.core.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.KeyEvent;

import com.prim.primweb.core.webview.IAgentWebView;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：默认加载WebViewClient处理，需要自己处理的重写WebViewClient
 * 修订历史：
 * ================================================
 */
public class DefaultAgentWebViewClient extends WebViewClient {

    private static final String TAG = "DefaultAgentWebViewClie";

    private WeakReference<Context> context;

    public DefaultAgentWebViewClient(Context context) {
        super(context);
        this.context = new WeakReference<>(context);
    }

    @Override
    public void onPageStarted(IAgentWebView view, String url, Bitmap favicon) {

    }


    @Override
    public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
        if (url == null) {
            return true;
        }
        String newUrl = url;
        if (!url.contains(HTTP_SCHEME) && !url.contains(HTTPS_SCHEME)) {
            newUrl = "http://" + url;
            view.loadAgentUrl(newUrl);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(IAgentWebView view, String url) {

    }

    @Override
    public void onReceivedError(IAgentWebView view, int errorCode, String description, String url) {

    }

    @Override
    public void shouldInterceptRequest(IAgentWebView view, String url) {

    }

    @Override
    public boolean shouldOverrideKeyEvent(IAgentWebView view, KeyEvent event) {
        return false;
    }

    @Override
    public void onLoadResource(IAgentWebView view, String url) {

    }

    @Override
    public void onPageCommitVisible(IAgentWebView view, String url) {

    }

    @Override
    public void doUpdateVisitedHistory(IAgentWebView view, String url, boolean isReload) {

    }

    @Override
    public void onUnhandledKeyEvent(IAgentWebView webView, KeyEvent keyEvent) {

    }
}
