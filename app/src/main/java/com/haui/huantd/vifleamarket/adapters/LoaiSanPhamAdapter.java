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
import com.haui.huantd.vifleamarket.models.LoaiSanPham;

import java.util.List;

public class LoaiSanPhamAdapter extends RecyclerView.Adapter<LoaiSanPhamAdapter.ItemLoaiSanPham> {

    private List<LoaiSanPham> mListLoaiSanPham;
    private OnItemClick onItemClick;
    private Context mContext;

    public LoaiSanPhamAdapter(List<LoaiSanPham> mListLoaiSanPham, Context mContext, OnItemClick onItemClick) {
        this.mListLoaiSanPham = mListLoaiSanPham;
        this.onItemClick = onItemClick;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemLoaiSanPham onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_danh_muc, parent, false);
        return new ItemLoaiSanPham(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLoaiSanPham holder, int position) {
        LoaiSanPham loaiSanPham = mListLoaiSanPham.get(position);
        holder.tvName.setText(loaiSanPham.getName());
    }

    @Override
    public int getItemCount() {
        return mListLoaiSanPham.size();
    }

    class ItemLoaiSanPham extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        public ItemLoaiSanPham(View itemView) {
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
