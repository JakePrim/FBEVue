package com.prim.web;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.client.MyX5WebChromeClient;
import com.prim.primweb.core.client.WebViewClient;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.PrimAgentWebView;

import org.json.JSONObject;

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
                .setWebViewType(PrimWeb.WebViewType.X5)
                .setModeType(PrimWeb.ModeType.Normal)
                .addJavascriptInterface("android", new MyJavaObject())
                .buildWeb()
                .readyOk()
                .launch("http://www.jd.com/");
    }

    /** 使用代理的WebViewClient */
    public class MyWebViewClient extends WebViewClient {
        MyWebViewClient(Context context) {
            super(context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(IAgentWebView view, String url) {
            super.onPageFinished(view, url);
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
