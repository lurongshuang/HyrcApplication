package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.ListAdapter
import com.hyrc.lrs.hyrcapplication.bean.ListBean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
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

    override fun initAdapter(adapter: BaseAdapter<Any>?): BaseAdapter<Any> {
        return ListAdapter(R.layout.item_list_adapter, this) as BaseAdapter<Any>
    }

    override fun getUrl(): String {
        return "$url$page/json";
    }

    override fun getParams(): Map<String?, String?>? {
        return null;
    }

    override fun failure(call: Call?, e: Exception?) {
    }

    override fun response(response: String) {
        var dataBean = Gson().fromJson(response, ListBean::class.javaObjectType)
        if (dataBean?.data?.datas?.size!! > 0) {
            dataBean?.data?.datas.forEach {
                adapter?.addData(it as Any)
            }
            showContent()
            if (page > 5) {
                noMoreData(true);
            } else {
                noMoreData(false);
            }
        } else {
            showEmpty();
        }
    }

    override fun getMethodType(): String {
        return HyrcHttpUtil.METHOD_GET;
    }

    override fun initView() {
        super.initView()
        setToolBar(true, "列表")
        setAutoLoad(true)
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

}