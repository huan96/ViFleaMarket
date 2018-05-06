package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.activities.SignInActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_add_product.AddProductActivity;
import com.haui.huantd.vifleamarket.activities.list_activity_add_product.DanhMucActivity;
import com.haui.huantd.vifleamarket.adapters.PostListAdapter;
import com.haui.huantd.vifleamarket.dialogs.TiepTucTinDialog;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;
import com.haui.huantd.vifleamarket.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ShowListProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShowListProductActivity";
    private ImageView btnBack;
    private EditText edtSearch;
    private TextView btnTimKiem;
    private LinearLayout layoutSearch;
    private LinearLayout btnKhuVuc, btnDanhMuc;
    private TextView tvKhuVuc, tvDanhMuc;
    private LinearLayout btnDangBan;
    private RecyclerView rvPost;
    private List<Product> listProduct;
    private List<Product> listProductShow;
    private PostListAdapter adapter;
    private String danhMuc, loai, ten, tinh, huyen;
    private LinearLayoutManager layoutManager;
    private ImageView btnClearKhuVuc, btnClearDanhMuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_product);
        setComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        initData();
        timKiem();
    }

    private void initData() {
        danhMuc = PreferencesManager.getDanhMuc2(this);
        loai = PreferencesManager.getLoaiSP2(this);
        tinh = PreferencesManager.getTinh2(this);
        huyen = PreferencesManager.getHuyen2(this);

        if (!danhMuc.equals("")) {
            tvDanhMuc.setText(danhMuc);
            btnClearDanhMuc.setVisibility(View.VISIBLE);
        }
        if (!loai.equals("")) {
            tvDanhMuc.setText(loai);
            btnClearDanhMuc.setVisibility(View.VISIBLE);
        }
        if (!tinh.equals("")) {
            tvKhuVuc.setText(tinh);
            btnClearKhuVuc.setVisibility(View.VISIBLE);
        }
        if (!huyen.equals("")) {
            tvKhuVuc.setText(huyen);
            btnClearKhuVuc.setVisibility(View.VISIBLE);
        }

        ten = edtSearch.getText().toString();
        if (ten == null) {
            ten = "";
        }
    }


    private void setComponents() {
        btnBack = findViewById(R.id.btn_back);
        edtSearch = findViewById(R.id.edt_search);
        btnTimKiem = findViewById(R.id.btn_tim_kiem);
        layoutSearch = findViewById(R.id.layout_search);
        btnKhuVuc = findViewById(R.id.btn_khu_vuc);
        btnDanhMuc = findViewById(R.id.btn_loai_sp);
        btnDangBan = findViewById(R.id.btn_dang_ban);
        rvPost = findViewById(R.id.rcv_post);
        tvDanhMuc = findViewById(R.id.tv_danh_muc);
        tvKhuVuc = findViewById(R.id.tv_khu_vuc);
        btnClearDanhMuc = findViewById(R.id.btn_clear_danhmuc);
        btnClearKhuVuc = findViewById(R.id.btn_clear_khu_vuc);

        btnBack.setOnClickListener(this);
        btnTimKiem.setOnClickListener(this);
        btnKhuVuc.setOnClickListener(this);
        btnDanhMuc.setOnClickListener(this);
        btnDangBan.setOnClickListener(this);
        btnClearDanhMuc.setOnClickListener(this);
        btnClearKhuVuc.setOnClickListener(this);

        listProduct = new ArrayList<>();
        listProductShow = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPost.setLayoutManager(layoutManager);
        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
        setAdapter();
        rvPost.setAdapter(adapter);
        setOnScrollRV();


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    timKiem();
                    setAdapter();
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setAdapter() {
        adapter = new PostListAdapter(rvPost, listProductShow, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(ShowListProductActivity.this, ShowProductActivity.class);
                Bundle bundle = new Bundle();
                Log.e(TAG, "onClick: " + position + listProductShow.get(position).getTieuDe());
                bundle.putSerializable(Constants.PRODUCT, listProductShow.get(position));
                intent.putExtra(Constants.PRODUCT, bundle);
                startActivity(intent);
            }
        });
        adapter.setLoadMore(new PostListAdapter.ILoadMore() {
            @Override
            public void onLoadMore() {
                listProductShow.add(null);
                adapter.notifyItemInserted(listProductShow.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listProductShow.remove(listProductShow.size() - 1);
                        adapter.notifyItemRemoved(listProductShow.size());
                        addNewPost();
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                    }
                }, 300); // Time to load
            }
        });
        rvPost.setAdapter(adapter);
    }

    private void addProduct(DataSnapshot dataSnapshot) {
        try {
            Product product = dataSnapshot.getValue(Product.class);
            product.setId(dataSnapshot.getKey());
            if (!danhMuc.equals("") && loai.equals("")) {
                if (!product.getDanhMuc().equals(danhMuc))
                    return;
            }
            if (!loai.equals("")) {
                if (!product.getLoaiSP().equals(loai))
                    return;
            }
            if (!tinh.equals("") && huyen.equals("")) {
                if (!product.getTinh().equals(tinh))
                    return;
            }
            if (!huyen.equals("")) {
                if (!product.getTinh().equals(huyen))
                    return;
            }
            ten = edtSearch.getText().toString();
            if (!ten.equals("")) {
                if (!product.getTieuDe().contains(ten))
                    return;
            }
            listProduct.add(product);
            if (listProductShow.size() < 10) {
                listProductShow.add(product);
            }
            Log.e(TAG, "Them san pham vao list");
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "addProduct: " + e.toString());
        }
    }

    private void addNewPost() {
        int sizeListProductShow = listProductShow.size() - 1;
        int sizeListProduct = listProduct.size() - 1;
        if ((sizeListProduct - sizeListProductShow) >= 10) {
            int sizeListProductShowNew = sizeListProductShow + 10;
            for (int i = sizeListProductShow; i < sizeListProductShowNew; i++) {
                listProductShow.add(listProduct.get(i));
            }
        } else {
            for (int i = sizeListProductShow; i < sizeListProduct; i++) {
                listProductShow.add(listProduct.get(i));
            }
        }
    }

    private static int firstVisibleInListview;


    private void setOnScrollRV() {
        rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rvPost, int dx, int dy) {
                super.onScrolled(rvPost, dx, dy);
                if (listProductShow.size() < 10)
                    return;
                int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisible > firstVisibleInListview) {
                    Log.i("RecyclerView scrolled: ", "scroll up!");
                    if (btnDangBan.getVisibility() == View.GONE) {
                        btnDangBan.setVisibility(View.VISIBLE);
                    }
                    if (layoutSearch.getVisibility() == View.GONE) {
                        layoutSearch.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Scrolling down
                    if (btnDangBan.getVisibility() == View.VISIBLE) {
                        btnDangBan.setVisibility(View.GONE);
                    }
                    if (layoutSearch.getVisibility() == View.VISIBLE) {
                        layoutSearch.setVisibility(View.GONE);
                    }
                    Log.i("RecyclerView scrolled: ", "scroll down!");
                }

                firstVisibleInListview = currentFirstVisible;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_tim_kiem:
                timKiem();
                break;
            case R.id.btn_loai_sp:
                startActivity(new Intent(ShowListProductActivity.this, TimKiemDanhMucActivity.class));
                break;
            case R.id.btn_khu_vuc:
                startActivity(new Intent(ShowListProductActivity.this, TimKhuVucActivity.class));
                break;
            case R.id.btn_dang_ban:
                showActivityAddProduct();
                break;
            case R.id.btn_clear_khu_vuc:
                clearKhuVuc();
                break;
            case R.id.btn_clear_danhmuc:
                clearDanhMuc();
                break;

        }
    }

    private void clearDanhMuc() {
        danhMuc = "";
        loai = "";
        PreferencesManager.saveLoaiSP2("", this);
        PreferencesManager.saveDanhMuc2("", this);
        tvDanhMuc.setText(R.string.danh_muc);
        btnClearDanhMuc.setVisibility(View.GONE);
        timKiem();
    }

    private void clearKhuVuc() {
        tinh = "";
        huyen = "";
        PreferencesManager.saveTinh2("", this);
        PreferencesManager.saveHuyen2("", this);
        tvKhuVuc.setText(R.string.khu_vuc);
        btnClearKhuVuc.setVisibility(View.GONE);
        timKiem();
    }


    private void timKiem() {
        //lay du lieu tu cac tv va edittext, sau do update du lieu
        listProduct.clear();
        listProductShow.clear();
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.CONFIRM_POST);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addProduct(dataSnapshot);
                Log.e(TAG, "onChildAdded:  addProduct");
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
    }

    private void showActivityAddProduct() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, R.string.must_dang_nhap, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            if (!PreferencesManager.getDanhMuc(this).equals("")) {
                TiepTucTinDialog tiepTucTinDialog = new TiepTucTinDialog(this, new TiepTucTinDialog.OnButtonClicked() {
                    @Override
                    public void onTiepTucClicked() {
                        startActivity(new Intent(ShowListProductActivity.this, AddProductActivity.class));
                    }

                    @Override
                    public void onTaoMoiClicked() {
                        Util.ResessPrefernces(ShowListProductActivity.this);
                        startActivity(new Intent(ShowListProductActivity.this, DanhMucActivity.class));
                    }
                });
                tiepTucTinDialog.show();
            } else {
                startActivity(new Intent(ShowListProductActivity.this, DanhMucActivity.class));
            }

        }
    }

}
