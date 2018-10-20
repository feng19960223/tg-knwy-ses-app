package com.turingoal.bjcorona.ses.ui.activity.common;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgMainFragmentFactory;
import com.turingoal.bjcorona.ses.app.TgSystemHelper;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.ui.adapter.TgMainTabAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 程序主页面
 */
@Route(path = TgArouterCommonPaths.MAIN_INDEX)
public class TgMainActivity extends TgBaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager; // viewPager
    @BindView(R.id.tablayout)
    TabLayout tabLayout; // tab 布局

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_main_tab;
    }

    @Override
    protected void initialized() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>(); // fragments
        fragments.add(TgMainFragmentFactory.createFragment(TgMainFragmentFactory.FRAGMENT_HOME));  // 首页Fragment
        fragments.add(TgMainFragmentFactory.createFragment(TgMainFragmentFactory.FRAGMENT_PARAMETER)); // 参数Fragment
        fragments.add(TgMainFragmentFactory.createFragment(TgMainFragmentFactory.FRAGMENT_SETUP)); // 设置Fragment
        int[] images = {R.drawable.home_selector, R.drawable.parameter_selector, R.drawable.setup_selector}; // 图标
        TgMainTabAdapter adapter = new TgMainTabAdapter(getSupportFragmentManager(), fragments, images);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置自定义视图
        for (int i = 0, size = tabLayout.getTabCount(); i < size; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i, getBaseContext()));
        }
    }

    /**
     * 点击返回按钮
     */
    @Override
    public void onBackPressed() {
        TgSystemHelper.dbClickExit(); //  再按一次退出系统
    }
}
