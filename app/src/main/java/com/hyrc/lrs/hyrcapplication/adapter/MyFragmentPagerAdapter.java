package com.hyrc.lrs.hyrcapplication.adapter;

import android.text.Html;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragment;
    private List<String> mTitleList;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();

    /**
     * 主页使用
     */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 接收首页传递的标题
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragment, List<String> titleList) {
        super(fm);
        this.mFragment = fragment;
        this.mTitleList = titleList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

    /**
     * 应该是创建Fragment的地方
     */
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment != null ? mFragment.size() : 3;
    }

    /**
     * 首页显示title，每日推荐等..
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null && position < mTitleList.size()) {
            return Html.fromHtml(mTitleList.get(position));
        } else {
            return "";
        }
    }
}
