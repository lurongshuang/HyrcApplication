package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.List2Adapter
import com.hyrc.lrs.hyrcapplication.bean.List2Bean
import com.hyrc.lrs.hyrcapplication.bean.ListBean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import okhttp3.Call

class List2Activity : BaseListActivity() {
    override fun isEnableRefresh(): Boolean {
        return true;
    }

    override fun isEnableLoadMore(): Boolean {
        return true;
    }

    override fun initListView() {
        setToolBar(true, "列表2")
    }

    override fun initAdapter(): Any {
        return List2Adapter(R.layout.list2_item, this);
    }

    override fun loadData(adapter: BaseAdapter<Any>, page: Int) {
        var url = "https://gank.io/api/v2/data/category/GanHuo/type/Android/page/${page}/count/10";

        HyrcHttpUtil.httpGet(url, null, object : CallBackUtil.CallBackString() {
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
                var data = Gson().fromJson(response, List2Bean::class.javaObjectType);
                if (data.data.isNotEmpty()) {
                    data.data.forEach {
                        adapter?.addData(it)
                    }
                    showContent();
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