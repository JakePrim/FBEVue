package com.prim.primweb.core.webclient.base;

import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：6/15 0015
 * 描    述：默认的x5 - WebViewClient
 * 修订历史：
 * ================================================
 */
public class DefaultX5WebViewClient extends BaseX5WebViewClient {
    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
    }

    @Override
    public void setWebViewClient(AgentWebViewClient webViewClient, IAgentWebView agentWebView) {
        super.setWebViewClient(webViewClient, agentWebView);
    }
}
