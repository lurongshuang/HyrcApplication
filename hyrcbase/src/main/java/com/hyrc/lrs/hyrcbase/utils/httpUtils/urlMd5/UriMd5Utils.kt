package com.hyrc.lrs.hyrcbase.utils.httpUtils.urlMd5

import com.hyrc.lrs.hyrcbase.utils.time.TimeUtils
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * @description 作用:
 * @date: 2020/6/2
 * @author: 卢融霜
 */
class UriMd5Utils {
    fun getMD5_16(info: String): String {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(info.toByteArray(StandardCharsets.UTF_8))
            val encryption = md5.digest()
            val strBuf = StringBuffer()
            for (i in encryption.indices) {
                if (Integer.toHexString(0xff and encryption[i].toInt()).length == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff and encryption[i].toInt()))
                } else {
                    strBuf.append(Integer.toHexString(0xff and encryption[i].toInt()))
                }
            }
            //16位大写
            strBuf.toString().substring(8, 24).toUpperCase()
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    fun getMD5_32(info: String): String? {
        var reStr: String? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(info.toByteArray())
            val stringBuffer = StringBuffer()
            for (b in bytes) {
                val bt: Int = b.toInt()
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt))
            }
            reStr = stringBuffer.toString().toUpperCase()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return reStr
    }

    fun getMD5_32(info: String, isUpperCase: Boolean): String? {
        var reStr: String? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(info.toByteArray())
            val stringBuffer = StringBuffer()
            for (b in bytes) {
                val bt: Int = b.toInt()
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt))
            }
            reStr = if (isUpperCase) {
                stringBuffer.toString().toUpperCase()
            } else {
                stringBuffer.toString().toLowerCase()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return reStr
    }

    fun getMd5Token(map: Map<*, *>?): String? {
        return getMD5_32(SpliValue(map) + "" + TimeUtils.instance?.stringNowDate)
    }

    fun SpliValue(map: Map<*, *>?): String? {
        var splitString: String? = ""
        if (map != null && map.isNotEmpty()) {
            val keySet: Set<String> = map.keys as Set<String>
            val iter = keySet.iterator()
            while (iter.hasNext()) {
                val key = iter.next()
                splitString += map[key]
            }
        }
        return splitString
    }

    companion object {
        var uriMd5Utils: UriMd5Utils? = null
        val instance: UriMd5Utils?
            get() {
                if (uriMd5Utils == null) {
                    uriMd5Utils = UriMd5Utils()
                }
                return uriMd5Utils
            }
    }
}