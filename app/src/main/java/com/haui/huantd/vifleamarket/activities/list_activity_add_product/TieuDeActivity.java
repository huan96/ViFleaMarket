package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

public class TieuDeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack, btnShow;
    private Button btnTiepTuc;
    private EditText edtTieuDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieu_de);
        initViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnShow = findViewById(R.id.btn_show);
        btnTiepTuc = findViewById(R.id.btn_tiep_tuc);
        edtTieuDe = findViewById(R.id.edt_tieu_de);

        btnTiepTuc.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                Intent intent = new Intent(TieuDeActivity.this, GiaActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_show: {
                Intent intent = new Intent(TieuDeActivity.this,
                        AddProductActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_tiep_tuc: {
                tiepTuc();
                break;
            }

        }
    }

    private void tiepTuc() {
        try {
            if (edtTieuDe.getText().toString().equals("")) {
                Toast.makeText(this, R.string.nhap_tieu_de, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(TieuDeActivity.this,
                        MoTaChiTietActivity.class);
                PreferencesManager.saveTieuDe(edtTieuDe.getText().toString(), this);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            Toast.makeText(this, R.string.nhap_tieu_de, Toast.LENGTH_SHORT).show();
            edtTieuDe.setText("");
        }
    }
}