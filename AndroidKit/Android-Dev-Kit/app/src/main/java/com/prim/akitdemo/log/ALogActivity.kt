package com.prim.akitdemo.log

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prim.akitdemo.R
import com.prim.alibrary.log.*
import com.prim.alibrary.log.printer.ViewPrinter

/**
 * @desc
 * @author prim
 * @time 2020/5/31 - 6:30 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 * @version 1.0.0
 */
class ALogActivity : AppCompatActivity() {
    var viewPrinter: ViewPrinter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        viewPrinter= ViewPrinter(this)

        viewPrinter!!.viewProvider.showFloatingView()
        LogManager.getInstance().addPrinter(viewPrinter)
    }

    fun print(view: View) {
        ALog.e("hello log","papa")
//        ALog.eT("TestLog", "hello log","haha")
//        ALog.log(object:LogConfig(){
//            override fun isThreadInfo(): Boolean {
//                return true
//            }
//
//            override fun stackTraceDepth(): Int {
//                return 0
//            }
//        },LogType.E,"--------","5566","9988")
    }
}