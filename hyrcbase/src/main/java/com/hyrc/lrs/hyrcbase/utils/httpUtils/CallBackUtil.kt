package com.hyrc.lrs.hyrcbase.utils.httpUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import okhttp3.Call
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.floor
import kotlin.time.toDuration

abstract class CallBackUtil<T> {
    fun onProgress(progress: Float, total: Long) {}
    fun onError(call: Call?, e: Exception?) {
        mMainHandler.post { onFailure(call, e) }
    }

    fun onSeccess(call: Call?, response: Response) {
        val obj = onParseResponse(call, response)
        mMainHandler.post { onResponse(obj) }
    }

    /**
     * 解析response，执行在子线程
     */
    abstract fun onParseResponse(call: Call?, response: Response): T

    /**
     * 访问网络失败后被调用，执行在UI线程
     */
    abstract fun onFailure(call: Call?, e: Exception?)

    /**
     * 访问网络成功后被调用，执行在UI线程
     */
    abstract fun onResponse(response: T)
    abstract class CallBackDefault : CallBackUtil<Response>() {
        override fun onParseResponse(call: Call?, response: Response): Response {
            return response
        }
    }

    abstract class CallBackString : CallBackUtil<String>() {
        override fun onParseResponse(call: Call?, response: Response): String {
            return try {
                response.body!!.string()
            } catch (e: Exception) {
                RuntimeException("failure")
                ""
            }
        }
    }

    abstract class CallBackBitmap : CallBackUtil<Bitmap> {
        private var mTargetWidth = 0
        private var mTargetHeight = 0

        constructor() {}
        constructor(targetWidth: Int, targetHeight: Int) {
            mTargetWidth = targetWidth
            mTargetHeight = targetHeight
        }

        constructor(imageView: ImageView) {
            val width = imageView.width
            val height = imageView.height
            if (width <= 0 || height <= 0) {
                throw RuntimeException("无法获取ImageView的width或height")
            }
            mTargetWidth = width
            mTargetHeight = height
        }

        override fun onParseResponse(call: Call?, response: Response): Bitmap {
            return if (mTargetWidth == 0 || mTargetHeight == 0) {
                BitmapFactory.decodeStream(response.body!!.byteStream())
            } else {
                getZoomBitmap(response)
            }
        }

        /**
         * 压缩图片，避免OOM异常
         */
        private fun getZoomBitmap(response: Response): Bitmap {
            var data: ByteArray? = null
            try {
                data = response.body!!.bytes()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data!!.size, options)
            val picWidth = options.outWidth
            val picHeight = options.outHeight
            var sampleSize = 1
            val heightRatio = floor(picWidth.toFloat() / mTargetWidth.toFloat().toDouble()).toInt()
            val widthRatio = floor(picHeight.toFloat() / mTargetHeight.toFloat().toDouble()).toInt()
            if (heightRatio > 1 || widthRatio > 1) {
                sampleSize = Math.max(heightRatio, widthRatio)
            }
            options.inSampleSize = sampleSize
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeByteArray(data, 0, data.size, options)
                    ?: throw RuntimeException("Failed to decode stream.")
        }
    }

    /**
     * 下载文件时的回调类
     */
    abstract class CallBackFile
    /**
     *
     */(private val mDestFileDir: String, private val mdestFileName: String) : CallBackUtil<File?>() {
        override fun onParseResponse(call: Call?, response: Response): File? {
            var `is`: InputStream? = null
            val buf = ByteArray(1024 * 8)
            var len = 0
            var fos: FileOutputStream? = null
            try {
                `is` = response.body!!.byteStream()
                val total = response.body!!.contentLength()
                var sum: Long = 0
                val dir = File(mDestFileDir)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val file = File(dir, mdestFileName)
                fos = FileOutputStream(file)
                while (`is`.read(buf).also { len = it } != -1) {
                    sum += len.toLong()
                    fos.write(buf, 0, len)
                    val finalSum = sum
                    mMainHandler.post { onProgress(finalSum * 100.0f / total, total) }
                }
                fos.flush()
                return file
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    response.body!!.close()
                    `is`?.close()
                } catch (e: IOException) {
                }
                try {
                    fos?.close()
                } catch (e: IOException) {
                }
            }
            return null
        }

    }

    companion object {
        var mMainHandler = Handler(Looper.getMainLooper())
    }
}