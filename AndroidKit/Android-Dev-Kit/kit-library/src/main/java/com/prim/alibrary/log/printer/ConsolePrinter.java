package com.prim.alibrary.log.printer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.prim.alibrary.log.LogConfig;

/**
 * @author prim
 * @version 1.0.0
 * @desc 控制台打印器
 * @time 2020/7/28 - 2:24 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ConsolePrinter implements LogPrinter {
    @Override
    public void print(@NonNull LogConfig config, int level, String tag, String content) {
        //获取打印文字的长度
        int len = content.length();
        //根据设置最大的打印文字的长度，设置获取行数
        int lineOfSub = len / LogConfig.MAX_LEN;
        if (lineOfSub > 0){
            int index = 0;
            for (int i = 0; i < lineOfSub; i++) {
                Log.println(level,tag,content.substring(index,index+LogConfig.MAX_LEN));
                index += LogConfig.MAX_LEN;
            }
            //如果循环中没有把全部的日志打印 可能是行数得到的是小数而被舍弃了原因 直接从index到len全部打印出来
            if (index != len){
                Log.println(level,tag,content.substring(index,len));
            }
        }else{
            //如果行数没有一行全部打印
            Log.println(level,tag,content);
        }
    }
}
