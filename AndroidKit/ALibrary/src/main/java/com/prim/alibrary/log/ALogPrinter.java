package com.prim.alibrary.log;

import androidx.annotation.NonNull;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志打印接口
 * @time 2020/6/6 - 11:42 AM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public interface ALogPrinter {
    void print(@NonNull ALogConfig config, @ALogType.Type int level, String tag, String content);
}
