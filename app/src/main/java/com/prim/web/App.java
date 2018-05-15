package com.prim.web;

import android.app.Application;

import com.prim.primweb.core.PrimWeb;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrimWeb.init(this);
    }
}
