package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Account;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.Util;

public class ShowProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShowProductActivity";
    private TextView btnCall, btnSend;
    private ImageView btnBack;
    private ToggleButton swThich;
    private ImageView imgProduct;
    private ImageView imgNguoiBan;
    private TextView tvTieuDe, tvGia, tvThoiGian;
    private TextView tvTenNguoiBan, tvDiaChiNguoiBan;
    private TextView tvKhuVuc;
    private TextView tvMoTaChiTiet;
    private Product product;
    private Account currentAccount;
    private RequestOptions options;
    private String sdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        initComponent();
    }

    private void initComponent() {
        btnCall = findViewById(R.id.btn_call);
        btnSend = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.btn_back);
        swThich = findViewById(R.id.sw_like);
        imgProduct = findViewById(R.id.img_product);
        imgNguoiBan = findViewById(R.id.img_avatar);
        tvThoiGian = findViewById(R.id.tv_thoi_gian);
        tvGia = findViewById(R.id.tv_gia);
        tvTenNguoiBan = findViewById(R.id.tv_ten_nguoi_ban);
        tvTieuDe = findViewById(R.id.tv_tieu_de);
        tvDiaChiNguoiBan = findViewById(R.id.tv_dia_chi);
        tvKhuVuc = findViewById(R.id.tv_khu_vuc);
        tvMoTaChiTiet = findViewById(R.id.tv_mo_ta_chi_tiet);
        options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.spinner_background);

        btnCall.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        swThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        Log.e(TAG, "isCheck: ");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.LIST_PRODUCT_LIKE).child(product.getId());
                        databaseReference.setValue(product.getId());
                    } else {
                        Log.e(TAG, "noCheck: ");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.LIST_PRODUCT_LIKE).child(product.getId());
                        databaseReference.removeValue();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onCheckedChanged: " + e.toString());
                }
            }
        });
        btnBack.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfor();
    }

    private void getInfor() {
        try {
            Intent intent = getIntent();
            product = (Product) intent.getBundleExtra(Constants.PRODUCT).getSerializable(Constants.PRODUCT);
            tvTieuDe.setText(product.getTieuDe());
            tvGia.setText(product.getGia() + " " + getString(R.string.VND));
            String thoiGian = Util.getThoiGian(product.getThoiGian());
            tvThoiGian.setText(thoiGian);
            String huyen;
            if (product.getHuyen().equals("")) {
                huyen = product.getTinh();
            } else {
                huyen = product.getHuyen() + ", " + product.getTinh();
            }
            tvKhuVuc.setText(huyen);
            tvMoTaChiTiet.setText(product.getChiTiet());
            Glide.with(this).load(product.getUrlImage()).apply(options).into(imgProduct);

            String idNguoiBan = product.getIdNguoiBan();
            showInforNguoiBan(idNguoiBan);
        } catch (Exception e) {
            Log.e(TAG, "getInfor: " + e.toString());
        }
    }

    private void showInforNguoiBan(String idNguoiBan) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        Query queryUser = databaseReference.orderByChild("uid").equalTo(idNguoiBan);
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do with your result
                        Log.e("showInforNguoiBan", issue.getValue().toString());
                        currentAccount = issue.getValue(Account.class);
                        setValues(currentAccount);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkYeuThich() {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(currentAccount.getUid()).child(Constants.LIST_PRODUCT_LIKE);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String id = (String) dataSnapshot.getValue();
                    if (id.equals(product.getId())) {
                        Log.e(TAG, "checkYeuThich: " + id);
                        swThich.setChecked(true);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "checkYeuThich: " + e.toString());
        }

    }

    private void setValues(Account values) {
        if (values.getName() != null) {
            tvTenNguoiBan.setText(values.getName());
        }
        if (values.getAddress() != null) {
            tvDiaChiNguoiBan.setText(values.getAddress());
        }
        sdt = values.getPhone();
        Glide.with(this).load(values.getUrlAvatar()).apply(options).into(imgNguoiBan);
        checkYeuThich();
    }

    private void SendMessenger() {
        if (sdt == null || sdt.equals("")) {
            Toast.makeText(this, "Không thể nhắn tin!", Toast.LENGTH_SHORT).show();
        } else {
            Uri uri = Uri.parse("smsto:" + sdt);
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", "The SMS text");
            startActivity(it);
        }
    }

    private void Call() {
        if (sdt == null || sdt.equals("")) {
            Toast.makeText(this, "Không thể gọi!", Toast.LENGTH_SHORT).show();
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + sdt));
            if (ActivityCompat.checkSelfPermission(ShowProductActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                Call();
                break;
            case R.id.btn_send:
                SendMessenger();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
