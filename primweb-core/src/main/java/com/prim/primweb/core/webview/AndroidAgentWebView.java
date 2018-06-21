package com.prim.primweb.core.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prim.primweb.core.webclient.IAgentWebChromeClient;
import com.prim.primweb.core.webclient.MyAndroidWebChromeClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.utils.PrimWebUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
public class AndroidAgentWebView extends WebView implements IAgentWebView<WebSettings> {

    private static final String TAG = "PrimAgentWebView";
    public com.prim.primweb.core.listener.OnScrollChangeListener listener;
    private WebView.HitTestResult result;

    public AndroidAgentWebView(Context context) {
        this(context, null);
    }

    public AndroidAgentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        result = this.getHitTestResult();
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
        Log.e(TAG, "goBackAgent: " + this.canGoBack());
        if (this.canGoBack()) {
            this.goBack();
            return true;
        }
        return false;
    }

    @Override
    public Object getAgentHitTestResult() {
        return this.getHitTestResult();
    }

    @Override
    public int getAgentHeight() {
        return getHeight();
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
        this.scrollTo(x, y);
    }

    @Override
    public void agentScrollBy(int x, int y) {
        this.scrollTo(x, y);
    }

    @Override
    public String getAgentUrl() {
        return this.getUrl();
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
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
    public void setAgentWebViewClient(WebViewClient webViewClient) {
        this.setWebViewClient(webViewClient);
    }

    @Override
    public void setAgentWebViewClient(com.tencent.smtt.sdk.WebViewClient webViewClient) {

    }

    @Override
    public void setAgentWebChromeClient(IAgentWebChromeClient webChromeClient) {
        setWebChromeClient(new MyAndroidWebChromeClient(webChromeClient));
    }

    @Override
    public void setAndroidWebChromeClient(WebChromeClient webChromeClient) {
        setWebChromeClient(webChromeClient);
    }

    @Override
    public void setX5WebChromeClient(com.tencent.smtt.sdk.WebChromeClient webChromeClient) {

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
        releaseConfigCallback();
        super.destroy();
    }

    @Override
    public void clearHistory() {
        super.clearHistory();
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterfaceAgent(Object object, String name) {
        this.addJavascriptInterface(object, name);
    }

    // 解决WebView内存泄漏问题；参考AgentWeb X5 webview 解决了此问题，具体看x5webview源码
    private void releaseConfigCallback() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) { // JELLY_BEAN
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // KITKAT
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (PrimWebUtils.isDebug()) {
                    e.printStackTrace();
                }
            }
        }
    }

}
