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
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.client.MyAndroidWebViewClient;
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
public class PrimAgentWebView extends WebView implements IAgentWebView<WebSettings, WebChromeClient> {

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
        setWebViewClient(new MyAndroidWebViewClient(this, webViewClient));
    }

    @Override
    public void setAgentWebChromeClient(WebChromeClient webChromeClient) {
        setWebChromeClient(webChromeClient);
    }

    @Override
    public void setAndroidWebViewClient(WebViewClient webViewClient) {
        setWebViewClient(webViewClient);
    }

    @Override
    public void setX5WebViewClient(com.tencent.smtt.sdk.WebViewClient webViewClient) {

    }

    @Override
    public void setAndroidWebChromeClient(WebChromeClient webChromeClient) {
        setWebChromeClient(webChromeClient);
    }

    @Override
    public void setX5WebChromeClient(com.tencent.smtt.sdk.WebChromeClient webChromeClient) {

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
}
