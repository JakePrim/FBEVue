package com.prim.kit_library.log;

import androidx.annotation.NonNull;

import com.prim.kit_library.log.formatter.StackTraceUtil;
import com.prim.kit_library.log.printer.LogPrinter;

import java.util.Arrays;
import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc Log 的调用类
 * @time 2020/5/31 - 4:43 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ALog {
    //TODO 动态配置堆栈忽略包名
    private static final String A_Log_PACKAGE;

    static {
        String className = ALog.class.getName();
        A_Log_PACKAGE = className.substring(0, className.lastIndexOf(".") + 1);
    }

    public static void v(Object... contents) {
        log(LogType.V, contents);
    }

    public static void vT(String tag, Object... contents) {
        log(LogType.V, tag, contents);
    }

    public static void w(Object... contents) {
        log(LogType.W, contents);
    }

    public static void wT(String tag, Object... contents) {
        log(LogType.W, tag, contents);
    }

    public static void d(Object... contents) {
        log(LogType.D, contents);
    }

    public static void dT(String tag, Object... contents) {
        log(LogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(LogType.I, contents);
    }

    public static void iT(String tag, Object... contents) {
        log(LogType.I, tag, contents);
    }

    public static void e(Object... contents) {
        log(LogType.E, contents);
    }

    public static void eT(String tag, Object... contents) {
        log(LogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(LogType.A, contents);
    }

    public static void aT(String tag, Object... contents) {
        log(LogType.A, tag, contents);
    }

    public static void log(@LogType.Type int type, Object... contents) {
        log(type, LogManager.getInstance().getLogConfig().getGlobalTag(), contents);
    }

    public static void log(@LogType.Type int type, String tag, Object... contents) {
        log(LogManager.getInstance().getLogConfig(), type, tag, contents);
    }

    public static void log(@NonNull LogConfig config, @LogType.Type int type, @NonNull String tag, @NonNull Object... contents) {
        //日志是否需要打印要使用全局的配置
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        //判断是否包含线程信息
        if (config.isThreadInfo()) {
            String threadInfo = LogConfig.THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }

        //判断是否包含堆栈信息
        if (config.stackTraceDepth() > 0) {
            String stackInfo = LogConfig.STACK_TRACE_FORMATTER.format(
                    StackTraceUtil.getCroppedStackTrace(new Throwable().getStackTrace(), A_Log_PACKAGE,
                            config.stackTraceDepth()));
            sb.append(stackInfo).append("\n");
        }
        String body = parseBody(contents, config);
        sb.append(body);
        //使用日志打印器进行打印
        List<LogPrinter> logPrinters = config.printer() != null ? Arrays.asList(config.printer())
                : LogManager.getInstance().getPrinters();
        if (logPrinters == null || logPrinters.size() == 0) {
            return;
        }
        for (LogPrinter printer : logPrinters) {
            printer.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(Object[] contents, LogConfig config) {
        if (config.jsonParser() != null) {
            //json格式化
            return config.jsonParser().toJson(contents);
        }

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
