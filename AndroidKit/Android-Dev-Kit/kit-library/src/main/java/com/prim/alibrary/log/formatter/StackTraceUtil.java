package com.prim.alibrary.log.formatter;

/**
 * @author prim
 * @version 1.0.0
 * @desc 堆栈的工具类
 * @time 2020/7/28 - 3:06 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class StackTraceUtil {

    public static StackTraceElement[] getCroppedStackTrace(StackTraceElement[] stackTrace,String ignorePackage,int maxDepth){
        return cropStackTrace(getRealStackTrack(stackTrace,ignorePackage),maxDepth);
    }

    /**
     * 只打印项目中的堆栈信息 不打印ALog库的堆栈信息 减少无用的堆栈信息
     * @param stackTrace 堆栈信息
     * @param ignorePackage 忽略包
     * @return
     */
    private static StackTraceElement[] getRealStackTrack(StackTraceElement[] stackTrace,String ignorePackage){
        int ignoreDepth = 0;
        int allDepth = stackTrace.length;
        String className;
        for (int i = allDepth-1; i >=0 ; i--) {
            className = stackTrace[i].getClassName();
            if (ignorePackage!=null && className.startsWith(ignorePackage)){
                ignoreDepth=i+1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(stackTrace,ignoreDepth,realStack,0,realDepth);
        return realStack;
    }

    /**
     * 对堆栈信息进行裁剪
     * @param callStack
     * @param maxDepth
     * @return
     */
    private static  StackTraceElement[] cropStackTrace(StackTraceElement[] callStack,int maxDepth){
        int realDepth = callStack.length;
        if (maxDepth > 0){
            realDepth = Math.min(realDepth,maxDepth);
        }
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(callStack,0,realStack,0,realDepth);
        return realStack;
    }
}
