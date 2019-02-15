package com.beetech.serialport.dao;

import android.content.Context;

public class BaseSDDaoUtils {
    private Context mContext;

    private ReadDataSDDao readDataSDDao;
    private ReadDataRealtimeSDDao readDataRealtimeSDDao;
    private ModuleBufSDDao moduleBufSDDao;
    private AppLogSDDao appLogSDDao;
    private QueryConfigSDDao queryConfigSDDao;
    private QueryConfigRealtimeSDDao queryConfigRealtimeSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;

    public BaseSDDaoUtils(Context mContext){
        this.mContext = mContext;
        readDataSDDao = new ReadDataSDDao(mContext);
        moduleBufSDDao = new ModuleBufSDDao(mContext);
        appLogSDDao = new AppLogSDDao(mContext);
        queryConfigSDDao = new QueryConfigSDDao(mContext);
        readDataRealtimeSDDao = new ReadDataRealtimeSDDao(mContext);
        queryConfigRealtimeSDDao = new QueryConfigRealtimeSDDao(mContext);
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);
    }

    public void trancateAll(){
        readDataSDDao.truncate();
        moduleBufSDDao.truncate();
        appLogSDDao.truncate();
        queryConfigSDDao.truncate();
        readDataRealtimeSDDao.truncate();
        queryConfigRealtimeSDDao.truncate();
        vtSocketLogSDDao.truncate();
    }

    public void trancateLog(){
        moduleBufSDDao.truncate();
        appLogSDDao.truncate();
        queryConfigSDDao.truncate();
        vtSocketLogSDDao.truncate();
    }
}
