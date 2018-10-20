package com.turingoal.bjcorona.ses.app;

import android.support.v4.app.Fragment;

import com.turingoal.bjcorona.ses.ui.fragment.HomeFragment;
import com.turingoal.bjcorona.ses.ui.fragment.ParameterFragment;
import com.turingoal.bjcorona.ses.ui.fragment.SetupFragment;

import java.util.HashMap;

/**
 * 主页面Fragment工厂
 */
public class TgMainFragmentFactory {
    public static final int FRAGMENT_HOME = 0; // 首页Fragment
    public static final int FRAGMENT_PARAMETER = 1;// 参数Fragment
    public static final int FRAGMENT_SETUP = 2; // 设置Fragment
    private static Fragment fragment; // fragment
    private static HashMap<Integer, Fragment> mFragments = new HashMap<>(); //Fragment缓存

    /**
     * 根据类型创建Fragment
     */
    public static Fragment createFragment(final int type) {
        fragment = mFragments.get(type);
        if (fragment == null) {
            switch (type) {
                case FRAGMENT_HOME:
                    fragment = new HomeFragment(); // 首页Fragment
                    break;
                case FRAGMENT_PARAMETER:
                    fragment = new ParameterFragment();  // 参数Fragment
                    break;
                case FRAGMENT_SETUP:
                    fragment = new SetupFragment(); // 设置Fragment
                    break;
                default:
                    break;
            }
            if (fragment != null) {
                mFragments.put(type, fragment); // 如果新new了Fragment，加到缓存中
            }
        }
        return fragment;
    }
}
