package com.hyrc.lrs.hyrcbase.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.hyrc.lrs.hyrcbase.R
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.activity_list_base.*
import okhttp3.Call

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author: 卢融霜
 */
abstract class BaseListActivity : BaseActivity() {
    var page = 1;
    var adapter: BaseAdapter<Any>? = null;
    var layoutManager: LayoutManager? = null;
    override fun onErrorRefresh() {
    }

    override fun destroy() {
    }

    override fun loadView(): Int {
        return R.layout.activity_list_base;
    }

    open fun getListManager(): LayoutManager {
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager)?.orientation = LinearLayoutManager.VERTICAL
        return layoutManager as LayoutManager;
    }

    override fun initView() {
        recyclerView.layoutManager = getListManager();
        adapter = initAdapter() as BaseAdapter<Any>;
        recyclerView.adapter = adapter
        setRefreshData()
        /**
         * 下拉刷新
         */
        refreshLayout.setOnRefreshListener { refreshLayout ->
            page = 1;
            listonRefresh(refreshLayout, recyclerView)
            loadData(adapter!!, page)
        }
        /**
         * 上拉加载
         */
        refreshLayout.setOnLoadMoreListener { refreshLayout ->
            page += 1;
            listOnLoadMore(refreshLayout, recyclerView)
            loadData(adapter!!, page)
        }
        initListView();
        loadData(adapter!!, page)

    }


    /**
     * 配置 上拉 下拉
     */
    protected fun setRefreshData() {
        refreshLayout.setEnableRefresh(isEnableRefresh())
        refreshLayout.setEnableLoadMore(isEnableLoadMore())
    }

    /**
     * 是否启用下拉刷新
     */
    abstract fun isEnableRefresh(): Boolean

    /**
     * 是否启用上拉加载
     */
    abstract fun isEnableLoadMore(): Boolean


    /**
     *清理数据
     */
    protected fun clearData() {
        if (recyclerView != null && recyclerView.childCount > 0) {
            recyclerView.removeAllViews()
        }
        if (adapter != null && adapter?.itemCount!! > 0) {
            adapter?.data?.clear()
            adapter?.notifyDataSetChanged()
        }
    }


    /**
     * 刷新完成
     */
    protected fun finishRefresh() {
        refreshLayout.finishRefresh()
    }

    /**
     * 加载完成
     */
    protected fun finishLoadMore() {
        refreshLayout.finishLoadMore()
    }

    protected fun finishState() {
        finishRefresh();
        finishLoadMore();
    }

    /**
     * 无更多数据
     */
    protected fun noMoreData(state: Boolean) {
        if (state) {
            refreshLayout.finishRefreshWithNoMoreData()
        }
    }

    /**
     * 是否启用上拉加载
     */
    open fun setLoadMoreState(state: Boolean) {
        refreshLayout.setEnableLoadMore(state)
    }

    /**
     * 是否启用下拉刷新
     */
    open fun setRefreshState(state: Boolean) {
        refreshLayout.setEnableRefresh(state)
    }

    /**
     * 获取recyClerView
     */
    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    fun setAutoLoad(state: Boolean) {
        refreshLayout.setEnableAutoLoadMore(state)
    }

    /**
     * 初始化视图
     */
    protected abstract fun initListView()

    /**
     * 初始化adapter
     */
    protected abstract fun initAdapter(): Any

    /**
     * 加载数据
     */
    protected abstract fun loadData(adapter: BaseAdapter<Any>, page: Int)

    /**
     * 下拉刷新
     */
    protected abstract fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView)

    /**
     * 上拉加载
     */
    protected abstract fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView)
}