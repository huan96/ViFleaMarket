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
import com.haui.huantd.vifleamarket.adapters.DanhMucAdapter;
import com.haui.huantd.vifleamarket.interfaces.OnItemClick;
import com.haui.huantd.vifleamarket.models.DanhMuc;
import com.haui.huantd.vifleamarket.utils.Constants;
import com.haui.huantd.vifleamarket.utils.DataManager;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.util.List;

public class TimKiemDanhMucActivity extends AppCompatActivity {
    private static final String TAG = "TimKiemDanhMucActivity";
    private List<DanhMuc> mListDanhMuc;
    private ImageView btnClose;
    private RecyclerView mRcvDanhMuc;
    private DanhMucAdapter mDanhMucAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);
        initViews();
    }

    private void initViews() {
        try {
            mListDanhMuc = DataManager.getListDanhMuc(this);
            btnClose = findViewById(R.id.btn_stop);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            mRcvDanhMuc = findViewById(R.id.rcv_danh_muc);
            mDanhMucAdapter = new DanhMucAdapter(mListDanhMuc,
                    this, new OnItemClick() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(TimKiemDanhMucActivity.this,
                            TimLoaiSanPhamActivity.class);
                    //truyen ID cua danh muc sang
                    intent.putExtra(Constants.DANH_MUC, mListDanhMuc.get(position).getId());
                    PreferencesManager.saveDanhMuc2(mListDanhMuc.get(position).getName(), TimKiemDanhMucActivity.this);
                    PreferencesManager.saveLoaiSP2("", TimKiemDanhMucActivity.this);
                    startActivity(intent);
                    finish();
                }
            });
            mRcvDanhMuc.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
            mRcvDanhMuc.setAdapter(mDanhMucAdapter);
        } catch (Exception e) {
            Log.e(TAG, "initViews: " + e.toString());
        }
    }
}
