package com.hyrc.lrs.hyrcbase.utils.time

import java.text.SimpleDateFormat
import java.util.*

/**
 * @description 作用:关于时间的工具类
 * @date: 2019/11/20
 * @author: 卢融霜
 */
class TimeUtils {
    /**
     * @return //XXXX年XX月XX日 星期X
     */
    fun StringData(): String {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        val mYear: String
        val mMonth: String
        val mDay: String
        val mWay: String
        //获取当前年份
        mYear = c[Calendar.YEAR].toString()
        // 获取当前月份
        mMonth = (c[Calendar.MONTH] + 1).toString()
        // 获取当前月份的日期号码
        mDay = c[Calendar.DAY_OF_MONTH].toString()
        mWay = c[Calendar.DAY_OF_WEEK].toString()
        return mYear + "年" + mMonth + "月" + mDay + "日" + "星期" + getWay(mWay)
    }

    private fun getWay(mWay: String): String {
        val ways = arrayOf("日", "一", "二", "三", "四", "五", "六")
        return ways[mWay.toInt() - 1]
    }

    val stringNowDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val curDate = Date(System.currentTimeMillis())
            return formatter.format(curDate)
        }

    fun getStringNowDate(format: String?): String {
        val formatter = SimpleDateFormat(format)
        val curDate = Date(System.currentTimeMillis())
        return formatter.format(curDate)
    }

    /**
     * 获取年
     */
    val stringNowYear: String
        get() {
            val formatter = SimpleDateFormat("yyyy")
            val curDate = Date(System.currentTimeMillis())
            return formatter.format(curDate)
        }

    /**
     * 字符串转日期
     */
    fun parseServerTime(serverTime: String?, format: String?): Date? {
        var format = format
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        val sdf = SimpleDateFormat(format, Locale.CHINESE)
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        var date: Date? = null
        try {
            date = sdf.parse(serverTime)
        } catch (e: Exception) {
        }
        return date
    }

    /**
     * 日期转字符串
     */
    fun getDateStr(date: Date?, format: String?): String {
        var format = format
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        val formatter = SimpleDateFormat(format)
        return formatter.format(date)
    }

    /**
     * 毫秒转字符串日期
     */
    fun timeStampToDate(tsp: Long, format: String?): String {
        val sdf: SimpleDateFormat = if (format == null || format.isEmpty()) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat(format, Locale.getDefault())
        }
        sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
        return sdf.format(tsp)
    }

    /**
     * 毫秒转字符串日期
     */
    fun timeStampToDate(tsp: Long, format: String?, zone: TimeZone?): String {
        val sdf: SimpleDateFormat
        sdf = if (format == null || format.isEmpty()) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat(format, Locale.getDefault())
        }
        sdf.timeZone = zone
        return sdf.format(tsp)
    }

    /**
     * 将毫秒转时分秒
     */
    fun generateTime(time: Long): String {
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * HH:mm:ss 转秒
     */
    fun returnSeconde(instr: String): Int {
        val a = instr.split("\\.".toRegex()).toTypedArray()
        val b = a[0].split(":".toRegex()).toTypedArray()
        return Integer.valueOf(b[0]) * 60 * 60 + Integer.valueOf(b[1]) * 60 + Integer.valueOf(b[2])
    }

    /**
     * 秒转时间
     */
    fun FormatRunTime(runTime: Long): String {
        if (runTime < 0) {
            return "00:00:00"
        }
        val hour = runTime / 3600
        val minute = runTime % 3600 / 60
        val second = runTime % 60
        return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                unitTimeFormat(second)
    }

    companion object {
        private var timeUtils: TimeUtils? = null
        val instance: TimeUtils?
            get() {
                if (null == timeUtils) {
                    timeUtils = TimeUtils()
                }
                return timeUtils
            }

        private fun unitTimeFormat(number: Long): String {
            return String.format("%02d", number)
        }
    }
}