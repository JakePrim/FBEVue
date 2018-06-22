package com.prim.web.setting;

import android.os.Build;

import com.prim.primweb.core.websetting.BaseAgentWebSetting;
import com.tencent.smtt.sdk.WebSettings;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：6/22 0022
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CustomX5Setting extends BaseAgentWebSetting<WebSettings> {
    @Override
    protected void toSetting(WebSettings webSetting) {
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            webSetting.setAllowFileAccessFromFileURLs(false);
            // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            webSetting.setAllowUniversalAccessFromFileURLs(false);
        }
        // 允许加载本地文件html  file协议
        webSetting.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }
}
