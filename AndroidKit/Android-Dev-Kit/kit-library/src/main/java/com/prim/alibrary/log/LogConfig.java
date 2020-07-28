package com.prim.alibrary.log;

/**
 * @author prim
 * @version 1.0.0
 * @desc 日志信息配置类
 * @time 2020/5/31 - 4:47 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public abstract class LogConfig {
    /**
     * 配置打印最大的长度
     */
    static int MAX_LEN = 512;

    /**
     * 初始化线程格式化器
     */
    static ThreadFormatter THREAD_FORMATTER = new ThreadFormatter();

    /**
     * 初始化堆栈格式化器
     */
    static StackTraceFormatter STACK_TRACE_FORMATTER = new StackTraceFormatter();

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
     * 允许用户注册打印器
     * @return 默认返回null
     */
    public LogPrinter[] printer(){
        return null;
    }

    /**
     * 定义json 解析器接口 json 的解析交给调用者实现 本身库不需要实现序列化的工具可以一定程度上防止冲突
     */
    public interface JsonParser {
        String toJson(Object obj);
    }
}
