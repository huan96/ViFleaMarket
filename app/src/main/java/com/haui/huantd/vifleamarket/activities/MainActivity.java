package com.haui.huantd.vifleamarket.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.activities.list_activity_add_product.AddProductActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_add_product.DanhMucActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_show.ListFavouriteActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_show.ListSellProductActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_show.ShowListProductActivity;
import com.haui.huantd.vifleamarket.dialogs.RateAppDialog;
import com.haui.huantd.vifleamarket.dialogs.TiepTucTinDialog;
import com.haui.huantd.vifleamarket.models.Account;
import com.haui.huantd.vifleamarket.models.DanhMuc;
import com.haui.huantd.vifleamarket.utils.DataManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;
import com.haui.huantd.vifleamarket.utils.TouchDetectableScrollView;
import com.haui.huantd.vifleamarket.utils.Util;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1234;
    private LinearLayout btnDangBan;
    private CircleImageView imgAvatar;
    private ImageView btnMenu;
    private TextView tvName;
    private TextView tvInfo;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private boolean isLogin;
    private final static String TAG = "MainActivity";
    private TouchDetectableScrollView scrollView;
    private RelativeLayout btnXeCo, btnDoDienTu, btnThoiTrang, btnMevaBe;
    private RelativeLayout btnNoiNgoaiThat, btnGiaiTri, btnThuCung, btnDoVanPhong;
    private RelativeLayout btnViecLam, btnKhac;
    private LinearLayout btnSearch;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        checkPermission();
    }

    private void initComponents() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        imgAvatar = header.findViewById(R.id.img_avatar);
        tvName = header.findViewById(R.id.tv_name);
        tvInfo = header.findViewById(R.id.tv_address);
        btnDangBan = findViewById(R.id.btn_dang_ban);
        btnMenu = findViewById(R.id.btn_menu);
        scrollView = findViewById(R.id.scroll_view);
        btnXeCo = findViewById(R.id.btn_xe_co);
        btnDoDienTu = findViewById(R.id.btn_dien_tu);
        btnThoiTrang = findViewById(R.id.btn_thoi_trang);
        btnMevaBe = findViewById(R.id.btn_me_be);
        btnNoiNgoaiThat = findViewById(R.id.btn_noi_that);
        btnGiaiTri = findViewById(R.id.btn_so_thich);
        btnThuCung = findViewById(R.id.btn_thu_cung);
        btnDoVanPhong = findViewById(R.id.btn_van_phong);
        btnViecLam = findViewById(R.id.btn_viec_lam);
        btnKhac = findViewById(R.id.btn_loai_khac);
        btnSearch = findViewById(R.id.btn_search);

        btnMenu.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        btnDangBan.setOnClickListener(this);
        btnXeCo.setOnClickListener(this);
        btnDoDienTu.setOnClickListener(this);
        btnThoiTrang.setOnClickListener(this);
        btnMevaBe.setOnClickListener(this);
        btnNoiNgoaiThat.setOnClickListener(this);
        btnGiaiTri.setOnClickListener(this);
        btnThuCung.setOnClickListener(this);
        btnDoVanPhong.setOnClickListener(this);
        btnViecLam.setOnClickListener(this);
        btnKhac.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        scrollView.setMyScrollChangeListener(new TouchDetectableScrollView.OnMyScrollChangeListener() {
            @Override
            public void onScrollUp() {
                if (btnDangBan.getVisibility() == View.GONE) {
                    btnDangBan.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onScrollDown() {
                Log.d("scroll", "down");
                if (btnDangBan.getVisibility() == View.VISIBLE) {
                    btnDangBan.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            isLogin = false;
            tvName.setText(R.string.guest);
            tvInfo.setText(R.string.chua_co_thong_tin);
            Glide.with(this).load(R.drawable.ic_avatar).into(imgAvatar);
        } else {
            isLogin = true;
            String uID = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            Query queryUser = databaseReference.orderByChild("uid").equalTo(uID);
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do with your result
                            Log.e(TAG, "have account" + issue.getValue().toString());
                            Account currentAccount = issue.getValue(Account.class);
                            updateView(currentAccount);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateView(Account currentAccount) {
        if (currentAccount.getUrlAvatar() != null) {
            Glide.with(MainActivity.this).load(currentAccount.getUrlAvatar()).into(imgAvatar);
        }
        if (currentAccount.getName() != null) {
            tvName.setText(currentAccount.getName());
        }
        if (currentAccount.getAddress() != null) {
            tvInfo.setText(currentAccount.getAddress());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_products) {
            if (!isLogin) {
                Toast.makeText(this, R.string.can_dang_nhap, Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, ListSellProductActivity.class));
            }
        } else if (id == R.id.nav_favorite) {
            if (!isLogin) {
                Toast.makeText(this, R.string.can_dang_nhap, Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, ListFavouriteActivity.class));
            }
        } else if (id == R.id.nav_manage) {
            showActivityInfo();
        } else if (id == R.id.nav_share) {
            Util.shareApp(this);
        } else if (id == R.id.nav_vote) {
            rateApp();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateApp() {
        RateAppDialog rateAppDialog = new RateAppDialog(this, new RateAppDialog.OnButtonClicked() {
            @Override
            public void onRateClicked() {
                Util.rateApp(MainActivity.this);
                finish();
            }

            @Override
            public void onCancelClicked() {
            }
        });
        rateAppDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.img_avatar:
            case R.id.tv_name:
            case R.id.tv_address:
                showActivityInfo();
                break;
            case R.id.btn_dang_ban:
                showActivityAddProduct();
                break;
            case R.id.btn_xe_co:
                showFindProductActivity("1");
                break;
            case R.id.btn_dien_tu:
                showFindProductActivity("2");
                break;
            case R.id.btn_thoi_trang:
                showFindProductActivity("3");
                break;
            case R.id.btn_me_be:
                showFindProductActivity("4");
                break;
            case R.id.btn_noi_that:
                showFindProductActivity("5");
                break;
            case R.id.btn_so_thich:
                showFindProductActivity("6");
                break;
            case R.id.btn_thu_cung:
                showFindProductActivity("7");
                break;
            case R.id.btn_van_phong:
                showFindProductActivity("8");
                break;
            case R.id.btn_viec_lam:
                showFindProductActivity("9");
                break;
            case R.id.btn_loai_khac:
                showFindProductActivity("10");
                break;
            case R.id.btn_search:
                PreferencesManager.saveDanhMuc2("", this);
                PreferencesManager.saveLoaiSP2("", this);
                PreferencesManager.saveTinh2("", this);
                PreferencesManager.saveHuyen2("", this);
                Intent intent = new Intent(this, ShowListProductActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showFindProductActivity(String danhmuc) {
        List<DanhMuc> danhMucList = DataManager.getListDanhMuc(this);
        int size = danhMucList.size();
        for (int i = 0; i < size; i++) {
            if (danhMucList.get(i).getId().equals(danhmuc)) {
                PreferencesManager.saveDanhMuc2(danhMucList.get(i).getName(), this);
                PreferencesManager.saveLoaiSP2("", this);
                break;
            }
        }
        Intent intent = new Intent(this, ShowListProductActivity.class);
        startActivity(intent);
    }

    private void showActivityAddProduct() {
        if (!isLogin) {
            Toast.makeText(this, R.string.must_dang_nhap, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            if (!PreferencesManager.getDanhMuc(this).equals("")) {
                TiepTucTinDialog tiepTucTinDialog = new TiepTucTinDialog(this, new TiepTucTinDialog.OnButtonClicked() {
                    @Override
                    public void onTiepTucClicked() {
                        startActivity(new Intent(MainActivity.this, AddProductActivity.class));
                    }

                    @Override
                    public void onTaoMoiClicked() {
                        Util.ResessPrefernces(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, DanhMucActivity.class));
                    }
                });
                tiepTucTinDialog.show();
            } else {
                startActivity(new Intent(MainActivity.this, DanhMucActivity.class));
            }

        }
    }

    private void showActivityInfo() {
        if (!isLogin) {
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            startActivity(new Intent(this, InfoActivity.class));
        }
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, R.string.request_permistion, Toast.LENGTH_SHORT).show();
                    checkPermission();
                }
                return;
            }
        }
    }
}
