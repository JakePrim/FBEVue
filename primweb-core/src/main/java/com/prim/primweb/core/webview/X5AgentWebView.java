package com.prim.primweb.core.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.prim.primweb.core.client.IAgentWebChromeClient;
import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.client.MyX5WebChromeClient;
import com.prim.primweb.core.client.MyX5WebViewClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.utils.PrimWebUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.Method;
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

    private static final String TAG = "X5AgentWebView";

    public X5AgentWebView(Context context) {
        this(context, null);
    }

    public X5AgentWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void removeRiskJavascriptInterface() {
        //显式移除有风险的 Webview 系统隐藏接口
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");
    }

    /**
     * 使用Chrome DevTools 远程调试WebView
     */
    @TargetApi(19)
    @Override
    public void setWebChromeDebuggingEnabled() {
        if (PrimWebUtils.isDebug() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class<?> clazz = WebView.class;
                Method method = clazz.getMethod("setWebContentsDebuggingEnabled", boolean.class);
                method.invoke(null, true);
            } catch (Throwable e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            }
        }
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
        Log.e(TAG, "addJavascriptInterfaceAgent: name --> " + name + "| object --> " + object.getClass().getSimpleName());
        this.addJavascriptInterface(object, name);
    }

    @Override
    public void setAgentWebViewClient(IAgentWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        setWebViewClient(new MyX5WebViewClient(this, webViewClient));
    }

    @Override
    public void setAgentWebChromeClient(IAgentWebChromeClient webChromeClient) {
        setWebChromeClient(new MyX5WebChromeClient(webChromeClient));
    }

    @Override
    public void setAndroidWebViewClient(android.webkit.WebViewClient webViewClient) {

    }

    @Override
    public void setX5WebViewClient(WebViewClient webViewClient) {
        setWebViewClient(webViewClient);
    }

    @Override
    public void setAndroidWebChromeClient(android.webkit.WebChromeClient webChromeClient) {

    }

    @Override
    public void setX5WebChromeClient(WebChromeClient webChromeClient) {
        setWebChromeClient(webChromeClient);
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
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {//从父容器中移除webview
            ((ViewGroup) parent).removeAllViewsInLayout();
        }
        super.destroy();
    }
}
