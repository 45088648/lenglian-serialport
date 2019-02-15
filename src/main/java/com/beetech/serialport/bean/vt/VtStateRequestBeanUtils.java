package com.beetech.serialport.bean.vt;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Formatter;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.QueryConfigRealtime;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.QueryConfigRealtimeSDDao;
import com.beetech.serialport.dao.ReadDataRealtimeSDDao;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.utils.APKVersionCodeUtils;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VtStateRequestBeanUtils {

    private Context mContext;
    private AppLogSDDao appLogSDDao;
    private ReadDataSDDao readDataSDDao;
    private ReadDataRealtimeSDDao readDataRealtimeSDDao;
    private QueryConfigRealtimeSDDao queryConfigRealtimeSDDao;

    private Gson gson = new Gson();
    private MyApplication myApp;

    public VtStateRequestBeanUtils(Context context){
        this.mContext = context;
        myApp = (MyApplication) mContext.getApplicationContext();

        readDataSDDao = new ReadDataSDDao(mContext);
        readDataRealtimeSDDao = new ReadDataRealtimeSDDao(mContext);
        queryConfigRealtimeSDDao = new QueryConfigRealtimeSDDao(mContext);
    }

    public String getMessage(){
        try {
            QueryConfigRealtime queryConfigRealtime = queryConfigRealtimeSDDao.queryLast(null);
            if(queryConfigRealtime != null){
                VtStateRequestBean vtStateRequestBean = new VtStateRequestBean(queryConfigRealtime);
                StateRequestBean body = vtStateRequestBean.getBody();
                body.setBt(myApp.batteryPercent);
                body.setPower(myApp.power);
                body.setCsq(myApp.signalStrength);
                body.setVar(APKVersionCodeUtils.getVerName(mContext));
                body.setGwstate(myApp.initResult ? 1 : 0);

                List<ReadDataRealtime> readDataRealtimeList = readDataRealtimeSDDao.query(null);
                if(readDataRealtimeList != null && !readDataRealtimeList.isEmpty()){
                    List<StateData> data = new ArrayList<>();
                    for (ReadDataRealtime readDataRealtime: readDataRealtimeList) {
                        StateData stateData = new StateData(readDataRealtime);
                        data.add(stateData);
                    }
                    body.setLjs(readDataRealtimeList.size());
                    body.setData(data);
                }

                //获取手机基站信息
                try {
                    TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    String operator = manager.getNetworkOperator();
                    /**通过operator获取 MCC 和MNC */
                    int mcc = Integer.parseInt(operator.substring(0, 3));
                    int mnc = Integer.parseInt(operator.substring(3));

                    if(manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA){
                        GsmCellLocation gsmCellLocation = (GsmCellLocation) manager.getCellLocation();
                        int cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
                        int lac = gsmCellLocation.getLac(); //获取gsm网络编号

                        body.setMnc(mnc);
                        body.setCi(cid);
                        body.setLac(lac);

                        // 获取邻区基站信息
                        List<NeighboringCellInfo> infos = manager.getNeighboringCellInfo();
                        int index = 0;
                        for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
                            lac = info1.getLac(); // 取出当前邻区的LAC
                            cid = info1.getCid(); // 取出当前邻区的CID
                            int rssi = (-113 + 2 * info1.getRssi()); // 获取邻区基站信号强度

                            if(index == 0){
                                body.setMnc1(mnc);
                                body.setCi1(cid);
                                body.setLac1(lac);

                            } else if (index == 1){
                                body.setMnc2(mnc);
                                body.setCi2(cid);
                                body.setLac2(lac);
                            }
                            index++;
                        }

                    }

                    String iccid = manager.getSimSerialNumber();
                    body.setIccid(iccid);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    //获取SD卡可用剩余空间
                    String state = Environment.getExternalStorageState();
                    if(Environment.MEDIA_MOUNTED.equals(state)) {
                        File sdcardDir = Environment.getExternalStorageDirectory();
                        StatFs sf = new StatFs(sdcardDir.getPath());
                        long blockSize = sf.getBlockSize();
                        long blockCount = sf.getBlockCount();
                        long availCount = sf.getAvailableBlocks();
//                        Log.d(TAG, "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//                        Log.d(TAG, "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");
                        long sDFreeSpace = sdcardDir.getFreeSpace();
                        //格式化大小
                        String sDFreeSpaceFormat = Formatter.formatFileSize(mContext, sDFreeSpace);
//                        Log.d(TAG, "剩余空间格式化大小:"+ sDFreeSpaceFormat);

                        body.setUf(Long.valueOf(availCount*blockSize/1024/1024).intValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    int readDataCountNotSend = readDataSDDao.queryCount(0);
                    int readDataCount = readDataSDDao.queryCount(null);

                    body.setDatafile(readDataCount);
                    body.setUnup(readDataCountNotSend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String inText = gson.toJson(vtStateRequestBean);
                return inText;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
