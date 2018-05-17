package com.prim.primweb.core.jsloader;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.prim.primweb.core.webview.IAgentWebView;
import com.prim.primweb.core.utils.PrimWebUtils;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/14 0014
 * 描    述：js方法加载器的基类
 * 修订历史：
 * ================================================
 */
public abstract class BaseCallJsLoader implements ICallJsLoader {
    private IAgentWebView webView;

    private static final String TAG = "BaseCallJsLoader";


    BaseCallJsLoader(IAgentWebView webView) {
        this.webView = webView;
    }

    protected void call(String js, AgentValueCallback<String> callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.evaluateJs(js, callback);
        } else {
            this.loadJs(js);
        }
    }

    private void loadJs(final String js) {
        webView.loadAgentJs(js);
    }

    private void evaluateJs(final String js, final AgentValueCallback<String> callback) {
        webView.loadAgentJs(js, callback);
    }

    @Override
    public void callJs(String method, AgentValueCallback<String> callback, String... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:").append(method);
        if (params == null || params.length == 0) {
            sb.append("()");
        } else {
            sb.append("(").append(splice(params)).append(")");
        }

        call(sb.toString(), callback);
    }

    @Override
    public void callJs(String method, AgentValueCallback<String> callback) {
        this.callJs(method, callback, (String[]) null);
    }

    @Override
    public void callJS(String method, String... params) {
        this.callJs(method, null, params);
    }

    @Override
    public void callJS(String method) {
        this.callJS(method, (String[]) null);
    }

    /** 拼接参数 */
    private String splice(String... params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (PrimWebUtils.isJson(param)) {
                sb.append("\"").append(param).append("\"");
            } else {
                sb.append(param);
            }
            if (i != sb.length() - 1) {
                sb.append(" , ");
            }
        }
        return sb.toString();
    }

    @Override
    public void checkJsMethod(String method) {
        StringBuilder sb = new StringBuilder();
        sb.append("function checkJsFunction(){ if(typeof ")
                .append(method)
                .append(" != \"undefined\" && typeof ")
                .append(method)
                .append(" == \"function\")")
                .append("{console.log(\"")
                .append(method)
                .append("\");")
                .append("checkJsBridge['jsFunctionExit']();")
                .append("}else{")
                .append("if(typeof checkJsBridge == \"undefined\") return false;")
                .append("checkJsBridge['jsFunctionNo']();}}");
        call("javascript:" + sb.toString() + ";checkJsFunction()", null);
    }
}
