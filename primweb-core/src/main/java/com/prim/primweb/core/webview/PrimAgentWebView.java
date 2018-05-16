package com.prim.primweb.core.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prim.primweb.core.client.IAgentWebChromeClient;
import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.client.MyAndroidWebChromeClient;
import com.prim.primweb.core.client.MyAndroidWebViewClient;
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
public class PrimAgentWebView extends WebView implements IAgentWebView<WebSettings> {

    private static final String TAG = "PrimAgentWebView";
    public com.prim.primweb.core.listener.OnScrollChangeListener listener;
    private IAgentWebViewClient webViewClient;
    WebView.HitTestResult result;

    public PrimAgentWebView(Context context) {
        this(context, null);
    }

    public PrimAgentWebView(Context context, AttributeSet attrs) {
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
    public void removeRiskJavascriptInterface() {
        //显式移除有风险的 Webview 系统隐藏接口
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");
    }

    private View.OnLongClickListener longClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    };

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
    public void setAgentWebViewClient(IAgentWebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        setWebViewClient(new MyAndroidWebViewClient(this, webViewClient));
    }

    @Override
    public void setAgentWebChromeClient(IAgentWebChromeClient webChromeClient) {
        setWebChromeClient(new MyAndroidWebChromeClient(webChromeClient));
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
