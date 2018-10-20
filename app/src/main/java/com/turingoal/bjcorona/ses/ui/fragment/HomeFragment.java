package com.turingoal.bjcorona.ses.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.turingoal.bjcorona.ses.constant.ConstantAddress;
import com.turingoal.common.android.base.TgBaseFragment;
import com.turingoal.common.android.util.net.TgModbus4jUtil;

import butterknife.BindView;

/**
 * 首页fragment
 */
public class HomeFragment extends TgBaseFragment implements Runnable {
    @BindView(R.id.iv_pv1power)
    ImageView ivPV1Power;
    @BindView(R.id.pv1_power)
    TextView tvPv1Power; // pv1功率
    @BindView(R.id.iv_pv2power)
    ImageView ivPV2Power;
    @BindView(R.id.pv2_power)
    TextView tvPv2Power; // pv2功率
    @BindView(R.id.iv_batterypower)
    ImageView ivBatteryPower;
    @BindView(R.id.iv_inverter)
    ImageView ivInverter;
    @BindView(R.id.battery_power)
    TextView tvBatteryPower; // 电池功率
    @BindView(R.id.iv_homeloadpower)
    ImageView ivHomeLoadPower;
    @BindView(R.id.home_load_power)
    TextView tvHomeLoadPower; // homeload功率
    @BindView(R.id.iv_backuppower)
    ImageView ivBackupPower;
    @BindView(R.id.back_up_power)
    TextView tvBackUpPower; // backup功率
    @BindView(R.id.iv_ongridpower)
    ImageView ivOnGridPower;
    @BindView(R.id.ongrid_power)
    TextView tvOnGridPower; // 并网功率
    @BindView(R.id.ll_workStatus)
    LinearLayout llWorkStatus; // 工作状态
    @BindView(R.id.tv_workstatus)
    TextView tvWorkStatus; // 工作状态
    @BindView(R.id.ll_mode)
    LinearLayout llMode; // 模式显示控件
    @BindView(R.id.tv_mode)
    TextView tvMode; // 显示模式
    @BindView(R.id.tv_backup_power_top)
    TextView tvBackupPowerTop;
    private ModbusMaster master; // modbus master
    private Thread getDataThread; // 获取数据的子线程
    private volatile boolean threadRun = true; // 线程运行。要用volatile 修饰，可以确保同一时刻只有同一个线程访问
    private boolean threadSuspend = false;// 控制线程的暂停

    @Override
    protected int getLayoutId() {
        return R.layout.app_fragment_final_show;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title, "首页"); // 顶部工具条
        master = TgApplication.getMaster(); // 获得modbus master
        getDataThread = new Thread(this); // 创建线程
        getDataThread.start(); // 开启数据获得线程
    }

    /**
     * 显示默认数据
     */
    private void showDefaultData() {
        ivPV1Power.setImageResource(R.mipmap.pv1_power_off); // 未连接就将图标置换成灰色
        ivPV2Power.setImageResource(R.mipmap.pv1_power_off); // 未连接就将图标置换成灰色
        ivBatteryPower.setImageResource(R.mipmap.battery_power_off); // 未连接就将图标置换成灰色
        ivBackupPower.setImageResource(R.mipmap.backup_power_off); // 未连接就将图标置换成灰色
        tvBackUpPower.setBackgroundResource(R.mipmap.mid_vertical);
        tvHomeLoadPower.setBackgroundResource(R.mipmap.right_bottom);
        ivHomeLoadPower.setImageResource(R.mipmap.home_load_power_off); // 未连接就将图标置换成灰色
        ivOnGridPower.setImageResource(R.mipmap.ongrid_power_off);
        tvBatteryPower.setBackgroundResource(R.mipmap.left_bottom);
        tvOnGridPower.setBackgroundResource(R.mipmap.right_top);
        tvWorkStatus.setText("工作状态：" + " NA");
        ivInverter.setImageResource(R.mipmap.inverter_off);
        llWorkStatus.setBackgroundColor(Color.parseColor("#D7D7D8"));
        tvMode.setText("未连接");
        tvMode.setTextColor(Color.parseColor("#D7D7D8"));
        tvPv1Power.setText("0.00KW");
        tvPv2Power.setText("0.00KW");
        tvBatteryPower.setText("0.00KW");
        tvHomeLoadPower.setText("0.00KW");
        tvBackUpPower.setText("0.00KW");
        tvOnGridPower.setText("0.00KW");
    }

    /**
     * 显示数据
     */
    private void showData(final BatchResults<String> results) {
        if (results == null) {
            showDefaultData(); // 显示默认数据
        } else {
            // 工作状态
            if (results.getValue("workStatus") == null) { // 未连接
                ivInverter.setImageResource(R.mipmap.inverter_off);
                llWorkStatus.setBackgroundColor(Color.parseColor("#D7D7D8"));
            } else { // 连接
                short workStatus = (short) results.getValue("workStatus");
                if (workStatus == 0) {
                    tvWorkStatus.setText("工作状态：" + "待机");
                    ivInverter.setImageResource(R.mipmap.inverter_on);
                    llWorkStatus.setBackgroundColor(Color.parseColor("#5FB9F5"));
                } else if (workStatus == 1) {
                    tvWorkStatus.setText("工作状态：" + "并网运行");
                    ivInverter.setImageResource(R.mipmap.inverter_on);
                    llWorkStatus.setBackgroundColor(Color.parseColor("#3BC495"));
                } else if (workStatus == 2) {
                    tvWorkStatus.setText("工作状态：" + "离网运行");
                    ivInverter.setImageResource(R.mipmap.inverter_on);
                    llWorkStatus.setBackgroundColor(Color.parseColor("#FF9F14"));
                } else if (workStatus == 8) {
                    tvWorkStatus.setText("工作状态：" + "故障");
                    ivInverter.setImageResource(R.mipmap.inverter_on);
                    llWorkStatus.setBackgroundColor(Color.parseColor("#EC2C23"));
                } else {
                    tvWorkStatus.setText("工作状态：" + " NA");
                    ivInverter.setImageResource(R.mipmap.inverter_off);
                    llWorkStatus.setBackgroundColor(Color.parseColor("#D7D7D8"));
                }
                // 工作模式
                if (results.getValue("getMode") != null) {
                    llMode.setVisibility(View.VISIBLE);
                    short mode = (short) results.getValue("getMode");
                    if (mode == 1) {
                        tvMode.setText("经济模式");
                        tvMode.setTextColor(Color.parseColor("#3BC495"));
                    } else if (mode == 0) {
                        tvMode.setText("通用模式");
                        tvMode.setTextColor(Color.parseColor("#5FB9F5"));
                    } else if (mode == 2) {
                        tvMode.setText("备用模式");
                        tvMode.setTextColor(Color.parseColor("#5FB9F5"));
                    } else {
                        tvMode.setText("未连接");
                        tvMode.setTextColor(Color.parseColor("#D7D7D8"));
                    }
                } else {
                    tvMode.setText("未连接");
                    tvMode.setTextColor(Color.parseColor("#D7D7D8"));
                }
                // 数值
                // pv1侧连接标志
                if (results.getValue("pv1Connect") != null && (short) results.getValue("pv1Connect") == (short) 1) {
                    ivPV1Power.setImageResource(R.mipmap.pv1_power_on); //如果已连接，图标换成蓝色
                    tvPv1Power.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv1Power")) / 1000, 2) + "KW"); // pv1功率
                } else {
                    ivPV1Power.setImageResource(R.mipmap.pv1_power_off); //未连接就将图标置换成灰色
                    tvPv1Power.setText("0.00KW");
                }
                // pv2侧连接标志
                if (results.getValue("pv2Connect") != null && (short) results.getValue("pv2Connect") == (short) 1) {
                    ivPV2Power.setImageResource(R.mipmap.pv1_power_on); //如果已连接，图标换成蓝色
                    tvPv2Power.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("pv2Power")) / 1000, 2) + "KW"); // pv2功率
                } else {
                    ivPV2Power.setImageResource(R.mipmap.pv1_power_off); //未连接就将图标置换成灰色
                    tvPv2Power.setText("0.00KW");
                }
                // 电池连接标志
                if (results.getValue("batteryConnect") != null && (short) results.getValue("batteryConnect") == (short) 1) {
                    ivBatteryPower.setImageResource(R.mipmap.battery_power_on); //如果已连接，图标换成蓝色
                    tvBatteryPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("batteryPower")) / 1000, 2) + "KW");
                    // 电池充放电标志
                    if (results.getValue("batteryIsCharing") != null && (short) results.getValue("batteryIsCharing") == (short) 0) { // 电池充放电标志位
                        tvBatteryPower.setBackgroundResource(R.mipmap.left_bottom_discharge);
                    } else if ((short) results.getValue("batteryIsCharing") == (short) 1) {
                        tvBatteryPower.setBackgroundResource(R.mipmap.left_bottom_charge);
                    } else {
                        tvBatteryPower.setBackgroundResource(R.mipmap.left_bottom);
                    }
                } else {
                    ivBatteryPower.setImageResource(R.mipmap.battery_power_off); //未连接就将图标置换成灰色
                    tvBatteryPower.setBackgroundResource(R.mipmap.left_bottom);
                    tvBatteryPower.setText("0.00KW");
                }
                // 负载功率连接标志
                if (results.getValue("backupLoadConnect") != null && (short) results.getValue("backupLoadConnect") == (short) 1) { //非空判断
                    ivBackupPower.setImageResource(R.mipmap.backup_power_on); //如果已连接，图标换成蓝色
                    tvBackUpPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("backUppower")) / 1000, 2) + "KW");
                    tvBackupPowerTop.setBackgroundResource(R.mipmap.mid_vertical_arrow);
                } else {
                    ivBackupPower.setImageResource(R.mipmap.backup_power_off); //未连接就将图标置换成灰色
                    tvBackUpPower.setBackgroundResource(R.mipmap.mid_vertical);
                    tvBackUpPower.setText("0.00KW");
                }
                // 正载功率连接标志
                if (results.getValue("homeLoadConnect") != null && (short) results.getValue("homeLoadConnect") == (short) 1) { //非空判断
                    ivHomeLoadPower.setImageResource(R.mipmap.home_load_power_on); //如果已连接，图标换成蓝色
                    tvHomeLoadPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("homeLoadPower")) / 1000, 2) + "KW");
                    tvHomeLoadPower.setBackgroundResource(R.mipmap.right_bottom_arrow);
                } else {
                    tvHomeLoadPower.setBackgroundResource(R.mipmap.right_bottom);
                    ivHomeLoadPower.setImageResource(R.mipmap.home_load_power_off); //未连接就将图标置换成灰色
                    tvHomeLoadPower.setText("0.00KW");
                }
                // 电网连接标志
                if (results.getValue("isOnGrid") != null && (short) results.getValue("isOnGrid") == (short) 1) { //非空判断
                    ivOnGridPower.setImageResource(R.mipmap.ongrid_power_on); //如果已连接，图标换成蓝色
                    tvOnGridPower.setText(TgModbus4jUtil.keepPrecision(Float.valueOf((short) results.getValue("onGridPower")) / 1000, 2) + "KW");
                    // 发耗电标志
                    if (results.getValue("usePower") != null && (short) results.getValue("usePower") == (short) 0) { //非空判断,发耗电
                        tvOnGridPower.setBackgroundResource(R.mipmap.right_top_charge);
                    } else if ((short) results.getValue("usePower") == (short) 1) {
                        tvOnGridPower.setBackgroundResource(R.mipmap.right_top_discharge);
                    } else {
                        tvOnGridPower.setBackgroundResource(R.mipmap.right_top);
                    }
                } else {
                    ivOnGridPower.setImageResource(R.mipmap.ongrid_power_off);
                    tvOnGridPower.setBackgroundResource(R.mipmap.right_top);
                    tvOnGridPower.setText("0.00KW");
                }
            }
        }
    }

    /**
     * 获得数据
     */
    private BatchResults getData() {
        BatchResults<String> results = null;
        BatchRead<String> batch = new BatchRead<>();
        batch.setContiguousRequests(true);  // 默认false。设置为true，会分开发送指令，false会合并指令，数据过多会超出范围。数据量少的话设置为false提高效率，
        // 获取工作状态
        batch.addLocator("workStatus", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_STATUS_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 工作模式获取
        batch.addLocator("getMode", BaseLocator
                .holdingRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PATTERN_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // pv1侧连接标志
        batch.addLocator("pv1Connect", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_CONNECT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // pv2侧连接标志
        batch.addLocator("pv2Connect", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV2_CONNECT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池连接标志
        batch.addLocator("batteryConnect", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_CONNECT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池充放电标志
        batch.addLocator("batteryIsCharing", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_CHARGE_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电网连接标志
        batch.addLocator("isOnGrid", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_POWER_GRID_CONNECT_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 负载功率连接标志
        batch.addLocator("backupLoadConnect", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BACK_UP_LOAD_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 正载功率连接标志
        batch.addLocator("homeLoadConnect", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_HOME_LOAD_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 发耗电标志
        batch.addLocator("usePower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // pv1功率
        batch.addLocator("pv1Power", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV1_INPUT_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // pv2功率
        batch.addLocator("pv2Power", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_PV2_INPUT_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 电池功率
        batch.addLocator("batteryPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_BATTERY_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 正载功率
        batch.addLocator("homeLoadPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_HOME_LOAD_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 负载功率
        batch.addLocator("backUppower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_LOAD_LOW_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
        // 并网功率
        batch.addLocator("onGridPower", BaseLocator
                .inputRegister(TgAppConfig.INVERTER, ConstantAddress.GET_ON_POWER_GRID_LOW_POWER_OFFECT, DataType.TWO_BYTE_INT_SIGNED));
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
            synchronized (this) { // 方法前要加synchronized锁定，即是必须先加锁，然后再调用wait(),notify()方法。
                // 控制线程暂停、唤醒
                while (threadSuspend) {
                    try {
                        wait(); // 等待。 注意：应该调用lock对象的wait方法,而不是线程的wait方法
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            final BatchResults<String> results = getData(); // 获得数据
            // runOnUiThread(Runnable action)切换到主线程更新ui。
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showData(results); // 显示数据
                }
            });
            try {
                Thread.sleep(1000); // 延迟1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        threadRun = false; // 关闭线程
    }

    /**
     * 暂停线程
     */
    public synchronized void pauseGetDataThread() {
        threadSuspend = true;
    }

    /**
     * 线程继续运行
     */
    public synchronized void resumeGetDataThread() {
        threadSuspend = false;
        this.notify();
    }

    /**
     * Fragment虽然有onResume和onPause的，但是这两个方法是Activity的方法，调用时机也是与Activity相同，和ViewPager搭配使用这个方法就很鸡肋了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {  // 相当于Fragment的onResume
            resumeGetDataThread(); // 线程继续运行
        } else { // 相当于Fragment的onPause
            pauseGetDataThread(); // 暂停线程
        }
    }
}

