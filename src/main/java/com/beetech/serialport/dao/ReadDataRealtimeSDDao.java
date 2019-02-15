package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.db.DBSDHelper;

import java.util.ArrayList;
import java.util.List;

public class ReadDataRealtimeSDDao extends AbDBDaoImpl<ReadDataRealtime> {

    public ReadDataRealtimeSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public ReadDataRealtimeSDDao(Context context) {
        super(new DBSDHelper(context), ReadDataRealtime.class);
        exeSQL("create unique index if not exists read_data_realtime_id_index on read_data_realtime(_id)");
        exeSQL("create unique index if not exists read_data_realtime_sensor_id_index on read_data_realtime(sensor_id)");
    }

    public Long saveToDB(ReadDataRealtime readDataRealtime) {
        if (readDataRealtime == null) {
            return null;
        }
        return insert(readDataRealtime);
    }

    public Long save(ReadDataRealtime readDataRealtime){
        startWritableDatabase(false);
        if (readDataRealtime == null) {
            return null;
        }
        Long id = insert(readDataRealtime);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(ReadDataRealtime readDataRealtime) {
        startWritableDatabase(false);
        if (readDataRealtime == null) {
            return;
        }
        update(readDataRealtime);
        closeDatabase(false);
    }

    public List<ReadDataRealtime> query(String sensorId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataRealtime> list = null;
        // (2)执行查询
        if(sensorId != null && sensorId.length() > 0){
            list = queryList(null, "sensor_id = ? ", new String[]{sensorId}, null, null, null, "1");
        }else {
            list = queryList(null, null, null, null, null, "sensor_id", null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return new ArrayList<ReadDataRealtime>();
        }
        return list;
    }

    public int queryCount() {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataRealtime> list = null;
        // (2)执行查询
        Integer count  = queryCount(null, null);
        // (3)关闭数据库
        closeDatabase(false);
        return count == null ? 0 : count;
    }

    public ReadDataRealtime queryLast() {
        // (1)获取数据库
        startReadableDatabase(false);

        // (2)执行查询
        List<ReadDataRealtime> list = queryList(null, null, null, null, null, "_id desc limit 1 offset 0" , null);

        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void exeSQL(String sql){
        startWritableDatabase(false);
        try {
            execSql(sql, null);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        closeDatabase(false);
    }

    public void truncate(){
        exeSQL("DELETE FROM read_data_realtime");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'read_data_realtime'");
    }
}
