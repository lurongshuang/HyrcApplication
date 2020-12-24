package com.hyrc.lrs.hyrcbase.base

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyrc.lrs.hyrcbase.R
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_list_base.*
import kotlinx.android.synthetic.main.activity_search_base.*
import kotlinx.android.synthetic.main.activity_search_base.recyclerView
import kotlinx.android.synthetic.main.activity_search_base.refreshLayout
import kotlinx.android.synthetic.main.include_search_head.*
import okhttp3.Call

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author: 卢融霜
 */
abstract class BaseSearchListActivity : BaseActivity(), View.OnClickListener {
    var page = 1;
    var adapter: BaseAdapter<Any>? = null;
    var layoutManager: RecyclerView.LayoutManager? = null;

    override fun loadView(): Int {
        return -1;
    }

    override fun setContentView(layoutRes: Int) {
        //顶层基类View
        val baseView = View.inflate(this, R.layout.activity_search_base, null)
        //子类view
        thisContentView = baseView.findViewById(R.id.refreshLayout)

        //加载动画
        loadingView = (baseView.findViewById<View>(R.id.vs_loading) as ViewStub).inflate()
        emptyView = (baseView.findViewById<View>(R.id.vs_empty) as ViewStub).inflate()
        errorView = (baseView.findViewById<View>(R.id.vs_error_refresh) as ViewStub).inflate()
        toolBar = baseView.findViewById(R.id.toolbarSearch)
        val img = loadingView?.findViewById<ImageView>(R.id.img_progress)
        // 加载动画
        mAnimationDrawable = img?.drawable as AnimationDrawable
        //隐藏view
        thisContentView?.visibility = View.GONE
        window.setContentView(baseView)
    }


    override fun initView() {
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = initAdapter() as BaseAdapter<Any>
        recyclerView.adapter = adapter
        setRefreshData()
        setToolBar(toolbarSearch, true, "搜索");
        llTextSearch.setOnClickListener(this)
        /**
         * 下拉刷新
         */
        refreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            if (etSearch.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                page = 1;
                loadData(adapter, etSearch.text.toString(), true)
                hideKeyboard(etSearch)
                listonRefresh(refreshLayout, recyclerView)
            } else {
                finishRefresh()
                showToast(hit)
            }
        })
        /**
         * 上拉加载
         */
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener { refreshLayout ->
            if (etSearch.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                page += 1;
                loadData(adapter, etSearch.text.toString(), false)
                hideKeyboard(etSearch)
                listOnLoadMore(refreshLayout, recyclerView)
            } else {
                finishLoadMore()
                showToast(hit)
            }
        })
        showContent();
        showKeyboard(etSearch)

        //键盘搜索按钮
        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.isEmpty()) {
                    showToast(hit)
                    return@OnEditorActionListener true
                }
                loadData(adapter, v.text.toString(), true)
                hideKeyboard(v)
                return@OnEditorActionListener true
            }
            false
        })

    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.llTextSearch) {
            if (etSearch.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                loadData(adapter, etSearch.text.toString(), true)
                hideKeyboard(etSearch)
            } else {
                showToast(hit)
            }
        }
    }


    /**
     * 设置搜素框提示
     *
     * @param title 提示名称
     */
    open fun setSearchHit(title: String) {
        etSearch.hint = title
        hit = title
    }

    var hit = "请输入名称"

    open fun getSearchText(): String? {
        return etSearch.editableText.toString();
    }

    /**
     * 配置 上拉 下拉
     */
    protected fun setRefreshData() {
        refreshLayout.setEnableRefresh(isEnableRefresh())
        refreshLayout.setEnableLoadMore(isEnableLoadMore())
    }

    /**
     * 加载数据
     */
    abstract fun loadData(adapter: BaseAdapter<Any>?, title: String?, isNew: Boolean)

    /**
     * 初始化adapter
     */
    protected abstract fun initAdapter(): Any

    /**
     * 是否启用下拉刷新
     */
    abstract fun isEnableRefresh(): Boolean

    /**
     * 是否启用上拉加载
     */
    abstract fun isEnableLoadMore(): Boolean

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

    /**
     * 上拉加载
     */
    protected abstract fun listOnLoadMore(refreshLayout: RefreshLayout, recyclerView: RecyclerView)

    /**
     * 下拉刷新
     */
    protected abstract fun listonRefresh(refreshLayout: RefreshLayout, recyclerView: RecyclerView)

    protected fun finishState() {
        finishRefresh();
        finishLoadMore();
    }

    override fun onErrorRefresh() {
        loadData(adapter, getSearchText(), true);
    }


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
     * 无更多数据
     */
    protected fun noMoreData(state: Boolean) {
        if (state) {
            refreshLayout.finishRefreshWithNoMoreData()
        }
    }

}