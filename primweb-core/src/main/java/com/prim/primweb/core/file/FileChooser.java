package com.prim.primweb.core.file;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;

import com.prim.primweb.core.permission.FilePermissionWrap;
import com.prim.primweb.core.permission.PermissionMiddleActivity;
import com.prim.primweb.core.permission.WebPermission;
import com.prim.primweb.core.utils.ImageHandlerUtil;

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
public class FileChooser implements PermissionMiddleActivity.PermissionListener, FileValueCallbackMiddleActivity.ChooserFileListener {
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

    public interface UploadFileListener {
        void requestPermissionFile();
    }

    public static WeakReference<UploadFileListener> mUploadListener;

    public static void setUploadListener(UploadFileListener uploadFileListener) {
        mUploadListener = new WeakReference<UploadFileListener>(uploadFileListener);
    }

    public void updateFile() {
        PermissionMiddleActivity.setPermissionListener(this);
        //检查权限的中间件
        PermissionMiddleActivity.startCheckPermission((Activity) context.get(), WebPermission.CAMERA_TYPE);
    }

    @Override
    public void requestPermissionSuccess(String permissionType) {
        if (permissionType.equals(WebPermission.CAMERA_TYPE)) {//权限请求成功获取要打开的类型
            fileValueCallback();
        }
    }

    private void fileValueCallback() {
        Log.e(TAG, "fileValueCallback: " + acceptType.toString());
        if (acceptType != null) {
            //上传文件的类型
            String type = acceptType[0];
            FileValueCallbackMiddleActivity.setChooserFileListener(this);
            FileValueCallbackMiddleActivity.getFileValueCallback((Activity) context.get(), type);
        } else {
            FileValueCallbackMiddleActivity.setChooserFileListener(this);
            FileValueCallbackMiddleActivity.getFileValueCallback((Activity) context.get(), "file");
        }
    }

    @Override
    public void requestPermissionFailed(String permissionType) {
        cancelFilePathCallback();
        //交由第三方处理
        if (mUploadListener != null && mUploadListener.get() != null) {
            mUploadListener.get().requestPermissionFile();
        }
        mUploadListener = null;
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
    public void updateFile(Intent data, int requestCode, int resultCode) {
        Uri result = null;
        if (data != null) {
            result = data.getData();
        }
        if (valueCallbacks != null) {
            onActivityResultAboveL(requestCode, resultCode, data);
        } else if (valueCallback != null) {
            result = ImageHandlerUtil.geturi(data, context.get());
            if (result == null) {
                return;
            }
            valueCallback.onReceiveValue(result);
            valueCallback = null;
        }
    }

    @Override
    public void updateCancle() {
        cancelFilePathCallback();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (valueCallbacks != null) {
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    String dataString = intent.getDataString();
                    ClipData clipData = intent.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null)
                        results = new Uri[]{Uri.parse(dataString)};
                }
            }
            valueCallbacks.onReceiveValue(results);
            valueCallbacks = null;
        }
    }
}
