package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.haui.huantd.vifleamarket.adapters.PostListAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.DanhMuc;
import com.haui.huantd.vifleamarket.models.Product;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.DataManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

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
    private String idDanhMuc;
    private String danhMuc, loai, ten, tinh, huyen;
    private List<DanhMuc> danhMucList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_product);
        setComponents();

        initData();
    }

    private void initData() {
        danhMucList = DataManager.getListDanhMuc(this);
        Intent intent = getIntent();
        if (intent != null) {
            idDanhMuc = intent.getStringExtra(Constants.DANH_MUC);
        }
        if (idDanhMuc == null) {
            idDanhMuc = "0";
            danhMuc = "";
        }
        DanhMuc danhMuc1 = new DanhMuc(idDanhMuc);
        if (danhMucList.contains(danhMuc1)) {
            danhMuc = danhMucList.get(danhMucList.indexOf(danhMuc1)).getName();
            tvDanhMuc.setText(danhMuc);
        }
        tinh = PreferencesManager.getTinh(this);
        huyen = PreferencesManager.getHuyen(this);
        if (!tinh.equals("")) {
            tvKhuVuc.setText(tinh);
        } else {
            tvKhuVuc.setText(huyen);
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

        btnBack.setOnClickListener(this);
        btnTimKiem.setOnClickListener(this);
        btnKhuVuc.setOnClickListener(this);
        btnDanhMuc.setOnClickListener(this);
        btnDangBan.setOnClickListener(this);

        listProduct = new ArrayList<>();
        listProductShow = new ArrayList<>();

        rvPost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Constants.NON_CONFIRM_POST);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                listProduct.add(product);
                if (listProductShow.size() < 10) {
                    listProductShow.add(product);
                }
                Log.e("onChildAdded", "xxxxxxxx");
                adapter.notifyDataSetChanged();
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
        setOnScrollRV();
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


    private void setOnScrollRV() {
      /*  rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rvPost, int dx, int dy) {
                super.onScrolled(rvPost, dx, dy);
                if (dy > 0) {
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
                }
            }
        });*/
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
                themLoaiSP();
                break;
            case R.id.btn_khu_vuc:
                themKhuVuc();
                break;
            case R.id.btn_dang_ban:
                showActivityAddProduct();
                break;

        }
    }

    private void themKhuVuc() {
    }

    private void themLoaiSP() {
    }

    private void timKiem() {
        //lay du lieu tu cac tv va edittext, sau do update du lieu
    }

    private void showActivityAddProduct() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, R.string.must_dang_nhap, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            startActivity(new Intent(this, AddProductActivity.class));
        }
    }
}
