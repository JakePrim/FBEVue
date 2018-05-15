package com.prim.web;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.client.WebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.fl_web);
        PrimWeb primWeb = PrimWeb.with(this)
                .setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//                .setAgentWebView(new X5AgentWebView(this))
//                .setAgentWebSetting(new X5DefaultWebSetting(this))
//                .setAgentWebSetting(new DefaultWebSetting(this))
//                .setAgentWebView(new PrimAgentWebView(this))
                .setWebViewType(PrimWeb.WebViewType.X5)
                .addJavascriptInterface("jsAgent", new MyJavaObject())
                .setModeType(PrimWeb.ModeType.Normal)
                .setAgentWebViewClient(new MyWebViewClient(this))
                .build()
                .ready()
                .launch("https://blog.csdn.net/yy1300326388/article/details/43965493");

        primWeb.callJsLoader().callJS("jsMethod");
    }

    /** 使用代理的WebViewClient */
    public class MyWebViewClient extends WebViewClient {
        MyWebViewClient(Context context) {
            super(context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /** 注入js脚本 */
    public class MyJavaObject {

        @JavascriptInterface
        public void login(String data) {

        }

        public void medth() {

        }

    }
}
