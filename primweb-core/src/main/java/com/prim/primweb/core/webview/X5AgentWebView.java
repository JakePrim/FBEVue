package com.prim.primweb.core.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;


/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/14 0014
 * 描    述：代理腾讯x5的webView
 * 修订历史：
 * ================================================
 */
public class X5AgentWebView extends WebView implements IAgentWebView<WebSettings> {
    private com.prim.primweb.core.listener.OnScrollChangeListener listener;

    private IAgentWebViewClient webViewClient;

    public X5AgentWebView(Context context) {
        this(context, null);
    }

    public X5AgentWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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

    @Override
    public void setAgentWebViewClient(IAgentWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        setWebViewClient(new MyWebViewClient(this));
    }

    @Override
    public void loadAgentUrl(String url, Map headers) {
        this.loadUrl(url, headers);
    }

    @Override
    public void setOnScrollChangeListener(com.prim.primweb.core.listener.OnScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener) {
            listener.onScrollChange(this, l, t, oldl, oldt);
        }
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

    private class MyWebViewClient extends WebViewClient {
        private IAgentWebView agentWebView;

        public MyWebViewClient(IAgentWebView webView) {
            this.agentWebView = webView;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            webViewClient.onPageStarted(agentWebView, url, favicon);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return  webViewClient.shouldOverrideUrlLoading(agentWebView, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webViewClient.onPageFinished(agentWebView, url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webViewClient.onReceivedError(agentWebView, errorCode, description, failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            webViewClient.shouldInterceptRequest(agentWebView, url);
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            webViewClient.shouldOverrideKeyEvent(agentWebView, event);
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            webViewClient.onLoadResource(agentWebView, url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
            webViewClient.onUnhandledKeyEvent(agentWebView, keyEvent);
            super.onUnhandledKeyEvent(webView, keyEvent);
        }

        @Override
        public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
            webViewClient.doUpdateVisitedHistory(agentWebView, s, b);
            super.doUpdateVisitedHistory(webView, s, b);
        }
    }
}
