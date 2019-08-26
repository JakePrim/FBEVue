package com.prim.primweb.core.config;

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
import com.prim.primweb.core.webview.AndroidAgentWebView;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.X5AgentWebView;
import com.prim.primweb.core.webview.webpool.IJavascriptInterface;
import com.prim.primweb.core.webview.webpool.WebViewPool;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static com.prim.primweb.core.PrimWeb.getConfiguration;

/**
 * @author prim
 * @version 1.0.0
 * @desc
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
    public  @ColorInt
    int mColor = -1;
    public String colorPaser;
    public int height = 0;
    public  @LayoutRes
    int errorLayout = 0;
    public  @IdRes
    int errorClickId = 0;
    public  @LayoutRes
    int loadLayout = 0;

    public IJavascriptInterface javaBridge;

    public Handler handler = new Handler(Looper.getMainLooper());

    public PrimBuilder(Activity context) {
        this.context = new WeakReference<>(context);
    }

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

    /**
     * 所有设置完成
     */
    public PerBuilder build() {
        if (mViewGroup == null) {
            throw new NullPointerException("ViewGroup not null,please check your code!");
        }
        return new PerBuilder(new PrimWeb(this));
    }

    public void addJavaObject(String key, Object o) {
        if (mJavaObject == null) {
            mJavaObject = new HashMap<>(30);//初始化个数 30 * 0.75 多个左右,考虑到哈希表 默认大小只有 4 * 0.75 个
            // 而哈希表的缺点是:扩容性能会下降 初始化时提前计算好上限.
        }
        mJavaObject.put(key, o);
    }

    /**
     * 设置webview的类型
     *
     * @param webViewType 目前支持两种类型 X5 Android
     *                    TODO WebView初始化会消耗内存 加载速度慢 此处待优化
     */
    public void setWebViewType(final PrimWeb.WebViewType webViewType) {
        this.webViewType = webViewType;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.handler.post(new Runnable() {
                @Override
                public void run() {
                    initWeb(webViewType);
                }
            });
        } else {
            initWeb(webViewType);
        }
    }

    private void initWeb(PrimWeb.WebViewType webViewType) {
        if (null == this.webView) {
            try {
                boolean webPool = getConfiguration(ConfigKey.WEB_POOL);
                if (webPool) {
                    this.webView = WebViewPool.getInstance().get(webViewType);
                    this.mView = this.webView.getAgentWebView();
                } else {
                    if (webViewType == PrimWeb.WebViewType.X5) {
                        this.webView = new X5AgentWebView(context.get());
                        this.mView = this.webView.getAgentWebView();
                    } else {
                        this.webView = new AndroidAgentWebView(context.get());
                        this.mView = this.webView.getAgentWebView();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.webView = new X5AgentWebView(context.get());
                this.mView = this.webView.getAgentWebView();
            }
        }
    }
}
