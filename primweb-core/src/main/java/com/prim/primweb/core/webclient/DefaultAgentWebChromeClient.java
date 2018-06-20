package com.prim.primweb.core.webclient;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;

import com.prim.primweb.core.uicontroller.IndicatorController;
import com.prim.primweb.core.webclient.callback.GeolocationPermissionsCallback;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：默认代理的WebChromeClient
 * 修订历史：
 * ================================================
 */
public class DefaultAgentWebChromeClient extends WebChromeClient {
    private static final String TAG = "DefaultAgentWebChromeCl";

    IndicatorController indicatorController;

    public DefaultAgentWebChromeClient(Context context, IndicatorController indicatorController) {
        super(context);
        this.indicatorController = indicatorController;
    }

    @Override
    public void onReceivedTitle(View webView, String s) {
        super.onReceivedTitle(webView, s);
    }

    @Override
    public void onProgressChanged(View webView, int i) {
        super.onProgressChanged(webView, i);
        Log.e(TAG, "onProgressChanged: " + i);
        if (indicatorController != null) {
            indicatorController.progress(i);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        super.openFileChooser(valueCallback);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        super.openFileChooser(valueCallback, acceptType);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        super.openFileChooser(valueCallback, acceptType, capture);
    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, com.tencent.smtt.sdk.WebChromeClient.FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }
}
