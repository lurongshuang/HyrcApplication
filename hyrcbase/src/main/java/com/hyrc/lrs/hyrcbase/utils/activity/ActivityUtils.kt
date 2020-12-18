package com.hyrc.lrs.hyrcbase.utils.activity

import android.app.Activity
import com.hyrc.lrs.hyrcbase.base.BaseActivity
import java.util.*

/**
 * @description 作用:
 * @date: 2020/7/17
 * @author: 卢融霜
 */
class ActivityUtils {
    private val activityMap: MutableMap<String, BaseActivity> = HashMap()

    /**
     * 保存指定key值的activity（activity启动时调用）
     */
    fun addActivity(key: String, activity: BaseActivity) {
        if (activityMap[key] == null) {
            activityMap[key] = activity
        }
    }

    /**
     * 移除指定key值的activity （activity关闭时调用）
     */
    fun delActivity(key: String?) {
        val activity: Activity? = activityMap[key]
        if (activity != null) {
            if (activity.isDestroyed || activity.isFinishing) {
                activityMap.remove(key)
                return
            }
            activity.finish()
            activityMap.remove(key)
        }
    }

    companion object {
        private var activityUtils: ActivityUtils? = null
        val instance: ActivityUtils?
            get() {
                if (activityUtils == null) {
                    activityUtils = ActivityUtils()
                }
                return activityUtils
            }
    }
}