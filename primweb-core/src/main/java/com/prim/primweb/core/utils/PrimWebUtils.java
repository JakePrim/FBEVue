package com.prim.primweb.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.prim.primweb.core.PrimWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：5/14 0014
 * 描    述：工具类
 * 修订历史：
 * ================================================
 */
public class PrimWebUtils {

    /** 判断是否为json串 */
    public static boolean isJson(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }
        boolean tag = false;
        try {
            if (target.startsWith("[")) {
                new JSONArray(target);
            } else if (target.startsWith("{")) {
                new JSONObject(target);
            }
            tag = true;
        } catch (JSONException ignore) {
            tag = false;
        }
        return tag;
    }

    public static boolean isDebug() {
        return PrimWeb.DEBUG;
    }


    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static boolean isUIThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void runUIRunable(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static Uri getUriForFile(Context context, String name, File vFile) {
        Uri cameraUri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            cameraUri = Uri.fromFile(vFile);
        } else {
            cameraUri = FileProvider.getUriForFile(context, name, vFile);
        }
        return cameraUri;
    }
}
