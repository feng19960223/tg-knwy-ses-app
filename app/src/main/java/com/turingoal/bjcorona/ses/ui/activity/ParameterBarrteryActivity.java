package com.turingoal.bjcorona.ses.ui.activity;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.turingoal.bjcorona.ses.R;
import com.turingoal.bjcorona.ses.app.TgAppConfig;
import com.turingoal.bjcorona.ses.app.TgApplication;
import com.turingoal.bjcorona.ses.app.TgArouterPaths;
import com.turingoal.bjcorona.ses.constant.ConstantAddress;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.net.TgModbus4jUtil;

import butterknife.BindView;

/**
 * 电池参数页面
 */
@Route(path = TgArouterPaths.PARAMETER_BATTERY)
public class ParameterBarrteryActivity extends TgBaseActivity implements Runnable {
    // @BindView(R.id.tv_batterystatus)
    // TextView tvBatteryStatus; // 电池状态
    @BindView(R.id.tv_batteryvoltage)
    TextView tvBatteryVoltage; // 电池电压
    @BindView(R.id.tv_batterycurrent)
    TextView tvBatteryCurrent; // 电池电流
    @BindView(R.id.tv_batterypower)
    TextView tvBatteryPower; // 电池功率
    // @BindView(R.id.tv_batterycapacity)
    // TextView tvBatteryCapacity; // 电池容量
    // @BindView(R.id.tv_BMSstatus)
    // TextView tvBMSStatus; // BMS状态
    // @BindView(R.id.tv_BMShealthstatus)
    // TextView tvBMSHealthStatus; // BMS健康状况
    // @BindView(R.id.tv_BMS_charge_limit)
    // TextView tvBMSChargeLimit; // 充电限流
    // @BindView(R.id.tv_BMS_discharge_limit)
    // TextView tvBMSDischargeLimit; // 放电限流
    // @BindView(R.id.tv_batterywarring)
    // TextView tvBatteryWarring; // 电池警告
    // @BindView(R.id.tv_batterytemperature)
    // TextView tvBatteryTemperature; // 电池温度
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程
    private volatile boolean threadRun = true; // 线程运行。要用volatile 修饰，可以确保同一时刻只有同一个线程访问

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_parameter_battery;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "电池"); // 顶部工具条
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示默认数据
     */
    private void showDefaultData() {
        tvBatteryVoltage.setText("0.0"); // 电池电压
        tvBatteryCurrent.setText("0.0"); // 电池电流
        tvBatteryPower.setText("0.00"); // 电池功率
    }

    /**
     * 显示数据
     */
    private void showData(BatchResults<String> results) {
        if (results == null) {
            showDefaultData(); // 显示默认数据
        } else {
            // tvBatteryStatus.setText(obj.results.getValue("batteryStatus").toString());
            // 电池电压
            if (results.getValue("batteryVoltage") != null) {
                tvBatteryVoltage.setText(TgModbus4jUtil
                        .keepPrecision(Float.valueOf((short) results.getValue("batteryVoltage")) / 10, 1) + ""); //获取到电压值后除以10以后保留一位小数
            }
            // 电池电流
            if (results.getValue("batteryCurrent") != null) {
                tvBatteryCurrent.setText(TgModbus4jUtil
                        .keepPrecision(Float.valueOf((short) results.getValue("batteryCurrent")) / 10, 1) + ""); //获取到电流值后除以10以后保留一位小数
            }
            // 电池功率
            if (results.getValue("batteryPower") != null) {
                tvBatteryPower.setText(TgModbus4jUtil
                        .keepPrecision(Float.valueOf((short) results.getValue("batteryPower")) / 1000, 2) + ""); //获取到功率值后除以1000以后保留两位小数
            }
             /* tvBatteryCapacity.setText(obj.results.getValue("batteryCapacity") + "");
            tvBMSStatus.setText(obj.results.getValue("BMSStatus") + "");
            tvBMSHealthStatus.setText(obj.results.getValue("BMSHealthStatus") + "");
            tvBMSChargeLimit.setText(obj.results.getValue("BMSChargeLimit") + "");
            tvBMSDischargeLimit.setText(obj.results.getValue("BMSDischargeLimit") + "");
            tvBatteryWarring.setText(obj.results.getValue("batteryWarring") + "");
            tvBatteryTemperature.setText(obj.results.getValue("batteryTemperature") + "");*/
        }
    }

    /**
     * 获得数据
     */
    private BatchResults getData() {
        BatchResults<String> results = null;
        BatchRead<String> batch = new BatchRead<>();
        //batch.setContiguousRequests(true); // 默认false。设置为true，会分开发送指令，false会合并指令，数据过多会超出范围。数据量少的话设置为false提高效率，
        //batch.addLocator("batteryStatus", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池电压
        batch.addLocator("batteryVoltage", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池电流
        batch.addLocator("batteryCurrent", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池功率
        batch.addLocator("batteryPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.setContiguousRequests(true); //防止出现查询速度过快。
        //batch.addLocator("batteryCapacity", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.addLocator("BMSStatus", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.addLocator("BMSHealthStatus", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.addLocator("BMSChargeLimit", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // batch.addLocator("BMSDischargeLimit", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.addLocator("batteryWarring", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        //batch.addLocator("batteryTemperature", BaseLocator
        //.inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
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
        while (threadRun) {
            final BatchResults<String> results = getData(); // 获得数据
            // runOnUiThread(Runnable action)切换到主线程更新ui。
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showData(results); // 显示数据
                }
            });
            try {
                Thread.sleep(3000); // 延迟3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TgApplication.deleteActivity(this); // 从堆栈删除当前Activity
        threadRun = false; // 关闭线程
    }
}
