package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;
import com.haui.huantd.vifleamarket.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddProductActivity";
    private ImageView btnClose, btnAddImage, imgShow;
    private LinearLayout layoutAddImage, layoutShowImage;
    private TextView tvDanhMuc, tvLoaiSP, tvTinh, tvHuyen;
    private TextView tvGia, tvTieuDe, tvMoTa;
    private Button btnDangBan;
    private String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initComponents();
    }

    private void initComponents() {
        btnClose = findViewById(R.id.btn_back);
        btnAddImage = findViewById(R.id.btn_add_image);
        layoutAddImage = findViewById(R.id.layout_add_image);
        layoutShowImage = findViewById(R.id.layout_show_image);
        imgShow = findViewById(R.id.img_image);
        tvDanhMuc = findViewById(R.id.tv_danh_muc);
        tvLoaiSP = findViewById(R.id.tv_loai_sp);
        tvTinh = findViewById(R.id.tv_tinh);
        tvHuyen = findViewById(R.id.tv_huyen);
        tvGia = findViewById(R.id.tv_gia);
        tvTieuDe = findViewById(R.id.tv_tieu_de);
        tvMoTa = findViewById(R.id.tv_mo_ta);
        btnDangBan = findViewById(R.id.btn_dang_ban);

        btnClose.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        tvDanhMuc.setOnClickListener(this);
        tvLoaiSP.setOnClickListener(this);
        tvTinh.setOnClickListener(this);
        tvHuyen.setOnClickListener(this);
        tvGia.setOnClickListener(this);
        tvTieuDe.setOnClickListener(this);
        tvMoTa.setOnClickListener(this);
        btnDangBan.setOnClickListener(this);
        imgShow.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_image:
            case R.id.img_image:
                startActivity(new Intent(AddProductActivity.this, AddImagesActivity.class));
                finish();
                break;
            case R.id.tv_danh_muc:
                startActivity(new Intent(AddProductActivity.this, DanhMucActivity.class));
                finish();
                break;
            case R.id.tv_loai_sp:
                startActivity(new Intent(AddProductActivity.this, DanhMucActivity.class));
                finish();
                break;
            case R.id.tv_tinh:
                startActivity(new Intent(AddProductActivity.this, KhuVucActivity.class));
                finish();
                break;
            case R.id.tv_huyen:
                startActivity(new Intent(AddProductActivity.this, KhuVucActivity.class));
                finish();
                break;
            case R.id.tv_gia:
                startActivity(new Intent(AddProductActivity.this, GiaActivity.class));
                finish();
                break;
            case R.id.tv_tieu_de:
                startActivity(new Intent(AddProductActivity.this, TieuDeActivity.class));
                finish();
                break;
            case R.id.tv_mo_ta:
                startActivity(new Intent(AddProductActivity.this, MoTaChiTietActivity.class));
                finish();
                break;
            case R.id.btn_dang_ban:
                dangBan();
                break;
        }
    }

    private void dangBan() {
        //check su dung dan
        if (!checkFullInfor()) {
            return;
        }
        //them sp
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.NON_CONFIRM_POST);
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Product product = new Product();
        product.setIdNguoiBan(FirebaseAuth.getInstance().getCurrentUser().getUid());
        product.setTieuDe(PreferencesManager.getTieuDe(this));
        product.setGia(PreferencesManager.getGia(this));
        product.setChiTiet(PreferencesManager.getThongTin(this));
        product.setDanhMuc(PreferencesManager.getDanhMuc(this));
        product.setLoaiSP(PreferencesManager.getLoaiSP(this));
        product.setTinh(PreferencesManager.getTinh(this));
        product.setHuyen(PreferencesManager.getHuyen(this));
        product.setUrlImage(PreferencesManager.getUrlImage(this));
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMMAT);
        Calendar cal = Calendar.getInstance();
        product.setThoiGian(dateFormat.format(cal.getTime()));
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid() + dateFormat.format(cal.getTime());
        databaseReference.child(id).setValue(product);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        databaseReference2.child(UserID).child(Constants.LIST_POSTS).child(id).setValue(id);

        Toast.makeText(this, "Sản phẩm đã được đăng và chờ phê duyệt", Toast.LENGTH_SHORT).show();
        //reset
        Util.ResessPrefernces(this);
        finish();
    }

    private boolean checkFullInfor() {
        if (PreferencesManager.getTieuDe(this).equals("")) {
            Toast.makeText(this, R.string.hay_nhap_tieu_de, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (PreferencesManager.getGia(this).equals("")) {
            Toast.makeText(this, R.string.hay_nhap_gia, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (PreferencesManager.getThongTin(this).equals("")) {
            Toast.makeText(this, R.string.hay_nhap_mo_ta, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (PreferencesManager.getDanhMuc(this).equals("")) {
            Toast.makeText(this, R.string.hay_chon_danh_muc, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (PreferencesManager.getTinh(this).equals("")) {
            Toast.makeText(this, R.string.hay_chon_khu_vuc, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (PreferencesManager.getPathImage(this).equals("")) {
            Toast.makeText(this, R.string.hay_them_anh, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvDanhMuc.setText(PreferencesManager.getDanhMuc(this));
        tvLoaiSP.setText(PreferencesManager.getLoaiSP(this));
        tvTinh.setText(PreferencesManager.getTinh(this));
        tvHuyen.setText(PreferencesManager.getHuyen(this));
        tvGia.setText(PreferencesManager.getGia(this));
        tvTieuDe.setText(PreferencesManager.getTieuDe(this));
        tvMoTa.setText(PreferencesManager.getThongTin(this));
        showListImage();
    }


    private void showListImage() {
        String url = PreferencesManager.getPathImage(this);
        if (!url.equals("")) {
            layoutShowImage.setVisibility(View.VISIBLE);
            layoutAddImage.setVisibility(View.GONE);
            Glide.with(this).load(Uri.parse(url)).into(imgShow);
            Log.e(TAG, "showListImage: ");
        } else {
            Toast.makeText(this, R.string.chua_chon_anh, Toast.LENGTH_SHORT).show();
            layoutShowImage.setVisibility(View.GONE);
            layoutAddImage.setVisibility(View.VISIBLE);
        }
    }
}


