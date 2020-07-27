package com.prim.alibrary.log;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志信息配置类
 * @time 2020/5/31 - 4:47 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public abstract class ALogConfig {
    /**
     * 配置打印最大的行数
     */
    static int MAX_LEN = 512;

    static AThreadFormatter THREAD_FORMATTER = new AThreadFormatter();

    static AStackTraceFormatter STACK_TRACE_FORMATTER = new AStackTraceFormatter();

    /**
     * 配置全局的TAG
     *
     * @return
     */
    public String getGlobalTag() {
        return "ALog";
    }

    /**
     * 配置是否开启日志
     *
     * @return
     */
    public boolean enable() {
        return true;
    }

    /**
     * 配置json 解析器 默认为null
     *
     * @return
     */
    public JsonParser jsonParser() {
        return null;
    }

    /**
     * 配置日志是否包含线程相关的信息
     * @return
     */
    public boolean isThreadInfo(){
        return false;
    }

    /**
     * 配置堆栈信息的深度 如果堆栈太深没有必要看了
     * @return
     */
    public int stackTraceDepth(){
        return 5;
    }

    /**
     * 定义json 解析器接口 json 的解析交给调用者实现
     */
    public interface JsonParser {
        String toJson(Object obj);
    }
}
