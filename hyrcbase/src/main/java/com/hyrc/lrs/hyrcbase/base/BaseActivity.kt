package com.hyrc.lrs.hyrcbase.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hyrc.lrs.hyrcbase.R
import com.hyrc.lrs.hyrcbase.base.BaseApplication.Companion.context
import com.hyrc.lrs.hyrcbase.utils.CommonUtils
import com.hyrc.lrs.hyrcbase.utils.StatusBarUtil
import com.hyrc.lrs.hyrcbase.utils.activity.ActivityUtils
import com.hyrc.lrs.hyrcbase.utils.thread.ThreadUtils
import com.hyrc.lrs.hyrcbase.view.LoadBaseDialog

/**
 * @description 作用: Activity 顶层
 * @date: 2020/12/14
 * @author:
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var errorView: View? = null
    protected var loadingView: View? = null
    protected var emptyView: View? = null
    protected var toolBar: Toolbar? = null
    protected var mAnimationDrawable: AnimationDrawable? = null
    var thisContentView: View? = null
    private var myBroadCast: MyBroadCast? = null
    var BId: String? = null
    var loadBaseDialog: LoadBaseDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(loadView())
        BId = context!!.applicationInfo.processName
        loadBaseDialog = LoadBaseDialog(this)
        setBroadCast()
        //初始化状态栏
        initStatusBar()
        //设置bar
        setNoTitle()
        showLoading()
        //初始化视图
        initView()
    }

    private fun setBroadCast() {
        //退出监听
        myBroadCast = MyBroadCast()
        val intentFilter = IntentFilter(BId)
        registerReceiver(myBroadCast, intentFilter)
    }

    private fun initStatusBar() {
        // 设置透明状态栏，兼容4.4
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorPrimary), 0)
    }

    override fun setContentView(layoutResID: Int) {
        //顶层基类View
        val baseView = View.inflate(this, R.layout.activity_base, null)
        //子类view
        thisContentView = View.inflate(this, layoutResID, null)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        thisContentView?.setLayoutParams(params)
        val mContainer = baseView.findViewById<RelativeLayout>(R.id.container)

        //加载动画
        loadingView = (mContainer.findViewById<View>(R.id.vs_loading) as ViewStub).inflate()
        emptyView = (mContainer.findViewById<View>(R.id.vs_empty) as ViewStub).inflate()
        errorView = (mContainer.findViewById<View>(R.id.vs_error_refresh) as ViewStub).inflate()
        toolBar = baseView.findViewById(R.id.tool_bar)
        val img = loadingView?.findViewById<ImageView>(R.id.img_progress)
        // 加载动画
        mAnimationDrawable = img?.drawable as AnimationDrawable
        //隐藏view
        thisContentView?.setVisibility(View.GONE)
        //添加进去
        mContainer.addView(thisContentView)
        window.setContentView(baseView)
    }

    /**
     * 设置titlebar
     */
    protected fun setToolBar(isShowBack: Boolean, title: String?): Toolbar? {
        if (toolBar == null) {
            return null;
        }
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar != null && isShowBack) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back)
        }
        toolBar!!.setNavigationOnClickListener { supportFinishAfterTransition() }
        if (title != null) {
            toolBar!!.title = title
        }
        if (toolBar?.visibility != View.VISIBLE) {
            toolBar?.visibility = View.VISIBLE;
        }

        return toolBar;
    }

    protected fun setToolBar(tb: Toolbar, isShowBack: Boolean, title: String?) {
        toolBar = tb
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        if (actionBar != null && isShowBack) {
            //去除默认Title显示
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back)
        }
        toolBar!!.setNavigationOnClickListener { supportFinishAfterTransition() }
        if (title != null) {
            toolBar!!.title = title
            actionBar?.setDisplayShowTitleEnabled(false)
        } else {
            actionBar?.setDisplayShowTitleEnabled(true)
        }
        actionBar?.setDisplayShowTitleEnabled(false)
        if (toolBar?.visibility != View.VISIBLE) {
            toolBar?.visibility = View.VISIBLE;
        }

    }


    override fun setTitle(text: CharSequence) {
        toolBar!!.title = text
    }

    /**
     * 设置头部名称
     */
    protected fun setNoTitle() {
        toolBar!!.visibility = View.GONE
    }

    /**
     * 显示加载中
     */
    protected fun showLoading() {
        if (loadingView != null && loadingView!!.visibility != View.VISIBLE) {
            loadingView!!.visibility = View.VISIBLE
        }
        // 开始动画
        if (!mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.start()
        }
        if (thisContentView!!.visibility != View.GONE) {
            thisContentView!!.visibility = View.GONE
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE

        }
    }

    /**
     * 显示布局
     */
    protected fun showContent() {
        if (loadingView != null && loadingView!!.visibility != View.GONE) {
            loadingView!!.visibility = View.GONE
        }
        // 停止动画
        if (mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (errorView != null) {
            errorView!!.visibility = View.GONE
        }
        if (thisContentView!!.visibility != View.VISIBLE) {
            thisContentView!!.visibility = View.VISIBLE
        }
        if (emptyView != null) {
            emptyView!!.visibility = View.GONE

        }
    }

    /**
     * 显示加载失败布局
     */
    protected fun showError() {
        if (loadingView != null && loadingView!!.visibility != View.GONE) {
            loadingView!!.visibility = View.GONE
        }

        if (emptyView != null) {
            emptyView!!.visibility = View.GONE

        }
        // 停止动画
        if (mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
        if (errorView == null) {
            val viewStub = findViewById<View>(R.id.vs_error_refresh) as ViewStub
            errorView = viewStub.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener(View.OnClickListener {
                showLoading()
                onErrorRefresh()
            })
        } else {
            errorView!!.visibility = View.VISIBLE
        }
        if (thisContentView!!.visibility != View.GONE) {
            thisContentView!!.visibility = View.GONE
        }
    }


    protected fun showEmpty(text: String = "") {
        if (emptyView != null) {
            emptyView!!.visibility = View.VISIBLE
            if (text!!.isNotEmpty()) {
                (emptyView?.findViewById<View>(R.id.tv_tip_empty) as TextView).text = text
            }
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
        if (thisContentView != null && thisContentView!!.visibility != View.GONE) {
            thisContentView!!.visibility = View.GONE
        }
    }

    /**
     * 失败后点击刷新
     */
    protected abstract fun onErrorRefresh()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1f) {
            resources
        }
    }


    public override fun onDestroy() {
        super.onDestroy()
        ActivityUtils.instance?.delActivity(this.javaClass.name)
        if (myBroadCast != null) {
            unregisterReceiver(myBroadCast)
        }
        destroy()
    }

    /**
     * 退出
     */
    protected abstract fun destroy()

    /**
     * 获取布局
     *
     * @return view
     */
    protected abstract fun loadView(): Int

    /**
     * 初始化视图
     */
    protected abstract fun initView()

    /**
     * 提示信息  吐司
     */
    protected fun showToast(text: String?) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * 提示信息  吐司
     */
    protected fun showToast(id: Int) {
        Toast.makeText(applicationContext, id, Toast.LENGTH_SHORT).show()
    }

    /**
     * 提示信息  吐司
     */
    protected fun showToast(text: String?, loc: Boolean) {
        if (loc) {
            val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        } else {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 禁止改变字体大小
     */
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    /**
     * 退出广播
     */
    inner class MyBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val close = intent.getIntExtra("close", 0)
            if (close == 1) {
                ActivityUtils.instance?.delActivity(context.javaClass.name)
                finish()
            }
        }
    }

    /**
     * 显示键盘
     *
     * @param editText
     */
    fun showKeyboard(editText: EditText) {
        ThreadUtils.timeLapseHandler(1, object : ThreadUtils.OnNext {
            override fun accept() {
                editText.isFocusable = true
                editText.isFocusableInTouchMode = true
                editText.requestFocus()
                val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }
        });
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    fun hideKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /**
     * 跳转
     * @param cls 指定activity
     * @param bundle 参数
     */
    /**
     * 跳转
     * @param cls 指定activity
     */
    @JvmOverloads
    fun openActivity(cls: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 跳转
     * @param cls 指定 activity
     * @param fromId 标识
     * @param bundle 参数
     */
    fun openActivityForResult(cls: Class<*>?, fromId: Int, bundle: Bundle?) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, fromId)
    }
}