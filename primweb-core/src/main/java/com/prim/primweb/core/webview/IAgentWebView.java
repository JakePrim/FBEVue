package com.prim.primweb.core.webview;

import android.view.View;

import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.listener.OnScrollChangeListener;

import java.util.Map;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/11 0011
 * 描    述：代理webview接口
 * T:考虑到现在流行的x5 webSetting 设置为泛型 拥抱变化
 * 修订历史：v1.0.0
 * ================================================
 */
public interface IAgentWebView<T> {

    /** 获取代理的webview */
    View getAgentWebView();

    /** 获取代理的webview的设置 */
    T getWebSetting();

    /** 代理的webview加载js方法 */
    void loadAgentJs(String js);

    /** 代理的webview加载js方法 */
    void loadAgentJs(String js, AgentValueCallback<String> callback);

    /** 代理的webview加载url */
    void loadAgentUrl(String url);

    /** 代理的webview加载url */
    void loadAgentUrl(String url, Map<String, String> headers);

    void reloadAgent();

    void stopLoadingAgent();

    void postUrlAgent(String url, byte[] params);

    void loadDataAgent(String data, String mimeType, String encoding);

    void loadDataWithBaseURLAgent(String baseUrl, String data, String mimeType, String encoding, String historyUrl);

    /** 代理的webview注入js的方法 */
    void addJavascriptInterfaceAgent(Object object, String name);

    /** 设置代理的WebViewClient */
    void setAgentWebViewClient(IAgentWebViewClient webViewClient);

    void setOnScrollChangeListener(OnScrollChangeListener listener);

}
