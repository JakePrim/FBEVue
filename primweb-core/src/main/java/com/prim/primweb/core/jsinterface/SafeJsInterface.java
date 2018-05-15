package com.prim.primweb.core.jsinterface;

import android.util.Log;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webview.IAgentWebView;

import java.util.Map;
import java.util.Set;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/15 0015
 * 描    述：安全的注入js脚本
 * 修订历史：
 * ================================================
 */
public class SafeJsInterface extends BaseJsInterface {

    private PrimWeb.ModeType modeType;
    private IAgentWebView webView;

    private static final String TAG = "SafeJsInterface";

    public static SafeJsInterface getInstance(IAgentWebView webView, PrimWeb.ModeType modeType) {
        return new SafeJsInterface(modeType, webView);
    }

    public SafeJsInterface(PrimWeb.ModeType modeType, IAgentWebView webView) {
        super(modeType);
        this.modeType = modeType;
        this.webView = webView;
    }

    @Override
    public IJsInterface addJavaObjects(Map<String, Object> maps) {
        Set<Map.Entry<String, Object>> entries = maps.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            boolean checkJsInterface = checkJsInterface(value);
            Log.e(TAG, "addJavaObjects: modeType --> " + modeType + "| checkJsInterface --> " + checkJsInterface);
            if (!checkJsInterface) {
                throw new RuntimeException("This object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added");
            } else {
                webView.addJavascriptInterfaceAgent(value, entry.getKey());
            }
        }
        return this;
    }

    @Override
    public IJsInterface addJavaObject(Object o, String name) {
        boolean checkJsInterface = checkJsInterface(o);
        if (!checkJsInterface) {
            throw new RuntimeException("This object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added");
        } else {
            webView.addJavascriptInterfaceAgent(o, name);
        }
        return this;
    }
}
