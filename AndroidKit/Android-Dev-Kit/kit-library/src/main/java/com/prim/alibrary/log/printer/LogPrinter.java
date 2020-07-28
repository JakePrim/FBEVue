package com.prim.alibrary.log.printer;

import androidx.annotation.NonNull;

import com.prim.alibrary.log.LogConfig;
import com.prim.alibrary.log.LogType;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志打印接口
 * @time 2020/6/6 - 11:42 AM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public interface LogPrinter {
    /**
     * 日志打印实现的方法
     * @param config
     * @param level
     * @param tag
     * @param content
     */
    void print(@NonNull LogConfig config, @LogType.Type int level, String tag, String content);
}
