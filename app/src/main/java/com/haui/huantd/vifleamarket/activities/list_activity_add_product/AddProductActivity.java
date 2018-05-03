package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.AddHinhAnhAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.ImageManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddProductActivity";
    private ImageView btnClose, btnAddImage;
    private LinearLayout layoutAddImage, layoutShowImage;
    private RecyclerView rvImage;
    private TextView tvDanhMuc, tvLoaiSP, tvTinh, tvHuyen;
    private TextView tvGia, tvTieuDe, tvMoTa;
    private Button btnDangBan;
    private List<String> listImage;
    private AddHinhAnhAdapter hinhAnhAdapter;

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
        rvImage = findViewById(R.id.rv_images);
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
        listImage = new ArrayList<>();
        hinhAnhAdapter = new AddHinhAnhAdapter(listImage, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(AddProductActivity.this, AddImagesActivity.class));
            }
        });
        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImage.setAdapter(hinhAnhAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_image:
                startActivity(new Intent(AddProductActivity.this, AddImagesActivity.class));
                break;
            case R.id.tv_danh_muc:
                startActivity(new Intent(AddProductActivity.this, DanhMucActivity.class));
                break;
            case R.id.tv_loai_sp:
                startActivity(new Intent(AddProductActivity.this, DanhMucActivity.class));
                break;
            case R.id.tv_tinh:
                startActivity(new Intent(AddProductActivity.this, KhuVucActivity.class));
                break;
            case R.id.tv_huyen:
                startActivity(new Intent(AddProductActivity.this, KhuVucActivity.class));
                break;
            case R.id.tv_gia:
                startActivity(new Intent(AddProductActivity.this, GiaActivity.class));
                break;
            case R.id.tv_tieu_de:
                startActivity(new Intent(AddProductActivity.this, TieuDeActivity.class));
                break;
            case R.id.tv_mo_ta:
                startActivity(new Intent(AddProductActivity.this, MoTaChiTietActivity.class));
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
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.NON_CONFIRM_POST);
        String UserID = FirebaseAuth.getInstance().getUid();
        final Product product = new Product();
        product.setIdNguoiBan(FirebaseAuth.getInstance().getUid());
        product.setTieuDe(PreferencesManager.getTieuDe(this));
        product.setGia(PreferencesManager.getGia(this));
        product.setChiTiet(PreferencesManager.getThongTin(this));
        product.setDanhMuc(PreferencesManager.getDanhMuc(this));
        product.setLoaiSP(PreferencesManager.getLoaiSP(this));
        product.setTinh(PreferencesManager.getTinh(this));
        product.setHuyen(PreferencesManager.getHuyen(this));
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMMAT);
        Calendar cal = Calendar.getInstance();
        product.setThoiGian(dateFormat.format(cal.getTime()));
        final String id = FirebaseAuth.getInstance().getUid() + dateFormat.format(cal.getTime());
        databaseReference.child(id).setValue(product);


        //them vao list hinh Anh
        try {
            int size = listImage.size();
            for (int i = 0; i < size; i++) {
                String imageUri = listImage.get(i);
                String idImage = id + i;
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagePost").child((idImage + ".jpg"));
                Log.e("Add_image", String.valueOf(imageUri));
                UploadTask uploadTask = storageRef.putFile(Uri.parse(imageUri));
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String name = taskSnapshot.getMetadata().getName();
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child(id).child(Constants.LIST_IMAGES).child(name).setValue(url);
                    }
                });
            }

        } catch (Exception e) {
            Log.e(TAG, "dangBan: " + e.toString());
        }

        //them vao list bai dang chua xac nhan cua nguoi dung
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        databaseReference2.child(UserID).child(Constants.LIST_PRODUCT_CONFIRM).child(id).setValue(id);
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
        if (listImage.size() == 0) {
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

        listImage.clear();
        ImageManager imageManager = new ImageManager(this);
        listImage.addAll(imageManager.getAllImage());
        Log.e(TAG, "onResume: " + listImage.size());
        showListImage();
    }

    private void showListImage() {
        if (listImage.size() > 0) {
            layoutShowImage.setVisibility(View.VISIBLE);
            layoutAddImage.setVisibility(View.GONE);
            hinhAnhAdapter.notifyDataSetChanged();
        } else {
            layoutShowImage.setVisibility(View.GONE);
            layoutAddImage.setVisibility(View.VISIBLE);
        }
    }
}


   /* protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                RequestOptions options = new RequestOptions();
                options.centerCrop();
                Glide.with(this).load(imageUri).apply(options).into(img);
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
                Calendar cal = Calendar.getInstance();
                String id = FirebaseAuth.getInstance().getUid() + dateFormat.format(cal.getTime());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagePost").child((id + ".jpg"));
                Log.e("xxx", String.valueOf(imageUri));
                UploadTask uploadTask = storageRef.putFile(imageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, R.string.no_selected_photo, Toast.LENGTH_SHORT).show();
        }
    }


} */
