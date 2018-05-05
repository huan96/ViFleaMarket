package com.haui.huantd.vifleamarket.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.dialogs.DeleteFileDialog;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.Util;

import java.util.List;

/**
 * Created by huand on 02/10/18.
 */

public class SellProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SellProductListAdapter";
    private RequestOptions options = new RequestOptions();
    private List<Product> list;
    private Context mContext;
    private OnItemClick onItemClick;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public SellProductListAdapter(List<Product> list, Context mContext, OnItemClick onItemClick) {
        this.list = list;
        this.mContext = mContext;
        this.onItemClick = onItemClick;
        options.centerCrop();
        options.placeholder(R.drawable.spinner_background);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_post_sell, parent, false);
        return new ItemPost(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Product product = list.get(position);
            ItemPost viewHolder = (ItemPost) holder;
            viewHolder.tvName.setText(product.getTieuDe());
            viewHolder.tvPrice.setText(product.getGia() + mContext.getString(R.string.VND));
            String thoiGian = Util.getThoiGian(product.getThoiGian());
            String huyen = "null";
            if (product.getHuyen().equals("")) {
                huyen = product.getTinh();
            } else {
                huyen = product.getHuyen();
            }
            viewHolder.tvInfo.setText(thoiGian + " " + mContext.getString(R.string.space) + " " + huyen);
            if (!product.isConfirm()) {
                viewHolder.tvTrangThai.setText(R.string.chua_duyet);
                viewHolder.tvTrangThai.setTextColor(ContextCompat.getColor(mContext, R.color.color_price));
            } else {
                viewHolder.tvTrangThai.setText(R.string.da_duyet);
                viewHolder.tvTrangThai.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            }
            Glide.with(mContext).load(product.getUrlImage()).apply(options).into(viewHolder.img);
            //xu lu thong tin trang thai va delete
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: " + e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    class ItemPost extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvPrice;
        TextView tvInfo;
        ImageView img;
        ImageView imgDelete;
        TextView tvTrangThai;

        public ItemPost(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvInfo = itemView.findViewById(R.id.tv_info);
            img = itemView.findViewById(R.id.img_image);
            imgDelete = itemView.findViewById(R.id.btn_delete);
            tvTrangThai = itemView.findViewById(R.id.tv_trang_thai);
            img = itemView.findViewById(R.id.img_image);
            itemView.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (v.equals(imgDelete)) {
                    DeleteFileDialog deleteFileDialog = new DeleteFileDialog(mContext, new DeleteFileDialog.OnButtonClicked() {
                        @Override
                        public void onDeleteClicked() {
                            String idPost = list.get(getLayoutPosition()).getId();
                            Log.e(TAG, idPost);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();
                            databaseReference.child(Constants.USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child(Constants.LIST_POSTS).child(idPost).removeValue();
                            if (!list.get(getLayoutPosition()).isConfirm()) {
                                databaseReference.child(Constants.NON_CONFIRM_POST).child(idPost).removeValue();
                            } else {
                                databaseReference.child(Constants.CONFIRM_POST).child(idPost).removeValue();
                            }
                            Log.e(TAG, "onClick: " + list.get(getLayoutPosition()).getId());
                            list.remove(getLayoutPosition());
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                    deleteFileDialog.show();
                } else {
                    onItemClick.onClick(getLayoutPosition());
                }
            } catch (Exception e) {
                Log.e(TAG, "onClick: " + e.toString());
            }
        }
    }


}


