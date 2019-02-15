package com.beetech.serialport.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.beetech.module.R;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.QueryConfigRealtimeSDDao;
import com.beetech.serialport.fragment.HistoryAppLogFragment;
import com.beetech.serialport.fragment.HistoryModuleBufFragment;
import com.beetech.serialport.fragment.HistoryReadDataFragment;
import com.beetech.serialport.fragment.HistoryVtSocketLogFragment;
import com.beetech.serialport.fragment.RunStateFragment;
import com.beetech.serialport.service.DownloadService;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;

public class MainActivity extends AppCompatActivity {

	private final static String TAG = MainActivity.class.getSimpleName();

	private FragmentTabHost mTabHost;
	private FragmentManager fm;
	private AppLogSDDao appLogSDDao;
	private QueryConfigRealtimeSDDao queryConfigRealtimeSDDao;
    private int refreshDelayedInMs = 1000; //发送传感器数据线程启动延时
    private int refreshIntervalInMs = 1000*60; //发送传感器数据线程启动间隔
    private DownloadService ds;
    private TitleBar mTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        TitleBar.initStyle(new TitleBarLightStyle(this));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕唤醒
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mTitleBar = (TitleBar) findViewById(R.id.tb_main_title_bar);
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RealtimeMonitorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onTitleClick(View v) {
                Toast.makeText(MainActivity.this, "中间View被点击", Toast.LENGTH_SHORT);
            }

            @Override
            public void onRightClick(View v) {
                Toast.makeText(MainActivity.this, "右项View被点击", Toast.LENGTH_SHORT);
            }
        });

		fm = getSupportFragmentManager();
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, fm, R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("identification").setIndicator("运行状态"), RunStateFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("history").setIndicator("温度数据"), HistoryReadDataFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("bufHistory").setIndicator("串口通信"), HistoryModuleBufFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("appHistory").setIndicator("APP日志"), HistoryAppLogFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("vtSocketHistory").setIndicator("冷链网关通信"), HistoryVtSocketLogFragment.class, null);

		appLogSDDao = new AppLogSDDao(this);
		queryConfigRealtimeSDDao = new QueryConfigRealtimeSDDao(this);
//		appLogSDDao.save("MainActivity onCreate");

        //自动更新
        try {
            PackageManager manager = getPackageManager();
            ActivityInfo activityInfo = manager.getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            PackageManager pm = MainActivity.this.getPackageManager();//context为当前Activity上下文
            PackageInfo pi = pm.getPackageInfo(MainActivity.this.getPackageName(), 0);
            String appVersion = pi.versionName; // 版本名

            String updateInfoUrl = activityInfo.metaData.getString("update_url");
            Log.i(TAG, updateInfoUrl);// 更新客户端版本信息

            ds = new DownloadService(this, handler, appVersion, updateInfoUrl, "/sdcard/");
            handler.sendEmptyMessage(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    try {
                        ds.checkUpdateByUrl();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }

                    break;
                default:
                    break;
            }
        }
    };

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//        appLogSDDao.save("MainActivity onResume");
	}

}
