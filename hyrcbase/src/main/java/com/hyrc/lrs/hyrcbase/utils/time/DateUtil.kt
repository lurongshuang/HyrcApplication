package com.hyrc.lrs.hyrcbase.utils.time

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by jackerJin on 2018/10/10
 */
class DateUtil {
    /**
     * 获取系统时间戳 * @return
     */
    val curTimeLong: Long
        get() = System.currentTimeMillis()

    companion object {
        /**
         * 获取当前时间 * @param pattern * @return
         */
        fun getCurDate(pattern: String?): String {
            val sDateFormat = SimpleDateFormat(pattern)
            return sDateFormat.format(Date())
        }

        /**
         * 时间戳转换成字符窜 * @param milSecond * @param pattern * @return
         */
        fun getDateToString(milSecond: Long, pattern: String?): String {
            val date = Date(milSecond)
            val format = SimpleDateFormat(pattern)
            return format.format(date)
        }

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
         * 将字符串转为时间戳 * @param dateString * @param pattern * @return
         */
        fun getStringToDate(dateString: String?, pattern: String?): Long {
            val dateFormat = SimpleDateFormat(pattern)
            var date = Date()
            try {
                date = dateFormat.parse(dateString)
            } catch (e: ParseException) { // TODO Auto-generated catch block e.printStackTrace(); } return date.getTime(); }
            }
            return date.time
        }

        /**
         * 获取年
         */
        val year: Int
            get() {
                val cd = Calendar.getInstance()
                return cd[Calendar.YEAR]
            }

        /**
         * 获取月
         */
        val month: Int
            get() {
                val cd = Calendar.getInstance()
                return cd[Calendar.MONTH] + 1
            }

        /**
         * 获取日
         */
        val day: Int
            get() {
                val cd = Calendar.getInstance()
                return cd[Calendar.DATE]
            }

        /**
         * 获取时
         */
        val hour: Int
            get() {
                val cd = Calendar.getInstance()
                return cd[Calendar.HOUR]
            }

        /**
         * 获取分
         */
        val minute: Int
            get() {
                val cd = Calendar.getInstance()
                return cd[Calendar.MINUTE]
            }

        /**
         * 转化时间输入时间与当前时间的间隔
         */
        fun converTime(timestamp: Long): String? {
//        long currentSeconds = System.currentTimeMillis() / 1000;
            val currentSeconds = getStringToDate(getCurDate("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss")
            // 与现在时间相差秒数
            val timeGap = currentSeconds / 1000 - timestamp / 1000
            var timeStr: String? = null
            timeStr = if (timeGap > 24 * 60 * 60 * 300) {
                //300天以上
                getDateToString(timestamp, "yyyy年M月d日 H:mm")
            } else if (timeGap > 24 * 60 * 60) {
                // 1天以上
                getDateToString(timestamp, "M月d日 H:mm")
            } else if (timeGap > 60 * 60) {
                // 1小时-24小时
                getDateToString(timestamp, "M月d日 H:mm")
            } else if (timeGap > 60) {
                // 1分钟-59分钟
                (timeGap / 60).toString() + "分钟前"
            } else { // 1秒钟-59秒钟
                "刚刚"
            }
            return timeStr
        }
    }
}