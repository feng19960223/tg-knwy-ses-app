package com.turingoal.bjcorona.ses.app;

/**
 * 常量-Activity路径
 */
public interface TgArouterPaths {
    // 配置
    String CONFIG_IP_SETUP = TgAppConfig.APP_BASE_PATH + "config/ipSetup"; //  配置IP界面 ConfigIpSetupActivity
    String CONFIG_LOGIN = TgAppConfig.APP_BASE_PATH + "config/login"; //  配置登录界面 ConfigLoginActivity
    String CONFIG_WORK_PATTERN = TgAppConfig.APP_BASE_PATH + "config/workPattern"; //  选择工作模式界面 ConfigWorkPatternActivity
    String CONFIG_WORK_PATTERN_ECONOMIC = TgAppConfig.APP_BASE_PATH + "config/workPatternEconomic"; //  选择工作模式界面 ConfigWorkPatternEconomicActivity
    String CONFIG_LAST_SETUP = TgAppConfig.APP_BASE_PATH + "config/lastSetup"; // 最后设置页面. ConfigLastSetupActivity
    // 参数
    String PARAMETER_POWER_GRID = TgAppConfig.APP_BASE_PATH + "parameterPowerGrid"; //  电网参数界面 ParameterPowerGridActivity
    String PARAMETER_INVERTER = TgAppConfig.APP_BASE_PATH + "parameterInverter"; //  逆变器界面 ParameterInverterActivity
    String PARAMETER_LOAD = TgAppConfig.APP_BASE_PATH + "parameterLoad"; //  负载界面 ParameterLoadActivity
    String PARAMETER_PANEL = TgAppConfig.APP_BASE_PATH + "parameterPanel"; //  面板界面 ParameterPanelActivity
    String PARAMETER_BATTERY = TgAppConfig.APP_BASE_PATH + "parameterBattery"; //  电池界面 ParameterBarrteryActivity
}