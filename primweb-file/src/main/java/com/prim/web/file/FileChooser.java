package com.prim.web.file;

import android.content.Context;
import android.net.Uri;
import android.webkit.ValueCallback;

import com.prim.primweb.core.permission.FilePermissionWrap;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/16 0016
 * 描    述：文件选择器
 * 修订历史：
 * ================================================
 */
public class FileChooser {
    private FilePermissionWrap filePermissionWrap;

    private WeakReference<Context> context;

    public FileChooser(FilePermissionWrap filePermissionWrap, Context context) {
        this.filePermissionWrap = filePermissionWrap;
        this.context = new WeakReference<>(context);
    }

    public void checkPermission(){

    }
}
