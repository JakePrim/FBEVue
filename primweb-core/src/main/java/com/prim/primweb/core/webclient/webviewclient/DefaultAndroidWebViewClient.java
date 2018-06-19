package com.prim.primweb.core.webclient.webviewclient;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prim.primweb.core.webclient.PrimWebClient;
import com.prim.primweb.core.webclient.base.BaseAndroidWebClient;
import com.prim.primweb.core.webview.IAgentWebView;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.List;

import static com.prim.primweb.core.webclient.ClientConstance.INTENT_SCHEME;
import static com.prim.primweb.core.webclient.ClientConstance.SCHEME_SMS;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：6/15 0015
 * 描    述：默认的Android - WebViewClient
 * 可使用原声的WebViewClient 也可以使用具有兼容性代理的AgentWebViewClient
 * 修订历史：
 * ================================================
 */
public class DefaultAndroidWebViewClient extends BaseAndroidWebClient {

    private WeakReference<Activity> weakReference;

    private static final String TAG = "DefaultAndroidWebViewCl";

    /**
     * 拦截不知道的url
     */
    private boolean interceptUnkownUrl = true;

    /**
     * 是否允许打开其他界面 默认为允许
     */
    private boolean alwaysOpenOtherPage = true;

    private PrimWebClient.Builder builder;

    public DefaultAndroidWebViewClient(Activity activity, PrimWebClient.Builder builder) {
        weakReference = new WeakReference<>(activity);
        this.builder = builder;
        alwaysOpenOtherPage = builder.alwaysOpenOtherPage;
    }

    /**
     * WebViewClient
     *
     * @param webViewClient
     *         WebViewClient
     */
    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
    }

    /**
     * 使用具有兼容性代理的AgentWebViewClient
     *
     * @param webViewClient
     *         AgentWebViewClient
     * @param agentWebView
     *         IAgentWebView
     */
    @Override
    public void setWebViewClient(AgentWebViewClient webViewClient, IAgentWebView agentWebView) {
        super.setWebViewClient(webViewClient, agentWebView);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 常见的电话 短信 邮箱的跳转
        if (handleCommonLink(url)) {
            return true;
        }

        // intent:// 跳转
        if (url.startsWith(INTENT_SCHEME)) {
            intentUrl(url);
            return true;
        }

        //打开Url相对应的界面
        if (queryActivitysNum(url) > 0 && openOther(url)) {
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        // 常见的电话 短信 邮箱的跳转
        if (handleCommonLink(url)) {
            return true;
        }

        // intent:// 跳转
        if (url.startsWith(INTENT_SCHEME)) {
            intentUrl(url);
            return true;
        }

        //打开Url相对应的界面
        if (queryActivitysNum(url) > 0 && openOther(url)) {
            return true;
        }

        return super.shouldOverrideUrlLoading(view, request);
    }

    private int queryActivitysNum(String url) {
        try {
            if (weakReference == null || weakReference.get() == null) {
                return 0;
            }

            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            PackageManager packageManager = weakReference.get().getPackageManager();
            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return resolveInfo == null ? 0 : resolveInfo.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean openOther(String url) {
        if (alwaysOpenOtherPage) {
            loop(url);
            return true;
        }

        return false;
    }


    private void intentUrl(String url) {
        try {
            Intent intent = null;

            if (TextUtils.isEmpty(url) || !url.startsWith(INTENT_SCHEME)) {
                return;
            }

            if (loop(url)) return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean loop(String url) {
        try {
            Intent intent;
            if (weakReference == null || weakReference.get() == null) {
                return true;
            }

            Activity activity = weakReference.get();
            PackageManager packageManager = activity.getPackageManager();
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //跳到该应用
            if (resolveInfo != null) {
                activity.startActivity(intent);
                return true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 电话 短信 邮箱的跳转
     *
     * @param url
     *         url
     *
     * @return boolean
     */
    private boolean handleCommonLink(String url) {
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                Activity mActivity = null;
                if ((mActivity = weakReference.get()) == null) {
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
}
