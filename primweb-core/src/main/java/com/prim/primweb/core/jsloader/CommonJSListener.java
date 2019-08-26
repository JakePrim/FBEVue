package com.prim.primweb.core.jsloader;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/31 0031
 * 描    述：通用的js脚本监听
 * 修订历史：
 * ================================================
 */
public interface CommonJSListener {
    void jsFunExit(Object data);

    void jsFunNoExit(Object data);
}
