package com.beetech.serialport.db;

import android.content.Context;
import android.util.Log;
import com.ab.db.orm.AbSDDBHelper;
import com.beetech.serialport.bean.AppLog;
import com.beetech.serialport.bean.ModuleBuf;
import com.beetech.serialport.bean.QueryConfigRealtime;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.bean.vt.VtSocketLog;
import com.beetech.serialport.code.response.QueryConfigResponse;
import com.beetech.serialport.code.response.ReadDataResponse;

public class DBSDHelper extends AbSDDBHelper {
    private static final String TAG = "DBSDHelper";
    // 数据库名
    private static final String DBNAME = "module.db";
    // 数据库 存放路径
    private static final String DBPATH = "module/DB";

    // 当前数据库的版本
    private static final int DBVERSION = 1;
    // 要初始化的表
    private static final Class<?>[] clazz = {QueryConfigResponse.class, ReadDataResponse.class, ModuleBuf.class, AppLog.class, QueryConfigRealtime.class, ReadDataRealtime.class, VtSocketLog.class};

    public DBSDHelper(Context context) {

        super(context, DBPATH, DBNAME, null, DBVERSION, clazz);
        Log.i(TAG, "DBPATH:" + DBPATH);
    }

}
