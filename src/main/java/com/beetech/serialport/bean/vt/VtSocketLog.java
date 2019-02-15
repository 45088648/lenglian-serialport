package com.beetech.serialport.bean.vt;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import java.util.Date;

/**
 * VT 网关通信日志
 */
@Table(name = "vt_socket_log_t")
public class VtSocketLog {
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name="text", type="String")
    private String text;

    @Column(name="type")
    private int type; // 0 in, 1 out

    @Column(name="input_time", type="DATETIME")
    Date inputTime;

    @Column(name = "data_id")
    private int dataId;

    @Column(name="thread_name", type="String")
    private String threadName;

    public VtSocketLog(){}
    public VtSocketLog(String text, int type, int dataId, String threadName){
        this.text = text;
        this.type = type;
        this.dataId = dataId;
        this.inputTime = new Date();
        this.threadName = threadName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
