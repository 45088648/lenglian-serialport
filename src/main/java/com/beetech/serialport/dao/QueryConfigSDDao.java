package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.code.response.QueryConfigResponse;
import com.beetech.serialport.db.DBSDHelper;
import java.util.List;

public class QueryConfigSDDao extends AbDBDaoImpl<QueryConfigResponse> {

    public QueryConfigSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public QueryConfigSDDao(Context context) {
        super(new DBSDHelper(context), QueryConfigResponse.class);
        exeSQL("create unique index if not exists query_config_id_index on query_config_t(_id)");

    }

    public Long saveToDB(QueryConfigResponse queryConfigResponse) {
        if (queryConfigResponse == null) {
            return null;
        }
        return insert(queryConfigResponse);
    }

    public Long save(QueryConfigResponse queryConfigResponse){
        startWritableDatabase(false);
        if (queryConfigResponse == null) {
            return null;
        }
        Long id = insert(queryConfigResponse);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(QueryConfigResponse queryConfigResponse) {
        if (queryConfigResponse == null) {
            return;
        }
        update(queryConfigResponse);
    }

    public List<QueryConfigResponse> query(String gwId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<QueryConfigResponse> list = null;
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

    public QueryConfigResponse queryLast(String gwId) {
        // (1)获取数据库
        startReadableDatabase(false);
        List<QueryConfigResponse> list = null;
        // (2)执行查询
        if(gwId != null && gwId.length() > 0){
            list = queryList(null, "gwId = ? ", new String[]{gwId}, null, null, "_id desc limit 1 offset 0" , null);
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
        exeSQL("DELETE FROM query_config_t");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'query_config_t'");
    }
}
