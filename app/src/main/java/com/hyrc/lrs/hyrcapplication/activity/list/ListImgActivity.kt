package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.ImgAdapter
import com.hyrc.lrs.hyrcapplication.bean.ImgBean
import com.hyrc.lrs.hyrcapplication.bean.List2Bean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import okhttp3.Call


class ListImgActivity : BaseListActivity() {
    var pSize = 30;
    override fun isEnableRefresh(): Boolean {
        return true
    }

    override fun isEnableLoadMore(): Boolean {
        return true
    }

    override fun initListView() {
        setToolBar(true, "图片列表")
    }

    override fun initAdapter(): Any {
        return ImgAdapter(R.layout.item_welfare, this);
    }

    override fun loadData(adapter: BaseAdapter<Any>, page: Int) {
        var url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/${page}/count/${pSize}";

        HyrcHttpUtil.httpGet(url, null, object : CallBackUtil.CallBackString() {
            override fun onFailure(call: Call?, e: Exception?) {
                finishState()
                if (page == 1) {
                    showError()
                } else {
                    showContent()
                }
            }

            override fun onResponse(response: String) {
                if (page == 1) {
                    clearData()
                }
                finishState()
                var data = Gson().fromJson(response, ImgBean::class.java)
                if (data.data.isNotEmpty()) {
                    data.data.forEach {
                        adapter?.addData(it);
                    }
                    showContent();
                } else {
                    showEmpty()
                }
            }

        })
    }


    override fun getListManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {

    }
}