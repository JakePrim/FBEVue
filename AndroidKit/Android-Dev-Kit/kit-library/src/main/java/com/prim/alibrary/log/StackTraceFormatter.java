package com.prim.alibrary.log;

/**
 * @author prim
 * @version 1.0.0
 * @desc 堆栈信息进行格式化
 * @time 2020/6/1 - 8:14 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class StackTraceFormatter implements LogFormatter<StackTraceElement[]> {
    @Override
    public String format(StackTraceElement[] stackTraceElements) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTraceElements == null || stackTraceElements.length == 0) {
            return null;
        } else if (stackTraceElements.length == 1) {
            return "\t- " + stackTraceElements[0].toString();
        } else {
            for (int i = 0, len = stackTraceElements.length; i < len; i++) {
                if (i == 0) {
                    sb.append("StackTrace: \n");
                }
                if (i != len - 1) {
                    sb.append("\t|- ");
                    sb.append(stackTraceElements[i].toString());//堆栈信息
                    sb.append("\n");
                } else {
                    sb.append("\t「");
                    sb.append(stackTraceElements[i].toString());
                }
            }
            return sb.toString();
        }
    }
}
