package com.prim.alibrary.log;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志的管理类
 * @time 2020/5/31 - 4:48 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ALogManager {
    private static ALogManager logManager;

    private ALogConfig logConfig;

    public ALogManager(ALogConfig logConfig) {
        this.logConfig = logConfig;
    }

    /**
     * log 信息初始化
     *
     * @param logConfig
     */
    public static void init(ALogConfig logConfig) {
        logManager = new ALogManager(logConfig);
    }

    public static ALogManager getInstance() {
        return logManager;
    }

    public ALogConfig getLogConfig() {
        return this.logConfig;
    }
}
