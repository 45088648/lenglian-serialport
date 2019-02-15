package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.bean.QueryConfigRealtime;
import com.beetech.serialport.db.DBSDHelper;
import java.util.List;

public class QueryConfigRealtimeSDDao extends AbDBDaoImpl<QueryConfigRealtime> {

    public QueryConfigRealtimeSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public QueryConfigRealtimeSDDao(Context context) {
        super(new DBSDHelper(context), QueryConfigRealtime.class);

        exeSQL("create unique index if not exists query_config_realtime_id_index on query_config_realtime(_id)");
        exeSQL("create unique index if not exists query_config_realtime_gw_id_index on query_config_realtime(gw_id)");
    }

    public Long saveToDB(QueryConfigRealtime queryConfigRealtime) {
        if (queryConfigRealtime == null) {
            return null;
        }
        return insert(queryConfigRealtime);
    }

    public Long save(QueryConfigRealtime queryConfigRealtime){
        startWritableDatabase(false);
        if (queryConfigRealtime == null) {
            return null;
        }
        Long id = insert(queryConfigRealtime);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(QueryConfigRealtime queryConfigRealtime) {
        startWritableDatabase(false);
        if (queryConfigRealtime == null) {
            return;
        }
        update(queryConfigRealtime);
        closeDatabase(false);
    }

    public List<QueryConfigRealtime> query(String gwId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<QueryConfigRealtime> list = null;
        // (2)执行查询
        if(gwId != null && gwId.length() > 0){
            list = queryList("gw_id = ? ", new String[]{gwId});
        }else {
            list = queryList("", new String[]{});
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
    }

    public QueryConfigRealtime queryLast(String gwId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<QueryConfigRealtime> list = null;
        // (2)执行查询
        if(gwId != null && gwId.length() > 0){
            list = queryList(null, "gw_id = ? ", new String[]{gwId}, null, null, "_id desc limit 1 offset 0" , null);
        }else {
            list = queryList(null, null, null, null, null, "_id desc limit 1 offset 0" , null);
        }
        // (3)关闭数据库
        closeDatabase(false);
        if (list == null || list.size() < 1) {
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
        exeSQL("DELETE FROM query_config_realtime");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'query_config_realtime'");
    }
}
