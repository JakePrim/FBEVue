package com.prim.primweb.core.config.builder;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.jsloader.CommonJSListener;
import com.prim.primweb.core.jsloader.ICallJsLoader;
import com.prim.primweb.core.uicontroller.AbsWebUIController;
import com.prim.primweb.core.uicontroller.BaseIndicatorView;
import com.prim.primweb.core.urlloader.IUrlLoader;
import com.prim.primweb.core.webclient.webchromeclient.AgentChromeClient;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.websetting.IAgentWebSetting;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.webpool.IJavascriptInterface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author prim
 * @version 1.0.0
 * @desc PrimWeb的总配置
 * @time 2019-08-26 - 18:16
 */
public class PrimBuilder {
    public IAgentWebView webView;
    public View mView;
    public WeakReference<Activity> context;
    public ViewGroup mViewGroup;
    public ViewGroup.LayoutParams mLayoutParams;
    public int mIndex;
    public IAgentWebSetting setting;
    public Map<String, String> headers;
    public IUrlLoader urlLoader;
    public ICallJsLoader callJsLoader;
    public PrimWeb.ModeType modeType = PrimWeb.ModeType.Normal;
    public HashMap<String, Object> mJavaObject;
    public AgentWebViewClient agentWebViewClient;
    public PrimWeb.WebViewType webViewType = PrimWeb.WebViewType.Android;
    public WebViewClient webViewClient;
    public WebChromeClient webChromeClient;
    public com.tencent.smtt.sdk.WebViewClient x5WebViewClient;
    public com.tencent.smtt.sdk.WebChromeClient x5WebChromeClient;
    public AgentChromeClient agentWebChromeClient;
    public CommonJSListener commonJSListener;
    public boolean alwaysOpenOtherPage;
    public AbsWebUIController absWebUIController;
    public boolean isGeolocation = true;
    public boolean allowUploadFile = true;
    public boolean invokingThird = false;
    //UI Controller
    public boolean needTopIndicator;
    public boolean customTopIndicator;
    public View errorView;
    public View loadView;
    public BaseIndicatorView mIndicatorView;
    public @ColorInt
    int mColor = -1;
    public String colorPaser;
    public int height = 0;
    public @LayoutRes
    int errorLayout = 0;
    public @IdRes
    int errorClickId = 0;
    public @LayoutRes
    int loadLayout = 0;

    public IJavascriptInterface javaBridge;

    public Handler handler = new Handler(Looper.getMainLooper());

    public PrimBuilder(Activity context) {
        this.context = new WeakReference<>(context);
    }

    //------------------------------ 建议使用一下Api ------------------------------------//

    /**
     * 1.1.3 版本新添加,可以不用按照配置步骤自行选择配置项
     * 配置UI
     *
     * @return 配置 UIControllerBuilder
     */
    public UIControllerBuilder addUIBudiler() {
        return new UIControllerBuilder(this);
    }

    /**
     * 1.1.3 版本新添加,可以不用按照配置步骤可自行选择配置项
     * 配置指示器
     *
     * @return 配置 addIndicatorBuilder
     */
    public IndicatorBuilder addIndicatorBuilder() {
        return new IndicatorBuilder(this);
    }

    /**
     * 1.1.3 版本新添加,可以不用按照配置步骤自行选择配置项
     * 配置基本的WebView配置
     *
     * @return 配置 addWebBuilder
     */
    public WebBuilder addWebBuilder() {
        return new WebBuilder(this);
    }

    /**
     * 所有设置完成
     */
    public PerBuilder build() {
        if (mViewGroup == null) {
            throw new NullPointerException("ViewGroup not null,please check your code!");
        }
        return new PerBuilder(new PrimWeb(this));
    }

    //--------------------------- 下面API为1.1.3之前版本不建议使用，没有删除是为了兼容之前的版本 ----------------------//

    /**
     * 设置webview的父类
     */
    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp) {
        this.mViewGroup = v;
        this.mLayoutParams = lp;
        return new UIControllerBuilder(this);
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v) {
        this.mViewGroup = v;
        this.mLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT);
        return new UIControllerBuilder(this);
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp, int index) {
        this.mViewGroup = v;
        this.mLayoutParams = lp;
        this.mIndex = index;
        return new UIControllerBuilder(this);
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull int index) {
        this.mViewGroup = v;
        this.mLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mIndex = index;
        return new UIControllerBuilder(this);
    }


}
