package com.prim.primweb.core.client;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：默认代理的WebChromeClient
 * 修订历史：
 * ================================================
 */
public class DefaultAgentWebChromeClient<T> extends WebChromeClient<T> {
    private static final String TAG = "DefaultAgentWebChromeCl";

    public DefaultAgentWebChromeClient(Context context) {
        super(context);
        Log.e(TAG, "DefaultAgentWebChromeClient: 方法执行");
    }

    @Override
    public void onReceivedTitle(View webView, String s) {
        Log.e(TAG, "onReceivedTitle: " + s);
        super.onReceivedTitle(webView, s);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        Log.e(TAG, "onGeolocationPermissionsShowPrompt: 方法执行");
        geolocationPermissionsCallback.invoke(s, true, false);
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        Log.e(TAG, "openFileChooser: 方法执行 --> ");
        super.openFileChooser(valueCallback);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        Log.e(TAG, "openFileChooser: 方法执行 --> ");
        super.openFileChooser(valueCallback, acceptType);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        Log.e(TAG, "openFileChooser: 方法执行 --> ");
        super.openFileChooser(valueCallback, acceptType, capture);
    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, T fileChooserParams) {
        Log.e(TAG, "onShowFileChooser: 方法执行 --> " + fileChooserParams);
        if (fileChooserParams instanceof android.webkit.WebChromeClient.FileChooserParams) {
            android.webkit.WebChromeClient.FileChooserParams fileChooser = (android.webkit.WebChromeClient.FileChooserParams) fileChooserParams;
            return true;
        } else if (fileChooserParams instanceof com.tencent.smtt.sdk.WebChromeClient.FileChooserParams) {
            com.tencent.smtt.sdk.WebChromeClient.FileChooserParams x5FileChooser = (com.tencent.smtt.sdk.WebChromeClient.FileChooserParams) fileChooserParams;
            return true;
        }
        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }
}
