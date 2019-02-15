package com.beetech.serialport.bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import java.util.Date;

@Table(name = "app_log_t")
public class AppLog {
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name="input_time", type="DATETIME")
    Date inputTime;

    @Column(name="content", type="String")
    private String content;

    public AppLog(){}

    public AppLog(String content){
        this.content = content;
        inputTime = new Date();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
