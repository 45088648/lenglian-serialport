package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.bean.AppLog;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.db.DBSDHelper;
import java.util.List;
import java.util.Date;

public class AppLogSDDao extends AbDBDaoImpl<AppLog> {

    public AppLogSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public AppLogSDDao(Context context) {
        super(new DBSDHelper(context), AppLog.class);
        exeSQL("create unique index if not exists app_log_id_index on app_log_t(_id)");

    }

    public Long saveToDB(AppLog appLog) {
        if (appLog == null) {
            return null;
        }
        return insert(appLog);
    }

    public Long save(String content){
        AppLog appLog = new AppLog(content);
        startReadableDatabase(false);
        long id = saveToDB(appLog);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(AppLog appLog) {
        if (appLog == null) {
            return;
        }
        update(appLog);
    }

    public List<AppLog> query(Date inputTimeBegin, Date inputTimeEnd) {
        List<AppLog> list = null;
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
        exeSQL("DELETE FROM app_log_t");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'app_log_t'");
    }
}
