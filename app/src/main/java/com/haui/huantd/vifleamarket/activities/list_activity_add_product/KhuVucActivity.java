package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.activities.AddProductActivity;
import com.haui.huantd.vifleamarket.adapters.TinhAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.Tinh;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.DataManager;

import java.util.List;

public class KhuVucActivity extends AppCompatActivity {

    private List<Tinh> mListTinh;
    private ImageView btnBack;
    private ImageView btnShow;
    private RecyclerView mRcvTinh;
    private TinhAdapter mTinhAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khu_vuc);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        initViews();
    }

    private void initViews() {
        mListTinh = DataManager.getListTinh(this);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhuVucActivity.this,
                        AddProductActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mRcvTinh = findViewById(R.id.rcv_khu_vuc);
        mTinhAdapter = new TinhAdapter(mListTinh,
                this, new OnItemClick() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(KhuVucActivity.this,
                        HuyenActivity.class);
                //truyen ID cua danh muc sang
                intent.putExtra(Constants.TINH_THANH_PHO, mListTinh.get(position).getId());
                startActivity(intent);
                finish();
            }
        });
        mRcvTinh.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRcvTinh.setAdapter(mTinhAdapter);
    }
}
