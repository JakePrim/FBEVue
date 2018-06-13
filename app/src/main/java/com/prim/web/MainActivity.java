package com.prim.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webclient.WebChromeClient;
import com.prim.primweb.core.webclient.WebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    private static final String TAG = "MainActivity";

    private PrimWeb primWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.fl_web);
        primWeb = PrimWeb.with(this)
                .setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .setWebViewType(PrimWeb.WebViewType.Android)
                .setModeType(PrimWeb.ModeType.Strict)
                .addJavascriptInterface("nativeBridge", new JavaObjects())
                .buildWeb()
                .readyOk()
                .launch("http://front.52yingzheng.com/test/shiluTest/h5-standard/h5-standard.html");
    }

    public class JavaObjects {
        @JavascriptInterface
        public void geographicPosition(String data) {
            primWeb.getCallJsLoader().callJS("nativeBridgeCallback['networkRequestCallback']", "123");
        }
    }

    /** 自定义设置代理的WebViewClient 兼容android和x5 webview */
    public class AgentWebViewClient extends WebViewClient {

        public AgentWebViewClient(Context context) {
            super(context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    /** 自定义设置代理的WebChromeClient 兼容android和x5 webview */
    public class AgentWebChromeClient extends WebChromeClient {

        public AgentWebChromeClient(Context context) {
            super(context);
        }

        @Override
        public void onProgressChanged(View webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }

    public class AndroidWebViewClient extends android.webkit.WebViewClient{

    }

    public class x5WebViewClient extends com.tencent.smtt.sdk.WebViewClient{

    }

    /** 注入js脚本 */
    public class MyJavaObject {
        @JavascriptInterface
        public void jsFunctionExit() {
            Log.e(TAG, "jsFunctionExit: JS 方法存在");
        }

        @JavascriptInterface
        public void jsFunctionNo() {
            Log.e(TAG, "jsFunctionNo: JS 方法不存在 ");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!primWeb.back()) {
                finish();
            }
//            primWeb.getCallJsLoader().checkJsMethod("returnBackHandles");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        primWeb.webLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        primWeb.webLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        primWeb.webLifeCycle().onDestory();
    }
}
