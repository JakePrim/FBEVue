package com.prim.primweb.core.file;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;

import com.prim.primweb.core.permission.FilePermissionWrap;
import com.prim.primweb.core.permission.PermissionMiddleActivity;
import com.prim.primweb.core.permission.WebPermission;

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
public class FileChooser implements PermissionMiddleActivity.PermissionListener, FileValueCallbackActivity.ChooserFileListener {
    private FilePermissionWrap filePermissionWrap;

    private WeakReference<Context> context;

    public String[] acceptType;

    public ValueCallback<Uri> valueCallback;

    public ValueCallback<Uri[]> valueCallbacks;

    private static final String TAG = "FileChooser";

    public FileChooser(FilePermissionWrap filePermissionWrap, Context context) {
        this.filePermissionWrap = filePermissionWrap;
        this.context = new WeakReference<>(context);
        if (filePermissionWrap != null) {
            if (null != filePermissionWrap.valueCallback) {
                this.valueCallback = filePermissionWrap.valueCallback;
            }

            if (null != filePermissionWrap.valueCallbacks) {
                this.valueCallbacks = filePermissionWrap.valueCallbacks;
            }

            if (null != filePermissionWrap.acceptType) {
                this.acceptType = filePermissionWrap.acceptType;
            }
        }
    }

    public void updateFile() {
        PermissionMiddleActivity.setPermissionListener(this);
        //检查权限的中间件
        PermissionMiddleActivity.startCheckPermission((Activity) context.get(), WebPermission.CAMERA_TYPE);
    }

    @Override
    public void requestPermissionSuccess(String permissionType) {
        Log.e(TAG, "requestPermissionSuccess: 权限类型 --> " + permissionType);
        if (permissionType.equals(WebPermission.CAMERA_TYPE)) {//权限请求成功获取要打开的类型
            fileValueCallback();
        }
    }

    private void fileValueCallback() {
        if (acceptType != null) {
            //上传文件的类型
            String type = acceptType[0];
            Log.e(TAG, "fileValueCallback: type --> " + type);
            FileValueCallbackActivity.setChooserFileListener(this);
            FileValueCallbackActivity.getFileValueCallback((Activity) context.get(), type);
        }
    }

    @Override
    public void requestPermissionFailed(String permissionType) {
        Log.e(TAG, "requestPermissionFailed: 权限类型 --> " + permissionType);
        cancelFilePathCallback();
    }

    /**
     * 取消mFilePathCallback回调
     */
    private void cancelFilePathCallback() {
        if (valueCallback != null) {
            valueCallback.onReceiveValue(null);
            valueCallback = null;
        } else if (valueCallbacks != null) {
            valueCallbacks.onReceiveValue(null);
            valueCallbacks = null;
        }
    }

    @Override
    public void updateFile(Uri uri) {
        if (valueCallbacks != null) {
            Uri[] uris = new Uri[1];
            uris[0] = uri;
            valueCallbacks.onReceiveValue(uris);
        } else if (valueCallback != null) {
            valueCallback.onReceiveValue(uri);
            valueCallback = null;
        }
    }

    @Override
    public void updateCancle() {
        if (valueCallback != null) {
            valueCallback.onReceiveValue(null);
            valueCallback = null;
        } else if (valueCallbacks != null) {
            valueCallbacks.onReceiveValue(null);
            valueCallbacks = null;
        }
    }
}
