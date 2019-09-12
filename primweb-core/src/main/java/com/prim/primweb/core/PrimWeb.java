package com.prim.primweb.core;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.prim.primweb.core.config.ConfigAnnotation;
import com.prim.primweb.core.config.ConfigKey;
import com.prim.primweb.core.config.Configurator;
import com.prim.primweb.core.config.builder.PrimBuilder;
import com.prim.primweb.core.file.FileValueCallbackMiddleActivity;
import com.prim.primweb.core.handler.IKeyEvent;
import com.prim.primweb.core.handler.IKeyEventInterceptor;
import com.prim.primweb.core.handler.KeyEventHandler;
import com.prim.primweb.core.jsloader.CommonJSListener;
import com.prim.primweb.core.jsloader.CommonJavaObject;
import com.prim.primweb.core.listener.OnDownloadListener;
import com.prim.primweb.core.uicontroller.AbsWebUIController;
import com.prim.primweb.core.uicontroller.DefaultWebUIController;
import com.prim.primweb.core.uicontroller.IndicatorController;
import com.prim.primweb.core.uicontroller.IndicatorHandler;
import com.prim.primweb.core.uicontroller.WebViewManager;
import com.prim.primweb.core.jsinterface.IJsInterface;
import com.prim.primweb.core.jsinterface.SafeJsInterface;
import com.prim.primweb.core.jsloader.ICallJsLoader;
import com.prim.primweb.core.jsloader.SafeCallJsLoaderImpl;
import com.prim.primweb.core.utils.PWLog;
import com.prim.primweb.core.webclient.PrimChromeClient;
import com.prim.primweb.core.webclient.PrimWebClient;
import com.prim.primweb.core.webclient.webchromeclient.AgentChromeClient;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.weblife.IWebLifeCycle;
import com.prim.primweb.core.weblife.WebLifeCycle;
import com.prim.primweb.core.websetting.BaseAgentWebSetting;
import com.prim.primweb.core.websetting.DefaultWebSetting;
import com.prim.primweb.core.websetting.IAgentWebSetting;
import com.prim.primweb.core.websetting.X5DefaultWebSetting;
import com.prim.primweb.core.urlloader.IUrlLoader;
import com.prim.primweb.core.urlloader.UrlLoader;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.AndroidAgentWebView;
import com.prim.primweb.core.webview.webpool.IJavascriptInterface;
import com.prim.primweb.core.webview.webpool.WebViewPool;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/11 0011
 * 描    述：PrimWeb的总入口
 * TODO 优化等级SSR
 * 修订历史：1.1.3
 * ================================================
 */
public class PrimWeb {

    public static final String BRIDGECHECK = "checkJsBridge";

    private ICallJsLoader callJsLoader;

    public static boolean DEBUG = false;

    private IAgentWebView webView;
    private ViewGroup mViewGroup;
    private ViewGroup.LayoutParams mLayoutParams;
    private WeakReference<Activity> context;
    private int mIndex = 0;
    private IAgentWebSetting setting;
    private Map<String, String> headers;
    private IUrlLoader urlLoader;
    private View mView;

    /**
     * 设置webview的模式:
     * Strict - 严格模式
     * Normal - 正常模式
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

    private AgentWebViewClient agentWebViewClient;

    private AgentChromeClient agentWebChromeClient;

    //初始化个数 30 * 0.75 多个左右,考虑到哈希表默认大小只有 4 * 0.75 个
    //而哈希表的缺点是:扩容性能会下降 初始化时提前计算好上限.
    private HashMap<String, Object> mJavaObject = new HashMap<>(30);
    private WebViewClient webViewClient;
    private WebChromeClient webChromeClient;
    private com.tencent.smtt.sdk.WebViewClient x5WebViewClient;
    private com.tencent.smtt.sdk.WebChromeClient x5WebChromeClient;

    private CommonJSListener commonJSListener;

    //webview的生命周期管理
    private IWebLifeCycle webLifeCycle;

    //返回键的处理类
    private IKeyEvent keyEvent;

    // 返回键拦截，有特殊处理的可以实现此接口
    private IKeyEventInterceptor keyEventInterceptor;

    //是否允许网页打开其他应用
    private boolean alwaysOpenOtherPage;

    private WebViewManager webViewManager;

    private AbsWebUIController absWebUIController;

    private boolean isGeolocation;

    private boolean allowUploadFile;

    private boolean invokingThird;

    private Context mAppContext;

    private static AtomicBoolean sLazyInitTag = new AtomicBoolean(false);

    /**
     * 懒初始化
     *
     * @param ctx 上下文
     * @return Configurator
     */
    @NonNull
    public static Configurator lazyInit(@NonNull Context ctx) {
        return lazyInit(ctx, false);
    }

    public static boolean x5CoreInited = false;

    @NonNull
    public static Configurator lazyInit(@NonNull Context ctx, boolean useX5) {
        if (sLazyInitTag.get()) return Configurator.getInstance();
        sLazyInitTag.set(true);
        Configurator.getInstance().getConfigurator().put(ConfigKey.APPLICATION_CONTEXT, ctx);
        Configurator.getInstance().getConfigurator().put(ConfigKey.WEB_X5CORE, useX5);
        return Configurator.getInstance();
    }


    public static void initWebPool(Context ctx, BaseAgentWebSetting setting, IJavascriptInterface javascriptInterface, String name) {
        boolean enableWebPool = getConfiguration(ConfigKey.WEB_POOL);
        if (enableWebPool) {
            WebViewPool.getInstance().initPool(ctx, setting, javascriptInterface, name);
        }
    }

    public static <T> T getConfiguration(@ConfigAnnotation int key) {
        return Configurator.getInstance().getConfiguration(key);
    }

    @NonNull
    public static Application getApplicationContext() {
        return getConfiguration(ConfigKey.APPLICATION_CONTEXT);
    }

    @NonNull
    public static PrimBuilder with(@NonNull Activity context) {
        if (context == null) {
            throw new NullPointerException("context can not be null");
        }
        return new PrimBuilder(context);
    }

    /**
     * 飞船建造阶段 -- 初始化各种配置设定
     *
     * @param builder 飞船设定系统
     */
    public PrimWeb(PrimBuilder builder) {

        //创建webView复用池
        createWebPool();

        //检查设置是否合理
        doCheckSafe(builder);

        //创建WebView的父View
        createUILayout(builder);

        //WebView的生命周期
        webLifeCycle = new WebLifeCycle(webView);

        //存储js脚本
        if (builder.mJavaObject != null && !builder.mJavaObject.isEmpty()) {
            this.mJavaObject.putAll(builder.mJavaObject);
        }
    }

    private void createWebPool() {
        //处理WebView复用池
//        boolean enableWebPool = getConfiguration(ConfigKey.WEB_POOL);
//        if (!WebViewPool.getInstance().isInitPool() && enableWebPool) {  //初始化时将setting设置进去
//            initWebPool(builder.context.get(), (BaseAgentWebSetting) builder.setting, builder.javaBridge, "nativeBridge");
//        }
    }


    /**
     * webView 安全检查加载配置 --- 检查飞船的各种设定是否安全
     */
    private void doCheckSafe(PrimBuilder builder) {
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
        this.commonJSListener = builder.commonJSListener;
        this.alwaysOpenOtherPage = builder.alwaysOpenOtherPage;
        this.absWebUIController = builder.absWebUIController;
        this.isGeolocation = builder.isGeolocation;
        this.allowUploadFile = builder.allowUploadFile;
        this.invokingThird = builder.invokingThird;

        if (null == webView) {//webview 不能为空
            webView = new AndroidAgentWebView(context.get());
            mView = webView.getAgentWebView();
        }
        //移除存在风险的JavascriptInterface
        webView.removeRiskJavascriptInterface();
        if (this.absWebUIController == null) {
            this.absWebUIController = new DefaultWebUIController(context.get());
        }
    }

    /**
     * 创建WebView的layout --- 创建飞船整体
     *
     * @param builder PrimBuilder
     */
    private void createUILayout(PrimBuilder builder) {
        webViewManager = WebViewManager.createWebView()
                .setActivity(context.get())
                .setViewGroup(mViewGroup)
                .setAgentWebView(webView)
                .setLp(mLayoutParams)
                .setCustomTopIndicator(builder.customTopIndicator)
                .setNeedTopProgress(builder.needTopIndicator)
                .setColor(builder.mColor)
                .setIndex(mIndex)
                .setColorPaser(builder.colorPaser)
                .setErrorClickId(builder.errorClickId)
                .setErrorLayout(builder.errorLayout)
                .setErrorView(builder.errorView)
                .setLoadView(builder.loadView)
                .setHeight(builder.height)
                .setIndicatorView(builder.mIndicatorView)
                .setLoadLayout(builder.loadLayout)
                .setAbsWebUIController(absWebUIController)
                .build();
    }

    /**
     * 飞船建造完毕进入 --- 准备阶段,检查所有引擎是否正常工作
     */
    public void ready() {
        //加载 webView设置
        createSetting();

        //加载 url加载器
        createUrlLoader();

        //加载webViewClient 系统设置的优先
        createWebViewClient();

        //加载webChromeClient 系统设置的优先
        createWebChromeClient();

        //加载js脚本注入
        createJsInterface();
    }

    /**
     * url加载器
     */
    private void createUrlLoader() {
        // 加载 url加载器
        if (null == urlLoader) {
            urlLoader = new UrlLoader(webView);
        }
    }

    /**
     * 加载js脚本注入
     */
    private void createJsInterface() {
        if (null == mJsInterface) {
            mJsInterface = SafeJsInterface.getInstance(webView, modeType);
        }

        // 注入通用的js脚本
        mJavaObject.put(BRIDGECHECK, new CommonJavaObject(commonJSListener));

        if (mJavaObject != null && !mJavaObject.isEmpty()) {
            mJsInterface.addJavaObjects(mJavaObject);
        }
    }

    /**
     * 初始化设置
     */
    private void createSetting() {
        if (null == setting) {
            if (webViewType == WebViewType.Android) {
                setting = new DefaultWebSetting(context.get());
            } else {
                setting = new X5DefaultWebSetting(context.get());
            }
        }
        setting.setSetting(webView);
    }

    /**
     * WebChromeClient
     */
    private void createWebChromeClient() {
        IndicatorController indicatorController = IndicatorHandler.getInstance().setIndicator(webViewManager.getIndicator());
        PrimChromeClient.createChromeBuilder()
                .setActivity(context.get())
                .setType(webViewType)
                .setWebView(webView)
                .setWebChromeClient(x5WebChromeClient)
                .setWebChromeClient(webChromeClient)
                .setWebChromeClient(agentWebChromeClient)
                .setAbsWebUIController(absWebUIController)
                .setAllowUploadFile(allowUploadFile)
                .setGeolocation(isGeolocation)
                .setIndicatorController(indicatorController)
                .setInvokingThird(invokingThird)
                .build();
    }

    /**
     * WebViewClient
     */
    private void createWebViewClient() {
        PrimWebClient.createClientBuilder()
                .setActivity(context.get())
                .setType(webViewType)
                .setWebView(webView)
                .setWebViewClient(x5WebViewClient)
                .setWebViewClient(webViewClient)
                .setWebViewClient(agentWebViewClient)
                .setAlwaysOpenOtherPage(alwaysOpenOtherPage)
                .setAbsWebUIController(absWebUIController)
                .build();
    }

    /**
     * 准备完毕 发起最终阶段 加载url -------> 飞船发射
     */
    public PrimWeb launch(String url) {
        PWLog.d("Web-Log -> 加载URL：" + url);
        if (null == headers || headers.isEmpty()) {
            urlLoader.loadUrl(url);
        } else {
            urlLoader.loadUrl(url, headers);
        }
        return this;
    }

    /**
     * 获取WebView的类型
     */
    public WebViewType getWebViewType() {
        return webViewType;
    }

    /**
     * 获取webview的父view
     */
    public FrameLayout getRootView() {
        if (null != webViewManager) {
            return webViewManager.getWebParentView();
        }
        return null;
    }

    /**
     * 获取调用js方法
     */
    public ICallJsLoader getCallJsLoader() {
        checkWebView();
        if (callJsLoader == null) {
            callJsLoader = SafeCallJsLoaderImpl.getInstance(webView);
        }
        return callJsLoader;
    }

    /**
     * 获取注入js脚本方法
     */
    public IJsInterface getJsInterface() {
        checkWebView();
        if (mJsInterface == null) {
            mJsInterface = SafeJsInterface.getInstance(webView, modeType);
        }
        return mJsInterface;
    }

    /**
     * 获取websettings， Object具体的是android webSetting 还是x5 webSetting 自己判断强转
     */
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

    /**
     * 长按图片等会用到 类型自己转换
     */
    public Object getHitTestResult() {
        checkWebView();
        return webView.getAgentHitTestResult();
    }

    /**
     * 获取url加载器 加载URL和刷新url操作
     */
    public IUrlLoader getUrlLoader() {
        checkWebView();
        if (null == urlLoader) {
            urlLoader = new UrlLoader(webView);
        }
        return urlLoader;
    }

    /**
     * 设置webview的生命周期
     */
    public IWebLifeCycle webLifeCycle() {
        if (webLifeCycle == null) {
            if (webView != null) {
                webLifeCycle = new WebLifeCycle(webView);
            }
        }
        return webLifeCycle;
    }

    /**
     * 获取webview
     */
    public IAgentWebView getWebView() {
        checkWebView();
        return webView;
    }

    /**
     * 获取真实的webview 类型可以自己强转
     */
    public View getRealWebView() {
        checkWebView();
        return webView.getAgentWebView();
    }

    /**
     * Check for the presence of a JS method
     *
     * @param checkJsFunction CommonJSListener
     */
    public void setListenerCheckJsFunction(CommonJSListener checkJsFunction) {
        if (null == commonJSListener) {
            this.commonJSListener = checkJsFunction;
        }
    }

    /**
     * 监听下载监听
     *
     * @param onDownloadListener
     */
    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        if (onDownloadListener != null) {
            webView.setAgentDownloadListener(onDownloadListener);
        }
    }

    /**
     * 监听WebView 长按事件
     *
     * @param longClick
     */
    public void setOnWebViewLongClick(IAgentWebView.OnWebViewLongClick longClick) {
        if (longClick != null) {
            webView.setOnWebViewLongClick(longClick);
        }
    }

    /**
     * handler back button
     *
     * @return true handler ;false no handler
     */
    public boolean handlerBack() {
        checkWebView();
        if (keyEvent == null) {
            keyEvent = KeyEventHandler.getInstance(webView, keyEventInterceptor);
        }
        return keyEvent.back();
    }

    /**
     * handler onKeyDown
     *
     * @param keyCode keyCode
     * @param event   KeyEvent
     * @return true handler;false no handler
     */
    public boolean handlerKeyEvent(int keyCode, KeyEvent event) {
        checkWebView();
        if (keyEvent == null) {
            keyEvent = KeyEventHandler.getInstance(webView, keyEventInterceptor);
        }
        return keyEvent.onKeyDown(keyCode, event);
    }

    public String getUrl() {
        checkWebView();
        return webView.getAgentUrl();
    }

    public void copyUrl() {
        ClipboardManager mClipboardManager = (ClipboardManager) context.get().getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, getUrl()));
    }

    public void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(context.get(), targetUrl + "无效的链接无法使用浏览器打开", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        context.get().startActivity(intent);
    }

    public void clearWebViewCache() {
        webView.clearWeb();
    }

    public void setJsUploadChooserCallback(FileValueCallbackMiddleActivity.JsUploadChooserCallback jsUploadChooserCallback) {
        FileValueCallbackMiddleActivity.setJsUploadChooserCallback(jsUploadChooserCallback);
    }

    public static void removeJsUploadChooserCallback() {
        FileValueCallbackMiddleActivity.removeJsUploadChooserCallback();
    }

    /**
     * 用第三方库选择文件,回调逻辑需自己处理
     *
     * @param thriedChooserListener ThriedChooserListener
     */
    public void setThriedChooserListener(FileValueCallbackMiddleActivity.ThriedChooserListener thriedChooserListener) {
        FileValueCallbackMiddleActivity.setThriedChooserListener(thriedChooserListener);
    }

    public static void removeThriedChooserListener() {
        FileValueCallbackMiddleActivity.removeThriedChooserListener();
    }

    /**
     * check webView not null
     */
    private void checkWebView() {
        if (null == webView) {
            throw new NullPointerException("webView most not be null,please check your code!");
        }
    }
}
