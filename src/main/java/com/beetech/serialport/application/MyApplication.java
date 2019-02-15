package com.beetech.serialport.application;

import android.app.Application;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.handler.CrashHandler;
import com.beetech.serialport.thread.ThreadModuleInit;
import com.beetech.serialport.thread.ThreadModuleReceive;
import com.beetech.serialport.thread.ThreadSendShtrf;
import com.beetech.serialport.thread.ThreadSendVtState;
import com.beetech.serialport.utils.APKVersionCodeUtils;
import com.beetech.serialport.utils.MobileInfoUtil;
import com.beetech.serialport.utils.PhoneInfoUtils;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.rscja.deviceapi.Module;
import org.apache.mina.core.session.IoSession;

public class MyApplication extends Application {
    public Module module;
    public boolean initResult; //模块上电初始化结果
    public long createTime;
    public long initTime;
    public long lastReadTime;
    public long lastWriteTime;

    public int batteryPercent = 0; // 电量百分比
    public int power = 1;// 0断开  1接通

    public int netWorkType = 0;// 网络类型
    public int signalStrength = 0;// 信号强度

    public String gwId = "00000000";
    public int serialNo = 0;
    public long readDataResponseTime;

    public ThreadModuleReceive threadModuleReceive;
    public ThreadModuleInit threadModuleInit;

    public ThreadSendShtrf threadSendShtrf;
    public ThreadSendVtState threadSendVtState;

    public IoSession session;

    @Override
    public void onCreate() {
        super.onCreate();

        final CrashHandler appException = CrashHandler.getInstance();
        appException.init(getApplicationContext());

        new ANRWatchDog().setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                // Handle the error. For example, log it to HockeyApp:
                appException.saveCrashInfo2File(error);
            }
        }).start();

        Constant.verName = APKVersionCodeUtils.getVerName(this);
        Constant.imei = MobileInfoUtil.getIMEI(this);
        Constant.phoneNumber = new PhoneInfoUtils(this).getNativePhoneNumber();
    }

}
