package com.prim.primweb.core.client;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：WebChromeClient的代理接口,代理常用部分方法
 * 修订历史：
 * ================================================
 */
public interface IAgentWebChromeClient<T> {
    void onReceivedTitle(View webView, String s);

    void onReceivedIcon(View webView, Bitmap bitmap);

    void onReceivedTouchIconUrl(View webView, String s, boolean b);

    void onRequestFocus(View webView);

    void onGeolocationPermissionsHidePrompt();

    void openFileChooser(ValueCallback<Uri> valueCallback);

    void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType);

    void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture);

    boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, T fileChooserParams);

    void onProgressChanged(View webView, int i);

    boolean onJsTimeout();

    void onHideCustomView();

    boolean onJsAlert(View webView, String s, String s1);

    boolean onJsConfirm(View webView, String s, String s1);

    boolean onJsPrompt(View webView, String s, String s1, String s2);

    boolean onJsBeforeUnload(View webView, String s, String s1);

    void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback);

    void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback);

    Bitmap getDefaultVideoPoster();

    View getVideoLoadingProgressView();

    void getVisitedHistory(ValueCallback<String[]> callback);
}
