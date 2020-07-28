package com.prim.akitdemo

import android.app.Application
import com.prim.alibrary.log.ALogConfig
import com.prim.alibrary.log.ALogManager

/**
 * @desc
 * @author prim
 * @time 2020/5/31 - 6:22 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 * @version 1.0.0
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ALogManager.init(object : ALogConfig() {
            override fun getGlobalTag(): String {
                return "ALogDemo"
            }

            override fun enable(): Boolean {
                return true
            }
        })
    }
}