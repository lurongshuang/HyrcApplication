package com.hyrc.lrs.hyrcbase.base

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hyrc.lrs.hyrcbase.R

/**
 * @description 作用:
 * @date: 2020/12/15
 * @author:
 */
abstract class BaseFragment : Fragment() {
    protected var baseActivity: BaseActivity? = null

    // 布局view
    protected var bindingView: View? = null

    // fragment是否显示了
    protected var mIsVisible = false

    // 加载中
    private var loadingView: View? = null

    // 加载失败
    private var errorView: View? = null

    // 空布局
    private var emptyView: View? = null

    // 动画
    private var mAnimationDrawable: AnimationDrawable? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_base, null)
        bindingView = View.inflate(activity, setContent(), null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        bindingView?.layoutParams = params
        val mContainer = inflate.findViewById<RelativeLayout>(R.id.container)
        mContainer.addView(bindingView)
        bindingView?.visibility = View.GONE
        return inflate
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

    protected abstract fun onInvisible()
    protected abstract fun onVisible()
    protected fun <T : View?> getView(id: Int): T {
        return view!!.findViewById<View>(id) as T
    }

    /**
     * 初始化View
     */
    protected abstract fun initView()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLoading()
        initView()
    }

    /**
     * 布局
     */
    abstract fun setContent(): Int

    /**
     * 加载失败后点击后的操作
     */
    protected fun onRefresh() {}

    /**
     * 显示加载中状态
     */
    protected fun showLoading() {
        val viewStub = getView<ViewStub>(R.id.vs_loading)
        if (viewStub != null) {
            loadingView = viewStub.inflate()
            val img = loadingView?.findViewById<ImageView>(R.id.img_progress)
            mAnimationDrawable = img?.drawable as AnimationDrawable
        }
        if (loadingView != null && loadingView!!.visibility != View.VISIBLE) {
            loadingView!!.visibility = View.VISIBLE
        }
        // 开始动画
        if (mAnimationDrawable != null && !mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }
        if (bindingView!!.visibility != View.GONE) {
            bindingView!!.visibility = View.GONE
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (emptyView != null && emptyView!!.visibility != View.GONE) {
            emptyView!!.visibility = View.GONE
        }
    }

    /**
     * 加载完成的状态
     */
    protected fun showContent() {
        if (bindingView!!.visibility != View.VISIBLE) {
            bindingView!!.visibility = View.VISIBLE
        }
        if (loadingView != null && loadingView!!.visibility != View.GONE) {
            loadingView!!.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (emptyView != null && emptyView!!.visibility != View.GONE) {
            emptyView!!.visibility = View.GONE
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    protected fun showError() {
        val viewStub = getView<ViewStub>(R.id.vs_error_refresh)
        if (viewStub != null) {
            errorView = viewStub.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener(View.OnClickListener {
                showLoading()
                onRefresh()
            })
        }
        if (errorView != null) {
            errorView!!.visibility = View.VISIBLE
        }
        if (loadingView != null && loadingView!!.visibility != View.GONE) {
            loadingView!!.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (bindingView!!.visibility != View.GONE) {
            bindingView!!.visibility = View.GONE
        }
        if (emptyView != null && emptyView!!.visibility != View.GONE) {
            emptyView!!.visibility = View.GONE
        }
    }

    protected fun showEmpty(text: String? = "") {
        // 需要这样处理，否则二次显示会失败
        val viewStub = getView<ViewStub>(R.id.vs_empty)
        if (viewStub != null) {
            emptyView = viewStub.inflate()
            if (text!!.isNotEmpty()) {
                (emptyView?.findViewById<View>(R.id.tv_tip_empty) as TextView).text = text
            }
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.VISIBLE
        }
        if (loadingView != null && loadingView!!.visibility != View.GONE) {
            loadingView!!.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (bindingView != null && bindingView!!.visibility != View.GONE) {
            bindingView!!.visibility = View.GONE
        }
    }
}