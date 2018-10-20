package com.turingoal.bjcorona.ses.ui.activity;


import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgAppConfig;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.app.TgSystemHelper;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.bean.TgValidateResultBean;
import com.turingoal.common.android.util.ui.TgToastUtil;
import com.turingoal.common.android.validatror.TgUsernamePasswordValidator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 配置登录界面
 */
@Route(path = TgArouterPaths.CONFIG_LOGIN)
public class ConfigLoginActivity extends TgBaseActivity {
    @BindView(R.id.et_password)
    EditText etPassword; // 密码

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_login;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "登录"); // 顶部工具条
    }

    /**
     * 点击事件
     */
    @OnClick(R.id.btn_login)
    public void onClick(final View v) {
        String passWordStr = etPassword.getText().toString().trim(); // 密码
        TgValidateResultBean validateResult = TgUsernamePasswordValidator.validatePassword(passWordStr);  // 校验密码
        if (!validateResult.isSuccess()) {
            TgToastUtil.showShort(validateResult.getMsg());
        } else {
            if (passWordStr.equals(TgAppConfig.CONFIG_PASSWORD)) {
                TgApplication.getTgUserPreferences().setUserLoginStatus(true); // 保存登录状态为true
                TgSystemHelper.handleIntentAndFinish(TgArouterPaths.CONFIG_WORK_PATTERN, this); // 跳转到工作模式配置页面
            } else {
                TgToastUtil.showShort("密码错误，请重新输入！");
            }
        }
    }
}
