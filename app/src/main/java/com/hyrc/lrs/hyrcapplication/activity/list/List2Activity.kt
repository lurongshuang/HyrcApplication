package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.List2Adapter
import com.hyrc.lrs.hyrcapplication.bean.List2Bean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
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

    override fun getUrl(): String {

        return "https://gank.io/api/v2/data/category/GanHuo/type/Android/page/${page}/count/10";
    }

    override fun getParams(): Map<String?, String?>? {

        return null;
    }

    override fun failure(call: Call?, e: Exception?) {

    }

    override fun response(response: String) {
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

    override fun getMethodType(): String {
        return HyrcHttpUtil.METHOD_GET
    }

    override fun initAdapter(adapter: BaseAdapter<Any>?): BaseAdapter<Any> {
        return List2Adapter(R.layout.list2_item, this) as BaseAdapter<Any>;
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
    }

    override fun initView() {
        super.initView()
        setToolBar(true, "列表2")
    }
}