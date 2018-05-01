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
import com.haui.huantd.vifleamarket.models.DanhMuc;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ItemDanhMuc> {

    private List<DanhMuc> mListDanhMuc;
    private OnItemClick onItemClick;
    private Context mContext;

    public DanhMucAdapter(List<DanhMuc> mListDanhMuc, Context mContext, OnItemClick onItemClick) {
        this.mListDanhMuc = mListDanhMuc;
        this.onItemClick = onItemClick;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemDanhMuc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_danh_muc, parent, false);
        return new ItemDanhMuc(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDanhMuc holder, int position) {
        DanhMuc danhMuc = mListDanhMuc.get(position);
        holder.tvName.setText(danhMuc.getName());
    }

    @Override
    public int getItemCount() {
        return mListDanhMuc.size();
    }

    class ItemDanhMuc extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        public ItemDanhMuc(View itemView) {
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
