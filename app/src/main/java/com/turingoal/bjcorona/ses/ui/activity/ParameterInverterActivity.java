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
 * 逆变器参数页面
 */
@Route(path = TgArouterPaths.PARAMETER_INVERTER)
public class ParameterInverterActivity extends TgBaseActivity implements Runnable {
    @BindView(R.id.tv_number)
    TextView tvNumber; // 序列编号
    @BindView(R.id.tv_version)
    TextView tvVersion; // 固件版本
    @BindView(R.id.tv_machine_type)
    TextView tvMachineType; // 机器型号
    // @BindView(R.id.tv_power_generation)
    // TextView tvPowerGeneration; // 总发电量
    // @BindView(R.id.tv_day_running_time)
    // TextView tvDayRunningTime; // 日运行时间
    // @BindView(R.id.tv_total_running_time)
    // TextView tvTotalRunningTime; // 总运行时间
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程
    private volatile boolean threadRun = true; // 线程运行。要用volatile 修饰，可以确保同一时刻只有同一个线程访问

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_parameter_inverter;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, R.id.iv_back, "逆变器"); // 顶部工具条
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示默认数据
     */
    private void showDefaultData() {
        tvNumber.setText("未知"); // 产品编号
        tvVersion.setText("未知"); // 固件版本号
        tvMachineType.setText("未知"); // 机器型号
    }

    /**
     * 显示数据
     */
    private void showData(final BatchResults<String> results) {
        if (results == null) {
            showDefaultData(); // 显示默认数据
        } else {
            // 产品编号。高位（4位数字的16进制）+中位（4位数字的16进制）+低位（4位数字的16进制）。如：002835AB5ABC
            if (results.getValue("numberLow") != null) {
                String numberStr = TgModbus4jUtil.short2Hex4Nums((short) results.getValue("numberHigh")) +
                        TgModbus4jUtil.short2Hex4Nums((short) results.getValue("numberMid")) +
                        TgModbus4jUtil.short2Hex4Nums((short) results.getValue("numberLow"));
                tvNumber.setText(numberStr);
            }
            // 固件版本号。4位数字的16进制，前两位+“.”+后两位
            if (results.getValue("version") != null) {
                String versionStr = TgModbus4jUtil.short2Hex4Nums((short) results.getValue("version"));
                String versionStrResult = versionStr.substring(0, 2) + "." + versionStr.substring(2);
                tvVersion.setText(versionStrResult);
            }
            // 机器型号
            if (results.getValue("machineTypeLow") != null) {
                tvMachineType.setText(getMachineType((int) results.getValue("machineTypeLow")));
            }
            // tvPowerGeneration.setText(results.getValue("powerGeneration") + "");
            // tvDayRunningTime.setText(results.getValue("dayRunningTime") + "");
            // tvTotalRunningTime.setText(results.getValue("totalRunningTime") + "");
        }
    }

    /**
     * 获得数据
     */
    private BatchResults getData() {
        BatchResults<String> results = null;
        BatchRead<String> batch = new BatchRead<>();
        //batch.setContiguousRequests(true); // 默认false。设置为true，会分开发送指令，false会合并指令，数据过多会超出范围。数据量少的话设置为false提高效率，
        // 机器编号低字
        batch.addLocator("machineTypeLow", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_INVERTER_MACHINE_TYPE_LOW_OFFECT, DataType.TWO_BYTE_INT_UNSIGNED));
        // 固件版本号
        batch.addLocator("version", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_INVERTER_FIRMWARE_VERSION_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 产品编号高字
        batch.addLocator("numberHigh", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_INVERTER_NUMBER_HIGH_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 产品编号中字
        batch.addLocator("numberMid", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_INVERTER_NUMBER_MID_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 产品编号低字
        batch.addLocator("numberLow", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_INVERTER_NUMBER_LOW_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // batch.addLocator("powerGeneration", BaseLocator.holdingRegister(TgAppConfig.INVERTER, ConstantAddress.POWER_GENERATION_HIGH_ADDRESS_OFFECT, DataType.FOUR_BYTE_FLOAT));
        // batch.addLocator("dayRunningTime", BaseLocator.holdingRegister(TgAppConfig.INVERTER, ConstantAddress.DAY_RUNNING_TIME_ADDRESS_OFFECT, DataType.FOUR_BYTE_FLOAT));
        // batch.addLocator("totalRunningTime", BaseLocator.holdingRegister(TgAppConfig.INVERTER, ConstantAddress.TOTAL_RUNNING_TIME_ADDRESS_OFFECT, DataType.FOUR_BYTE_FLOAT));
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

    /**
     * 返回的机器型号
     */
    private String getMachineType(int type) {
        String machineType = "";
        switch (type) {
            case 0x0100:
                machineType = "1500";
                break;
            case 0x1100:
                machineType = "2000";
                break;
            case 0x2100:
                machineType = "2500";
                break;
            case 0x3100:
                machineType = "2800";
                break;
            case 0x4100:
                machineType = "3000";
                break;
            case 0x5100:
                machineType = "3600";
                break;
            case 0x6100:
                machineType = "4000";
                break;
            case 0x7100:
                machineType = "4600";
                break;
            case 0x8100:
                machineType = "5000";
                break;
            default:
                machineType = "1000";
                break;
        }
        return machineType + " W";
    }
}
