package com.prim.primweb.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.prim.primweb.core.client.DefaultAgentWebChromeClient;
import com.prim.primweb.core.client.DefaultAgentWebViewClient;
import com.prim.primweb.core.client.IAgentWebChromeClient;
import com.prim.primweb.core.client.IAgentWebViewClient;
import com.prim.primweb.core.jsinterface.IJsInterface;
import com.prim.primweb.core.jsinterface.SafeJsInterface;
import com.prim.primweb.core.jsloader.ICallJsLoader;
import com.prim.primweb.core.jsloader.SafeCallJsLoaderImpl;
import com.prim.primweb.core.life.IWebLifeCycle;
import com.prim.primweb.core.life.WebLifeCycle;
import com.prim.primweb.core.setting.DefaultWebSetting;
import com.prim.primweb.core.setting.IAgentWebSetting;
import com.prim.primweb.core.setting.X5DefaultWebSetting;
import com.prim.primweb.core.urlloader.IUrlLoader;
import com.prim.primweb.core.urlloader.UrlLoader;
import com.prim.primweb.core.utils.PrimWebUtils;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.PrimAgentWebView;
import com.prim.primweb.core.webview.X5AgentWebView;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/11 0011
 * 描    述：代理webview的总入口
 * 修订历史：
 * ================================================
 */
public class PrimWeb {

    private ICallJsLoader callJsLoader;

    public static boolean DEBUG = false;

    private IAgentWebView webView;
    private ViewGroup mViewGroup;
    private ViewGroup.LayoutParams mLayoutParams;
    private WeakReference<Context> context;
    private int mIndex = 0;
    private IAgentWebSetting setting;
    private Map<String, String> headers;
    private IUrlLoader urlLoader;
    private View mView;

    /**
     * 设置webview的模式 分为
     * Strict - 严格的模式：api小于17 禁止注入js,大于 17 注入js的对象所有方法必须都包含JavascriptInterface注解
     * Normal - 为正常模式
     */
    public enum ModeType {
        Strict, Normal
    }

    public enum WebViewType {
        Android, X5
    }

    private WebViewType webViewType = WebViewType.Android;

    private ModeType modeType = ModeType.Normal;

    private static final String TAG = "PrimWeb";

    private IJsInterface mJsInterface;

    private IAgentWebViewClient agentWebViewClient;

    private IAgentWebChromeClient agentWebChromeClient;

    //初始化个数 30 * 0.75 多个左右,考虑到哈希表默认大小只有 4 * 0.75 个
    //而哈希表的缺点是:扩容性能会下降 初始化时提前计算好上限.
    private HashMap<String, Object> mJavaObject = new HashMap<>(30);

    private WebViewClient webViewClient;
    private com.tencent.smtt.sdk.WebViewClient x5WebViewClient;
    private WebChromeClient webChromeClient;
    private com.tencent.smtt.sdk.WebChromeClient x5WebChromeClient;

    //webview的生命周期管理
    private IWebLifeCycle webLifeCycle;

    public static void init(Application application) {
        // X5浏览器实列化
        QbSdk.initX5Environment(application, null);
    }

    PrimWeb(PrimBuilder builder) {
        this.webView = builder.webView;
        this.mView = builder.mView;
        this.mViewGroup = builder.mViewGroup;
        this.mLayoutParams = builder.mLayoutParams;
        this.context = builder.context;
        this.mIndex = builder.mIndex;
        this.setting = builder.setting;
        this.headers = builder.headers;
        this.urlLoader = builder.urlLoader;
        this.callJsLoader = builder.callJsLoader;
        this.modeType = builder.modeType;
        this.agentWebViewClient = builder.agentWebViewClient;
        this.webViewClient = builder.webViewClient;
        this.x5WebViewClient = builder.x5WebViewClient;
        this.webChromeClient = builder.webChromeClient;
        this.x5WebChromeClient = builder.x5WebChromeClient;
        this.webViewType = builder.webViewType;
        this.agentWebChromeClient = builder.agentWebChromeClient;
        doCheckSafe();
        webLifeCycle = new WebLifeCycle(webView);
        if (builder.mJavaObject != null && !builder.mJavaObject.isEmpty()) {
            this.mJavaObject.putAll(builder.mJavaObject);
        }
        if (mViewGroup != null) {
            mViewGroup.addView(mView, mIndex, mLayoutParams);
        } else {//考虑到极端的情况
            PrimWebUtils.scanForActivity(context.get()).setContentView(mView);
        }
    }

    /** webview 安全检查 */
    private void doCheckSafe() {
        if (null == webView) {//webview 不能为空
            webView = new PrimAgentWebView(context.get());
            mView = webView.getAgentWebView();
        }
        webView.removeRiskJavascriptInterface();
    }

    /** 获取调用js方法 */
    public ICallJsLoader getCallJsLoader() {
        if (null == webView) {
            throw new NullPointerException("webView most not be null,please check your code!");
        }
        if (callJsLoader == null) {
            callJsLoader = SafeCallJsLoaderImpl.getInstance(webView);
        }
        return callJsLoader;
    }

    /** 获取注入js脚本方法 */
    public IJsInterface getJsInterface() {
        if (null == webView) {
            throw new NullPointerException("webView most not be null,please check your code!");
        }
        if (mJsInterface == null) {
            mJsInterface = SafeJsInterface.getInstance(webView, modeType);
        }
        return mJsInterface;
    }

    /** 获取websettings， Object具体的是android webSetting 还是x5 webSetting 自己判断强转 */
    public Object getWebSettings() {
        if (null == setting) {
            if (webViewType == WebViewType.Android) {
                setting = new DefaultWebSetting(context.get());
            } else {
                setting = new X5DefaultWebSetting(context.get());
            }
        }
        return setting.getWebSetting();
    }

    /** 获取url加载器 加载URL和刷新url操作 */
    public IUrlLoader getUrlLoader() {
        if (null == webView) {
            throw new NullPointerException("webView most not be null,please check your code!");
        }
        if (null == urlLoader) {
            urlLoader = new UrlLoader(webView);
        }
        return urlLoader;
    }

    /** 设置webview的生命周期 */
    public IWebLifeCycle webLifeCycle() {
        if (webLifeCycle == null) {
            if (webView != null) {
                webLifeCycle = new WebLifeCycle(webView);
            }
        }
        return webLifeCycle;
    }

    /** 获取webview */
    public IAgentWebView getWebView() {
        return webView;
    }

    /** 准备阶段,检查完毕后加载url */
    void ready() {
        // 加载webview设置
        if (null == setting) {
            if (webViewType == WebViewType.Android) {
                setting = new DefaultWebSetting(context.get());
            } else {
                setting = new X5DefaultWebSetting(context.get());
            }
        }
        setting.setSetting(webView);

        // 加载url加载器
        if (null == urlLoader) {
            urlLoader = new UrlLoader(webView);
        }

        // 加载webViewClient 系统设置的优先
        if (webViewClient != null || x5WebViewClient != null) {
            if (webViewClient != null) {
                webView.setAndroidWebViewClient(webViewClient);
            }
            if (x5WebViewClient != null) {
                webView.setX5WebViewClient(x5WebViewClient);
            }
        } else {
            // 代理加载webViewClient
            if (null == agentWebViewClient) {
                agentWebViewClient = new DefaultAgentWebViewClient(context.get());
            }
            webView.setAgentWebViewClient(agentWebViewClient);
        }

        //加载webChromeClient 系统设置的优先
        if (webChromeClient != null || x5WebChromeClient != null) {
            if (webChromeClient != null) {
                webView.setAndroidWebChromeClient(webChromeClient);
            }
            if (x5WebChromeClient != null) {
                webView.setX5WebChromeClient(x5WebChromeClient);
            }
        } else {
            //加载代理的webChromeClient
            if (null == agentWebChromeClient) {
                if (webViewType == WebViewType.Android) {
                    agentWebChromeClient = new DefaultAgentWebChromeClient<WebChromeClient.FileChooserParams>(context.get());
                } else if (webViewType == WebViewType.X5) {
                    agentWebChromeClient = new DefaultAgentWebChromeClient<com.tencent.smtt.sdk.WebChromeClient.FileChooserParams>(context.get());
                }
            }
            webView.setAgentWebChromeClient(agentWebChromeClient);
        }

        // 加载js脚本注入
        if (null == mJsInterface) {
            mJsInterface = SafeJsInterface.getInstance(webView, modeType);
        }

        if (mJavaObject != null && !mJavaObject.isEmpty()) {
            mJsInterface.addJavaObjects(mJavaObject);
        }
    }

    /** 发起最终阶段 加载url */
    PrimWeb launch(String url) {
        if (null == headers || headers.isEmpty()) {
            urlLoader.loadUrl(url);
        } else {
            urlLoader.loadUrl(url, headers);
        }
        return this;
    }

    public static PrimBuilder with(Context context) {
        if (context == null) {
            throw new NullPointerException("context can not be null");
        }
        return new PrimBuilder(context);
    }

    public static class PrimBuilder {
        private IAgentWebView webView;
        private View mView;
        private WeakReference<Context> context;
        private ViewGroup mViewGroup;
        private ViewGroup.LayoutParams mLayoutParams;
        private int mIndex;
        private IAgentWebSetting setting;
        private Map<String, String> headers;
        private IUrlLoader urlLoader;
        private ICallJsLoader callJsLoader;
        private ModeType modeType = ModeType.Normal;
        private HashMap<String, Object> mJavaObject;
        private IAgentWebViewClient agentWebViewClient;
        private WebViewType webViewType = WebViewType.Android;
        private WebViewClient webViewClient;
        private com.tencent.smtt.sdk.WebViewClient x5WebViewClient;
        private WebChromeClient webChromeClient;
        private com.tencent.smtt.sdk.WebChromeClient x5WebChromeClient;
        private IAgentWebChromeClient agentWebChromeClient;

        PrimBuilder(Context context) {
            this.context = new WeakReference<>(context);
        }

        /** 设置webview的父类 */
        public CommonBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp) {
            this.mViewGroup = v;
            this.mLayoutParams = lp;
            return new CommonBuilder(this);
        }

        public CommonBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp, int index) {
            this.mViewGroup = v;
            this.mLayoutParams = lp;
            this.mIndex = index;
            return new CommonBuilder(this);
        }

        /** 所有设置完成 */
        public PerBuilder build() {
            if (mViewGroup == null) {
                throw new NullPointerException("ViewGroup not null,please check your code!");
            }
            return new PerBuilder(new PrimWeb(this));
        }

        private void addJavaObject(String key, Object o) {
            if (mJavaObject == null) {
                mJavaObject = new HashMap<>(30);//初始化个数 30 * 0.75 多个左右,考虑到哈希表 默认大小只有 4 * 0.75 个
                // 而哈希表的缺点是:扩容性能会下降 初始化时提前计算好上限.
            }
            mJavaObject.put(key, o);
        }

        private void setWebViewType(WebViewType webViewType) {
            this.webViewType = webViewType;
            if (null == this.webView) {
                if (webViewType == WebViewType.X5) {
                    this.webView = new X5AgentWebView(context.get());
                    this.mView = this.webView.getAgentWebView();
                } else {
                    this.webView = new PrimAgentWebView(context.get());
                    this.mView = this.webView.getAgentWebView();
                }
            }
        }
    }

    public static class CommonBuilder {
        private PrimBuilder primBuilder;

        public CommonBuilder(PrimBuilder primBuilder) {
            this.primBuilder = primBuilder;
        }

        /** 设置代理的webview 若不设置使用默认的 */
        public CommonBuilder setAgentWebView(IAgentWebView webView) {
            primBuilder.webView = webView;
            primBuilder.mView = webView.getAgentWebView();
            if (primBuilder.mView instanceof WebView) {
                primBuilder.setWebViewType(WebViewType.X5);
            } else {
                primBuilder.setWebViewType(WebViewType.Android);
            }
            return this;
        }

        /** web的代理设置 */
        public CommonBuilder setAgentWebSetting(IAgentWebSetting agentWebSetting) {
            primBuilder.setting = agentWebSetting;
            return this;
        }

        /** 设置url加载器 */
        public CommonBuilder setUrlLoader(IUrlLoader urlLoader) {
            primBuilder.urlLoader = urlLoader;
            return this;
        }

        /** 设置js 方法加载器 */
        public CommonBuilder setCallJsLoader(ICallJsLoader callJsLoader) {
            primBuilder.callJsLoader = callJsLoader;
            return this;
        }

        /** 设置模式 js脚本的注入模式 */
        public CommonBuilder setModeType(ModeType modeType) {
            primBuilder.modeType = modeType;
            return this;
        }

        /** 设置WebView的类型 如果设置了setAgentWebView 此方法最好不要调用setAgentWebView 会默认判断webview的类型 */
        public CommonBuilder setWebViewType(WebViewType webViewType) {
            primBuilder.setWebViewType(webViewType);
            return this;
        }

        /** 设置代理的WebViewClient 兼容android webview 和 x5 webview */
        public CommonBuilder setAgentWebViewClient(IAgentWebViewClient agentWebViewClient) {
            primBuilder.agentWebViewClient = agentWebViewClient;
            return this;
        }

        /** 设置代理的WebChromeClient 兼容android webview 和 x5 webview */
        public CommonBuilder setAgentWebChromeClient(IAgentWebChromeClient agentWebChromeClient) {
            primBuilder.agentWebChromeClient = agentWebChromeClient;
            return this;
        }


        /** 如果不想要使用代理的 通过以下方法来调用系统自带的 但是兼容android webview 和 x5 webview 需要各自实现 */
        public CommonBuilder setAndroidWebViewClient(WebViewClient webViewClient) {
            primBuilder.webViewClient = webViewClient;
            return this;
        }

        public CommonBuilder setX5WebViewClient(com.tencent.smtt.sdk.WebViewClient webViewClient) {
            primBuilder.x5WebViewClient = webViewClient;
            return this;
        }

        public CommonBuilder setAndroidWebChromeClient(WebChromeClient webChromeClient) {
            primBuilder.webChromeClient = webChromeClient;
            return this;
        }

        public CommonBuilder setX5WebChromeClient(com.tencent.smtt.sdk.WebChromeClient webChromeClient) {
            primBuilder.x5WebChromeClient = webChromeClient;
            return this;
        }

        /** 注入js脚本 */
        public JsInterfaceBuilder addJavascriptInterface(@NonNull String name, @NonNull Object o) {
            return new JsInterfaceBuilder(primBuilder).addJavascriptInterface(name, o);
        }

        /** 设置完成开始建造 */
        public PerBuilder buildWeb() {
            return primBuilder.build();
        }
    }

    public static class JsInterfaceBuilder {
        private PrimBuilder primBuilder;

        public JsInterfaceBuilder(PrimBuilder primBuilder) {
            this.primBuilder = primBuilder;
        }

        /** 注入js脚本 */
        public JsInterfaceBuilder addJavascriptInterface(@NonNull String name, @NonNull Object o) {
            primBuilder.addJavaObject(name, o);
            return this;
        }

        /** 设置完成开始建造 */
        public PerBuilder buildWeb() {
            return primBuilder.build();
        }

    }

    /** 设置完成准备发射 */
    public static class PerBuilder {
        private PrimWeb primWeb;
        private boolean isReady = false;

        public PerBuilder(PrimWeb primWeb) {
            this.primWeb = primWeb;
        }

        public PerBuilder readyOk() {
            if (!isReady) {
                primWeb.ready();
                isReady = true;
            }
            return this;
        }

        public PrimWeb launch(@NonNull String url) {
            if (!isReady) {
                readyOk();
            }
            return primWeb.launch(url);
        }
    }
}
