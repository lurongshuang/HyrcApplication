package com.hyrc.lrs.hyrcapplication.application;

import com.hyrc.lrs.hyrcbase.base.BaseApplication;
import com.xuexiang.xui.XUI;

/**
 * @description 作用:
 * @date: 2020/12/14
 * @author:
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this);
    }

}
