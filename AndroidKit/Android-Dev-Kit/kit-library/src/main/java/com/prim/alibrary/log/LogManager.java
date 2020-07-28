package com.prim.alibrary.log;

import com.prim.alibrary.log.printer.LogPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志的管理类
 * @time 2020/5/31 - 4:48 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class LogManager {
    private static LogManager logManager;

    private LogConfig logConfig;

    private List<LogPrinter> printerList = new ArrayList<>();

    public LogManager(LogConfig logConfig, LogPrinter[] printers) {
        this.logConfig = logConfig;
        printerList.addAll(Arrays.asList(printers));
    }

    public static LogManager getInstance() {
        return logManager;
    }

    public LogConfig getLogConfig() {
        return this.logConfig;
    }

    /**
     * log 信息初始化
     *
     * @param logConfig 日志的配置信息
     * @param printers  日志打印器配置
     */
    public static void init(LogConfig logConfig, LogPrinter... printers) {
        logManager = new LogManager(logConfig, printers);
    }


    //-------------------------------------- printers API ----------------------------//

    public List<LogPrinter> getPrinters(){
        return  printerList;
    }

    public void addPrinter(LogPrinter printer){
        printerList.add(printer);
    }

    public void removePrinter(LogPrinter printer){
        printerList.remove(printer);
    }


}
