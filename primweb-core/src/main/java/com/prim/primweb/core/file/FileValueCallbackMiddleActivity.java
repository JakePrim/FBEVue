package com.prim.primweb.core.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.prim.primweb.core.R;
import com.prim.primweb.core.utils.ImageHandlerUtil;
import com.prim.primweb.core.utils.PrimWebUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/17 0017
 * 描    述：上传文件的回调中间件Activity
 * 修订历史：
 * ================================================
 */
public class FileValueCallbackMiddleActivity extends Activity implements View.OnClickListener {

    private static final String KEY_TYPE = "type";

    private static int ACTION_IMAGE_CAPTURE = 1009;

    private static int ACTION_VIDEO_CAPTURE = 1010;

    private static int FILE_CHOOSER_RESULT_CODE = 1011;

    private CommonDialog commentDialog;

    private boolean PHOTO_VIDEO_FLAG;

    public static void getFileValueCallback(Activity activity, String type) {
        Intent intent = new Intent(activity, FileValueCallbackMiddleActivity.class);
        intent.putExtra(KEY_TYPE, type);
        activity.startActivity(intent);
    }

    public interface ChooserFileListener {
        void updateFile(Uri uri);

        void updateFile(Intent data, int requestCode, int resultCode);

        void updateCancle();
    }

    public interface ThriedChooserListener {
        void jsOpenVideos();

        void jsOpenPick();
    }

    private static WeakReference<ChooserFileListener> mChooserFileListener;

    public static void setChooserFileListener(ChooserFileListener chooserFileListener) {
        mChooserFileListener = new WeakReference<>(chooserFileListener);
    }

    private static WeakReference<ThriedChooserListener> mThriedChooserListener;

    public static void setThriedChooserListener(ThriedChooserListener thriedChooserListener) {
        mThriedChooserListener = new WeakReference<>(thriedChooserListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra(KEY_TYPE);
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, "请设定要上传的文件类型", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            getFile(type);
        }
    }

    private void clear() {
        mThriedChooserListener = null;
        mChooserFileListener = null;
    }

    private void getFile(String type) {
        if (type.equals(FileType.WEB_IMAGE)) {
            PHOTO_VIDEO_FLAG = true;
            showDialog(type);
        } else if (type.equals(FileType.WEB_VIDEO)) {
            PHOTO_VIDEO_FLAG = false;
            showDialog(type);
        } else {
            openFile();
        }
    }

    private void showDialog(String web) {
        CommonBuilder builder = new CommonBuilder(this);
        builder.style(R.style.shareStyles)
                .view(R.layout.dialog_web_layout)
                .gravity(Gravity.BOTTOM)
                .setPicture(R.id.btn_take_a_picture, web)
                .setSencoed(R.id.btn_select_photo, web)
                .addViewOnclick(R.id.btn_take_a_picture, this)
                .addViewOnclick(R.id.btn_dialog_cancel, this)
                .addViewOnclick(R.id.btn_select_photo, this);
        commentDialog = builder.build();
        commentDialog.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_select_photo) {
            if (PHOTO_VIDEO_FLAG) {
                jsOpenPick();
            } else {
                jsOpenVideos();
            }

        } else if (i == R.id.btn_take_a_picture) {
            if (PHOTO_VIDEO_FLAG) {
                openCapture();
            } else {
                openCamera();
            }
        } else if (i == R.id.btn_dialog_cancel) {
            cancelFilePathCallback();
            if (commentDialog != null) {
                commentDialog.dismiss();
            }
            finish();
        }
    }

    private void jsOpenVideos() {
        if (mThriedChooserListener != null && mThriedChooserListener.get() != null) {
            mThriedChooserListener.get().jsOpenVideos();
        }
        commentDialog.dismiss();
        finish();
    }

    private void jsOpenPick() {
        if (mThriedChooserListener != null && mThriedChooserListener.get() != null) {
            mThriedChooserListener.get().jsOpenPick();
        }
        commentDialog.dismiss();
        finish();
    }


    /**
     * 打开文件
     */
    public void openFile() {
        try {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri cameraUri;

    private String imagePaths;

    /** 打开照相机拍照 */
    public void openCapture() {
        commentDialog.dismiss();
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
        commentDialog.dismiss();
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
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE && resultCode == RESULT_OK) {
            requestImgVideo(data, requestCode, resultCode);
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE && resultCode == RESULT_CANCELED) {
            cancelFilePathCallback();
        }
    }

    /** 上传视频或者图片 */
    private void requestImgVideo(Intent data, int requestCode, int resultCode) {
        if (mChooserFileListener != null && mChooserFileListener.get() != null) {
            mChooserFileListener.get().updateFile(data, requestCode, resultCode);
        }
        mChooserFileListener = null;
        finish();
    }

    /** 上传拍摄的视频 */
    private void updateCamera(Intent data) {
        Uri uri = null;
        uri = cameraUri;
        if (mChooserFileListener != null && mChooserFileListener.get() != null) {
            mChooserFileListener.get().updateFile(uri);
        }
        mChooserFileListener = null;
        finish();
    }

    /** 上传拍的照片 */
    private void updateCapture(Intent data) {
        Uri uri = null;
        ImageHandlerUtil.afterOpenCamera(imagePaths, this);
        uri = cameraUri;
        if (mChooserFileListener != null && mChooserFileListener.get() != null) {
            mChooserFileListener.get().updateFile(uri);
        }
        mChooserFileListener = null;
        finish();
    }

    /**
     * 取消mFilePathCallback回调
     */
    private void cancelFilePathCallback() {
        if (mChooserFileListener != null && mChooserFileListener.get() != null) {
            mChooserFileListener.get().updateCancle();
        }
        mChooserFileListener = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        if (commentDialog != null) {
            commentDialog.dismiss();
            commentDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        clear();
        super.finish();
    }
}
