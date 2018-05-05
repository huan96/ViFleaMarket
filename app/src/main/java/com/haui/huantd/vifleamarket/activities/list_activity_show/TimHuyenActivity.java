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
import com.haui.huantd.vifleamarket.adapters.HuyenAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Huyen;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.DataManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class TimHuyenActivity extends AppCompatActivity {
    private static final String TAG = TimHuyenActivity.class.getName();
    private List<Huyen> mListHuyen;
    private List<Huyen> mListHuyenShow;
    private ImageView btnBack;
    private ImageView btnShow;
    private RecyclerView mRcvHuyen;
    private HuyenAdapter mHuyenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huyen);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getData();
    }

    public void getData() {
        try {
            Intent intent = getIntent();
            String idTinh = intent.getStringExtra(Constants.TINH_THANH_PHO);
            if (idTinh == null) {
                finish();
            } else {
                mListHuyen = DataManager.getListHuyen(this);
                getListShow(idTinh);
            }
        } catch (Exception e) {
            Log.d(TAG, "getData: " + e.toString());
            // finish();
        }
    }

    private void getListShow(String idTinh) {
        mListHuyenShow = new ArrayList<>();
        for (Huyen huyen : mListHuyen) {
            if (huyen.getIdTinh().equals(idTinh)) {
                mListHuyenShow.add(huyen);
            }
        }
        Log.d(TAG, "getListShow: " + mListHuyenShow.size());
        if (mListHuyenShow.size() == 0) {
            finish();
        } else {
            showListHuyen();
        }
    }

    private void showListHuyen() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShow = findViewById(R.id.btn_show);
        btnShow.setVisibility(View.INVISIBLE);

        mHuyenAdapter = new HuyenAdapter(mListHuyenShow, this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                PreferencesManager.saveHuyen2(mListHuyenShow.get(position).getName(), TimHuyenActivity.this);
                finish();
            }
        });
        mRcvHuyen = findViewById(R.id.rcv_khu_vuc);
        mRcvHuyen.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRcvHuyen.setAdapter(mHuyenAdapter);
    }
}
