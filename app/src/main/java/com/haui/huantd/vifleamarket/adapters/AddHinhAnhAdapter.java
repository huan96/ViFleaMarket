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
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;

import java.util.List;

public class AddHinhAnhAdapter extends RecyclerView.Adapter<AddHinhAnhAdapter.ItemHinhAnh> {

    private List<String> mListHinhAnh;
    private OnItemClick onItemClick;
    private Context mContext;

    public AddHinhAnhAdapter(List<String> mListHinhAnh, Context mContext, OnItemClick onItemClick) {
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
        Glide.with(mContext).load(Uri.parse(url)).thumbnail((float) 0.1).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mListHinhAnh.size();
    }

    class ItemHinhAnh extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;

        public ItemHinhAnh(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_image);
            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(getLayoutPosition());
        }
    }

}
