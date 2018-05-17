package com.prim.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webclient.WebChromeClient;
import com.prim.primweb.core.webclient.WebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;

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
                .setAgentWebViewClient(new AgentWebViewClient(this))
                .setAgentWebChromeClient(new AgentWebChromeClient(this))
                .addJavascriptInterface("android", new MyJavaObject())
                .buildWeb()
                .readyOk()
                .launch("http://front.52yingzheng.com/test/shiluTest/h5-standard/h5-standard.html");
    }

    /** 使用代理的WebViewClient */
    public class AgentWebViewClient extends WebViewClient {
        AgentWebViewClient(Context context) {
            super(context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(IAgentWebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    public class AgentWebChromeClient extends WebChromeClient<com.tencent.smtt.sdk.WebChromeClient.FileChooserParams> {

        public AgentWebChromeClient(Context context) {
            super(context);
        }
    }

    /** 注入js脚本 */
    public class MyJavaObject {
        @JavascriptInterface
        public void callAndroid(final String msg) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.i("Info", "main Thread:" + Thread.currentThread());
                    Toast.makeText(MainActivity.this, "" + msg, Toast.LENGTH_LONG).show();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }
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
