package com.prim.primweb.core.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：代理的WebChromeClient,可以在这里做一些默认的设置,好处就是我们可以减少部分代码
 * 修订历史：
 * ================================================
 */
public abstract class WebChromeClient<T> implements IAgentWebChromeClient<T> {
    private WeakReference<Context> context;

    public WebChromeClient(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    public void onReceivedTitle(View webView, String s) {

    }

    @Override
    public void onRequestFocus(View webView) {

    }

    @Override
    public void onReceivedTouchIconUrl(View webView, String s, boolean b) {

    }

    @Override
    public void onReceivedIcon(View webView, Bitmap bitmap) {

    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {

    }

    @Override
    public void onHideCustomView() {

    }

    @Override
    public boolean onJsTimeout() {
        return false;
    }

    @Override
    public boolean onJsBeforeUnload(View webView, String s, String s1) {
        return false;
    }

    @Override
    public boolean onJsPrompt(View webView, String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean onJsConfirm(View webView, String s, String s1) {
        return false;
    }

    @Override
    public boolean onJsAlert(View webView, String s, String s1) {
        return false;
    }

    @Override
    public void onProgressChanged(View webView, int i) {

    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {

    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {

    }

    @Override
    public View getVideoLoadingProgressView() {
        return null;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback) {

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {

    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, T fileChooserParams) {

        return false;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {

    }
}
