package com.beetech.serialport.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.beetech.module.KeepAliveConnection;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.client.ClientConnectManager;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.receiver.ConnectReceiver;
import com.beetech.serialport.receiver.WakeReceiver;
import com.beetech.serialport.thread.ThreadModuleInit;
import com.beetech.serialport.thread.ThreadModuleReceive;
import com.beetech.serialport.utils.ServiceAliveUtils;
import org.apache.mina.core.session.IoSession;

public class ModuleService extends Service {
    private final static String TAG = ModuleService.class.getSimpleName();
    public final static ConnectReceiver conncetReceiver = new ConnectReceiver();

    private AppLogSDDao appLogSDDao;
    private MyApplication myApp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new KeepAliveConnection.Stub() {};
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "---->onCreate");

        //  服务保活 start
        startGuardService();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(conncetReceiver , filter);

        Intent innerIntent = new Intent(this, GrayInnerService.class);
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startService(innerIntent);
        startForeground(Constant.GRAY_SERVICE_ID, notification);

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.ACTION);
        alarmIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent operation = PendingIntent.getBroadcast(this, Constant.WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constant.ALARM_INTERVAL, operation);

        //  服务保活 end

        if(appLogSDDao == null){
            appLogSDDao = new AppLogSDDao(this);
        }
        appLogSDDao.save(TAG + " onCreate");

        myApp = (MyApplication)getApplicationContext();

        /* 启动VT服务端通信服务 */
        IoSession session = myApp.session;
        if(session == null || !session.isConnected()){
            ClientConnectManager.getInstance(this).connect();
        }

        //串口模块上电初始化
        if(myApp.threadModuleInit == null){
            myApp.threadModuleInit = ThreadModuleInit.getInstance();
            myApp.threadModuleInit.init(this);
            myApp.threadModuleInit.start();
        }

        handler.postDelayed(runnable, 3000);
     }

    private Handler handler = new Handler(){};
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //接收串口数据
            if(myApp.threadModuleReceive == null) {
                myApp.threadModuleReceive = ThreadModuleReceive.getInstance();
                myApp.threadModuleReceive.init(ModuleService.this);
                myApp.threadModuleReceive.start();
            }

//            //发送状态数据到VT网关
//            if(myApp.threadSendVtState == null) {
//                myApp.threadSendVtState = ThreadSendVtState.getInstance();
//                myApp.threadSendVtState.init(ModuleService.this);
//                myApp.threadSendVtState.start();
//            }
//
//            //发送温度数据到VT网关
//            if(myApp.threadSendShtrf == null) {
//                myApp.threadSendShtrf = ThreadSendShtrf.getInstance();
//                myApp.threadSendShtrf.init(ModuleService.this);
//                myApp.threadSendShtrf.start();
//            }

        }
    };

    public void startGuardService() {
        Intent intent = new Intent();
        intent.setClass(this, GuardService.class);
        startService(intent);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "StepService:建立链接");
            boolean isServiceRunning = ServiceAliveUtils.isServiceRunning(ModuleService.this, "com.beetech.serialport.service.ScreenCheckService");
            if (!isServiceRunning) {
                Intent i = new Intent(ModuleService.this, ScreenCheckService.class);
                startService(i);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开链接
            startService(new Intent(ModuleService.this, RemoteService.class));
            // 重新绑定
            bindService(new Intent(ModuleService.this, RemoteService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appLogSDDao.save(TAG+" onStartCommand");

        // 绑定建立链接
        bindService(new Intent(this, RemoteService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "---->onDestroy");
        appLogSDDao.save(TAG+" onDestroy");
        super.onDestroy();

        Intent intent = new Intent(ConnectReceiver.ACTION);
        sendBroadcast(intent);
        unregisterReceiver( conncetReceiver );
        Log.d(TAG, "sendBroadcast[" + ConnectReceiver.ACTION + "]");
    }



    private Handler handlerToast = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Object toastMsg = msg.obj;
            if(toastMsg != null){
                Toast.makeText(getApplicationContext(), toastMsg.toString(), Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "InnerService -> onStartCommand");
            Notification notification = new Notification();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(Constant.GRAY_SERVICE_ID, notification);
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "InnerService -> onDestroy");
            super.onDestroy();
        }
    }

}
