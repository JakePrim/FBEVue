package com.prim.alibrary.log;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author prim
 * @version 1.0.0
 * @desc Log 的调用类
 * @time 2020/5/31 - 4:43 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ALog {
    public static void v(Object... contents) {
        log(ALogType.V, contents);
    }

    public static void vT(String tag, Object... contents) {
        log(ALogType.V, tag, contents);
    }

    public static void w(Object... contents) {
        log(ALogType.W, contents);
    }

    public static void wT(String tag, Object... contents) {
        log(ALogType.W, tag, contents);
    }

    public static void d(Object... contents) {
        log(ALogType.D, contents);
    }

    public static void dT(String tag, Object... contents) {
        log(ALogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(ALogType.I, contents);
    }

    public static void iT(String tag, Object... contents) {
        log(ALogType.I, tag, contents);
    }

    public static void e(Object... contents) {
        log(ALogType.E, contents);
    }

    public static void eT(String tag, Object... contents) {
        log(ALogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(ALogType.A, contents);
    }

    public static void aT(String tag, Object... contents) {
        log(ALogType.A, tag, contents);
    }

    private static void log(@ALogType.Type int type, Object... contents) {
        log(type, ALogManager.getInstance().getLogConfig().getGlobalTag(), contents);
    }

    private static void log(@ALogType.Type int type, String tag, Object... contents) {
        log(ALogManager.getInstance().getLogConfig(), type, tag, contents);
    }

    private static void log(@NonNull ALogConfig config, @ALogType.Type int type, @NonNull String tag, @NonNull Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String body = parseBody(contents);
        stringBuilder.append(body);
        Log.println(type, tag, stringBuilder.toString());
    }

    private static String parseBody(Object[] contents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object content : contents) {
            stringBuilder.append(content.toString()).append(";");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
