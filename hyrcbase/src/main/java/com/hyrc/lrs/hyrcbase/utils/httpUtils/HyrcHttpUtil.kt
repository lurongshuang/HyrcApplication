package com.hyrc.lrs.hyrcbase.utils.httpUtils

import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil.CallBackBitmap
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil.CallBackFile
import java.io.File

object HyrcHttpUtil {
    const val METHOD_GET = "GET"
    const val METHOD_POST = "POST"
    const val METHOD_PUT = "PUT"
    const val METHOD_DELETE = "DELETE"
    const val FILE_TYPE_FILE = "file/*"
    const val FILE_TYPE_IMAGE = "image/*"
    const val FILE_TYPE_AUDIO = "audio/*"
    const val FILE_TYPE_VIDEO = "video/*"

    /**
     * get请求
     */
    fun httpGet(url: String, callBack: CallBackUtil<*>?) {
        httpGet(url, null, null, callBack)
    }

    /**
     * get请求，可以传递参数
     */
    fun httpGet(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpGet(url, paramsMap, null, callBack)
    }

    /**
     * get请求，可以传递参数
     */
    fun http(methodType: String, url: String, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(methodType, url, paramsMap, null, callBack).execute()
    }

    /**
     * get请求，可以传递参数
     */
    fun httpGet(url: String, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_GET, url, paramsMap, headerMap, callBack).execute()
    }


    /**
     * post请求
     */
    fun httpPost(url: String, callBack: CallBackUtil<*>?) {
        httpPost(url, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPost(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpPost(url, paramsMap, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPost(url: String, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_POST, url, paramsMap, headerMap, callBack).execute()
    }

    /**
     * post请求
     */
    fun httpPut(url: String, callBack: CallBackUtil<*>?) {
        httpPut(url, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPut(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpPut(url, paramsMap, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPut(url: String, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_PUT, url, paramsMap, headerMap, callBack).execute()
    }

    /**
     * post请求
     */
    fun httpDelete(url: String, callBack: CallBackUtil<*>?) {
        httpDelete(url, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpDelete(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpDelete(url, paramsMap, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpDelete(url: String, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_DELETE!!, url!!, paramsMap, headerMap, callBack).execute()
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPostJson(url: String, jsonStr: String?, callBack: CallBackUtil<*>?) {
        httpPostJson(url, jsonStr, null, callBack)
    }

    /**
     * post请求，可以传递参数
     */
    fun httpPostJson(url: String, jsonStr: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_POST, url!!, jsonStr, headerMap, callBack).execute()
    }

    /**
     * post请求，上传单个文件
     */
    fun httpUploadFile(url: String, file: File?, fileKey: String?, fileType: String?, callBack: CallBackUtil<*>?) {
        httpUploadFile(url, file, fileKey, fileType, null, callBack)
    }

    /**
     * post请求，上传单个文件
     */
    fun httpUploadFile(url: String, file: File?, fileKey: String?, fileType: String?, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpUploadFile(url, file, fileKey, fileType, paramsMap, null, callBack)
    }

    /**
     * post请求，上传单个文件
     */
    fun httpUploadFile(url: String, file: File?, fileKey: String?, fileType: String?, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_POST, url, paramsMap, file, fileKey, fileType, headerMap, callBack).execute()
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     */
    fun httpUploadListFile(url: String, fileList: List<File>?, fileKey: String?, fileType: String?, callBack: CallBackUtil<*>?) {
        httpUploadListFile(url, null, fileList, fileKey, fileType, callBack)
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     */
    fun httpUploadListFile(url: String, paramsMap: Map<String?, String?>?, fileList: List<File>?, fileKey: String?, fileType: String?, callBack: CallBackUtil<*>?) {
        httpUploadListFile(url, paramsMap, fileList, fileKey, fileType, null, callBack)
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     */
    fun httpUploadListFile(url: String, paramsMap: Map<String?, String?>?, fileList: List<File>?, fileKey: String?, fileType: String?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_POST, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack).execute()
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     */
    fun httpUploadMapFile(url: String, fileMap: Map<String?, File?>?, fileType: String?, callBack: CallBackUtil<*>?) {
        httpUploadMapFile(url, fileMap, fileType, null, callBack)
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     */
    fun httpUploadMapFile(url: String, fileMap: Map<String?, File?>?, fileType: String?, paramsMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        httpUploadMapFile(url, fileMap, fileType, paramsMap, null, callBack)
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     */
    fun httpUploadMapFile(url: String, fileMap: Map<String?, File?>?, fileType: String?, paramsMap: Map<String?, String?>?, headerMap: Map<String?, String?>?, callBack: CallBackUtil<*>?) {
        RequestUtil(METHOD_POST, url!!, paramsMap, fileMap, fileType, headerMap, callBack!!).execute()
    }

    /**
     * 下载文件,不带参数
     */
    fun httpDownloadFile(url: String, callBack: CallBackFile?) {
        httpDownloadFile(url, null, callBack)
    }

    /**
     * 下载文件,带参数
     */
    fun httpDownloadFile(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackFile?) {
        httpGet(url, paramsMap, null, callBack)
    }

    /**
     * 加载图片
     */
    fun httpGetBitmap(url: String, callBack: CallBackBitmap?) {
        httpGetBitmap(url, null, callBack)
    }

    /**
     * 加载图片，带参数
     */
    fun httpGetBitmap(url: String, paramsMap: Map<String?, String?>?, callBack: CallBackBitmap?) {
        httpGet(url, paramsMap, null, callBack)
    }
}