package com.turingoal.bjcorona.ses.app;

import com.turingoal.common.android.base.TgBaseAppConfig;

/**
 * 系统配置
 */
public class TgAppConfig extends TgBaseAppConfig {
    // 关于
    public static final String CONTRACT_WEBSITE = "www.bjcorona.com"; // 联系网址
    public static final String CONTRACT_TEL = "010-82378899"; //  联系电话
    public static final String CONTRACT_ADDRESS = "北京海淀王庄路1号清华同方科技广场B座23层"; // 联系地址
    public static final String CONTRACT_NAME = "北京科诺伟业科技股份有限公司"; // 联系名称
    // 基本配置
    public static final String PROJECT_NAME = "tg_knwy_ses"; // 项目名字
    public static final String APP_BASE_PATH = "/knwy/ses/"; // 页面路由库，要求二级路径，防止出错
    public static final String LOG_TAG = PROJECT_NAME + "-log"; // log tag
    public static final String SP_NAME = PROJECT_NAME + "-sp"; // SharedPreferences名称
    // 服务器配置
    public static final String SERVER_IP = "192.168.1.1"; //ip 192.168.1.1 默认IP
    public static final Integer SERVER_PORT = 8899; // 默认端口号8899
    // 其他
    public static final Integer INVERTER = 1; // 逆变器slaveID
    public static final Integer BATTERY = 2; // 电池slaveID
    public static final Integer METERING = 3; // 计量器slaveID
    public static final String CONFIG_PASSWORD = "bjcorona"; // 配置密码
}
