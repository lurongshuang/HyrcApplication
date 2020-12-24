package com.hyrc.lrs.hyrcbase.base

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author:
 */
open abstract class BaseAdapter<T>

//        //设置布局
//        setMultiTypeDelegate(new MultiTypeDelegate<BeanDate>() {
//            @Override
//            protected int getItemType(BeanDate beanDate) {
//                return beanDate.getType();
//            }
//        });
//        //添加布局
//        getMultiTypeDelegate()
//                .registerItemType(BeanDate.zero, R.layout.one)
//                .registerItemType(BeanDate.one,R.layout.two);

(layoutResId: Int, var activityContext: Context) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: T) {
        itemInit(helper, helper.itemViewType, item)
    }

    /**
     * 初始化item
     *
     * @param helper viewHolder
     * @param type   类型
     * @param item   数据源
     */
    protected abstract fun itemInit(helper: BaseViewHolder?, type: Int, item: T)
}