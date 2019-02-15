package com.beetech.serialport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beetech.module.R;
import com.beetech.serialport.bean.ModuleBuf;
import java.text.SimpleDateFormat;
import java.util.List;

public class ModuleBufRvAdapter extends RecyclerView.Adapter<ModuleBufRvAdapter.ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<ModuleBuf> mList;

    public ModuleBufRvAdapter(List<ModuleBuf> data) {
        this.mList = data;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_buf_list_item, parent, false);
        ModuleBufRvAdapter.ViewHolder viewHolder = new ModuleBufRvAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ModuleBufRvAdapter.ViewHolder holder, int position) {
        ModuleBuf moduleBuf = mList.get(position);
        holder.tvId.setText(moduleBuf.get_id()+"");
        holder.tvType.setText(moduleBuf.getType() == 0 ? "写" : "读");
        String cmd = "其他";
        switch (moduleBuf.getCmd()){
            case 1:
                cmd = "配置";
             case 4:
                cmd = "授时";
             case 7:
                cmd = "数据";
            default:cmd = "其他";
        }
        holder.tvCmd.setText(cmd);
        holder.tvBuf.setText(moduleBuf.getBufHex());
        holder.tvResult.setText(moduleBuf.isResult() ? "成功" : "失败");
        holder.tvInputTime.setText(dateFormat.format(moduleBuf.getInputTime()));
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvType;
        public TextView tvCmd;
        public TextView tvBuf;
        public TextView tvResult;
        public TextView tvInputTime;

        public ViewHolder(View convertView) {
            super(convertView);

            tvId = (TextView) convertView.findViewById(R.id.tvId);
            tvType = (TextView) convertView.findViewById(R.id.tvType);
            tvCmd = (TextView) convertView.findViewById(R.id.tvCmd);
            tvBuf = (TextView) convertView.findViewById(R.id.tvBuf);
            tvResult = (TextView) convertView.findViewById(R.id.tvResult);
            tvInputTime = (TextView) convertView.findViewById(R.id.tvInputTime);
        }
    }

}
