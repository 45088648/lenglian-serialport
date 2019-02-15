package com.beetech.serialport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.beetech.serialport.service.GuardService;
import com.beetech.serialport.service.ModuleService;

public class ConnectReceiver extends BroadcastReceiver {
 
    private final static String TAG = ConnectReceiver.class.getSimpleName();
    public final static String ACTION = "com.beetech.module.receiver.CONNECT_SERVICE";

    public ConnectReceiver() {
    }
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent mIntent = new Intent();
        mIntent.setClass( context , ModuleService.class );
        context.startService( mIntent );
        Log.d(TAG, "start ModuleService");
 
        Intent intent1 = new Intent();
        intent1.setClass( context , GuardService.class);
        context.startService( intent1 );
        Log.d(TAG, "start GuardService");
    }
}
