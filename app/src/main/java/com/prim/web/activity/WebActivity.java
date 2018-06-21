package com.prim.web.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.widget.RelativeLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webclient.webviewclient.AgentWebViewClient;
import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.web.R;

public class WebActivity extends AppCompatActivity {

    private RelativeLayout rl_web;

    private PrimWeb primWeb;

    private static final String TAG = "WebActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        rl_web = (RelativeLayout) findViewById(R.id.rl_web);
        primWeb = PrimWeb.with(this)
                .setWebParent(rl_web, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultUI()
                .useDefaultTopIndicator(getResources().getColor(R.color.colorAccent))
                .setWebViewType(PrimWeb.WebViewType.Android)
                .buildWeb()
                .lastGo()
                .launch("https://m.jd.com/");
    }

    private class WebViewClient extends AgentWebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            //如果想自己定义拦截Url 逻辑 将 shouldOverrideUrlLoading 去掉
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!primWeb.handlerBack()) {
                    this.finish();
                }
                return true;
            case R.id.more:
                showPoPup(findViewById(R.id.more));
                return true;
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private PopupMenu mPopupMenu;

    /**
     * 显示更多菜单
     *
     * @param view
     *         菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }


    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.refresh:
                    if (primWeb != null) {
                        primWeb.getUrlLoader().reload();
                    }
                    return true;
                case R.id.copy:
                    if (primWeb != null) {
                        primWeb.copyUrl();
                    }
                    return true;
                case R.id.default_browser:
                    if (primWeb != null) {
                        primWeb.openBrowser(primWeb.getUrl());
                    }
                    return true;
                case R.id.default_clean:
                    if (primWeb != null) {
                        primWeb.clearWebViewCache();
                    }
                    return true;
                case R.id.error_website:
                    if (primWeb != null) {
                        primWeb.getUrlLoader().loadUrl("http://www.unkownwebsiteblog.me");
                    }
                    return true;
                case R.id.enter_website:
                    if (primWeb != null) {
                        primWeb.getUrlLoader().loadUrl("https://m.jd.com/");
                    }
                    return true;
                default:
                    return false;
            }

        }
    };

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
