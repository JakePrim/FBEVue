package com.prim.primweb.core.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;

import java.util.Map;


/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/11 0011
 * 描    述：代理webview
 * 修订历史：1.0.0
 * ================================================
 */
public class PrimAgentWebView extends WebView implements IAgentWebView<WebSettings> {

    private static final String TAG = "PrimAgentWebView";
    public com.prim.primweb.core.listener.OnScrollChangeListener listener;

    public PrimAgentWebView(Context context) {
        this(context, null);
    }

    public PrimAgentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener) {
            listener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    @Override
    public void setOnScrollChangeListener(com.prim.primweb.core.listener.OnScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public View getAgentWebView() {
        return this;
    }

    @Override
    public WebSettings getWebSetting() {
        return this.getSettings();
    }

    @Override
    public void loadAgentJs(String js) {
        this.loadUrl(js);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void loadAgentJs(String js, final AgentValueCallback<String> callback) {
        ValueCallback<String> valueCallback = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String o) {
                if (null != callback) {
                    callback.onReceiveValue(o);
                }
            }
        };
        this.evaluateJavascript(js, valueCallback);
    }

    @Override
    public void loadAgentUrl(String url) {
        this.loadUrl(url);
    }

    @Override
    public void loadAgentUrl(String url, Map<String, String> headers) {
        this.loadUrl(url, headers);
    }

    @Override
    public void reloadAgent() {
        this.reload();
    }

    @Override
    public void stopLoadingAgent() {
        this.stopLoading();
    }

    @Override
    public void postUrlAgent(String url, byte[] params) {
        this.postUrl(url, params);
    }

    @Override
    public void loadDataAgent(String data, String mimeType, String encoding) {
        this.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURLAgent(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void addJavascriptInterfaceAgent(Object object, String name) {
        this.addJavascriptInterface(object, name);
    }

    private IAgentWebViewClient webViewClient;

    @Override
    public void setAgentWebViewClient(IAgentWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        setWebViewClient(new MyWebViewClient(this));
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterface(Object object, String name) {
        super.addJavascriptInterface(object, name);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void destroy() {
        removeAllViewsInLayout();
        super.destroy();
    }


    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    private class MyWebViewClient extends WebViewClient {
        private IAgentWebView webView;

        public MyWebViewClient(IAgentWebView webView) {
            this.webView = webView;
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
}
