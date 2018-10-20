package com.turingoal.bjcorona.ses.app;

import android.content.Context;

import com.turingoal.common.android.base.TgBaseUserPreferences;

/**
 * 用户数据_参数保存服务
 */

public class TgUserPreferences extends TgBaseUserPreferences {
    public TgUserPreferences(final Context context, final String spName) {
        super(context, spName);
    }

    /*** 设置ip */
    public void setIp(final String ip) {
        sharedPreferences.edit().putString("server_ip", ip).commit();
    }

    /*** 获取ip */
    public String getIp() {
        return sharedPreferences.getString("server_ip", TgAppConfig.SERVER_IP);
    }

    /*** 设置port */
    public void setPort(final Integer port) {
        sharedPreferences.edit().putInt("server_port", port).apply();
    }

    /*** 获取port */
    public int getPort() {
        return sharedPreferences.getInt("server_port", TgAppConfig.SERVER_PORT);
    }

    /*** 设置用户登录状态*/
    public void setUserLoginStatus(final boolean loginStatus) {
        sharedPreferences.edit().putBoolean("loginStatus", loginStatus).commit();
    }

    /*** 获取用户登录状态 */
    public boolean getUserLoginStatus() {
        return sharedPreferences.getBoolean("loginStatus", false); // 默认fasle
    }
}
