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
import com.haui.huantd.vifleamarket.models.Huyen;

import java.util.List;

public class HuyenAdapter extends RecyclerView.Adapter<HuyenAdapter.ItemHuyen> {

    private List<Huyen> mListHuyen;
    private OnItemClick onItemClick;
    private Context mContext;

    public HuyenAdapter(List<Huyen> mListHuyen, Context mContext, OnItemClick onItemClick) {
        this.mListHuyen = mListHuyen;
        this.onItemClick = onItemClick;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemHuyen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_danh_muc, parent, false);
        return new ItemHuyen(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHuyen holder, int position) {
        Huyen danhMuc = mListHuyen.get(position);
        if (danhMuc.getType().equals("Huyện")) {
            holder.tvName.setText(danhMuc.getName());
        } else {
            holder.tvName.setText(danhMuc.getType() + " " + danhMuc.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mListHuyen.size();
    }

    class ItemHuyen extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;

        public ItemHuyen(View itemView) {
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
