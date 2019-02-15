package com.beetech.serialport.thread;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.vt.VtRequestBean;
import com.beetech.serialport.client.ClientConnectManager;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import com.google.gson.Gson;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import java.util.List;

public class ThreadSendShtrf extends Thread {

    private final static String TAG = ThreadSendShtrf.class.getSimpleName();
    public final static int INTERVAL = 1000*13;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadSendShtrf instance;

    public synchronized static ThreadSendShtrf getInstance() {
        if (null == instance) {
            synchronized(ThreadSendShtrf.class){
                instance = new ThreadSendShtrf();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private ReadDataSDDao readDataSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;
    private MyApplication myApp;
    private int queryCount = 30;
    private Gson gson = new Gson();

    public void init(Context mContext){
        this.mContext = mContext;
        readDataSDDao = new ReadDataSDDao(mContext);
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);

        myApp = (MyApplication) mContext.getApplicationContext();
        NUM = 0;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {
                IoSession mSession = myApp.session;
                if(mSession != null && mSession.isConnected()){
                    List<ReadDataResponse> readDataResponseList = readDataSDDao.queryForSend(queryCount, 0);
                    if(readDataResponseList != null && !readDataResponseList.isEmpty()){
                        for (ReadDataResponse readDataResponse : readDataResponseList){
                            VtRequestBean vtRequestBean = new VtRequestBean(readDataResponse);

                            String inText = gson.toJson(vtRequestBean);
                            //保存日志
                            try {
                                vtSocketLogSDDao.save(inText, 0, readDataResponse.get_id(), Thread.currentThread().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            WriteFuture writeResult= mSession.write(inText);

                            writeResult.addListener(new IoFutureListener() {
                                public void operationComplete(IoFuture future) {
                                    WriteFuture wfuture = (WriteFuture) future;
                                    // 写入成功  
                                    if (wfuture.isWritten()) {
                                        return;
                                    }
                                    // 写入失败，自行进行处理  
                                }
                            });

                        }
                    }

                } else {
                    ClientConnectManager.getInstance(myApp).rePeatConnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;//捕获到异常之后，执行break跳出循环。
            }
        }

    }
}
