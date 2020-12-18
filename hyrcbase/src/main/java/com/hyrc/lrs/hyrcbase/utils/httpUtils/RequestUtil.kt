package com.hyrc.lrs.hyrcbase.utils.httpUtils

import android.text.TextUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.*
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

internal class RequestUtil private constructor(/*
    请求方式，目前只支持get和post
     */
        private val mMetyodType: String, //接口
        private var mUrl: String, /*
    json类型的参数，post方式
     */
        private val mJsonStr: String?, //文件的参数，post方式,只有一个文件
        private val mFile: File?, //文件集合，这个集合对应一个key，即mfileKey
        private val mfileList: List<File?>?, //上传服务器的文件对应的key
        private val mfileKey: String?, //文件集合，每个文件对应一个key
        private val mfileMap: Map<String?, File?>?, //文件类型的参数，与file同时存在
        private val mFileType: String?, /*
    键值对类型的参数，只有这一种情况下区分post和get。
     */
        private val mParamsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {

    //头参数
    private val mHeaderMap: Map<String?, String?>?

    //回调接口
    private val mCallBack: CallBackUtil<*>?

    //OKhttpClient对象
    private var mOkHttpClient: OkHttpClient? = null

    //请求对象
    private var mOkHttpRequest: Request? = null

    //请求对象的构建者
    private var mRequestBuilder: Request.Builder? = null

    constructor(methodType: String, url: String, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) : this(methodType, url, null, null, null, null, null, null, paramsMap, headerMap, callBack) {}
    constructor(methodType: String, url: String, jsonStr: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) : this(methodType, url, jsonStr, null, null, null, null, null, null, headerMap, callBack) {}
    constructor(methodType: String, url: String, paramsMap: Map<String?, String?>?, file: File?, fileKey: String?, fileType: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) : this(methodType, url, null, file, null, fileKey, null, fileType, paramsMap, headerMap, callBack) {}
    constructor(methodType: String, url: String, paramsMap: Map<String?, String?>?, fileList: List<File>?, fileKey: String?, fileType: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) : this(methodType, url, null, null, fileList, fileKey, null, fileType, paramsMap, headerMap, callBack) {}
    constructor(methodType: String, url: String, paramsMap: Map<String?, String?>?, fileMap: Map<String?, File?>?, fileType: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) : this(methodType, url, null, null, null, null, fileMap, fileType, paramsMap, headerMap, callBack) {}//设置参数
    //mRequestBuilder.addHeader("Authorization","Bearer "+"token");可以把token添加到这儿
//先判断是否有文件，

    /**
     * 创建OKhttpClient实例。
     */
    private val instance: Unit
        private get() {
            mOkHttpClient = OkHttpClient()
            mRequestBuilder = Request.Builder()
            if (mFile != null || mfileList != null || mfileMap != null) { //先判断是否有文件，
                setFile()
            } else {
                //设置参数
                when (mMetyodType) {
                    HyrcHttpUtil.METHOD_GET -> setGetParams()
                    HyrcHttpUtil.METHOD_POST -> mRequestBuilder!!.post(requestBody)
                    HyrcHttpUtil.METHOD_PUT -> mRequestBuilder!!.put(requestBody)
                    HyrcHttpUtil.METHOD_DELETE -> mRequestBuilder!!.delete(requestBody)
                }
            }
            mRequestBuilder!!.url(mUrl)
            if (mHeaderMap != null) {
                setHeader()
            }
            //mRequestBuilder.addHeader("Authorization","Bearer "+"token");可以把token添加到这儿
            mOkHttpRequest = mRequestBuilder!!.build()
        }
    /**
     * post,put,delete都需要body，但也都有body等于空的情况，此时也应该有body对象，但body中的内容为空
     *///数据类型为json格式，
    //json数据，
    /**
     * 首先判断mJsonStr是否为空，由于mJsonStr与mParamsMap不可能同时存在，所以先判断mJsonStr
     */

    /**
     * 得到body对象
     */
    private val requestBody: RequestBody
        private get() {
            /**
             * 首先判断mJsonStr是否为空，由于mJsonStr与mParamsMap不可能同时存在，所以先判断mJsonStr
             */
            if (!TextUtils.isEmpty(mJsonStr)) {
                val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!! //数据类型为json格式，
                return RequestBody.create(JSON, mJsonStr!!) //json数据，
            }
            /**
             * post,put,delete都需要body，但也都有body等于空的情况，此时也应该有body对象，但body中的内容为空
             */
            val formBody = FormBody.Builder()
            if (mParamsMap != null) {
                for (key in mParamsMap.keys) {
                    formBody.add(key!!, mParamsMap[key]!!)
                }
            }
            return formBody.build()
        }

    /**
     * get请求，只有键值对参数
     */
    private fun setGetParams() {
        if (mParamsMap != null) {
            mUrl = "$mUrl?"
            for (key in mParamsMap.keys) {
                mUrl = mUrl + key + "=" + mParamsMap[key] + "&"
            }
            mUrl = mUrl.substring(0, mUrl.length - 1)
        }
    }

    /**
     * 设置上传文件
     */
    private fun setFile() {
        if (mFile != null) { //只有一个文件，且没有文件名
            if (mParamsMap == null) {
                setPostFile()
            } else {
                setPostParameAndFile()
            }
        } else if (mfileList != null) { //文件集合，只有一个文件名。所以这个也支持单个有文件名的文件
            setPostParameAndListFile()
        } else if (mfileMap != null) { //多个文件，每个文件对应一个文件名
            setPostParameAndMapFile()
        }
    }

    /**
     * 只有一个文件，且提交服务器时不用指定键，没有参数
     */
    private fun setPostFile() {
        if (mFile != null && mFile.exists()) {
            val fileType: MediaType? = mFileType.toString().toMediaTypeOrNull()
            val body = RequestBody.create(fileType, mFile) //json数据，
            mRequestBuilder!!.post(ProgressRequestBody(body, mCallBack))
        }
    }

    /**
     * 只有一个文件，且提交服务器时不用指定键，带键值对参数
     */
    private fun setPostParameAndFile() {
        if (mParamsMap != null && mFile != null) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            for (key in mParamsMap.keys) {
                builder.addFormDataPart(key!!, mParamsMap[key]!!)
            }
            builder.addFormDataPart(mfileKey!!, mFile.name, RequestBody.create(mFileType.toString().toMediaTypeOrNull(), mFile))
            mRequestBuilder!!.post(ProgressRequestBody(builder.build(), mCallBack))
        }
    }

    /**
     * 文件集合，可能带有键值对参数
     */
    private fun setPostParameAndListFile() {
        if (mfileList != null) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            if (mParamsMap != null) {
                for (key in mParamsMap.keys) {
                    builder.addFormDataPart(key!!, mParamsMap[key]!!)
                }
            }
            for (f in mfileList) {
                builder.addFormDataPart(mfileKey!!, f!!.name, RequestBody.create(mFileType.toString().toMediaTypeOrNull(), f!!))
            }
            mRequestBuilder!!.post(builder.build())
        }
    }

    /**
     * 文件Map，可能带有键值对参数
     */
    private fun setPostParameAndMapFile() {
        if (mfileMap != null) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            if (mParamsMap != null) {
                for (key in mParamsMap.keys) {
                    builder.addFormDataPart(key!!, mParamsMap[key]!!)
                }
            }
            for (key in mfileMap.keys) {
                builder.addFormDataPart(key!!, mfileMap[key]!!.name, RequestBody.create(mFileType.toString().toMediaTypeOrNull(), mfileMap[key]!!))
            }
            mRequestBuilder!!.post(builder.build())
        }
    }

    /**
     * 设置头参数
     */
    private fun setHeader() {
        if (mHeaderMap != null) {
            for (key in mHeaderMap.keys) {
                mRequestBuilder!!.addHeader(key!!, mHeaderMap[key]!!)
            }
        }
    }

    fun execute() {
        mOkHttpClient!!.newCall(mOkHttpRequest!!).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mCallBack?.onError(call, e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mCallBack?.onSeccess(call, response)
            }
        })
    }

    /**
     * 自定义RequestBody类，得到文件上传的进度
     */
    private class ProgressRequestBody internal constructor(//实际的待包装请求体
            private val requestBody: RequestBody, private val callBack: CallBackUtil<*>?) : RequestBody() {

        //包装完成的BufferedSink
        private var bufferedSink: BufferedSink? = null

        /**
         * 重写调用实际的响应体的contentType
         */
        override fun contentType(): MediaType? {
            return requestBody.contentType()
        }

        /**
         * 重写调用实际的响应体的contentLength ，这个是文件的总字节数
         */
        @Throws(IOException::class)
        override fun contentLength(): Long {
            return requestBody.contentLength()
        }

        /**
         * 重写进行写入
         */
        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            if (bufferedSink == null) {
                bufferedSink = sink(sink).buffer()
            }
            requestBody.writeTo(bufferedSink!!)
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink!!.flush()
        }

        /**
         * 写入，回调进度接口
         */
        private fun sink(sink: BufferedSink): Sink {
            return object : ForwardingSink(sink) {
                //当前写入字节数
                var bytesWritten = 0L

                //总字节长度，避免多次调用contentLength()方法
                var contentLength = 0L

                @Throws(IOException::class)
                override fun write(source: Buffer, byteCount: Long) {
                    super.write(source, byteCount) //这个方法会循环调用，byteCount是每次调用上传的字节数。
                    if (contentLength == 0L) {
                        //获得总字节长度
                        contentLength = contentLength()
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount
                    val progress = bytesWritten * 1.0f / contentLength
                    CallBackUtil.mMainHandler.post { callBack!!.onProgress(progress, contentLength) }
                }
            }
        }

    }

    init {
        mHeaderMap = headerMap
        mCallBack = callBack
        instance
    }
}