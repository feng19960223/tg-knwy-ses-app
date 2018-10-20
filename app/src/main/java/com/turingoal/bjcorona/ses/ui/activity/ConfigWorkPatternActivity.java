package com.turingoal.bjcorona.ses.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgAppConfig;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.bean.WorkMode;
import com.turingoal.bjcorona.ses.constant.ConstantAddress;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.net.TgModbus4jUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择工作模式界面
 */
@Route(path = TgArouterPaths.CONFIG_WORK_PATTERN)
public class ConfigWorkPatternActivity extends TgBaseActivity implements Runnable {
    private static final long serialVersionUID = 1L;
    @BindView(R.id.iv_common_mode)
    ImageView commonMode; // 通用模式
    @BindView(R.id.iv_economic_mode)
    ImageView economicMode;  // 经济模式
    @BindView(R.id.iv_spare_mode)
    ImageView spareMode;  // 备用模式
    @BindView(R.id.common_mode)
    RadioButton rbCommonMode; // 通用模式按钮
    @BindView(R.id.economic_mode)
    RadioButton rbEconomicMode;  // 经济模式按钮
    @BindView(R.id.spare_mode)
    RadioButton rbSpareMode;  // 备用模式按钮
    @BindView(R.id.btn_cancle)
    Button btnCancle; // 上一步按钮
    @BindView(R.id.btn_next)
    Button btnNext; // 下一步按钮
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_work_pattern;
    }

    @Override
    protected void initialized() {
        btnNext.setText("下一步");
        btnCancle.setText("上一步");
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示数据
     */
    private void showData(final short workMode) {
        if (workMode == 0) { // 通用模式
            rbCommonMode.setChecked(true); // 选中通用模式
            commonMode.setImageResource(R.mipmap.common_mode_checked); // 选中通用模式
            commonMode.setBackgroundResource(R.drawable.background_line); // 选中通用模式
            economicMode.setImageResource(R.mipmap.economical_mode);
            economicMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
            spareMode.setImageResource(R.mipmap.spare_mode);
            spareMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (workMode == 1) { // 经济模式
            rbEconomicMode.setChecked(true); // 选中经济模式
            economicMode.setImageResource(R.mipmap.economical_mode_checked); // 选中经济模式
            economicMode.setBackgroundResource(R.drawable.background_line); // 选中经济模式
            commonMode.setImageResource(R.mipmap.common_mode);
            commonMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
            spareMode.setImageResource(R.mipmap.spare_mode);
            spareMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (workMode == 2) { // 备用模式
            rbSpareMode.setChecked(true); // 选中备用模式
            spareMode.setImageResource(R.mipmap.spare_mode_checked); // 选中备用模式
            spareMode.setBackgroundResource(R.drawable.background_line); // 选中备用模式
            commonMode.setImageResource(R.mipmap.common_mode);
            commonMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
            economicMode.setImageResource(R.mipmap.economical_mode);
            economicMode.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    /**
     * 获得数据
     */
    private short getData() {
        short workMode = 0; // 现在设置的模式
        try {
            workMode = (short) TgModbus4jUtil.readHoldingRegister(master, TgAppConfig.INVERTER, ConstantAddress.GET_PATTERN_OFFECT, DataType.TWO_BYTE_INT_SIGNED);
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
        return workMode;
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.btn_cancle, R.id.btn_next, R.id.iv_common_mode, R.id.iv_economic_mode, R.id.iv_spare_mode})
    public void onClick(final View v) { //根据选择的模式来进行下一步设置，如果是通用模式直接进入最后设置页面。如果是经济模式则需要进入设置时间的页面
        WorkMode workMode = new WorkMode();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_next: // 点击下一步
                if (rbCommonMode.isChecked()) { // 通用模式
                    workMode.setMode(0);
                    // TgSystemHelper.handleIntentWithObj(TgActivityPaths.CONFIG_LAST_SETUP, "workMode", workMode);
                    intent.setClass(ConfigWorkPatternActivity.this, ConfigLastSetupActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workMode", workMode); //序列化
                    intent.putExtras(bundle); //发送数据
                    startActivity(intent); //启动intent
                } else if (rbEconomicMode.isChecked()) { // 经济模式
                    workMode.setMode(1);
                    // TgSystemHelper.handleIntentWithObj(TgActivityPaths.CONFIG_WORK_PATTERN_ECONOMIC, "workMode", workMode);
                    intent.setClass(ConfigWorkPatternActivity.this, ConfigWorkPatternEconomicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workMode", workMode); //序列化
                    intent.putExtras(bundle); //发送数据
                    startActivity(intent); //启动intent
                } else if (rbSpareMode.isChecked()) { // 备用模式
                    workMode.setMode(2);
                    // TgSystemHelper.handleIntentWithObj(TgActivityPaths.CONFIG_LAST_SETUP, "workMode", workMode);
                    intent.setClass(ConfigWorkPatternActivity.this, ConfigLastSetupActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workMode", workMode); //序列化
                    intent.putExtras(bundle); //发送数据
                    startActivity(intent); //启动intent
                } else {
                    TgToastUtil.showShort("请检查设备是否连接！");
                }


                break;
            case R.id.iv_common_mode: // 选择通用模式
                showData((short) 0);
                break;
            case R.id.iv_economic_mode: // 选择经济模式
                showData((short) 1);
                break;
            case R.id.iv_spare_mode: // 选择备用模式
                showData((short) 2);
                break;
            case R.id.btn_cancle:
                defaultFinish(); // 取消
            default:
                defaultFinish();
                break;
        }
    }

    /**
     * 数据获得线程
     */
    @Override
    public void run() {
        final short workMode = getData(); // 获得数据
        // runOnUiThread(Runnable action)切换到主线程更新ui。
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showData(workMode); // 显示数据
            }
        });
    }
}
