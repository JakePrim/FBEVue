package com.prim.primweb.core.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.prim.primweb.core.permission.FilePermissionWrap;
import com.prim.primweb.core.utils.ImageHandlerUtil;
import com.prim.primweb.core.utils.PrimWebUtils;

import java.io.File;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/17 0017
 * 描    述：上传文件的回调中间件Activity
 * 修订历史：
 * ================================================
 */
public class FileValueCallbackActivity extends Activity {

    private static final String KEY_TYPE = "type";

    private static int ACTION_IMAGE_CAPTURE = 1009;

    private static int ACTION_VIDEO_CAPTURE = 1010;

    public static void getFileValueCallback(Activity activity, String type) {
        Intent intent = new Intent(activity, FileValueCallbackActivity.class);
        intent.putExtra(KEY_TYPE, type);
        activity.startActivity(intent);
    }

    public interface ChooserFileListener {
        void updateFile(Uri uri);

        void updateCancle();
    }

    private static ChooserFileListener mChooserFileListener;

    public static void setChooserFileListener(ChooserFileListener chooserFileListener) {
        mChooserFileListener = chooserFileListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra(KEY_TYPE);
        if (TextUtils.isEmpty(type)) {
            finish();
        }
        getFile(type);
    }

    private void getFile(String type) {
        if (type.equals(FileType.WEB_IMAGE)) {
            openCapture();
        } else if (type.equals(FileType.WEB_VIDEO)) {
            openCamera();
        } else if (type.equals(FileType.WEB_AUDIO)) {

        }
    }

    private Uri cameraUri;

    private String imagePaths;

    /** 打开照相机拍照 */
    public void openCapture() {
        try {
            Intent take_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imagePaths = Environment.getExternalStorageDirectory().getPath()
                    + "/人民日报/temp/" + (System.currentTimeMillis() + ".jpg");
            // 必须确保文件夹路径存在，否则拍照后无法完成回调
            File vFile = new File(imagePaths);
            if (!vFile.exists()) {
                File vDirPath = vFile.getParentFile();
                vDirPath.mkdirs();
            } else {
                if (vFile.exists()) {
                    vFile.delete();
                }
            }
            cameraUri = PrimWebUtils.getUriForFile(this, getPackageName(), vFile);
            take_intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(take_intent, ACTION_IMAGE_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 打开照相机录像 */
    public void openCamera() {
        try {
            // 激活系统的照相机进行录像
            Intent intent = new Intent();
            intent.setAction("android.media.action.VIDEO_CAPTURE");
            intent.addCategory("android.intent.category.DEFAULT");
            // 保存录像到指定的路径
            imagePaths = Environment.getExternalStorageDirectory().getPath()
                    + "/人民日报/temp/" + (System.currentTimeMillis() + ".mp4");
            // 必须确保文件夹路径存在，否则拍照后无法完成回调
            File vFile = new File(imagePaths);
            if (!vFile.exists()) {
                File vDirPath = vFile.getParentFile();
                vDirPath.mkdirs();
            } else {
                if (vFile.exists()) {
                    vFile.delete();
                }
            }
            cameraUri = PrimWebUtils.getUriForFile(this, getPackageName(), vFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(intent, ACTION_VIDEO_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            updateCapture(data);
        } else if (requestCode == ACTION_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            updateCamera(data);
        } else if (requestCode == ACTION_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            cancelFilePathCallback();
        } else if (requestCode == ACTION_VIDEO_CAPTURE && resultCode == RESULT_CANCELED) {
            cancelFilePathCallback();
        }
    }

    /** 上传拍摄的视频 */
    private void updateCamera(Intent data) {
        Uri uri = null;
        uri = cameraUri;
        if (mChooserFileListener != null) {
            mChooserFileListener.updateFile(uri);
        }
        finish();
    }

    /** 上传拍的照片 */
    private void updateCapture(Intent data) {
        Uri uri = null;
        ImageHandlerUtil.afterOpenCamera(imagePaths, this);
        uri = cameraUri;
        if (mChooserFileListener != null) {
            mChooserFileListener.updateFile(uri);
        }
        finish();
    }

    /**
     * 取消mFilePathCallback回调
     */
    private void cancelFilePathCallback() {
        if (mChooserFileListener != null) {
            mChooserFileListener.updateCancle();
        }
        finish();
    }
}
