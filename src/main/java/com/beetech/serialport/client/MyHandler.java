package com.beetech.serialport.client;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.bean.vt.VtResponseBean;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyHandler extends IoHandlerAdapter {

    private final String TAG = MyHandler.class.getSimpleName();

    private Context mContext;
    private AppLogSDDao appLogSDDao;
    private ReadDataSDDao readDataSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;
    Gson gson = new Gson();

    public MyHandler(Context context) {
        this.mContext = context;

        readDataSDDao = new ReadDataSDDao(mContext);
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Log.d(TAG,  "接收到服务器端消息：" + message.toString());

        String msg = message.toString();
        if(msg != null && !msg.isEmpty()){
            //更新传感器数据
            try {
                VtResponseBean vtResponseBean = gson.fromJson(msg, VtResponseBean.class);
                if(vtResponseBean != null){

                    //保存日志
                    try {
                        vtSocketLogSDDao.save(msg, 1, vtResponseBean.getId(), Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String cmd = vtResponseBean.getCmd();
                    boolean success = vtResponseBean.getSuccess();

                    if("SHTRF".equals(cmd) && success){
                        Integer id = vtResponseBean.getId();
                        ReadDataResponse readDataResponse = readDataSDDao.queryById(id);
                        if(readDataResponse != null){
                            readDataResponse.setSendFlag(1);
                            readDataSDDao.updateToDB(readDataResponse);
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用messageSent" + message.toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionCreated");
        session.getConfig().setBothIdleTime(ConnectUtils.IDLE_TIME);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionIdle");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionOpened");
    }
}
