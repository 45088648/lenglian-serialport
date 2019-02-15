package com.beetech.serialport.constant;

import android.graphics.Color;

import java.text.SimpleDateFormat;

public class Constant {
    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFromat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");

    public final static int module = 4;
    public final static int baudrate = 19200;

    /**
     * 定时唤醒的时间间隔
     */
    public final static int ALARM_INTERVAL = 1000 * 60 * 5;
    public final static int WAKE_REQUEST_CODE = 6666;
    public final static int GRAY_SERVICE_ID = -1001;


    // 监测点背景设
    public static final int SENSOR_COLOR = Color.parseColor("#0673B4");
    public static final int SENSOR_COLOR1 = Color.parseColor("#DE5F50");
    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");
    public static final int COLOR_BLUE = Color.parseColor("#33B5E5");
    public static final int COLOR_VIOLET = Color.parseColor("#AA66CC");
    public static final int COLOR_GREEN = Color.parseColor("#99CC00");
    public static final int COLOR_ORANGE = Color.parseColor("#FFBB33");
    public static final int COLOR_RED = Color.parseColor("#FF4444");
//    public static final int[] COLORS = new int[]{SENSOR_COLOR, SENSOR_COLOR1, COLOR_BLUE, COLOR_VIOLET, COLOR_GREEN, COLOR_ORANGE, COLOR_RED};
    public static final int[] COLORS = new int[]{COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE, COLOR_BLUE};

    public static final String PACKAGE_NAME = "";

    public static String verName;
    public static String imei;
    public static String phoneNumber;
}
