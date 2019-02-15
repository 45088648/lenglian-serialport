package com.beetech.serialport.bean;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import java.util.Date;

/**
 * 传感器实时数据
 */
@Table(name="read_data_realtime")
public class ReadDataRealtime {
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id
    @Column(name = "_id")
    private int _id;

    @Column(name = "sensor_id", type = "String")
    private String sensorId; //传感器ID, 传感器编号

    @Column(name = "sensor_data_time", type="DATETIME")
    private Date sensorDataTime; // 本条数据的采集时间，BCD码，格式：“年 月 日 时 分 秒”。

    @Column(name = "temp")
    private double temp; // 温度；

    @Column(name = "temp1")
    private double temp1; //

    @Column(name = "rh")
    private double rh; // 湿度, T = (MSB*256+LSB)/100，单位：%；

    @Column(name = "rh1")
    private double rh1; // 湿度1

    public ReadDataRealtime(){}

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Date getSensorDataTime() {
        return sensorDataTime;
    }

    public void setSensorDataTime(Date sensorDataTime) {
        this.sensorDataTime = sensorDataTime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public double getTemp1() {
        return temp1;
    }

    public void setTemp1(double temp1) {
        this.temp1 = temp1;
    }

    public double getRh1() {
        return rh1;
    }

    public void setRh1(double rh1) {
        this.rh1 = rh1;
    }
}
