package com.hyrc.lrs.hyrcapplication.bean

/**
 * @description 作用:
 * @date: 2020/12/18
 * @author: 卢融霜
 */
  data class ImgBean(
    var `data`: List<DataImg>,
    var page: Int,
    var page_count: Int,
    var status: Int,
    var total_counts: Int
)

data class DataImg(
    var _id: String,
    var author: String,
    var category: String,
    var createdAt: String,
    var desc: String,
    var images: List<String>,
    var likeCounts: Int,
    var publishedAt: String,
    var stars: Int,
    var title: String,
    var type: String,
    var url: String,
    var views: Int
)