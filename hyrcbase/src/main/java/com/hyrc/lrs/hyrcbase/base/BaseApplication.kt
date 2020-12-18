package com.hyrc.lrs.hyrcbase.base

import android.content.Context
import androidx.multidex.MultiDexApplication

/**
 * @description 作用:
 * @date: 2020/12/14
 * @author:
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: Context? = null
            private set
    }
}