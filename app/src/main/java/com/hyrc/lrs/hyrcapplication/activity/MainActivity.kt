package com.hyrc.lrs.hyrcapplication.activity


import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.hyrc.lrs.hyrcapplication.R
import com.hyrc.lrs.hyrcapplication.activity.fragment.MainFragment
import com.hyrc.lrs.hyrcapplication.activity.fragment.TwoFragment
import com.hyrc.lrs.hyrcapplication.activity.search.SearchActivity
import com.hyrc.lrs.hyrcapplication.adapter.MyFragmentPagerAdapter
import com.hyrc.lrs.hyrcbase.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_bottom_button.*

class MainActivity : BaseActivity() {
    override fun onErrorRefresh() {}

    public override fun destroy() {}

    public override fun loadView(): Int {
        return R.layout.activity_main
    }

    public override fun initView() {
        setToolBar(false, "首页")
        showContent()
        val mf = MainFragment();
        val mf1 = TwoFragment();
        val list = listOf(mf, mf1);
        val tile = listOf("1", "2")
        val ar = MyFragmentPagerAdapter(supportFragmentManager, list, tile);
        vpList.adapter = ar;

        vpList.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                navigation.selectedItemId = navigation.menu.getItem(position).itemId
            }

        })


        navigation.selectedItemId = R.id.navigation_main
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_main -> {
                    vpList.setCurrentItem(0, true)
                    return@OnNavigationItemSelectedListener true;
                }
                R.id.navigation_serve -> {
                    vpList.setCurrentItem(1, true)
                    return@OnNavigationItemSelectedListener true;
                }
            }
            false
        })
        navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            openActivity(SearchActivity::class.java)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

