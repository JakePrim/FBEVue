package com.prim.web.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.web.R;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019/1/9 - 3:01 PM
 */
public class BrowserActivity extends AppCompatActivity {
    private FrameLayout webParent;
    private static final String mHomeUrl = "http://app.html5.qq.com/navi/index";
    private final int disable = 120;
    private final int enable = 255;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webParent = findViewById(R.id.webView1);
        PrimWeb.with(this)
                .setWebParent(webParent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultUI()
                .useDefaultTopIndicator(true)
                .setWebViewType(PrimWeb.WebViewType.X5)
                .buildWeb()
                .launch(mHomeUrl);
    }
}
