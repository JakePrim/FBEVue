package com.prim.primweb.core.config.builder;

import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.jsloader.CommonJSListener;
import com.prim.primweb.core.jsloader.ICallJsLoader;
import com.prim.primweb.core.uicontroller.AbsWebUIController;
import com.prim.primweb.core.urlloader.IUrlLoader;
import com.prim.primweb.core.webclient.webchromeclient.AgentChromeClient;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.websetting.IAgentWebSetting;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.webpool.IJavascriptInterface;
import com.tencent.smtt.sdk.WebView;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019-08-26 - 18:37
 */
public class CommonBuilder {
    private PrimBuilder primBuilder;

    public CommonBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    /**
     * 设置代理的webview 若不设置使用默认的
     */
    public CommonBuilder setAgentWebView(IAgentWebView webView) {
        primBuilder.webView = webView;
        primBuilder.mView = webView.getAgentWebView();
        if (primBuilder.mView instanceof WebView) {
            primBuilder.setWebViewType(PrimWeb.WebViewType.X5);
        } else {
            primBuilder.setWebViewType(PrimWeb.WebViewType.Android);
        }
        return this;
    }

    /**
     * web的代理设置
     */
    public CommonBuilder setWebSetting(IAgentWebSetting agentWebSetting) {
        primBuilder.setting = agentWebSetting;
        return this;
    }

    /**
     * 设置自定义的url加载器
     */
    public CommonBuilder setUrlLoader(IUrlLoader urlLoader) {
        primBuilder.urlLoader = urlLoader;
        return this;
    }

    /**
     * 设置自定义js 方法加载器
     */
    public CommonBuilder setCallJsLoader(ICallJsLoader callJsLoader) {
        primBuilder.callJsLoader = callJsLoader;
        return this;
    }

    /**
     * 设置模式 js脚本的注入模式
     */
    public CommonBuilder setModeType(PrimWeb.ModeType modeType) {
        primBuilder.modeType = modeType;
        return this;
    }

    /**
     * 设置WebView的类型 如果设置了setAgentWebView 此方法最好不要调用setAgentWebView 会默认判断webview的类型
     */
    public CommonBuilder setWebViewType(PrimWeb.WebViewType webViewType) {
        primBuilder.setWebViewType(webViewType);
        return this;
    }

    /**
     * 设置代理的WebViewClient 兼容android webView 和 x5 webView
     */
    public CommonBuilder setWebViewClient(AgentWebViewClient agentWebViewClient) {
        primBuilder.agentWebViewClient = agentWebViewClient;
        return this;
    }

    public CommonBuilder setWebViewClient(WebViewClient webViewClient) {
        primBuilder.webViewClient = webViewClient;
        return this;
    }

    public CommonBuilder setWebViewClient(com.tencent.smtt.sdk.WebViewClient webViewClient) {
        primBuilder.x5WebViewClient = webViewClient;
        return this;
    }

    /**
     * 设置代理的WebChromeClient 兼容android webView 和 x5 webView
     */
    public CommonBuilder setWebChromeClient(AgentChromeClient agentWebChromeClient) {
        primBuilder.agentWebChromeClient = agentWebChromeClient;
        return this;
    }

    public CommonBuilder setWebChromeClient(WebChromeClient webChromeClient) {
        primBuilder.webChromeClient = webChromeClient;
        return this;
    }

    public CommonBuilder setWebChromeClient(com.tencent.smtt.sdk.WebChromeClient webChromeClient) {
        primBuilder.x5WebChromeClient = webChromeClient;
        return this;
    }

    /**
     * 检查js方法是否存在
     */
    public CommonBuilder setListenerCheckJsFunction(CommonJSListener commonJSListener) {
        primBuilder.commonJSListener = commonJSListener;
        return this;
    }

    /**
     * 注入js脚本
     */
    public CommonBuilder addJavascriptInterface(@NonNull String name, @NonNull Object o) {
        primBuilder.addJavaObject(name, o);
        return this;
    }

    /**
     * 是否允许打开其他应用
     */
    public CommonBuilder alwaysOpenOtherPage(boolean alwaysOpenOtherPage) {
        primBuilder.alwaysOpenOtherPage = alwaysOpenOtherPage;
        return this;
    }

    /**
     * 设置自定义的UI控制器
     */
    public CommonBuilder setWebUIController(AbsWebUIController absWebUIController) {
        primBuilder.absWebUIController = absWebUIController;
        return this;
    }

    /**
     * 设置是否允许上传文件 默认为允许
     */
    public CommonBuilder setAllowUploadFile(boolean flag) {
        primBuilder.allowUploadFile = flag;
        return this;
    }

    /**
     * 设置是否允许定位 默认为允许
     */
    public CommonBuilder setGeolocation(boolean flag) {
        primBuilder.isGeolocation = flag;
        return this;
    }

    /**
     * 上传文件 false 调用系统文件  true 调用自定义的文件库
     */
    public CommonBuilder setUpdateInvokThrid(boolean flag) {
        primBuilder.invokingThird = flag;
        return this;
    }

    /**
     * 设置
     *
     * @param javascriptInterface
     * @return
     */
    public CommonBuilder setJavaScriptInterface(IJavascriptInterface javascriptInterface) {
        primBuilder.javaBridge = javascriptInterface;
        return this;
    }

    /**
     * 设置完成开始建造
     */
    public PerBuilder buildWeb() {
        return primBuilder.build();
    }
}
