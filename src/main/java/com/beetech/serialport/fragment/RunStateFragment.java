package com.beetech.serialport.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.beetech.module.R;
import com.beetech.serialport.activity.MainActivity;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.client.ConnectUtils;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.BaseSDDaoUtils;
import com.beetech.serialport.utils.ModuleUtils;
import com.beetech.serialport.utils.NetUtils;
import com.beetech.serialport.utils.ServiceAliveUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import org.apache.mina.core.session.IoSession;
import java.util.Date;

public class RunStateFragment extends Fragment {
    private static final String TAG = RunStateFragment.class.getSimpleName();

    private MainActivity mContext;
    MyApplication myApp;

    @ViewInject(R.id.stateTv)
    TextView stateTv;

    @ViewInject(R.id.btnRefresh)
    Button btnRefresh;

    @ViewInject(R.id.btnTruncateLog)
    Button btnTruncateLog;

    @ViewInject(R.id.btnTruncateAll)
    Button btnTruncateAll;

    @ViewInject(R.id.btnReboot)
    private Button btnReboot;
    private ModuleUtils moduleUtils;

    private int refreshInterval = 1000*10; //刷新数据线程启动间隔
    private BaseSDDaoUtils baseSDDaoUtils;
    public Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.run_state_fragment, container, false);
        ViewUtils.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        myApp = (MyApplication)mContext.getApplicationContext();
        baseSDDaoUtils = new BaseSDDaoUtils(mContext);
        moduleUtils = new ModuleUtils(mContext);

        // 启动刷新运行状态
        getState();
//        handlerRefresh.removeCallbacks(runnableRefresh);
//        handlerRefresh.postDelayed(runnableRefresh, refreshInterval);
        handlerRefreshRight.removeCallbacks(runnableRefreshRight);
        handlerRefreshRight.postDelayed(runnableRefreshRight, 1000);

        if(progressDialog == null){
            progressDialog = new Dialog(mContext, R.style.progress_dialog);
            progressDialog.setContentView(R.layout.dialog);
            progressDialog.setCancelable(true);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("加载中");
        }
    }

    @OnClick(R.id.btnRefresh)
    public void btnRefresh_onClick(View v) {
        getState();
    }


    @OnClick(R.id.btnTruncateLog)
    public void btnTruncateLog_onClick(View v) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(mContext);
        builder.setMessage("确定要清空全部日志数据吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                TruncateLogAsyncTask task = new TruncateLogAsyncTask();
                task.execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    class TruncateLogAsyncTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            if(progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            baseSDDaoUtils.trancateLog();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @OnClick(R.id.btnTruncateAll)
    public void btnTruncateAll_onClick(View v) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(mContext);
        builder.setMessage("确定要清空全部数据吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                TruncateAllAsyncTask task = new TruncateAllAsyncTask();
                task.execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    class TruncateAllAsyncTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            if(progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            baseSDDaoUtils.trancateAll();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @OnClick(R.id.btnReboot)
    public void btnReboot_OnClick(View v) {
        String bootMsg = btnReboot.getText().toString();
        AlertDialog.Builder builder = new  AlertDialog.Builder(mContext);
        builder.setMessage("确定要 "+bootMsg+" 吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(myApp.initResult){
                    moduleUtils.free();
                    btnReboot.setText("开始监控");
                } else {
                    moduleUtils.init();
                    btnReboot.setText("结束监控");
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //定时刷新运行状态
    private Handler handlerRefresh = new Handler(){};
    Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            try {
                getState();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlerRefresh.postDelayed(this, refreshInterval);
        }
    };

    public void getState(){
        try {

            StringBuffer stateSb = new StringBuffer(ConnectUtils.stringNowTime()).append(": ");
            stateSb.append("APP版本:").append(Constant.verName).append(" ");
            stateSb.append("ModuleService: ").append(ServiceAliveUtils.isServiceRunning(mContext,"com.beetech.serialport.service.ModuleService") ? "运行" : "未运行") .append(" ");

            stateSb.append("网络：").append(NetUtils.network_type_name.get(myApp.netWorkType)).append(" ");
            stateSb.append("信号：").append(myApp.signalStrength).append(" dbm");
            stateSb.append("\n");
            stateSb.append("IMEI：").append(Constant.imei).append(" ");
            stateSb.append("\n");
            String moduleName = myApp.module+"";
            moduleName = moduleName.substring(moduleName.lastIndexOf(".")+1);
            stateSb.append("模块：").append(moduleName.equals("")? "空" : moduleName).append(" ");
            stateSb.append(myApp.initResult ? "上电" : "释放").append(" ").append(Constant.sdf.format(new Date(myApp.initTime))).append(" ");
            stateSb.append("读：").append(Constant.sdf.format(new Date(myApp.lastReadTime))).append(" ");
            stateSb.append("\n");


            IoSession mSession = myApp.session;
            stateSb.append("会话：").append(mSession).append("\n");
            if(mSession != null){
                stateSb.append("连接：").append(mSession.isConnected() ? "连通" : "断开").append(" ");
                stateSb.append("创建：").append(Constant.sdf.format(new Date(mSession.getCreationTime()))).append(" ");
                stateSb.append("IO：").append(Constant.sdf.format(new Date(mSession.getLastIoTime()))).append(" ");
                stateSb.append("Idle：").append(Constant.sdf.format(new Date(mSession.getLastBothIdleTime()))).append(" ");
            }
            stateSb.append("\n");

            stateSb.append("初始化：").append(myApp.threadModuleInit);
            if(myApp.threadModuleInit != null){
                stateSb.append(" ").append(myApp.threadModuleInit.isAlive() ? "alive" : "");
                stateSb.append(" ").append(Constant.sdf.format(new Date(myApp.threadModuleInit.runTime)));
                stateSb.append(" ").append(myApp.threadModuleInit.NUM);
            }
            stateSb.append("\n");

            stateSb.append("读串口：").append(myApp.threadModuleReceive);
            if(myApp.threadModuleReceive != null){
                stateSb.append(" ").append(myApp.threadModuleReceive.isAlive() ? "alive" : "");
                stateSb.append(" ").append(Constant.sdf.format(new Date(myApp.threadModuleReceive.runTime)));
                stateSb.append(" ").append(myApp.threadModuleReceive.NUM);
            }
            stateSb.append("\n");

            stateSb.append("发温度：").append(myApp.threadSendShtrf);
            if(myApp.threadSendShtrf != null){
                stateSb.append(" ").append(myApp.threadSendShtrf.isAlive() ? "alive" : "");
                stateSb.append(" ").append(Constant.sdf.format(new Date(myApp.threadSendShtrf.runTime)));
                stateSb.append(" ").append(myApp.threadSendShtrf.NUM);
            }
            stateSb.append("\n");

            stateSb.append("发状态：").append(myApp.threadSendVtState);
            if(myApp.threadSendVtState != null){
                stateSb.append(" ").append(myApp.threadSendVtState.isAlive() ? "alive" : "");
                stateSb.append(" ").append(Constant.sdf.format(new Date(myApp.threadSendVtState.runTime)));
                stateSb.append(" ").append(myApp.threadSendVtState.NUM);
            }
            stateSb.append("\n");

            stateTv.setText(stateSb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handlerRefreshRight = new Handler(){};
    Runnable runnableRefreshRight = new Runnable() {
        @Override
        public void run() {
            try {
                if(myApp.initResult){
                    btnReboot.setText("结束监控");
                } else {
                    btnReboot.setText("开始监控");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlerRefreshRight.postDelayed(this, 3000);
        }
    };

}
