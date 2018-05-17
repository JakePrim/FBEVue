package com.prim.primweb.core.webclient;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.*;

import com.prim.primweb.core.webview.IAgentWebView;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyAndroidWebViewClient extends android.webkit.WebViewClient {
    private IAgentWebView webView;
    private IAgentWebViewClient webViewClient;

    public MyAndroidWebViewClient(IAgentWebView webView, IAgentWebViewClient webViewClient) {
        this.webView = webView;
        this.webViewClient = webViewClient;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        webViewClient.onPageStarted(webView, url, favicon);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return webViewClient.shouldOverrideUrlLoading(webView, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        webViewClient.onPageFinished(webView, url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        webViewClient.onReceivedError(webView, errorCode, description, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        webViewClient.shouldInterceptRequest(webView, url);
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        webViewClient.shouldOverrideKeyEvent(webView, event);
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        webViewClient.onLoadResource(webView, url);
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        webViewClient.onPageCommitVisible(webView, url);
        super.onPageCommitVisible(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        webViewClient.doUpdateVisitedHistory(webView, url, isReload);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        webViewClient.onUnhandledKeyEvent(webView, event);
        super.onUnhandledKeyEvent(view, event);
    }
}
