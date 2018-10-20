package com.turingoal.bjcorona.ses.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.bean.WorkMode;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.net.TgModbus4jUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 根据模式设定时间段
 */
@Route(path = TgArouterPaths.CONFIG_WORK_PATTERN_ECONOMIC)
public class ConfigWorkPatternEconomicActivity extends TgBaseActivity {
    @BindView(R.id.tp_startcharge)
    TimePicker tpStartCharge; // 充电开始时间
    @BindView(R.id.tp_endcharge)
    TimePicker tpEndCharge; // 充电结束时间
    @BindView(R.id.tp_startdischarge)
    TimePicker tpStartDischarge; // 放电开始时间
    @BindView(R.id.tp_enddischarge)
    TimePicker tpEndDischarge; // 放电结束时间
    @BindView(R.id.et_charge_power_percentage)
    EditText etChargePowerPercentage; // 充电功率百分比
    int chargePowerPercentage;
    @BindView(R.id.et_discharge_power_percentage)
    EditText etDischargePowerPercentage; // 放电功率百分比
    int dischargePowerPercentage;
    @BindView(R.id.btn_cancle)
    Button btnCancle; // 上一步
    @BindView(R.id.btn_next)
    Button btnNext; // 下一步
    // @Autowired
    WorkMode workMode;
    private int startChargeTime; // 充电开始时间
    private int endChargeTime; // 充电结束时间
    private int startDischargeTime; // 放电开始时间
    private int endDischargeTime; // 放电结束时间

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_work_pattern_economic;
    }

    @Override
    protected void initialized() {
        btnCancle.setText("上一步");
        btnNext.setText("下一步");
        Intent intent = this.getIntent();
        workMode = (WorkMode) intent.getSerializableExtra("workMode");
        tpStartCharge.setIs24HourView(true); // 设置成24小时制
        tpEndCharge.setIs24HourView(true); // 设置成24小时制
        tpStartDischarge.setIs24HourView(true); // 设置成24小时制
        tpEndDischarge.setIs24HourView(true); // 设置成24小时制
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23 , android 6
            tpStartCharge.setHour(0);
            tpStartCharge.setMinute(0);
            tpEndCharge.setHour(0);
            tpEndCharge.setMinute(0);
            tpStartDischarge.setHour(0);
            tpStartDischarge.setMinute(0);
            tpEndDischarge.setHour(0);
            tpEndDischarge.setMinute(0);
        } else { // 低于API 23版本
            tpStartCharge.setCurrentHour(0);
            tpStartCharge.setCurrentMinute(0);
            tpEndCharge.setCurrentHour(0);
            tpEndCharge.setCurrentMinute(0);
            tpStartDischarge.setCurrentHour(0);
            tpStartDischarge.setCurrentMinute(0);
            tpEndDischarge.setCurrentHour(0);
            tpEndDischarge.setCurrentMinute(0);
        }
    }

    /**
     * 获取时间参数
     */
    public void getTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23 , android 6
            startChargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue((short) tpStartCharge.getHour(), (short) tpStartCharge.getMinute());
            endChargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue((short) tpEndCharge.getHour(), (short) tpEndCharge.getMinute());
            startDischargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue((short) tpStartDischarge.getHour(), (short) tpStartDischarge.getMinute());
            endDischargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue((short) tpEndDischarge.getHour(), (short) tpEndDischarge.getMinute());
        } else { // 低于API 23版本
            startChargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue(tpStartCharge.getCurrentHour().shortValue(), tpStartCharge.getCurrentMinute().shortValue());
            endChargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue(tpEndCharge.getCurrentHour().shortValue(), tpEndCharge.getCurrentMinute().shortValue());
            startDischargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue(tpStartDischarge.getCurrentHour().shortValue(), tpStartDischarge.getCurrentMinute().shortValue());
            endDischargeTime = TgModbus4jUtil.twoByteHexSplit2ShortValue(tpEndDischarge.getCurrentHour().shortValue(), tpEndDischarge.getCurrentMinute().shortValue());
        }
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.btn_next, R.id.btn_cancle})
    public void onClick(final View v) {
        getTime(); // 获取时间参数
        switch (v.getId()) {
            case R.id.btn_next:
                //参数放入bean中 传到下一个界面
                String chargePowerPercentageStr = etChargePowerPercentage.getText().toString().trim(); // 充电功率百分比
                String dischargePowerPercentageStr = etDischargePowerPercentage.getText().toString().trim(); // 放电功率百分比
                if (!TextUtils.isEmpty(chargePowerPercentageStr) && !TextUtils.isEmpty(dischargePowerPercentageStr)) {
                    chargePowerPercentage = Integer.parseInt(chargePowerPercentageStr); // 充电功率百分比
                    dischargePowerPercentage = Integer.parseInt(dischargePowerPercentageStr); // 放电功率百分比
                    if (chargePowerPercentage > 0 && chargePowerPercentage <= 100 && dischargePowerPercentage > 0 && dischargePowerPercentage <= 100) { //判断百分比输入是否正确
                        workMode.setStartChargeTime(startChargeTime); // 充电开始时间
                        workMode.setEndChargeTime(endChargeTime); // 充电结束时间
                        workMode.setStartDischargeTime(startDischargeTime); // 放电开始时间
                        workMode.setEndDischargeTime(endDischargeTime); // 放电结束时间
                        workMode.setChargePowerPercentage(chargePowerPercentage); // 充电功率百分比
                        workMode.setDisChargePowerPercentage(dischargePowerPercentage); // 放电功率百分比
                        // TgSystemHelper.handleIntentAndFinishWithObj(TgActivityPaths.CONFIG_LAST_SETUP, "workMode", workMode, ConfigWorkPatternEconomicActivity.this);
                        Intent intent = new Intent();
                        intent.setClass(ConfigWorkPatternEconomicActivity.this, ConfigLastSetupActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("workMode", workMode); //序列化
                        intent.putExtras(bundle); //发送数据
                        startActivity(intent); //启动intent
                        this.finish();
                    } else {
                        TgToastUtil.showShort("请检查输入的百分比设置是否正确！");
                    }
                } else {
                    TgToastUtil.showShort("百分比设置不能为空！");
                }
                break;
            case R.id.btn_cancle:
                defaultFinish(); //关闭当前页面
                break;
            default:
                break;
        }
    }
}
