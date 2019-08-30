package com.prim.primweb.core.config.builder;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.config.ConfigKey;
import com.prim.primweb.core.jsloader.CommonJSListener;
import com.prim.primweb.core.jsloader.ICallJsLoader;
import com.prim.primweb.core.uicontroller.AbsWebUIController;
import com.prim.primweb.core.urlloader.IUrlLoader;
import com.prim.primweb.core.webclient.webchromeclient.AgentChromeClient;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.websetting.IAgentWebSetting;
import com.prim.primweb.core.webview.AndroidAgentWebView;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.X5AgentWebView;
import com.prim.primweb.core.webview.webpool.IJavascriptInterface;
import com.prim.primweb.core.webview.webpool.WebViewPool;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;

import static com.prim.primweb.core.PrimWeb.getConfiguration;

/**
 * @author prim
 * @version 1.0.0
 * @desc 通用设置配置
 * @time 2019-08-26 - 18:37
 */
public class WebBuilder {

    private PrimBuilder primBuilder;

    public WebBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    /**
     * 设置代理的webview 若不设置使用默认的
     */
    public WebBuilder setAgentWebView(IAgentWebView webView) {
        primBuilder.webView = webView;
        primBuilder.mView = webView.getAgentWebView();
        if (primBuilder.mView instanceof WebView) {
            setWebViewType(PrimWeb.WebViewType.X5);
        } else {
            setWebViewType(PrimWeb.WebViewType.Android);
        }
        return this;
    }

    /**
     * web的代理设置
     */
    public WebBuilder setWebSetting(IAgentWebSetting agentWebSetting) {
        primBuilder.setting = agentWebSetting;
        return this;
    }

    /**
     * 设置自定义的url加载器
     */
    public WebBuilder setUrlLoader(IUrlLoader urlLoader) {
        primBuilder.urlLoader = urlLoader;
        return this;
    }

    /**
     * 设置自定义js 方法加载器
     */
    public WebBuilder setCallJsLoader(ICallJsLoader callJsLoader) {
        primBuilder.callJsLoader = callJsLoader;
        return this;
    }

    /**
     * 设置模式 js脚本的注入模式
     */
    public WebBuilder setModeType(PrimWeb.ModeType modeType) {
        primBuilder.modeType = modeType;
        return this;
    }

    /**
     * 设置webview的类型
     * 设置WebView的类型 如果设置了setAgentWebView 此方法最好不要调用setAgentWebView 会默认判断webview的类型
     *
     * @param webViewType 目前支持两种类型 X5 Android
     */
    public WebBuilder setWebViewType(final PrimWeb.WebViewType webViewType) {
        primBuilder.webViewType = webViewType;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            primBuilder.handler.post(new Runnable() {
                @Override
                public void run() {
                    initWeb(webViewType);
                }
            });
        } else {
            initWeb(webViewType);
        }
        return this;
    }

    private void initWeb(PrimWeb.WebViewType webViewType) {
        if (null == primBuilder.webView) {
            try {
                boolean webPool = getConfiguration(ConfigKey.WEB_POOL);
                if (webPool) {
                    primBuilder.webView = WebViewPool.getInstance().get(webViewType);
                    primBuilder.mView = primBuilder.webView.getAgentWebView();
                } else {
                    if (webViewType == PrimWeb.WebViewType.X5) {
                        primBuilder.webView = new X5AgentWebView(primBuilder.context.get());
                        primBuilder.mView = primBuilder.webView.getAgentWebView();
                    } else {
                        primBuilder.webView = new AndroidAgentWebView(primBuilder.context.get());
                        primBuilder.mView = primBuilder.webView.getAgentWebView();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                primBuilder.webView = new X5AgentWebView(primBuilder.context.get());
                primBuilder.mView = primBuilder.webView.getAgentWebView();
            }
        }
    }

    /**
     * 设置代理的WebViewClient 兼容android webView 和 x5 webView
     */
    public WebBuilder setWebViewClient(AgentWebViewClient agentWebViewClient) {
        primBuilder.agentWebViewClient = agentWebViewClient;
        return this;
    }

    public WebBuilder setWebViewClient(WebViewClient webViewClient) {
        primBuilder.webViewClient = webViewClient;
        return this;
    }

    public WebBuilder setWebViewClient(com.tencent.smtt.sdk.WebViewClient webViewClient) {
        primBuilder.x5WebViewClient = webViewClient;
        return this;
    }

    /**
     * 设置代理的WebChromeClient 兼容android webView 和 x5 webView
     */
    public WebBuilder setWebChromeClient(AgentChromeClient agentWebChromeClient) {
        primBuilder.agentWebChromeClient = agentWebChromeClient;
        return this;
    }

    public WebBuilder setWebChromeClient(WebChromeClient webChromeClient) {
        primBuilder.webChromeClient = webChromeClient;
        return this;
    }

    public WebBuilder setWebChromeClient(com.tencent.smtt.sdk.WebChromeClient webChromeClient) {
        primBuilder.x5WebChromeClient = webChromeClient;
        return this;
    }

    /**
     * 检查js方法是否存在
     */
    public WebBuilder setListenerCheckJsFunction(CommonJSListener commonJSListener) {
        primBuilder.commonJSListener = commonJSListener;
        return this;
    }

    /**
     * 注入js脚本
     */
    public WebBuilder addJavascriptInterface(@NonNull String name, @NonNull Object o) {
        addJavaObject(name, o);
        return this;
    }

    private void addJavaObject(String key, Object o) {
        if (primBuilder.mJavaObject == null) {
            primBuilder.mJavaObject = new HashMap<>(30);//初始化个数 30 * 0.75 多个左右,考虑到哈希表 默认大小只有 4 * 0.75 个
            // 而哈希表的缺点是:扩容性能会下降 初始化时提前计算好上限.
        }
        primBuilder.mJavaObject.put(key, o);
    }


    /**
     * 是否允许打开其他应用
     */
    public WebBuilder alwaysOpenOtherPage(boolean alwaysOpenOtherPage) {
        primBuilder.alwaysOpenOtherPage = alwaysOpenOtherPage;
        return this;
    }

    /**
     * 设置自定义的UI控制器
     */
    public WebBuilder setWebUIController(AbsWebUIController absWebUIController) {
        primBuilder.absWebUIController = absWebUIController;
        return this;
    }

    /**
     * 设置是否允许上传文件 默认为允许
     */
    public WebBuilder setAllowUploadFile(boolean flag) {
        primBuilder.allowUploadFile = flag;
        return this;
    }

    /**
     * 设置是否允许定位 默认为允许
     */
    public WebBuilder setGeolocation(boolean flag) {
        primBuilder.isGeolocation = flag;
        return this;
    }

    /**
     * 上传文件 false 调用系统文件  true 调用自定义的文件库
     */
    public WebBuilder setUpdateInvokThrid(boolean flag) {
        primBuilder.invokingThird = flag;
        return this;
    }

    /**
     * 设置
     *
     * @param javascriptInterface
     */
    public WebBuilder setJavaScriptInterface(IJavascriptInterface javascriptInterface) {
        primBuilder.javaBridge = javascriptInterface;
        return this;
    }

    /**
     * 下一项配置
     */
    public PrimBuilder next() {
        return primBuilder;
    }

    /**
     * 设置完成开始建造
     */
    public PerBuilder buildWeb() {
        return primBuilder.build();
    }
}
