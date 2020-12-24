package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.ListAdapter
import com.hyrc.lrs.hyrcapplication.bean.ListBean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import okhttp3.Call

class ListActivity : BaseListActivity() {
    private val url = "https://www.wanandroid.com/article/list/";
    override fun isEnableRefresh(): Boolean {
        return true;
    }

    override fun isEnableLoadMore(): Boolean {
        return true;
    }

    override fun initListView() {
        setToolBar(true, "列表")
        setAutoLoad(true)
    }

    override fun initAdapter(): Any {
        return ListAdapter(R.layout.item_list_adapter, this);

    }

    override fun loadData(adapter: BaseAdapter<Any>, page: Int) {
        var urls = "$url$page/json";
        HyrcHttpUtil.httpGet(urls, null, object : CallBackUtil.CallBackString() {
            override fun onFailure(call: Call?, e: Exception?) {
                finishState()
                if (page == 1 && adapter.data.size == 0) {
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
                var dataBean = Gson().fromJson(response, ListBean::class.javaObjectType)
                if (dataBean?.data?.datas?.size!! > 0) {
                    dataBean?.data?.datas.forEach {
                        adapter?.addData(it)
                    }
                    if (page > 10) {
                        noMoreData(true);
                    } else {
                        noMoreData(false);
                    }
                    showContent()
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