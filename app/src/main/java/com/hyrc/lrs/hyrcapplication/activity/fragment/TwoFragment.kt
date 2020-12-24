package com.hyrc.lrs.hyrcapplication.activity.fragment

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.List2Adapter
import com.hyrc.lrs.hyrcapplication.bean.List2Bean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseFragment
import com.hyrc.lrs.hyrcbase.base.BaseListFragment
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.fragment_two.*
import okhttp3.Call

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author:
 */
class TwoFragment : BaseListFragment() {
    var isShow = false;
    override fun onInvisible() {
    }

    override fun onVisible() {
        activity?.title = "列表"
        if (isShow) {
            return;
        }
        isShow = true;
        loadData(adapter!!, page)
    }

    override fun isEnableRefresh(): Boolean {
        return true;
    }

    override fun isEnableLoadMore(): Boolean {
        return true;
    }

    override fun initAdapter(): Any {
        return List2Adapter(R.layout.list2_item, activity!!);
    }

    override fun loadData(adapter: BaseAdapter<Any>, page: Int) {
        var url = "https://gank.io/api/v2/data/category/GanHuo/type/Android/page/${page}/count/10";
        HyrcHttpUtil.httpGet(url, null, object : CallBackUtil.CallBackString() {
            override fun onFailure(call: Call?, e: Exception?) {
                finishRefresh()
                finishLoadMore()
                if (page == 1) {
                    showError()
                }
            }

            override fun onResponse(response: String) {
                finishRefresh()
                finishLoadMore()
                if (page == 1) {
                    clearData()
                }
                var data = Gson().fromJson(response, List2Bean::class.javaObjectType);
                if (data.data.isNotEmpty()) {
                    data.data.forEach {
                        adapter?.addData(it)
                        showContent();
                    }
                } else {
                    showEmpty();
                }
            }

        })
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {

    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {

    }
}