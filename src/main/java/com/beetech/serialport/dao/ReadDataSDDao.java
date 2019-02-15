package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.db.DBSDHelper;
import java.util.List;
import java.util.Date;

public class ReadDataSDDao extends AbDBDaoImpl<ReadDataResponse> {

    public ReadDataSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public ReadDataSDDao(Context context) {
        super(new DBSDHelper(context), ReadDataResponse.class);
        exeSQL("create unique index if not exists read_data_id_index on read_data_t(_id)");
        exeSQL("create index if not exists read_data_send_flag_index on read_data_t(send_flag)");
        exeSQL("create index if not exists read_data_sensor_id_index on read_data_t(sensor_id)");
        exeSQL("create index if not exists read_data_sensor_data_time_index on read_data_t(sensor_data_time)");
        exeSQL("create unique index if not exists read_data_sensor_id_sensor_data_time_index on read_data_t(sensor_id, sensor_data_time)");
    }

    public Long saveToDB(ReadDataResponse readDataResponse) {
        if (readDataResponse == null) {
            return null;
        }
        return insert(readDataResponse);
    }

    public Long save(ReadDataResponse readDataResponse){
        startWritableDatabase(false);
        if (readDataResponse == null) {
            return null;
        }
        Long id = insert(readDataResponse);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(ReadDataResponse readDataResponse) {
        startWritableDatabase(false);
        if (readDataResponse == null) {
            return;
        }
        update(readDataResponse);
        closeDatabase(false);
    }

    public ReadDataResponse queryById(int _id) {
        startReadableDatabase(false);
        List<ReadDataResponse> list = queryList(null, "_id = ? ", new String[]{_id+""}, null, null, null, null);
        if (list == null || list.size() < 1) {
            return null;
        }
        ReadDataResponse readDataResponse = list.get(0);
        closeDatabase(false);
        return readDataResponse;
    }

    public List<ReadDataResponse> query(String sensorId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        if(sensorId != null && sensorId.length() > 0){
            list = queryList(null, "sensor_id = ? ", new String[]{sensorId}, null, null, null, null);
        }else {
            list = queryList(null, "", new String[]{}, null, null, null, null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }


    public ReadDataResponse query(ReadDataResponse readDataResponse) {
        String sensorId = readDataResponse.getSensorId();
        Date sensorDataTime = readDataResponse.getSensorDataTime();
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        if(sensorId != null && sensorId.length() > 0){
            list = queryList(null, "sensor_id = ? and sensor_data_time = ? ", new String[]{sensorId, sensorDataTime.getTime()+""}, null, null, "_id desc limit 1 offset 0" , null);
        }else {
            list = queryList(null, null, null, null, null, "_id desc limit 1 offset 0" , null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<ReadDataResponse> query(String sensorId, int count, int startPosition) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        if(sensorId != null && sensorId.length() > 0){
            list = queryList(null, "sensor_id = ? ", new String[]{sensorId}, null, null, "sensor_data_time desc limit " + String.valueOf(count)+ " offset " + startPosition, null);
        }else {
            list = queryList(null, "", new String[]{}, null, null, "sensor_data_time desc limit " + String.valueOf(count)+ " offset " + startPosition, null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }

    public List<ReadDataResponse> query(String sensorId, long sensorDataTimeInMills, int count, int startPosition) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        if(sensorId != null && sensorId.length() > 0){
            list = queryList(null, "sensor_id = ? and sensor_data_time < ? ", new String[]{sensorId, sensorDataTimeInMills+""}, null, null, "sensor_data_time desc limit " + String.valueOf(count)+ " offset " + startPosition, null);
        }else {
            list = queryList(null, "sensor_data_time < ? ", new String[]{sensorDataTimeInMills+""}, null, null, "sensor_data_time desc limit " + String.valueOf(count)+ " offset " + startPosition, null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }

    public List<ReadDataResponse> queryForSend(int count, int startPosition) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        list = queryList(null, "send_flag = ? ", new String[]{"0"}, null, null, "_id asc limit " + String.valueOf(count)+ " offset " + startPosition, null);

        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }

    public int queryCount(Integer sendFlag) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<ReadDataResponse> list = null;
        // (2)执行查询
        Integer queryCount = null;
        if(sendFlag == null){
            queryCount = queryCount(null, null);
        } else {
            queryCount = queryCount("send_flag = ? ", new String[]{sendFlag+""});
        }

        // (3)关闭数据库
        closeDatabase(false);
        if (queryCount == null) {
            return 0;
        }
        return queryCount.intValue();
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
        exeSQL("DELETE FROM read_data_t");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'read_data_t'");
    }
}
