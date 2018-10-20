package com.turingoal.bjcorona.ses.constant;

/**
 * 地址常量,由于设备使用的PLC Address,计数方式从1开始，所以通讯时地址统一减去1，
 */
public interface ConstantAddress {
    //------逆变器485通讯地址：01；即slaveId=1 ------//
    //###### 读 ######//
    //*** 首页 ***//
    String GET_PATTERN = "40231";
    Integer GET_PATTERN_OFFECT = 230; // 通用模式。0通用模式，1经济模式，2备用模式。指令：01 03 00 E6 00 01
    String GET_STATUS = "30026";
    Integer GET_STATUS_OFFECT = 25; // 当前状态。 待机 0x00 并网运行 0x01 离网运行0x02 故障 0x08。指令：01 04 00 19 00 01
    String GET_PV1_CONNECT = "30041";
    Integer GET_PV1_CONNECT_OFFECT = 40; // PV1侧接通标志位。接通1、未接通0 指令：01 04 00 28 00 01
    String GET_PV2_CONNECT = "30042";
    Integer GET_PV2_CONNECT_OFFECT = 41; // PV2侧接通标志位。接通1、未接通0 指令：01 04 00 29 00 01
    String GET_BATTERY_CONNECT = "30043";
    Integer GET_BATTERY_CONNECT_OFFECT = 42; // 电池接通标志位。接通1、未接通0 指令：01 04 00 2A 00 01
    String GET_BATTERY_CHARGE = "30044";
    Integer GET_BATTERY_CHARGE_OFFECT = 43; // 电池充放电标志位。充电1、放电0 指令：01 04 00 2B 00 01
    String GET_POWER_GRID_CONNECT = "30045";
    Integer GET_POWER_GRID_CONNECT_OFFECT = 44; // 电网接通标志位。接通1、未接通0 指令：01 04 00 2C 00 01
    String GET_POWER = "30046";
    Integer GET_POWER_OFFECT = 45; // 发电耗电标志位。发电1、耗电0 指令：01 04 00 2D 00 01
    String GET_BACK_UP_LOAD = "30047";
    Integer GET_BACK_UP_LOAD_OFFECT = 46; // Back up 侧接负载标志位。接通1、未接通0 46 指令：01 04 00 2E 00 01
    String GET_HOME_LOAD = "30048";
    Integer GET_HOME_LOAD_OFFECT = 47; // Home Load 正接负载标志位。接通1、未接通0 指令：01 04 00 2F 00 01
    String GET_WHETHER_POWER_GRID = "30049";
    Integer GET_WHETHER_POWER_GRID_OFFECT = 48; // 并离网运行标志位。并网1、离网0 指令：01 04 00 30 00 01
    String GET_HOME_LOAD_POWER = "30051";
    Integer GET_HOME_LOAD_POWER_OFFECT = 50; //Home Load 功率。单位W，精度1 指令：01 04 00 32 00 01
    //*** 太阳能电池板参数 ***//
    String GET_PV1_VOLTAGE = "30001";
    Integer GET_PV1_VOLTAGE_OFFECT = 0; // PV1电压。单位V，精度0.1 指令：01 04 00 00 00 01
    String GET_PV1_CURRENT = "30002";
    Integer GET_PV1_CURRENT_OFFECT = 1; // PV1电流。单位A，精度0.01 指令：01 04 00 01 00 01
    String GET_PV2_VOLTAGE = "30003";
    Integer GET_PV2_VOLTAGE_OFFECT = 2; // PV2电压。单位V，精度0.1 指令：01 04 00 02 00 01
    String GET_PV2_CURRENT = "30004";
    Integer GET_PV2_CURRENT_OFFECT = 3; // PV2电流。单位A，精度0.01 指令：01 04 00 03 00 01
    String GET_PV1_INPUT_POWER = "30005";
    Integer GET_PV1_INPUT_POWER_OFFECT = 4; // PV1输入功率。单位W，精度1 指令：01 04 00 04 00 01
    String GET_PV2_INPUT_POWER = "30006";
    Integer GET_PV2_INPUT_POWER_OFFECT = 5; // PV2输入功率。单位W，精度1 指令：01 04 00 05 00 01
    //*** 电网（计量表）参数 ***//
    String GET_POWER_GRID_VOLTAGE = "30007";
    Integer GET_POWER_GRID_VOLTAGE_OFFECT = 6; // 电网电压。单位V，精度0.1 指令：01 04 00 06 00 01
    String GET_POWER_GRID_CURRENT = "30010";
    Integer GET_POWER_GRID_CURRENT_OFFECT = 9; // 电网电流。单位A，精度0.01 指令：01 04 00 09 00 01
    String GET_ON_POWER_GRID_HIGH_POWER = "30017";
    Integer GET_ON_POWER_GRID_HIGH_POWER_OFFECT = 16; // 并网功率high。单位W，精度1 指令：01 04 00 10 00 01
    String GET_ON_POWER_GRID_LOW_POWER = "30018";
    Integer GET_ON_POWER_GRID_LOW_POWER_OFFECT = 17; // 并网功率low。单位W，精度1 指令：01 04 00 11 00 01
    String GET_LOAD_HIGH_POWER = "30019";
    Integer GET_LOAD_HIGH_POWER_OFFECT = 18; // Back up 负载功率high。单位W，精度1 指令：01 04 00 12 00 01
    String GET_LOAD_LOW_POWER = "30020";
    Integer GET_LOAD_LOW_POWER_OFFECT = 19; // Back up 负载功率low。单位W，精度1 指令：01 04 00 13 00 01
    //*** 电池参数 ***//
    String GET_BATTERY_VOLTAGE = "30035";
    Integer GET_BATTERY_VOLTAGE_OFFECT = 34; // 电池电压。单位V，精度0.1 指令：01 04 00 22 00 01
    String GET_BATTERY_CURRENT = "30036";
    Integer GET_BATTERY_CURRENT_OFFECT = 35; // 电池电流。单位A，精度0.01 指令：01 04 00 23 00 01
    String GET_BATTERY_POWER = "30037";
    Integer GET_BATTERY_POWER_OFFECT = 36; // 电池功率。单位W，精度0.01 指令：01 04 00 24 00 01
    //*** 逆变器 ***//
    String GET_INVERTER_MACHINE_TYPE_HIGH = "40001";
    Integer GET_INVERTER_MACHINE_TYPE_HIGH_OFFECT = 0; // 机器型号高 指令：01 03 00 00 00 01
    String GET_INVERTER_MACHINE_TYPE_LOW = "40002";
    Integer GET_INVERTER_MACHINE_TYPE_LOW_OFFECT = 1; // 机器型号低 指令：01 03 00 01 00 01
    String GET_INVERTER_FIRMWARE_VERSION = "40003";
    Integer GET_INVERTER_FIRMWARE_VERSION_OFFECT = 2; // 固件版本号 指令：01 03 00 02 00 01
    String GET_INVERTER_NUMBER_HIGH = "40007";
    Integer GET_INVERTER_NUMBER_HIGH_OFFECT = 6; //产品编号高字 指令：01 03 00 06 00 01
    String GET_INVERTER_NUMBER_MID = "40008";
    Integer GET_INVERTER_NUMBER_MID_OFFECT = 7; // 产品编号中字 指令：01 03 00 07 00 01
    String GET_INVERTER_NUMBER_LOW = "40009";
    Integer GET_INVERTER_NUMBER_LOW_OFFECT = 8; // 产品编号低字 指令：01 03 00 08 00 01
    //*** 暂时未用 ***//
    String GET_POWER_GENERATION_HIGH = "30021";
    Integer GET_POWER_GENERATION_HIGH_OFFECT = 20; // 总发电量 高位。单位kWh，精度0.1 指令：01 04 00 14 00 01
    String GET_POWER_GENERATION_LOW = "30022";
    Integer GET_POWER_GENERATION_LOW_OFFECT = 21; // 总发电量 低位。单位kWh，精度0.1 指令：01 04 00 15 00 01
    String GET_DAY_RUNNING_TIME = "30023";
    Integer GET_DAY_RUNNING_TIME_OFFECT = 22; // 日运行时间。单位分钟，精度1 指令：01 04 00 16 00 01
    String GET_TOTAL_RUNNING_TIME_HIGH = "30024";
    Integer GET_TOTAL_RUNNING_TIME_HIGH_OFFECT = 23; // 总运行运行时间 低位。单位小时，精度1 指令：01 04 00 17 00 01
    String GET_TOTAL_RUNNING_TIME_LOW = "30025";
    Integer GET_TOTAL_RUNNING_TIME_LOW_OFFECT = 24; // 总运行运行时间 低位。单位小时，精度1 指令：01 04 00 18 00 01
    //*** 负载 ***//
    String GET_LOAD_VOLTAGE = "30008";
    Integer GET_LOAD_VOLTAGE_OFFECT = 7; // 负载电压。单位V，精度0.1 指令：01 04 00 07 00 01
    String GET_LOAD_CURRENT = "30011";
    Integer GET_LOAD_CURRENT_OFFECT = 10; // 负载电流。单位A，精度0.01 指令：01 04 00 0A 00 01
    String GET_LOAD_FREQUENCY = "30014";
    Integer GET_LOAD_FREQUENCY_OFFECT = 13; //负载频率 指令：01 04 00 0D 00 01
    //###### 写 ######//
    String SET_METER_POWER = "40241";
    Integer SET_METER_POWER_OFFECT = 240; // 计量表功率设置。指令：01 06 00 F0 + 两位的数值
    String SET_INVERTER_OUTPUT_ONGRID_POWER = "40242";
    Integer SET_INVERTER_OUTPUT_ONGRID_POWER_OFFECT = 241; // 逆变器输出并网功率设置 指令：01 06 00 F1 + 两位的数值
    String SET_START_CHARGE = "40243";
    Integer SET_START_CHARGE_OFFECT = 242; // 充电开始时间。高字节小时，低字节分钟 指令：01 06 00 F2 + 两位的数值
    String SET_END_CHARGE = "40244";
    Integer SET_END_CHARGE_OFFECT = 243; // 充电结束时间。高字节小时，低字节分钟 指令：01 06 00 F3 + 两位的数值
    String SET_START_DISCHARGE = "40245";
    Integer SET_START_DISCHARGE_OFFECT = 244; // 放电开始时间。高字节小时，低字节分钟 指令：01 06 00 F4 + 两位的数值
    String SET_END_DISCHARGE = "40246";
    Integer SET_END_DISCHARGE_OFFECT = 245; // 放电结束时间。高字节小时，低字节分钟 指令：01 06 00 F5 + 两位的数值
    String SET_CHARGE_POWER_PERCENTAGE = "40237";
    Integer SET_CHARGE_POWER_PERCENTAGE_OFFECT = 236; // 充电功率百分比。数值0-100 指令：01 06 00 EC + 两位的数值
    String SET_DISCHARGE_POWER_PERCENTAGE = "40238";
    Integer SET_DISCHARGE_POWER_PERCENTAGE_OFFECT = 237; // 放电功率百分比。数值0-100 指令：01 06 EF F0 + 两位的数值
    String SET_INVERTER_CHARGE_VOLTAGE = "40232";
    Integer SET_INVERTER_CHARGE_VOLTAGE_OFFECT = 231; // 充电电压设置。单位V，数值100-590，精度0.1 指令：01 06 00 E7 + 两位的数值
    String SET_INVERTER_CHARGE_CURRENT = "40233";
    Integer SET_INVERTER_CHARGE_CURRENT_OFFECT = 232; // 充电电流设置。单位A，数值0-10000，精度0.1 指令：01 06 00 E8 + 两位的数值
    String SET_INVERTER_CHARGE_POWER = "40234";
    Integer SET_INVERTER_CHARGE_POWER_OFFECT = 233; // 充电功率设置。单位W，数值0-5000，精度1 指令：01 06 00 E9 + 两位的数值
    String SET_INVERTER_DISCHARGE_CURRENT = "40235";
    Integer SET_INVERTER_DISCHARGE_CURRENT_OFFECT = 234; // 放电电流设置。单位A，数值0-10000，精度0.1 指令：01 06 00 EA + 两位的数值
    String SET_INVERTER_DISCHARGE_POWER = "40236";
    Integer SET_INVERTER_DISCHARGE_POWER_OFFECT = 235; // 放电功率设置。单位W，数值0-5000，精度1 指令：01 06 00 EB + 两位的数值

    //------ 电池侧485通讯地址：02；即slaveId=2 ------//
    //*** 电池参数 ***//

    //------ 计量侧485通讯地址：03；即slaveId=3 ------//
    String GET_ONGRID_POWER = "40103";
    Integer GET_ONGRID_POWER_OFFECT = 102;  // 读取计量表的并网功率，然后取到值后设置到地址40241 单位KW 精度0.001 指令： 03 04 00 00 66 00 01
}