package com.hyrc.lrs.hyrcapplication.activity.list

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.list.adapter.ImgAdapter
import com.hyrc.lrs.hyrcapplication.bean.ImgBean
import com.hyrc.lrs.hyrcbase.base.BaseAdapter
import com.hyrc.lrs.hyrcbase.base.BaseListActivity
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import okhttp3.Call


class ListImgActivity : BaseListActivity() {
    var pa = 1;
    var pSize = 30;
    override fun isEnableRefresh(): Boolean {
        return true
    }

    override fun isEnableLoadMore(): Boolean {
        return true
    }

    override fun getUrl(): String {
        pa += pSize;
        pa -= 1;
        return "https://image.baidu.com/search/acjson?tn=resultjson_com" +
                "&logid=11237076728105854830&ipn=rj&ct=201326592&is=&fp=result&queryWord=" +
                "高清壁纸&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=&hd=&latest=" +
                "&copyright=&word=高清壁纸&s=&se=&tab=&width=&height=&face=&istype=" +
                "&qc=&nc=1&fr=&expermode=&force=&pn=${pa}&rn=${pSize}&gsm=b4&1608261605968=";
    }

    override fun getParams(): Map<String?, String?>? {
        return null
    }

    override fun failure(call: Call?, e: Exception?) {

    }

    override fun response(response: String) {
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

    override fun getMethodType(): String {
        return HyrcHttpUtil.METHOD_GET
    }

    override fun getListManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initAdapter(adapter: BaseAdapter<Any>?): BaseAdapter<Any> {
        return ImgAdapter(R.layout.item_welfare, this) as BaseAdapter<Any>;
    }

    override fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {
        pa = 1;
    }

    override fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView) {

    }

    override fun initView() {
        super.initView()
        setToolBar(true, "图片列表")
    }

}