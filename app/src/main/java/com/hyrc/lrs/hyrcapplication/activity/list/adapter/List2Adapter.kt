package com.hyrc.lrs.hyrcapplication.activity.list.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.webView.WebViewActivity
import com.hyrc.lrs.hyrcapplication.bean.Data2
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.utils.glide.GlideUtil

/**
 * @description 作用:
 * @date: 2020/12/17
 * @author: 卢融霜
 */
class List2Adapter(layoutResId: Int, activityContext: Context) : BaseAdapter<Data2>(layoutResId, activityContext) {
    override fun itemInit(helper: BaseViewHolder?, type: Int, item: Data2) {
        helper?.getView<View>(R.id.iv_all_welfare)?.visibility = View.GONE
        helper!!.getView<View>(R.id.ll_welfare_other).visibility = View.VISIBLE
        // 显示gif图片会很耗内存
        if (item.images != null && item.images.isNotEmpty() && !TextUtils.isEmpty(item.images[0])) {
            helper!!.getView<View>(R.id.iv_android_pic).visibility = View.VISIBLE
            GlideUtil.displayGif(item.images[0], helper!!.getView<View>(R.id.iv_android_pic) as ImageView?)
        } else {
            helper!!.getView<View>(R.id.iv_android_pic).visibility = View.GONE
        }

        helper.getView<TextView>(R.id.tv_android_des).text = item.desc
        helper.getView<TextView>(R.id.tv_android_who).text = "${item.author}"
        helper.getView<TextView>(R.id.tv_content_type).text = " · ${item.type}"
        helper.getView<TextView>(R.id.tv_android_time).text = "${item.publishedAt}"

        helper.getView<LinearLayout>(R.id.ll_all).setOnClickListener { v ->
            WebViewActivity.loadUrl(v.context, item.url, item.desc)
        }
    }
}