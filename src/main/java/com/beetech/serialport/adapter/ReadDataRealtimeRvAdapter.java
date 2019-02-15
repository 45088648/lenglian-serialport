package com.beetech.serialport.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beetech.module.R;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.constant.Constant;
import java.util.List;

public class ReadDataRealtimeRvAdapter extends RecyclerView.Adapter<ReadDataRealtimeRvAdapter.ViewHolder> {

    List<ReadDataRealtime> mList;

    public ReadDataRealtimeRvAdapter(List<ReadDataRealtime> mList){
        this.mList = mList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_data_realtime_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ReadDataRealtime readDataRealtime = mList.get(position);

        holder.tvSensorId.setText(readDataRealtime.getSensorId());
        holder.tvTemp.setText(readDataRealtime.getTemp()+"℃");
        holder.tvTemp1.setText(readDataRealtime.getTemp1()+"℃");
        holder.tvRh.setText(readDataRealtime.getRh()+"%RH");
        holder.tvRh1.setText(readDataRealtime.getRh1()+"%RH");
        holder.tvSensorDataTime.setText(Constant.sdf.format(readDataRealtime.getSensorDataTime()));
        setText(holder, position);

        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }

    public void setText(ViewHolder holder, final int position){

        //动态设置监测点背景色
//        holder.ll.setBackgroundColor(Constant.COLORS[position % (Constant.COLORS.length-1)]);
        int color = Constant.COLORS[position % (Constant.COLORS.length-1)];
        holder.tvSensorId.setTextColor(color);
        holder.tvRh.setTextColor(color);
        holder.tvRh1.setTextColor(color);

        holder.tvTemp.setTextColor(color);
        holder.tvTemp1.setTextColor(color);

        holder.tvSensorDataTime.setTextColor(color);
        if(mList.size() <= 2){
            holder.tvSensorId.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.tvRh.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.tvRh1.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

            holder.tvTemp.setTextSize(TypedValue.COMPLEX_UNIT_SP,36);
            holder.tvTemp1.setTextSize(TypedValue.COMPLEX_UNIT_SP,36);

            holder.tvSensorDataTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll;
        public TextView tvSensorId;
        public TextView tvTemp;
        public TextView tvTemp1;
        public TextView tvRh;
        public TextView tvRh1;
        public TextView tvSensorDataTime;

        public ViewHolder(View convertView) {
            super(convertView);

            ll = (LinearLayout) convertView.findViewById(R.id.ll);
            tvSensorId = (TextView) convertView.findViewById(R.id.tvSensorId);
            tvTemp = (TextView) convertView.findViewById(R.id.tvTemp);
            tvRh = (TextView) convertView.findViewById(R.id.tvRh);
            tvTemp1 = (TextView) convertView.findViewById(R.id.tvTemp1);
            tvRh1 = (TextView) convertView.findViewById(R.id.tvRh1);
            tvSensorDataTime = (TextView) convertView.findViewById(R.id.tvSensorDataTime);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
}
