package com.beetech.serialport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.support.annotation.Nullable;
import com.beetech.serialport.service.ScreenCheckService;
import com.beetech.serialport.utils.ScreenManager;
import com.beetech.serialport.utils.ServiceAliveUtils;

public class SinglePixelActivity extends Activity {
    private static final String TAG = SinglePixelActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        ScreenManager.getInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (!ServiceAliveUtils.isServiceRunning(this, "com.beetech.serialport.service.ScreenCheckService")) {
            Intent intentAlive = new Intent(this, ScreenCheckService.class);
            startService(intentAlive);
        }
        super.onDestroy();
    }
}