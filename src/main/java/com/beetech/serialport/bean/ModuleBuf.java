package com.beetech.serialport.bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import com.beetech.serialport.utils.ByteUtilities;
import java.util.Date;

@Table(name = "module_buf_t")
public class ModuleBuf {
    @Id
    @Column(name = "_id")
    private int _id;

    byte[] buf;

    @Column(name="bufHex", type="String")
    String bufHex;

    @Column(name="type")
    int type; // 0: 请求; 1: 响应

    @Column(name="input_time", type="DATETIME")
    Date inputTime;

    @Column(name="cmd")
    int cmd;

    @Column(name = "result")
    boolean result;

    public ModuleBuf(){}

    public ModuleBuf(byte[] buf, int type, int cmd, boolean result){
        this.buf = buf;
        this.bufHex = ByteUtilities.asHex(buf).toUpperCase();
        this.type = type;
        this.inputTime = new Date();
        this.cmd = cmd;
        this.result = result;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    public String getBufHex() {
        return bufHex;
    }

    public void setBufHex(String bufHex) {
        this.bufHex = bufHex;
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

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
