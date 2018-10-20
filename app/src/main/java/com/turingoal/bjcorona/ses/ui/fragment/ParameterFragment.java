package com.turingoal.bjcorona.ses.ui.fragment;

import android.view.View;

import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.app.TgSystemHelper;
import com.turingoal.common.android.base.TgBaseFragment;

import butterknife.OnClick;

/**
 * 参数页面fragment
 */
public class ParameterFragment extends TgBaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.app_fragment_parameter;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, "参数"); // 顶部工具条
    }

    /**
     * onClick
     */
    @OnClick({R.id.output_parameter, R.id.inverter_parameter, R.id.load_parameter, R.id.panel_parameter, R.id.battery_parameter})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.output_parameter:
                TgSystemHelper.handleIntent(TgArouterPaths.PARAMETER_POWER_GRID); //跳转到电网参数
                break;
            case R.id.inverter_parameter:
                TgSystemHelper.handleIntent(TgArouterPaths.PARAMETER_INVERTER); //跳转到逆变器
                break;
            case R.id.load_parameter:
                TgSystemHelper.handleIntent(TgArouterPaths.PARAMETER_LOAD); //跳转到负载
                break;
            case R.id.panel_parameter:
                TgSystemHelper.handleIntent(TgArouterPaths.PARAMETER_PANEL); //跳转到面板
                break;
            case R.id.battery_parameter:
                TgSystemHelper.handleIntent(TgArouterPaths.PARAMETER_BATTERY); //跳转到电池
                break;
            default:
                break;
        }
    }
}
