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
 * 电网参数页面
 */
@Route(path = TgArouterPaths.PARAMETER_POWER_GRID)
public class ParameterPowerGridActivity extends TgBaseActivity implements Runnable {
    @BindView(R.id.tv_powergrid_voltage)
    TextView tvPowerGridVoltage; // 电网电压
    @BindView(R.id.tv_powergrid_current)
    TextView tvPowerGridCurrent; // 电网电流
    @BindView(R.id.tv_ongrid_power)
    TextView tvOnGridPower; // 并网功率
    @BindView(R.id.tv_backup_power)
    TextView tvBackupPower; // 负载功率
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程
    private volatile boolean threadRun = true; // 线程运行。要用volatile 修饰，可以确保同一时刻只有同一个线程访问

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_parameter_power_grid;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "电网"); // 顶部工具条
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示默认数据
     */
    private void showDefaultData() {
        tvPowerGridVoltage.setText("0.0"); // 电网电压
        tvPowerGridCurrent.setText("0.00");  // 电网电流
        tvOnGridPower.setText("0.00"); // 并网功率
        tvBackupPower.setText("0.00");  // 负载功率
    }

    /**
     * 显示数据
     */
    private void showData(final BatchResults<String> results) {
        if (results == null) {
            showDefaultData(); // 显示默认数据
        } else {
            // 电网电压
            if (results.getValue("powerGridVoltage") != null) {
                tvPowerGridVoltage.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("powerGridVoltage")) / 10, 1) + ""); // 保留1位小数
            }
            // 电网电流
            if (results.getValue("powerGridCurrent") != null) {
                tvPowerGridCurrent.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("powerGridCurrent")) / 100, 2) + ""); // 保留两位小数
            }
            // 并网功率
            if (results.getValue("onGridPower") != null) {
                tvOnGridPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("onGridPower")) / 1000, 2) + ""); // 保留两位小数
            }
            // 负载功率
            if (results.getValue("backupPower") != null) {
                tvBackupPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("backupPower")) / 1000, 2) + ""); // 保留两位小数
            }
        }
    }

    /**
     * 获得数据
     */
    private BatchResults getData() {
        BatchResults<String> results = null;
        BatchRead<String> batch = new BatchRead<>();
        // batch.setContiguousRequests(true); // 默认false。设置为true，会分开发送指令，false会合并指令，数据过多会超出范围。数据量少的话设置为false提高效率，
        batch.addLocator("powerGridVoltage", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_POWER_GRID_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // 电网电压
        batch.addLocator("powerGridCurrent", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_POWER_GRID_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // 电网电流
        batch.addLocator("onGridPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_ON_POWER_GRID_LOW_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // 并网功率
        batch.addLocator("backupPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_LOAD_LOW_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // 负载功率
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
