package com.turingoal.bjcorona.ses.ui.activity.common;

import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgAppConfig;
import com.turingoal.bjcorona.ses.app.TgSystemHelper;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseActivity;

/**
 * 欢迎页
 */
public class TgWelcomeActivity extends TgBaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_welcome;
    }

    @Override
    protected void initialized() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TgSystemHelper.handleIntentAndFinish(TgArouterCommonPaths.MAIN_INDEX, TgWelcomeActivity.this); // 跳转到主页面,关闭当前页面
            }
        }, TgAppConfig.WELCOME_DELAY_TIME); //设置延迟，再进入正式界面
    }
}
