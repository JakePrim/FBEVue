package com.prim.akitdemo.log

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prim.akitdemo.R
import com.prim.alibrary.log.ALog

/**
 * @desc
 * @author prim
 * @time 2020/5/31 - 6:30 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 * @version 1.0.0
 */
class ALogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
    }

    fun print(view: View) {
        ALog.e("hello log","papa")
        ALog.eT("TestLog", "hello log","haha")
    }
}