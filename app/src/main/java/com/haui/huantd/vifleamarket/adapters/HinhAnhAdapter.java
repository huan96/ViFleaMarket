package com.haui.huantd.vifleamarket.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haui.huantd.vifleamarket.R;

import java.util.List;

public class HinhAnhAdapter extends RecyclerView.Adapter<HinhAnhAdapter.ItemHinhAnh> {

    private List<String> mListHinhAnh;
    private OnImageClick onItemClick;
    private Context mContext;

    public HinhAnhAdapter(List<String> mListHinhAnh, Context mContext, OnImageClick onItemClick) {
        this.mListHinhAnh = mListHinhAnh;
        this.onItemClick = onItemClick;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemHinhAnh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_add_image, parent, false);
        return new ItemHinhAnh(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHinhAnh holder, int position) {
        String url = mListHinhAnh.get(position);
        if (!url.equals("")) {
            Glide.with(mContext).load(Uri.parse(url)).thumbnail((float) 0.1).into(holder.img);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onImageClick();
                }
            });
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mListHinhAnh.size();
    }

    class ItemHinhAnh extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        ImageView btnDelete;

        public ItemHinhAnh(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_image);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onDeleteClick(getLayoutPosition());
        }
    }

    public interface OnImageClick {
        void onImageClick();

        void onDeleteClick(int position);
    }
}
