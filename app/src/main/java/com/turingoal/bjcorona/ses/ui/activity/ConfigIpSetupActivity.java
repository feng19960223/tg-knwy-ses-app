package com.turingoal.bjcorona.ses.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.bean.TgValidateResultBean;
import com.turingoal.common.android.util.ui.TgDialogUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;
import com.turingoal.common.android.validatror.TgNetValidator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 配置IP界面
 */
@Route(path = TgArouterPaths.CONFIG_IP_SETUP)
public class ConfigIpSetupActivity extends TgBaseActivity {
    @BindView(R.id.et_ip)
    EditText etIp; // 设备IP设置
    @BindView(R.id.et_port)
    EditText etPort; // 设备端口配置

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_ip_setup;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "设备IP配置"); // 顶部工具条
        String ip = TgApplication.getTgUserPreferences().getIp();
        if (!TextUtils.isEmpty(ip)) {
            etIp.setText(ip); // 如果IP已设置显示进输入框
        }
        Integer port = TgApplication.getTgUserPreferences().getPort();
        if (port != null && port > 0) {
            etPort.setText(port + ""); // 如果端口已设置显示进输入框
        }
    }

    /**
     * 点击事件
     */
    @OnClick(R.id.btn_confirm)
    public void onClick(final View v) {
        final String ip = etIp.getText().toString().trim(); // ip
        final String port = etPort.getText().toString().trim(); // 端口
        TgValidateResultBean validateResult = TgNetValidator.validateIpAndPort(ip, port);  // 校验IP地址和端口
        if (!validateResult.isSuccess()) {
            TgToastUtil.showShort(validateResult.getMsg());
        } else {
            TgDialogUtil.showWarningDialog(this, "提示", "设置IP端口后需退出后重新打开应用后生效！确认退出程序吗？", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(final MaterialDialog dialog, final DialogAction which) {
                    TgApplication.getTgUserPreferences().setIp(ip); //将IP存入SP
                    TgApplication.getTgUserPreferences().setPort(Integer.parseInt(port)); //将端口号存入SP
                    TgApplication.getMaster().destroy(); // 销毁modbus master
                    TgApplication.clearActivitys(); // 清除Activity栈
                    System.exit(0); //退出程序
                }
            });
        }
    }
}
