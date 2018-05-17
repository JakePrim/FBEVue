package com.prim.primweb.core.webclient;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MyAndroidWebChromeClient extends WebChromeClient {
    private IAgentWebChromeClient agentWebChromeClient;

    public MyAndroidWebChromeClient(IAgentWebChromeClient agentWebChromeClient) {
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
    public void onGeolocationPermissionsShowPrompt(String origin, final GeolocationPermissions.Callback callback) {
        GeolocationPermissionsCallback geolocationPermissionsCallback = new GeolocationPermissionsCallback() {
            @Override
            public void invoke(String var1, boolean var2, boolean var3) {
                callback.invoke(var1, var2, var3);
            }
        };
        agentWebChromeClient.onGeolocationPermissionsShowPrompt(origin, geolocationPermissionsCallback);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        agentWebChromeClient.onGeolocationPermissionsHidePrompt();
        super.onGeolocationPermissionsHidePrompt();
    }

    //  Android < 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        invokeMethod(this, "openFileChooser", new Object[]{valueCallback}, ValueCallback.class);
        agentWebChromeClient.openFileChooser(valueCallback);
    }

    //  Android  >= 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        invokeMethod(this, "openFileChooser", new Object[]{valueCallback, acceptType}, ValueCallback.class, String.class);
        agentWebChromeClient.openFileChooser(valueCallback, acceptType);
    }

    // Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        invokeMethod(this, "openFileChooser", new Object[]{valueCallback, acceptType, capture}, ValueCallback.class, String.class, String.class);
        agentWebChromeClient.openFileChooser(valueCallback, acceptType, capture);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        agentWebChromeClient.onJsAlert(webView, s, s1);
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
        agentWebChromeClient.onJsConfirm(webView, s, s1);
        return super.onJsConfirm(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
        agentWebChromeClient.onJsPrompt(webView, s, s1, s2);
        return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
    }

    @Override
    public boolean onJsBeforeUnload(WebView webView, String s, String s1, JsResult jsResult) {
        agentWebChromeClient.onJsBeforeUnload(webView, s, s1);
        return super.onJsBeforeUnload(webView, s, s1, jsResult);
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
    public void onShowCustomView(View view, int requestedOrientation, final CustomViewCallback callback) {
        com.prim.primweb.core.webclient.CustomViewCallback customViewCallback = new com.prim.primweb.core.webclient.CustomViewCallback() {
            @Override
            public void onCustomViewHidden() {
                callback.onCustomViewHidden();
            }
        };
        agentWebChromeClient.onShowCustomView(view, requestedOrientation, customViewCallback);
        super.onShowCustomView(view, requestedOrientation, callback);
    }


    @Override
    public Bitmap getDefaultVideoPoster() {
        return agentWebChromeClient.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        return agentWebChromeClient.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        agentWebChromeClient.getVisitedHistory(callback);
        super.getVisitedHistory(callback);
    }

    public void invokeMethod(WebChromeClient chromeClient, String method, Object[] os, Class... clz) {
        if (chromeClient == null) {
            return;
        }
        try {
            Class<? extends WebChromeClient> aClass = chromeClient.getClass();
            Method method1 = aClass.getMethod(method, clz);
            method1.invoke(chromeClient, os);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
