package com.prim.primweb.core.webview.webpool;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prim.primweb.core.utils.PWLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019-06-26 - 15:33
 */
public class WebClientHelper {
    private WebViewClient webViewClient;

    private WebChromeClient webChromeClient;

    private OnWebClientListener onWebClientListener;

    private static final WebClientHelper ourInstance = new WebClientHelper();

    public static WebClientHelper getInstance() {
        return ourInstance;
    }


    public WebClientHelper() {
        webViewClient = new MyWebViewClient();
        webChromeClient = new MyWebChromeClient();
    }

    public WebViewClient getWebViewClient() {
        return webViewClient;
    }

    public WebChromeClient getWebChromeClient() {
        return webChromeClient;
    }

    public void setOnWebClientListener(OnWebClientListener listener) {
        this.onWebClientListener = listener;
    }

    public void removeOnWebClientListener(OnWebClientListener listener) {
        this.onWebClientListener = null;
    }

    protected class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (onWebClientListener != null) {
                onWebClientListener.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (onWebClientListener != null) {
                onWebClientListener.onPageFinished(view, url);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (onWebClientListener != null) {
                return onWebClientListener.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String url) {
            super.onReceivedError(view, errorCode, description, url);
            if (onWebClientListener != null) {
                onWebClientListener.onReceivedError(view, errorCode, description, url);
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            PWLog.e("Web-Log doUpdateVisitedHistory:" + url);
            if (needClearHistory) {
                view.clearHistory();
                needClearHistory = false;
            }
        }
    }

    private boolean needClearHistory = true;

    public void setNeedClearHistory(boolean needClearHistory) {
        this.needClearHistory = true;
    }

    protected class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }
}
