package com.prim.alibrary.log;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2020/5/31 - 4:55 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class LogType {
    public static final int V = Log.VERBOSE;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int A = Log.ASSERT;
    public static final int D = Log.DEBUG;

    @IntDef({V, I, W, E, A, D})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface Type {

    }
}
