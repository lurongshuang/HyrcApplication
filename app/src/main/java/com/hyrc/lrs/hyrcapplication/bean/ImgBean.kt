package com.hyrc.lrs.hyrcapplication.bean

/**
 * @description 作用:
 * @date: 2020/12/18
 * @author: 卢融霜
 */
data class ImgBean(
    var bdFmtDispNum: String,
    var bdIsClustered: String,
    var bdSearchTime: String,
    var `data`: List<DataImg>,
    var displayNum: Int,
    var gsm: String,
    var isNeedAsyncRequest: Int,
    var listNum: Int,
    var queryEnc: String,
    var queryExt: String
)

data class DataImg(
    var adPicId: String,
    var adType: String,
    var bdFromPageTitlePrefix: String,
    var bdImgnewsDate: String,
    var bdSetImgNum: Int,
    var bdSourceName: String,
    var bdSrcType: String,
    var cs: String,
    var currentIndex: String,
    var di: String,
    var face_info: Any?,
    var filesize: String,
    var fromPageTitle: String,
    var fromPageTitleEnc: String,
    var fromURL: String,
    var fromURLHost: String,
    var hasAspData: String,
    var hasLarge: Int,
    var hasThumbData: String,
    var height: Int,
    var hoverURL: String,
    var imgCollectionWord: String,
    var imgType: String,
    var `is`: String,
    var isAspDianjing: Int,
    var isCopyright: Int,
    var is_gif: Int,
    var largeTnImageUrl: String,
    var middleURL: String,
    var objURL: String,
    var os: String,
    var pageNum: Int,
    var partnerId: Int,
    var personalized: String,
    var pi: String,
    var replaceUrl: List<ReplaceUrl>,
    var resourceInfo: Any?,
    var simid: String,
    var simid_info: Any?,
    var source_type: String,
    var spn: Int,
    var strategyAssessment: String,
    var thumbURL: String,
    var token: String,
    var type: String,
    var width: Int,
    var xiangshi_info: Any?
)

data class ReplaceUrl(
    var FromURL: String,
    var FromUrl: String,
    var ObjURL: String,
    var ObjUrl: String
)