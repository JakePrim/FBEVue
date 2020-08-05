package com.prim.akitdemo

import android.app.Application
import com.google.gson.Gson
import com.prim.kit_library.log.printer.ConsolePrinter
import com.prim.kit_library.log.LogConfig
import com.prim.kit_library.log.LogManager

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
        LogManager.init(object : LogConfig() {
            override fun getGlobalTag(): String {
                return "ALogDemo"
            }

            override fun jsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }

            override fun enable(): Boolean {
                return true
            }
        }, ConsolePrinter())
    }
}