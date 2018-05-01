package com.haui.huantd.vifleamarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Tinh;

import java.util.List;

public class TinhAdapter extends RecyclerView.Adapter<TinhAdapter.ItemTinh> {

    private List<Tinh> mListTinh;
    private OnItemClick onItemClick;
    private Context mContext;

    public TinhAdapter(List<Tinh> mListTinh, Context mContext, OnItemClick onItemClick) {
        this.mListTinh = mListTinh;
        this.onItemClick = onItemClick;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemTinh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_danh_muc, parent, false);
        return new ItemTinh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTinh holder, int position) {
        Tinh danhMuc = mListTinh.get(position);
        holder.tvName.setText(danhMuc.getName());
    }

    @Override
    public int getItemCount() {
        return mListTinh.size();
    }

    class ItemTinh extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        public ItemTinh(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(getLayoutPosition());
        }
    }
}
