package com.hyrc.lrs.hyrcapplication.activity.list.adapter

import android.content.Context
import android.util.Log
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.bean.DataImg
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.utils.glide.GlideUtil


/**
 * @description 作用:
 * @date: 2020/12/18
 * @author: 卢融霜
 */
class ImgAdapter(layoutResId: Int, activityContext: Context) : BaseAdapter<DataImg>(layoutResId, activityContext) {

    override fun itemInit(helper: BaseViewHolder?, type: Int, item: DataImg) {
//        GlideUtil.displayRandom(1, item.images[0], helper?.getView(R.id.iv_welfare))
    }

}