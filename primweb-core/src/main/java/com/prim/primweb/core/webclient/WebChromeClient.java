package com.prim.primweb.core.webclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.ValueCallback;

import com.prim.primweb.core.file.FileChooser;
import com.prim.primweb.core.permission.FilePermissionWrap;
import com.prim.primweb.core.permission.PermissionMiddleActivity;
import com.prim.primweb.core.permission.WebPermission;
import com.prim.primweb.core.webclient.callback.CustomViewCallback;
import com.prim.primweb.core.webclient.callback.GeolocationPermissionsCallback;

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
public abstract class WebChromeClient implements IAgentWebChromeClient {
    private WeakReference<Context> context;

    public WebChromeClient(Context context) {
        this.context = new WeakReference<>(context);
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

    /** 默认去处理文件请求 */
    //  Android < 3.0
    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        FilePermissionWrap filePermissionWrap = new FilePermissionWrap(valueCallback);
        fileChooser(filePermissionWrap);
    }

    //  Android  >= 3.0
    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        FilePermissionWrap filePermissionWrap = new FilePermissionWrap(valueCallback, acceptType);
        fileChooser(filePermissionWrap);
    }

    // Android  >= 4.1
    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        FilePermissionWrap filePermissionWrap = new FilePermissionWrap(valueCallback, acceptType);
        fileChooser(filePermissionWrap);
    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FilePermissionWrap filePermissionWrap = new FilePermissionWrap(null, valueCallback, fileChooserParams.getAcceptTypes());
            fileChooser(filePermissionWrap);
        }
        return true;
    }

    @Override
    public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, com.tencent.smtt.sdk.WebChromeClient.FileChooserParams fileChooserParams) {
        FilePermissionWrap filePermissionWrap = new FilePermissionWrap(null, valueCallback, fileChooserParams.getAcceptTypes());
        fileChooser(filePermissionWrap);
        return true;
    }

    /** 设置定位默认开启 */
    @Override
    public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissionsCallback geolocationPermissionsCallback) {
        if (context == null || context.get() == null) {
            geolocationPermissionsCallback.invoke(s, false, false);
            return;
        }
        PermissionMiddleActivity.setPermissionListener(new PermissionMiddleActivity.PermissionListener() {
            @Override
            public void requestPermissionSuccess(String permissionType) {
                geolocationPermissionsCallback.invoke(s, true, false);
            }

            @Override
            public void requestPermissionFailed(String permissionType) {
                geolocationPermissionsCallback.invoke(s, false, false);
            }
        });
        PermissionMiddleActivity.startCheckPermission((Activity) context.get(), WebPermission.LOCATION_TYPE);
    }

    /** 选择文件上传 */
    protected void fileChooser(FilePermissionWrap filePermissionWrap) {
        if (context != null && context.get() != null) {
            new FileChooser(filePermissionWrap, context.get()).updateFile();
        }
    }
}
