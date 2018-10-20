package com.turingoal.bjcorona.ses.app;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.turingoal.bjcorona.ses.BuildConfig;
import com.turingoal.common.android.base.TgBaseApplication;
import com.turingoal.common.android.util.net.TgModbus4jUtil;

/**
 * 主应用
 */
public class TgApplication extends TgBaseApplication {
    private static TgUserPreferences userPreferences; // 用户数据存储
    private static ModbusMaster master = null;

    /**
     * 系统初始化配置
     */
    private void appInit() {
        userPreferences = new TgUserPreferences(this, TgAppConfig.SP_NAME); // 初始化 TgUserPreferences
        userPreferences.setUserLoginStatus(false); // 启动时将登陆状态设置为false
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        //    MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext(); //context
        Utils.init(this); // 初始化Utils工具类
        initLogger(TgAppConfig.LOG_TAG, BuildConfig.DEBUG); // 初始化Logger
        initARouter(BuildConfig.DEBUG); // 初始化arouter
        appInit(); // 初始化 app
        // HotFixManger.getInstance().init(this, "");
    }

    /**
     * 设置master
     */
    public static ModbusMaster getMaster() {
        if (master == null || !master.isConnected()) {
            String ip = TgApplication.getTgUserPreferences().getIp(); // 设置的ip
            int port = TgApplication.getTgUserPreferences().getPort(); // 设置的端口
            try {
                if (ip == null) {
                    master = TgModbus4jUtil.getRtuOverTcpMaster(TgAppConfig.SERVER_IP, TgAppConfig.SERVER_PORT);
                } else {
                    master = TgModbus4jUtil.getRtuOverTcpMaster(ip, port);
                }
            } catch (ModbusInitException e) {
                e.printStackTrace();
            }
        }
        return master;
    }

    /**
     * getTgUserPreferences
     */
    public static TgUserPreferences getTgUserPreferences() {
        return userPreferences;
    }
}
