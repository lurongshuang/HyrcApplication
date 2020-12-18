package com.hyrc.lrs.hyrcbase.utils.thread

import android.os.Handler

object ThreadUtils {
    /**
     * 延时执行
     *
     * @param senCond
     * @param onNext
     */
    fun timeLapseHandler(senCond: Int, onNext: OnNext?) {
        Handler().postDelayed({ onNext?.accept() }, senCond * 1000.toLong())
    }

    /**
     * 延时执行
     *
     * @param senCond
     * @param onNext
     */
    fun timeLapseHandlerMill(senCond: Int, onNext: OnNext?) {
        Handler().postDelayed({ onNext?.accept() }, senCond.toLong())
    }

    interface OnNext {
        fun accept()
    }
}