package com.prim.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.client.DefaultAgentWebViewClient;
import com.prim.primweb.core.client.WebViewClient;
import com.prim.primweb.core.setting.X5DefaultWebSetting;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.X5AgentWebView;

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
                .setAgentWebView(new X5AgentWebView(this))
                .setAgentWebSetting(new X5DefaultWebSetting(this))
//                .setAgentWebSetting(new DefaultWebSetting(this))
//                .setAgentWebView(new PrimAgentWebView(this))
                .addJavascriptInterface("jsAgent", new MyJavaObject())
                .setModeType(PrimWeb.ModeType.Normal)
                .setWebViewClient(new MyWebViewClient(this))
                .build()
                .ready()
                .launch("https://blog.csdn.net/yy1300326388/article/details/43965493");

        primWeb.callJsLoader().callJS("jsMethod");
    }

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

    public class MyJavaObject {

        @JavascriptInterface
        public void login(String data) {

        }

        public void medth() {

        }

    }
}
