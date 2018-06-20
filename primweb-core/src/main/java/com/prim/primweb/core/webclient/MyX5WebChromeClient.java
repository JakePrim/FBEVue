package com.prim.primweb.core.webclient;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.prim.primweb.core.webclient.callback.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyX5WebChromeClient extends WebChromeClient {
    private IAgentWebChromeClient agentWebChromeClient;

    private static final String TAG = "MyX5WebChromeClient";

    public MyX5WebChromeClient(IAgentWebChromeClient agentWebChromeClient) {
        this.agentWebChromeClient = agentWebChromeClient;
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        agentWebChromeClient.onReceivedTitle(webView, s);
        super.onReceivedTitle(webView, s);
    }

    @Override
    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
        agentWebChromeClient.onReceivedIcon(webView, bitmap);
        super.onReceivedIcon(webView, bitmap);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView webView, String s, boolean b) {
        agentWebChromeClient.onReceivedTouchIconUrl(webView, s, b);
        super.onReceivedTouchIconUrl(webView, s, b);
    }

    @Override
    public void onRequestFocus(WebView webView) {
        agentWebChromeClient.onRequestFocus(webView);
        super.onRequestFocus(webView);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, final GeolocationPermissionsCallback geolocationPermissionsCallback) {
        com.prim.primweb.core.webclient.callback.GeolocationPermissionsCallback geolocationPermissionsCallback1 = new com.prim.primweb.core.webclient.callback.GeolocationPermissionsCallback() {
            @Override
            public void invoke(String var1, boolean var2, boolean var3) {
                geolocationPermissionsCallback.invoke(var1, var2, var3);
            }
        };
        agentWebChromeClient.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback1);
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        agentWebChromeClient.onGeolocationPermissionsHidePrompt();
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        agentWebChromeClient.openFileChooser(valueCallback, s, s1);
        super.openFileChooser(valueCallback, s, s1);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return agentWebChromeClient.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        agentWebChromeClient.onProgressChanged(webView, i);
        super.onProgressChanged(webView, i);
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        return agentWebChromeClient.onJsAlert(webView, s, s1);
    }

    @Override
    public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
        return agentWebChromeClient.onJsConfirm(webView, s, s1);
    }

    @Override
    public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
        return agentWebChromeClient.onJsPrompt(webView, s, s1, s2);
    }

    @Override
    public boolean onJsBeforeUnload(WebView webView, String s, String s1, JsResult jsResult) {
        return agentWebChromeClient.onJsBeforeUnload(webView, s, s1);
    }

    @Override
    public boolean onJsTimeout() {
        return agentWebChromeClient.onJsTimeout();
    }

    @Override
    public void onHideCustomView() {
        agentWebChromeClient.onHideCustomView();
        super.onHideCustomView();
    }


    @Override
    public void onShowCustomView(View view, int i, final IX5WebChromeClient.CustomViewCallback customViewCallback) {
        CustomViewCallback customViewCallback1 = new CustomViewCallback() {
            @Override
            public void onCustomViewHidden() {
                customViewCallback.onCustomViewHidden();
            }
        };
        agentWebChromeClient.onShowCustomView(view, i, customViewCallback1);
        super.onShowCustomView(view, i, customViewCallback);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        agentWebChromeClient.getVisitedHistory(callback);
        super.getVisitedHistory(callback);
    }
}
