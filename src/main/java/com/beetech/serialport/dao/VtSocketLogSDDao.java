package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.bean.vt.VtSocketLog;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.db.DBSDHelper;
import java.util.Date;
import java.util.List;

public class VtSocketLogSDDao extends AbDBDaoImpl<VtSocketLog> {

    public VtSocketLogSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public VtSocketLogSDDao(Context context) {
        super(new DBSDHelper(context), VtSocketLog.class);
        exeSQL("create unique index if not exists vt_socket_log_id_index on vt_socket_log_t(_id)");
    }

    public Long saveToDB(VtSocketLog vtSocketLog) {
        if (vtSocketLog == null) {
            return null;
        }
        return insert(vtSocketLog);
    }

    public Long save(String text, int type, int dataId, String threadName){
        VtSocketLog vtSocketLog = new VtSocketLog(text, type, dataId, threadName);
        startReadableDatabase(false);
        long id = saveToDB(vtSocketLog);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(VtSocketLog vtSocketLog) {
        if (vtSocketLog == null) {
            return;
        }
        startReadableDatabase(false);
        update(vtSocketLog);
        closeDatabase(false);
    }

    public List<VtSocketLog> query(Date inputTimeBegin, Date inputTimeEnd) {
        List<VtSocketLog> list = null;
        if(inputTimeBegin != null && inputTimeEnd != null){
            list = queryList("inputTime between ? and ? ", new String[]{Constant.sdf.format(inputTimeBegin), Constant.sdf.format(inputTimeEnd)});
        }else {
            list = queryList("", new String[]{});
        }

        if (list == null || list.size() < 1) {
            return null;
        }
        return list;
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
        exeSQL("DELETE FROM vt_socket_log_t");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'vt_socket_log_t'");
    }
}
