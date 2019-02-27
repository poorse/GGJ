package com.ruoyu.pigroad.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyu.pigroad.myapplication.Bean.OilListBean;
import com.ruoyu.pigroad.myapplication.R;

import java.util.List;

/**
 * Created by PIGROAD on 2018/11/17
 * Email:920015363@qq.com
 */
public class OilListAdapter extends RecyclerView.Adapter<OilListAdapter.ViewHolder> {

    private Context context;
    private List<OilListBean> datas;
    private OnItemClickLitener mOnItemClickLitener;

    public OilListAdapter(Context context, List<OilListBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @NonNull
    @Override
    public OilListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_oilstation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OilListAdapter.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;

            if (mOnItemClickLitener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }

            holder.tv_oil_name.setText(datas.get(position).getName());
            holder.tv_oil_address.setText(datas.get(position).getAddress());
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_oil_name, tv_oil_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_oil_name = itemView.findViewById(R.id.tv_oil_name);
            tv_oil_address = itemView.findViewById(R.id.tv_oil_address);
        }
    }
}
