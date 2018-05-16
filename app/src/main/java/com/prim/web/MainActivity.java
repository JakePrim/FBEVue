package com.prim.web;

import android.content.Context;
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
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.webview.PrimAgentWebView;

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
                .addJavascriptInterface("nativeBridge", new MyJavaObject())
                .buildWeb()
                .readyOk()
                .launch("http://front.52yingzheng.com/test/shiluTest/h5-standard/h5-standard.html");
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
        public void closeWebView(String data) {
            finish();
        }

        @JavascriptInterface
        public void signIn(String data) {
            primWeb.getCallJsLoader().callJS("nativeBridgeCallback['networkRequestCallback']", "'测试调用h5端的Js方法'");
            Toast.makeText(MainActivity.this, "登录", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void bindTel(String data) {
            Toast.makeText(MainActivity.this, "绑定手机号", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void camera(String data) {
            Toast.makeText(MainActivity.this, "调取摄像头", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void photos(String data) {
            Toast.makeText(MainActivity.this, "调取相册", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void geographicPosition(String data) {
            Toast.makeText(MainActivity.this, "获取地理位置", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void copyText(String data) {
            Toast.makeText(MainActivity.this, "复制文本", Toast.LENGTH_LONG).show();
        }

    }
}
