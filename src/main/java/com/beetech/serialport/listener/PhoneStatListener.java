package com.beetech.serialport.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.utils.NetUtils;

//获取信号强度
public class PhoneStatListener extends PhoneStateListener {
    private Context mContext;
    public PhoneStatListener(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onSignalStrengthChanged(int asu) {
        super.onSignalStrengthChanged(asu);
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        //获取网络信号强度
        int cdmaDbm = signalStrength.getCdmaDbm();
        int evdoDbm = signalStrength.getEvdoDbm();
//        System.out.println("cdmaDbm=====" + cdmaDbm);
//        System.out.println("evdoDbm=====" + evdoDbm);

        int gsmSignalStrength = signalStrength.getGsmSignalStrength();
        int dbm = -113 + 2 * gsmSignalStrength;
//        System.out.println("dbm===========" + dbm);

        //获取网络类型
        int netWorkType = NetUtils.getNetworkState(mContext);
        MyApplication myApplication = (MyApplication)mContext.getApplicationContext();

        myApplication.netWorkType = netWorkType;
        myApplication.signalStrength = dbm;

        switch (netWorkType) {
            case NetUtils.NETWORK_WIFI: // 当前网络为wifi
                break;
            case NetUtils.NETWORK_2G: //  当前网络为2G移动网络
                break;
            case NetUtils.NETWORK_3G: // 当前网络为3G移动网络
                break;
            case NetUtils.NETWORK_4G: // 当前网络为4G移动网络
                break;
            case NetUtils.NETWORK_NONE: // 当前没有网络
                break;
            case -1: //当前网络错误
                break;
        }
    }
}