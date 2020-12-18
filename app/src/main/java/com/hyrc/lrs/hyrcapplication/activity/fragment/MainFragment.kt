package com.hyrc.lrs.hyrcapplication.activity.fragment

import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.List2Activity
import com.hyrc.lrs.hyrcapplication.activity.list.ListActivity
import com.hyrc.lrs.hyrcapplication.activity.list.ListImgActivity
import com.hyrc.lrs.hyrcbase.base.BaseActivity
import com.hyrc.lrs.hyrcbase.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author:
 */
class MainFragment : BaseFragment() {

    override fun initView() {
        showContent()
        activity?.title = "首页"

        tvTitle.setOnClickListener {
            baseActivity?.openActivity(ListActivity().javaClass)
        }
        tvTitle1.setOnClickListener {
            baseActivity?.openActivity(List2Activity().javaClass)
        }
        tvTitle2.setOnClickListener {
            baseActivity?.openActivity(ListImgActivity().javaClass)
        }
    }

    override fun onInvisible() {
    }

    override fun onVisible() {
        activity?.title = "首页"

    }

    override fun setContent(): Int {
        return R.layout.fragment_main
    }
}