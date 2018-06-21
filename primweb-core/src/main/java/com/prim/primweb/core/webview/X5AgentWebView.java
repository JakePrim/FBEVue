package com.prim.primweb.core.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.utils.PrimWebUtils;
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


    private static final String TAG = "X5AgentWebView";

    private WebView.HitTestResult result;

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

    @Override
    public Object getAgentHitTestResult() {
        return this.getHitTestResult();
    }

    @Override
    public int getAgentHeight() {
        return getView().getHeight();
    }

    @Override
    public int getAgentContentHeight() {
        return getContentHeight();
    }

    @Override
    public float getAgentScale() {
        return getScale();
    }

    @Override
    public void agentScrollTo(int x, int y) {
        getView().scrollTo(x, y);
    }

    @Override
    public void agentScrollBy(int x, int y) {
        getView().scrollBy(x, y);
    }

    @Override
    public String getAgentUrl() {
        return this.getUrl();
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
    public void setAgentWebViewClient(android.webkit.WebViewClient webViewClient) {

    }

    @Override
    public void setAgentWebViewClient(WebViewClient webViewClient) {
        this.setWebViewClient(webViewClient);
    }

    @Override
    public void setAgentWebChromeClient(android.webkit.WebChromeClient webChromeClient) {

    }

    @Override
    public void setAgentWebChromeClient(WebChromeClient webChromeClient) {
        this.setWebChromeClient(webChromeClient);
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
    public void destroy() {
        removeAllViewsInLayout();
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {//从父容器中移除webview
            ((ViewGroup) parent).removeAllViewsInLayout();
        }
        super.destroy();
    }

    @Override
    public void onAgentResume() {
        if (Build.VERSION.SDK_INT >= 11) {
            this.onResume();
        }
        this.resumeTimers();
    }

    @Override
    public void onAgentPause() {
        if (Build.VERSION.SDK_INT >= 11) {
            this.onPause();
        }
        this.pauseTimers();
    }

    @Override
    public void onAgentDestory() {
        this.resumeTimers();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        this.loadUrl("about:blank");
        this.stopLoading();
        if (this.getHandler() != null) {
            this.getHandler().removeCallbacksAndMessages(null);
        }
        this.removeAllViews();
        ViewGroup mViewGroup = null;
        if ((mViewGroup = ((ViewGroup) this.getParent())) != null) {
            mViewGroup.removeView(this);
        }
        this.setWebChromeClient(null);
        this.setWebViewClient(null);
        this.setTag(null);
        this.clearHistory();
        this.destroy();
    }

    @Override
    public boolean goBackAgent() {
        if (this.canGoBack()) {
            this.goBack();
            return true;
        }
        return false;
    }
}
