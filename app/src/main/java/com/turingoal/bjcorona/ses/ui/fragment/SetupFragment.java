package com.turingoal.bjcorona.ses.ui.fragment;

import android.view.View;

import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.app.TgSystemHelper;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseFragment;

import butterknife.OnClick;

/**
 * 设置页面fragment
 */

public class SetupFragment extends TgBaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.app_fragment_setup;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, "设置"); // 顶部工具条
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.base_setup, R.id.about_our, R.id.ip_setup})
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.base_setup:
                if (TgApplication.getTgUserPreferences().getUserLoginStatus()) { // 判断是否登录，如果已登录直接进入模式选择界面
                    TgSystemHelper.handleIntent(TgArouterPaths.CONFIG_WORK_PATTERN); // 跳转到模式选择界面
                } else {
                    TgSystemHelper.handleIntent(TgArouterPaths.CONFIG_LOGIN); // 跳转到登陆界面
                }
                break;
            case R.id.about_our:
                TgSystemHelper.handleIntent(TgArouterCommonPaths.COMMON_ABOUT); // 跳转到关于我们
                break;
            case R.id.ip_setup:
                TgSystemHelper.handleIntent(TgArouterPaths.CONFIG_IP_SETUP); //跳转到IP端口号设置页面
                break;
            default:
                break;
        }
    }
}