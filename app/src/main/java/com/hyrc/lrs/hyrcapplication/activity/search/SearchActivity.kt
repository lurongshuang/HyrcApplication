package com.hyrc.lrs.hyrcapplication.activity.search

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.List2Adapter
import com.hyrc.lrs.hyrcapplication.bean.List2Bean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseSearchListActivity
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import okhttp3.Call

/**
 * @description 作用:
 * @date: 2020/12/17
 * @author: 卢融霜
 */
class SearchActivity : BaseSearchListActivity() {

    override fun isEnableRefresh(): Boolean {
        return true;
    }

    override fun isEnableLoadMore(): Boolean {
        return true
    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }


    override fun destroy() {
    }


    override fun initAdapter(): Any {
        return List2Adapter(R.layout.list2_item, this);
    }


    /**
     * 加载数据
     */
    override fun loadData(adapter: BaseAdapter<Any>?, title: String?, isNew: Boolean) {
        var url = "https://gank.io/api/v2/search/${getSearchText()}/category/Article/type/Android/page/${page}/count/10";
        if (isNew) {
            page = 1;
        }
        if (page == 1 && adapter?.data?.size == 0) {
            showLoading()
        }
        HyrcHttpUtil.httpGet(url, null, object : CallBackUtil.CallBackString() {
            override fun onFailure(call: Call?, e: Exception?) {
                finishState()
                loadBaseDialog?.dismiss()
                if (page == 1) {
                    showError()
                } else {
                    showContent()
                }
            }

            override fun onResponse(response: String) {
                finishState()
                if (page == 1) {
                    clearData()
                }
                var data = Gson().fromJson(response, List2Bean::class.javaObjectType);
                if (data.data.isNotEmpty()) {
                    data.data.forEach {
                        adapter?.addData(it)
                        showContent();
                    }
                    if (data.data.isEmpty()) {
                        noMoreData(true)
                    }
                } else {
                    showEmpty("暂无数据")
                }
            }

        })
    }
}