package com.beetech.serialport.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.beetech.module.R;
import com.beetech.serialport.activity.MainActivity;
import com.beetech.serialport.adapter.ReadDataRvAdapter;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.List;

public class HistoryReadDataFragment extends Fragment {

    private static final String TAG = "HistoryReadDataFragment";

    private MainActivity mContext;

    private ReadDataSDDao dao;
    // 列表数据
    private List<ReadDataResponse> readDataList = new ArrayList<ReadDataResponse>();

    // 每一页显示的行数
    public int pageSize = 100;
    // 当前页数
    public int pageNum = 1;

    private ReadDataRvAdapter rvAdapter;

    @ViewInject(R.id.rvData)
    RecyclerView rvData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_read_data_fragment,  container, false);
        ViewUtils.inject(this, v);
        return v;
    }
    private Handler handler = new Handler(){};

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            initData();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        init();
//        handler.postDelayed(task, 1000*60*5);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void init() {
        registerForContextMenu(rvData);
    }

    private void initData() {
        dao = new ReadDataSDDao(mContext);
        // (1)获取数据库
        dao.startReadableDatabase(false);
        // (2)执行查询
        readDataList = dao.queryList(null, null, null, null, null,
                "_id desc limit " + String.valueOf(pageSize)+ " offset " + 0, null);
        // (3)关闭数据库
        dao.closeDatabase(false);

        rvData.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvAdapter = new ReadDataRvAdapter(readDataList);
        rvData.setAdapter(rvAdapter);
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
}
