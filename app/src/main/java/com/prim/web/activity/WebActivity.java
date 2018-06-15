package com.prim.web.activity;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webclient.WebChromeClient;
import com.prim.primweb.core.webclient.WebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.web.R;

public class WebActivity extends AppCompatActivity {

    private RelativeLayout rl_web;

    private PrimWeb primWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        // 设置窗口风格为顶部显示Actionbar
//        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rl_web = (RelativeLayout) findViewById(R.id.rl_web);
        primWeb = PrimWeb.with(this)
                .setWebParent(rl_web, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .setAgentWebViewClient(webViewClient)
                .setAgentWebChromeClient(webChromeClient)
                .setWebViewType(PrimWeb.WebViewType.Android)
                .buildWeb()
                .lastGo()
                .launch("https://m.jd.com/");
    }

    WebViewClient webViewClient = new WebViewClient(this) {
        @Override
        public void onPageStarted(IAgentWebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient(this) {
        @Override
        public void onReceivedTitle(View webView, String s) {
            super.onReceivedTitle(webView, s);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(s);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!primWeb.handlerBack()) {
                    this.finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (primWeb.handlerKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        primWeb.webLifeCycle().onDestory();
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
}
