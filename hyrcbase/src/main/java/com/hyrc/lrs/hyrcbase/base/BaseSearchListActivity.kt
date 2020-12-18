package com.hyrc.lrs.hyrcbase.base

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyrc.lrs.hyrcbase.R
import com.hyrc.lrs.hyrcbase.utils.httpUtils.CallBackUtil
import com.hyrc.lrs.hyrcbase.utils.httpUtils.HyrcHttpUtil
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_search_base.*
import kotlinx.android.synthetic.main.include_search_head.*
import okhttp3.Call

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author: 卢融霜
 */
abstract class BaseSearchListActivity : BaseListActivity(), View.OnClickListener {


    override fun loadView(): Int {
        return R.layout.activity_search_base;
    }

    override fun initView() {
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = initAdapter(adapter)
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
                listOnLoadMore(refreshLayout, recyclerView)
                loadData(adapter, etSearch.text.toString(), false)
                hideKeyboard(etSearch)
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
     * 加载数据
     */
    private fun loadData(adapter: BaseAdapter<Any>?, title: String?, isNew: Boolean) {
        if (isNew) {
            page = 1;
        }
        if (page == 1 && adapter?.data?.size == 0) {
            loadBaseDialog?.show()
        }
        HyrcHttpUtil.http(getMethodType(), getUrl(), getParams(), object : CallBackUtil.CallBackString() {
            override fun onFailure(call: Call?, e: Exception?) {
                finishRefresh()
                finishLoadMore()
                loadBaseDialog?.dismiss()
                if (page == 1) {
                    showError()
                }
                failure(call, e)
            }

            override fun onResponse(response: String) {
                finishRefresh()
                finishLoadMore()
                loadBaseDialog?.dismiss()
                if (page == 1) {
                    clearData()
                }
                response(response);
            }

        })
    }
}