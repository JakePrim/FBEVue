package com.prim.kit_library.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志的bean信息
 * @time 2020/7/28 - 3:32 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class LogBean {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
    public long timeMillis;//时间戳
    public int level;//日志级别
    public String tag;//日志tag
    public String log;//日志信息

    public LogBean(long timeMillis, int level, String tag, String log) {
        this.timeMillis = timeMillis;
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public String getLog(){
        return getFlattened() + "\n" + log;
    }

    public String getFlattened(){
        return format(timeMillis) + '|' +level+'|'+tag+'|';
    }

    private String format(long timeMillis){
        return sdf.format(timeMillis);
    }
}
