package com.prim.primweb.core.webclient;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.prim.primweb.core.webview.IAgentWebView;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.List;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：重写的WebViewClient
 * 修订历史：
 * ================================================
 */
public abstract class WebViewClient implements IAgentWebViewClient {

    /**
     * URI scheme for telephone number.
     */
    public static final String SCHEME_TEL = "tel:";
    /**
     * URI scheme for email address.
     */
    public static final String SCHEME_MAILTO = "mailto:";
    /**
     * URI scheme for map address.
     */
    public static final String SCHEME_GEO = "geo:0,0?q=";

    /**
     * SMS scheme
     */
    public static final String SCHEME_SMS = "sms:";

    /**
     * intent ' s scheme
     */
    public static final String INTENT_SCHEME = "intent://";

    /**
     * Wechat pay scheme ，用于唤醒微信支付
     */
    public static final String WEBCHAT_PAY_SCHEME = "weixin://wap/pay?";
    /**
     * 支付宝
     */
    public static final String ALIPAYS_SCHEME = "alipays://";
    /**
     * http scheme
     */
    public static final String HTTP_SCHEME = "http://";
    /**
     * https scheme
     */
    public static final String HTTPS_SCHEME = "https://";

    private WeakReference<Context> context;

    public WebViewClient(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    public void onPageStarted(IAgentWebView view, String url, Bitmap favicon) {

    }

    /** 对url进行类一些默认的处理 */
    @Override
    public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
        if (url == null) {
            return true;
        }

        //电话 邮箱 短信的处理
        if (handleCommonLink(url)) {
            return true;
        }

        //Intent scheme
        if (url.startsWith(INTENT_SCHEME)) {
            handleIntentUrl(url);
            return true;
        }

        //微信支付
        if (url.startsWith(WEBCHAT_PAY_SCHEME)) {
            startActivity(url);
            return true;
        }

        //支付宝
        if (url.startsWith(ALIPAYS_SCHEME) && lookup(url)) {
            return true;
        }

        return false;
    }

    @Override
    public void onPageFinished(IAgentWebView view, String url) {

    }

    @Override
    public void onReceivedError(IAgentWebView view, int errorCode, String description, String url) {

    }

    @Override
    public void shouldInterceptRequest(IAgentWebView view, String url) {

    }

    @Override
    public boolean shouldOverrideKeyEvent(IAgentWebView view, KeyEvent event) {
        return false;
    }

    @Override
    public void onLoadResource(IAgentWebView view, String url) {

    }

    @Override
    public void onPageCommitVisible(IAgentWebView view, String url) {

    }

    @Override
    public void doUpdateVisitedHistory(IAgentWebView view, String url, boolean isReload) {

    }

    @Override
    public void onUnhandledKeyEvent(IAgentWebView webView, KeyEvent keyEvent) {

    }

    private void startActivity(String url) {
        try {
            if (context == null || context.get() == null) {
                return;
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.get().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIntentUrl(String intentUrl) {
        try {
            Intent intent = null;
            if (TextUtils.isEmpty(intentUrl) || !intentUrl.startsWith(INTENT_SCHEME)) {
                return;
            }
            if (lookup(intentUrl)) {
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean lookup(String url) {
        try {
            if (context == null || context.get() == null) {
                return false;
            }
            Intent intent;
            Activity mActivity = null;
            if ((mActivity = (Activity) context.get()) == null) {
                return true;
            }
            PackageManager packageManager = mActivity.getPackageManager();
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            // 跳到该应用
            if (info != null) {
                mActivity.startActivity(intent);
                return true;
            }
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }

        return false;
    }

    private boolean handleCommonLink(String url) {
        if (url.startsWith(SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(SCHEME_MAILTO)
                || url.startsWith(SCHEME_GEO)) {
            try {
                Activity mActivity = null;
                if ((mActivity = (Activity) context.get()) == null) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private int queryActiviesNumber(String url) {
        try {
            if (context.get() == null) {
                return 0;
            }
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            PackageManager mPackageManager = context.get().getPackageManager();
            List<ResolveInfo> mResolveInfos = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return mResolveInfos == null ? 0 : mResolveInfos.size();
        } catch (URISyntaxException ignore) {
            ignore.printStackTrace();
            return 0;
        }
    }

}
