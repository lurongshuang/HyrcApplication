package com.hyrc.lrs.hyrcapplication.bean

/**
 * @description 作用:
 * @date: 2020/12/17
 * @author: 卢融霜
 */
data class List2Bean(
    var counts: Int,
    var `data`: List<Data2>,
    var status: Int
)

data class Data2(
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