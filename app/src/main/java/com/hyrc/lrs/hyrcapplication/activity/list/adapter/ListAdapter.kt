package com.hyrc.lrs.hyrcapplication.activity.list.adapter

import android.content.Context
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.webView.WebViewActivity
import com.hyrc.lrs.hyrcapplication.bean.DataX
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.xuexiang.xui.widget.alpha.XUIAlphaLinearLayout

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author: 卢融霜
 */
class ListAdapter(layoutResId: Int, activityContext: Context) : BaseAdapter<DataX>(layoutResId, activityContext) {

    override fun itemInit(helper: BaseViewHolder?, type: Int, item: DataX) {
        helper?.setText(R.id.tvTitle, item.title);
        helper?.setText(R.id.tvUser, item.author);
        helper?.setText(R.id.tvDate, item.niceShareDate);

        helper?.getView<XUIAlphaLinearLayout>(R.id.llItm)?.setOnClickListener { v ->
            WebViewActivity.loadUrl(v.context, item.link, item.desc)
        }
    }
}