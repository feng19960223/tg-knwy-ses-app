package com.turingoal.bjcorona.ses.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.RegexUtils;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgAppConfig;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.bean.WorkMode;
import com.turingoal.bjcorona.ses.constant.ConstantAddress;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.net.TgModbus4jUtil;
import com.turingoal.common.android.util.ui.TgDialogUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 最后设置页面.
 */
@Route(path = TgArouterPaths.CONFIG_LAST_SETUP)
public class ConfigLastSetupActivity extends TgBaseActivity implements Runnable {
    @BindView(R.id.et_charging_voltage)
    EditText etChargingVoltage; //充电电压设置
    short chargingVoltage;
    @BindView(R.id.et_charging_current)
    EditText etChargingCurrent; //充电电流设置
    short chargingCurrent;
    @BindView(R.id.et_charging_power)
    EditText etChargingPower; // 充电功率设置
    short chargingPower;
    @BindView(R.id.et_discharging_current)
    EditText etDischargingCurrent; //放电电流设置
    short dischargingCurrent;
    @BindView(R.id.et_discharging_power)
    EditText etDischargingPower; // 放电功率设置
    short dischargingPower;
    @BindView(R.id.et_inverter_output_ongrid_power)
    EditText etInverterOutputOngridPower; // 逆变器输出并网功率
    short inverterOutputOngridPower;
    // @Autowired
    WorkMode workMode;
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_last_setup;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, "最后设置"); // 顶部工具条
        Intent intent = this.getIntent();
        workMode = (WorkMode) intent.getSerializableExtra("workMode");
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据刷新线程
    }

    /**
     * 显示数据
     */
    private void showData(final BatchResults<String> results) {
        if (results != null) {
            // 充电电压设置
            if (results.getValue("chargingVoltage") != null) {
                etChargingVoltage.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("chargingVoltage")) / 10, 1));
            }
            // 充电电流
            if (results.getValue("chargingCurrent") != null) {
                etChargingCurrent.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("chargingCurrent")) / 10, 1));
            }
            // 充电功率
            if (results.getValue("chargingPower") != null) {
                etChargingPower.setText(results.getValue("chargingPower") + "");
            }
            // 放电电流
            if (results.getValue("dischargingCurrent") != null) {
                etDischargingCurrent.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("dischargingCurrent")) / 10, 1));
            }
            // 放电功率
            if (results.getValue("dischargingPower") != null) {
                etDischargingPower.setText(results.getValue("dischargingPower") + "");
            }
            // 逆变器输出并网功率
            if (results.getValue("inverterOutputOngridPower") != null) {
                etInverterOutputOngridPower.setText(results.getValue("inverterOutputOngridPower") + "");
            }
        }
    }

    /**
     * 获得数据
     */
    private BatchResults getData() {
        BatchResults<String> results = null;
        BatchRead<String> batch = new BatchRead<>();
        //batch.setContiguousRequests(true); // 默认false。设置为true，会分开发送指令，false会合并指令，数据过多会超出范围。数据量少的话设置为false提高效率，
        // 充电电压设置
        batch.addLocator("chargingVoltage", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 充电电流
        batch.addLocator("chargingCurrent", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 充电功率
        batch.addLocator("chargingPower", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 放电电流
        batch.addLocator("dischargingCurrent", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 放电功率
        batch.addLocator("dischargingPower", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 逆变器输出并网功率
        batch.addLocator("inverterOutputOngridPower", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_OUTPUT_ONGRID_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        batch.setContiguousRequests(true);
        try {
            results = master.send(batch);
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 数据获得线程
     */
    @Override
    public void run() {
        //try {
        // Thread.sleep(500); // 延迟500毫秒
        // } catch (InterruptedException e) {
        //  e.printStackTrace();
        //}
        final BatchResults<String> results = getData(); // 获得数据
        // runOnUiThread(Runnable action)切换到主线程更新ui。
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showData(results); // 显示数据
            }
        });
    }

    /**
     * 如果是通用模式不需要进行时间设置  直接获取输入值，传入进入设备中
     */
    private void setupParameterCommonMode() {
        // 在子线程中将数据设置进设备中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false; // 是否设置成功
                // 获取输入数据
                for (int i = 0; i < 3; i++) { // 为了确保设置成功，多设置几次
                    if (!result) {
                        try {
                            // 模式设置
                            boolean patternResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.GET_PATTERN_OFFECT, new short[]{(short) workMode.getMode()});
                            // 充电电压设置
                            boolean inverterChargeVoltageResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_VOLTAGE_OFFECT, new short[]{chargingVoltage});
                            // 充电电流设置
                            boolean inverterChargetCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_CURRENT_OFFECT, new short[]{chargingCurrent});
                            // 充电功率设置
                            boolean inverterChargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_POWER_OFFECT, new short[]{chargingPower});
                            // 放电电流设置
                            boolean inverterDischargeCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_CURRENT_OFFECT, new short[]{dischargingCurrent});
                            // 放电功率设置
                            boolean inverterDischargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_POWER_OFFECT, new short[]{dischargingPower});
                            // 逆变器离网功率设置
                            boolean inverterOutputOnGridPowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_OUTPUT_ONGRID_POWER_OFFECT, new short[]{inverterOutputOngridPower});
                            if (patternResult && inverterChargeVoltageResult && inverterChargetCurrentResult && inverterChargePowerResult && inverterDischargeCurrentResult && inverterDischargePowerResult && inverterOutputOnGridPowerResult) {
                                result = true; // 设置成功
                            }
                            // 需要在主线程弹出toast
                            if (result) {
                                // runOnUiThread(Runnable action)切换到主线程更新ui。
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置成功！");
                                        defaultFinish(); // 关闭
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置失败，请检查设备是否连接！");
                                    }
                                });
                            }
                        } catch (ModbusTransportException e) {
                            e.printStackTrace();
                        } catch (ModbusInitException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 如果是备用模式不需要进行时间设置  直接获取输入值，传入进入设备中
     */
    private void setupParameterSpareMode() {
        // 在子线程中将数据设置进设备中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false; // 是否设置成功
                // 获取输入数据
                for (int i = 0; i < 3; i++) { // 为了确保设置成功，多设置几次
                    if (!result) {
                        try {
                            // 模式设置
                            boolean patternResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.GET_PATTERN_OFFECT, new short[]{(short) workMode.getMode()});
                            // 充电电压设置
                            boolean inverterChargeVoltageResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_VOLTAGE_OFFECT, new short[]{chargingVoltage});
                            // 充电电流设置
                            boolean inverterChargetCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_CURRENT_OFFECT, new short[]{chargingCurrent});
                            // 充电功率设置
                            boolean inverterChargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_POWER_OFFECT, new short[]{chargingPower});
                            // 放电电流设置
                            boolean inverterDischargeCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_CURRENT_OFFECT, new short[]{dischargingCurrent});
                            // 放电功率设置
                            boolean inverterDischargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_POWER_OFFECT, new short[]{dischargingPower});
                            // 逆变器离网功率设置
                            boolean inverterOutputOnGridPowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_OUTPUT_ONGRID_POWER_OFFECT, new short[]{inverterOutputOngridPower});
                            if (patternResult && inverterChargeVoltageResult && inverterChargetCurrentResult && inverterChargePowerResult && inverterDischargeCurrentResult && inverterDischargePowerResult && inverterOutputOnGridPowerResult) {
                                result = true; // 设置成功
                            }
                            // 需要在主线程弹出toast
                            if (result) {
                                // runOnUiThread(Runnable action)切换到主线程更新ui。
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置成功！");
                                        defaultFinish(); // 关闭
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置失败，请检查设备是否连接！");
                                    }
                                });
                            }
                        } catch (ModbusTransportException e) {
                            e.printStackTrace();
                        } catch (ModbusInitException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 获取经济模式传入的时间，然后跟输入的数据一起传入进设备中
     */
    private void setupParameterEconomicMode() {
        // 在子线程中将数据设置进设备中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false; // 是否设置成功
                // 获取输入数据
                for (int i = 0; i < 3; i++) { // 为了确保设置成功，多设置几次
                    if (!result) {
                        try {
                            // 模式设置
                            boolean patternResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.GET_PATTERN_OFFECT, new short[]{(short) workMode.getMode()});
                            // 充电电压设置
                            boolean inverterChargeVoltageResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_VOLTAGE_OFFECT, new short[]{chargingVoltage});
                            // 充电电流设置
                            boolean inverterChargetCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_CURRENT_OFFECT, new short[]{chargingCurrent});
                            // 充电功率设置
                            boolean inverterChargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_CHARGE_POWER_OFFECT, new short[]{chargingPower});
                            // 放电电流设置
                            boolean inverterDischargeCurrentResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_CURRENT_OFFECT, new short[]{dischargingCurrent});
                            // 放电功率设置
                            boolean inverterDischargePowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_DISCHARGE_POWER_OFFECT, new short[]{dischargingPower});
                            // 逆变器离网功率设置
                            boolean inverterOutputOnGridPowerResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_INVERTER_OUTPUT_ONGRID_POWER_OFFECT, new short[]{inverterOutputOngridPower});
                            // 充电开始时间
                            boolean startChargeResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_START_CHARGE_OFFECT, new short[]{(short) workMode.getStartChargeTime()});
                            // 充电结束时间
                            boolean endChargeResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_END_CHARGE_OFFECT, new short[]{(short) workMode.getEndChargeTime()});
                            // 放电开始时间
                            boolean startDischargeResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_START_DISCHARGE_OFFECT, new short[]{(short) workMode.getStartDischargeTime()});
                            // 放电结束时间
                            boolean endDischargeResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_END_DISCHARGE_OFFECT, new short[]{(short) workMode.getEndDischargeTime()});
                            // 充电电功率百分比
                            boolean chargePowerPercentageResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_CHARGE_POWER_PERCENTAGE_OFFECT, new short[]{(short) workMode.getChargePowerPercentage()});
                            // 放电功率百分比
                            boolean disChargePowerPercentageResult = TgModbus4jUtil.writeRegisters(TgApplication.getMaster(), TgAppConfig.INVERTER, ConstantAddress.SET_DISCHARGE_POWER_PERCENTAGE_OFFECT, new short[]{(short) workMode.getDisChargePowerPercentage()});
                            if (patternResult && inverterChargeVoltageResult && inverterChargetCurrentResult && inverterChargePowerResult && inverterDischargeCurrentResult && inverterDischargePowerResult && inverterOutputOnGridPowerResult && startChargeResult && endChargeResult && startDischargeResult && endDischargeResult && chargePowerPercentageResult && disChargePowerPercentageResult) {
                                result = true; // 设置成功
                            }
                            // 需要在主线程弹出toast
                            if (result) {
                                // runOnUiThread(Runnable action)切换到主线程更新ui。
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置成功！");
                                        defaultFinish(); // 关闭
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TgToastUtil.showShort("设置失败，请检查设备是否连接！");
                                    }
                                });
                            }
                        } catch (ModbusTransportException e) {
                            e.printStackTrace();
                        } catch (ModbusInitException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 点击事件.
     */
    @OnClick({R.id.btn_next, R.id.btn_cancle})
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                defaultFinish(); // 关闭
                break;
            case R.id.btn_next:
                if (validateInputes()) { // 校验数据
                    TgDialogUtil.showConfirmDialog(this, "确认", "确认设置数据？", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(final @NonNull MaterialDialog materialDialog, final @NonNull DialogAction dialogAction) {
                            if (workMode.getMode() == 0) {
                                setupParameterCommonMode(); // 通用模式
                            } else if (workMode.getMode() == 1) {
                                setupParameterEconomicMode(); // 经济模式
                            } else if (workMode.getMode() == 2) {
                                setupParameterSpareMode(); // 备用模式
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取输入值进行判断判断是否合理
     *
     * @return 如果返回true 表示所有输入值合理，可以进行下一步设置
     */
    private boolean validateInputes() {
        // 充电电压校验
        if (TextUtils.isEmpty(etChargingVoltage.getText().toString().trim())) {
            TgToastUtil.showShort("充电电压不能为空！");
            return false;
        } else {
            if (RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_INTEGER, etChargingVoltage.getText().toString().trim()) || RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_FLOAT, etChargingVoltage.getText().toString().trim())) {
                if (RegexUtils.isMatch("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,1})?$", etChargingVoltage.getText().toString().trim())) {
                    chargingVoltage = (short) (Float.valueOf(etChargingVoltage.getText().toString().trim()) * 10 + 0);
                    if (100 > chargingVoltage || chargingVoltage > 590) {
                        TgToastUtil.showShort("充电电压设置应在10-59之间，请重新设置！");
                        return false;
                    }
                } else {
                    TgToastUtil.showShort("充电电压最多保留小数点后一位！");
                    return false;
                }
            } else {
                TgToastUtil.showShort("请输入正确充电电压！");
                return false;
            }
        }
        // 充电电流校验
        if (TextUtils.isEmpty(etChargingCurrent.getText().toString().trim())) {
            TgToastUtil.showShort("充电电流不能为空！");
            return false;
        } else {
            if (RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_INTEGER, etChargingCurrent.getText().toString().trim()) || RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_FLOAT, etChargingCurrent.getText().toString().trim())) {
                if (RegexUtils.isMatch("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,1})?$", etChargingCurrent.getText().toString().trim())) {
                    chargingCurrent = (short) (Float.valueOf(etChargingCurrent.getText().toString().trim()) * 10);
                    if (0 > chargingCurrent || chargingCurrent > 10000) {
                        TgToastUtil.showShort("充电电流设置应在0-1000之间，请重新设置！");
                        return false;
                    }
                } else {
                    TgToastUtil.showShort("充电电流最多保留小数点后一位！");
                    return false;
                }

            } else {
                TgToastUtil.showShort("请输入正确充电电流！");
                return false;
            }
        }
        // 充电功率校验
        if (TextUtils.isEmpty(etChargingPower.getText().toString().trim())) {
            TgToastUtil.showShort("充电功率不能为空！");
            return false;
        } else {
            chargingPower = (short) Integer.parseInt(etChargingPower.getText().toString().trim());
            if (0 > chargingPower || chargingPower > 5000) {
                TgToastUtil.showShort("充电功率设置应在0-5000之间，请重新设置！");
                return false;
            }
        }
        // 放电电流校验
        if (TextUtils.isEmpty(etDischargingCurrent.getText().toString().trim())) {
            TgToastUtil.showShort("放电电流不能为空！");
            return false;
        } else {
            if (RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_INTEGER, etDischargingCurrent.getText().toString().trim()) || RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_FLOAT, etDischargingCurrent.getText().toString().trim())) {
                if (RegexUtils.isMatch("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,1})?$", etDischargingCurrent.getText().toString().trim())) {
                    dischargingCurrent = (short) (Float.valueOf(etDischargingCurrent.getText().toString().trim()) * 10);
                    if (0 > dischargingCurrent || dischargingCurrent > 10000) {
                        TgToastUtil.showShort("放电电流设置应在0-1000之间，请重新设置！");
                        return false;
                    }
                } else {
                    TgToastUtil.showShort("放电电流最多保留小数点后一位！");
                    return false;
                }

            } else {
                TgToastUtil.showShort("请输入正确放电电流！");
                return false;
            }
        }
        // 放电功率校验
        if (TextUtils.isEmpty(etDischargingPower.getText().toString().trim())) {
            TgToastUtil.showShort("放电功率不能为空！");
            return false;
        } else {
            dischargingPower = (short) Integer.parseInt(etDischargingPower.getText().toString().trim());
            if (0 > dischargingPower || dischargingPower > 5000) {
                TgToastUtil.showShort("放电功率设置应在0-5000之间，请重新设置！");
                return false;
            }
        }
        // 逆变器输出并网功率
        if (TextUtils.isEmpty(etInverterOutputOngridPower.getText().toString().trim())) {
            TgToastUtil.showShort("逆变器输出并网功率不能为空！");
            return false;
        } else {
            inverterOutputOngridPower = (short) Integer.parseInt(etInverterOutputOngridPower.getText().toString().trim());
            if (0 > inverterOutputOngridPower || inverterOutputOngridPower > 5000) {
                TgToastUtil.showShort("逆变器输出并网功率设置应在0-5000之间，请重新设置！");
                return false;
            }
        }
        return true;
    }
}