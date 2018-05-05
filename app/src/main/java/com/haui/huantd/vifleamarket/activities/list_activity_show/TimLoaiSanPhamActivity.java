package com.haui.huantd.vifleamarket.activities.list_activity_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.LoaiSanPhamAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.LoaiSanPham;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.DataManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class TimLoaiSanPhamActivity extends AppCompatActivity {
    private static final String TAG = TimLoaiSanPhamActivity.class.getName();
    private List<LoaiSanPham> mListLoaiSP;
    private List<LoaiSanPham> mListLoaiSPShow;
    private ImageView btnClose;
    private RecyclerView mRcvLoaiSP;
    private LoaiSanPhamAdapter mLoaiSanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_san_pham);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getData();
    }

    public void getData() {
        try {
            Intent intent = getIntent();
            String idDanhMuc = intent.getStringExtra(Constants.DANH_MUC);
            if (idDanhMuc == null) {
                finish();
            } else {
                mListLoaiSP = DataManager.getListDanhMucNho(this);
                getListShow(idDanhMuc);
            }
        } catch (Exception e) {
            Log.d(TAG, "getData: " + e.toString());
            finish();
        }
    }

    private void getListShow(String idDanhMuc) {
        mListLoaiSPShow = new ArrayList<>();
        for (LoaiSanPham loaiSanPham : mListLoaiSP) {
            if (loaiSanPham.getIdDanhMuc().equals(idDanhMuc)) {
                mListLoaiSPShow.add(loaiSanPham);
            }
        }
        if (mListLoaiSPShow.size() == 0) {
            finish();
        } else {
            showListLoaiSanPham();
        }
    }

    private void showListLoaiSanPham() {
        btnClose = findViewById(R.id.btn_stop);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoaiSanPhamAdapter = new LoaiSanPhamAdapter(mListLoaiSPShow, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                PreferencesManager.saveLoaiSP2(mListLoaiSPShow.get(position).getName(), TimLoaiSanPhamActivity.this);
                finish();
            }
        });
        mRcvLoaiSP = findViewById(R.id.rcv_loai_sp);
        mRcvLoaiSP.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRcvLoaiSP.setAdapter(mLoaiSanPhamAdapter);
    }
}
