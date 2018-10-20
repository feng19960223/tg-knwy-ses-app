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
 * 面板参数
 */
@Route(path = TgArouterPaths.PARAMETER_PANEL)
public class ParameterPanelActivity extends TgBaseActivity implements Runnable {
    @BindView(R.id.tv_pv1_voltage)
    TextView tvPV1Voltage; // pv1电压
    @BindView(R.id.tv_pv2_voltage)
    TextView tvPV2Voltage; // pv2电压
    @BindView(R.id.tv_pv1_current)
    TextView tvPV1Current; // pv1 电流
    @BindView(R.id.tv_pv2_current)
    TextView tvPV2Current; // pv2 电流
    @BindView(R.id.tv_pv1_input_power)
    TextView tvPV1InputPower; // pv1输入功率
    @BindView(R.id.tv_pv2_input_power)
    TextView tvPV2InputPower; // pv2输入功率
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程
    private volatile boolean threadRun = true; // 线程运行。要用volatile 修饰，可以确保同一时刻只有同一个线程访问

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_parameter_panel;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "面板"); // 顶部工具条
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示默认数据
     */
    private void showDefaultData() {
        tvPV1Voltage.setText("0.0"); // pv1电压
        tvPV2Voltage.setText("0.0"); // pv2电压
        tvPV1Current.setText("0.00"); // pv1电流
        tvPV2Current.setText("0.00"); // pv2电流
        tvPV1InputPower.setText("0.00"); // pv1输入功率
        tvPV2InputPower.setText("0.00"); // pv2输入功率
    }

    /**
     * 显示数据
     */
    private void showData(final BatchResults<String> results) {
        if (results == null) {
            showDefaultData(); // 显示默认数据
        } else {
            // pv1电压
            if (results.getValue("pv1Voltage") != null) {
                tvPV1Voltage.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv1Voltage")) / 10, 1) + ""); //获取到数值除以10，保留1位小数
            }
            // pv2电压
            if (results.getValue("pv2Voltage") != null) {
                tvPV2Voltage.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv2Voltage")) / 10, 1) + ""); //获取到数值除以10，保留1位小数
            }
            // pv1电流
            if (results.getValue("pv1Current") != null) {
                tvPV1Current.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv1Current")) / 100, 2) + ""); //获取到数值除以100，保留2位小数
            }
            // pv2电流
            if (results.getValue("pv2Current") != null) {
                tvPV2Current.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv2Current")) / 100, 2) + ""); //获取到数值除以100，保留2位小数
            }
            // pv1输入功率
            if (results.getValue("pv1InputPower") != null) {
                tvPV1InputPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv1InputPower")) / 1000, 2) + "");
            }
            // pv2输入功率
            if (results.getValue("pv2InputPower") != null) {
                tvPV2InputPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv2InputPower")) / 1000, 2) + "");
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
        batch.addLocator("pv1Voltage", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv1电压
        batch.addLocator("pv2Voltage", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV2_VOLTAGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv2电压
        batch.addLocator("pv1Current", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv1电流
        batch.addLocator("pv2Current", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV2_CURRENT_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv2电流
        batch.addLocator("pv1InputPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_INPUT_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv1输入功率
        batch.addLocator("pv2InputPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV2_INPUT_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED)); // pv2输入功率
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
