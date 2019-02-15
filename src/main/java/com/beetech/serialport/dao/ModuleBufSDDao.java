package com.beetech.serialport.dao;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.beetech.serialport.bean.ModuleBuf;
import com.beetech.serialport.db.DBSDHelper;
import java.util.List;

public class ModuleBufSDDao extends AbDBDaoImpl<ModuleBuf> {

    public ModuleBufSDDao(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public ModuleBufSDDao(Context context) {

        super(new DBSDHelper(context), ModuleBuf.class);
    }

    public Long saveToDB(ModuleBuf moduleBuf) {
        if (moduleBuf == null) {
            return null;
        }
        return insert(moduleBuf);
    }

    public Long save(byte[] buf, int type, int cmd, boolean result){
        ModuleBuf moduleBuf = new ModuleBuf(buf, type, cmd, result);
        startReadableDatabase(false);
        long id = saveToDB(moduleBuf);
        closeDatabase(false);
        return id;
    }

    public void updateToDB(ModuleBuf moduleBuf) {
        if (moduleBuf == null) {
            return;
        }
        update(moduleBuf);
    }

    public List<ModuleBuf> query(Integer type) {
        List<ModuleBuf> list = null;
        if(type != null){
            list = queryList("type = ? ", new String[]{type+""});
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
        exeSQL("DELETE FROM module_buf_t");
        exeSQL("DELETE FROM sqlite_sequence WHERE name = 'module_buf_t'");
    }
}
