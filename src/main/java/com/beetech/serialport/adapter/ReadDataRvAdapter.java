package com.beetech.serialport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beetech.module.R;
import com.beetech.serialport.code.response.ReadDataResponse;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReadDataRvAdapter extends RecyclerView.Adapter<ReadDataRvAdapter.ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<ReadDataResponse> mList;

    public ReadDataRvAdapter(List<ReadDataResponse> data) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_data_list_item, parent, false);
        ReadDataRvAdapter.ViewHolder viewHolder = new ReadDataRvAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReadDataRvAdapter.ViewHolder holder, int position) {
        ReadDataResponse readData = mList.get(position);
        holder.tvId.setText(readData.get_id()+"");
        holder.tvSensorId.setText(readData.getSensorId());
        holder.tvTemp.setText(readData.getTemp()+"℃");
        holder.tvTemp1.setText(readData.getTemp1()+"℃");

        holder.tvRh.setText(readData.getRh()+"%RH");
        holder.tvRh1.setText(readData.getRh1()+"%RH");
        holder.tvSensorDataTime.setText(dateFormat.format(readData.getSensorDataTime()));
        holder.tvSendFlag.setText(readData.getSendFlag() == 0 ? "否" : "是");
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvSensorId;
        public TextView tvTemp;
        public TextView tvTemp1;
        public TextView tvRh;
        public TextView tvRh1;
        public TextView tvSensorDataTime;
        public TextView tvSendFlag;

        public ViewHolder(View convertView) {
            super(convertView);

            tvId = (TextView) convertView.findViewById(R.id.tvId);

            tvSensorId = (TextView) convertView.findViewById(R.id.tvSensorId);
            tvTemp = (TextView) convertView.findViewById(R.id.tvTemp);
            tvTemp1 = (TextView) convertView.findViewById(R.id.tvTemp1);
            tvRh = (TextView) convertView.findViewById(R.id.tvRh);
            tvRh1 = (TextView) convertView.findViewById(R.id.tvRh1);

            tvSensorDataTime = (TextView) convertView.findViewById(R.id.tvSensorDataTime);

            tvSendFlag = (TextView) convertView.findViewById(R.id.tvSendFlag);
        }
    }

}
