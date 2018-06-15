package com.prim.primweb.core.webclient.base;

import android.app.Activity;
import android.webkit.WebViewClient;

import com.prim.primweb.core.webview.IAgentWebView;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：6/15 0015
 * 描    述：所有的WebViewClient的总处理类
 * 修订历史：
 * ================================================
 */
public class PrimWebViewClient {
    private WeakReference<Activity> mActivity;

    private WebViewClient andWebViewClient;

    private com.tencent.smtt.sdk.WebViewClient x5WebViewClient;

    private com.prim.primweb.core.webclient.WebViewClient agentWebViewClient;

    private DefaultAndroidWebViewClient defaultAndroidWebViewClient;

    private DefaultX5WebViewClient defaultX5WebViewClient;

    private IAgentWebView webView;
}
