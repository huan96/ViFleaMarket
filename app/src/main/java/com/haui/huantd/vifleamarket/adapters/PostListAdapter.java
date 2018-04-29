package com.haui.huantd.vifleamarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Product;

import java.util.List;

/**
 * Created by huand on 02/10/18.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ItemPost> {
    private RequestOptions options = new RequestOptions();
    private List<Product> list;
    private Context mContext;
    private OnItemClick onItemClick;

    public PostListAdapter(List<Product> list, Context mContext, OnItemClick onItemClick) {
        this.list = list;
        this.mContext = mContext;
        this.onItemClick = onItemClick;
        options.centerCrop();
    }

    @Override
    public ItemPost onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ItemPost(itemView);
    }

    @Override
    public void onBindViewHolder(ItemPost holder, int position) {
        Product product = list.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice() + "đ");
        holder.tvInfo.setText(product.getTime() + " | " + product.getIdDistrict());
        Glide.with(mContext).load(product.getListImage().get(0)).apply(options).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClick {
        void onClick(int position);
    }

    class ItemPost extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvPrice;
        TextView tvInfo;
        ImageView img;

        public ItemPost(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvInfo = itemView.findViewById(R.id.tv_info);
            img = itemView.findViewById(R.id.img_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(getLayoutPosition());
        }
    }

}


