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

public class GiaActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack, btnShow;
    private ImageView btnClear;
    private Button btnTiepTuc;
    private EditText edtGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gia);
        initViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnShow = findViewById(R.id.btn_show);
        btnClear = findViewById(R.id.btn_cancel);
        btnTiepTuc = findViewById(R.id.btn_tiep_tuc);
        edtGia = findViewById(R.id.edt_gia);

        btnTiepTuc.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                Intent intent = new Intent(GiaActivity.this, AddImagesActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_show: {
                Intent intent = new Intent(GiaActivity.this,
                        AddProductActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_cancel:
                edtGia.setText("");
                break;
            case R.id.btn_tiep_tuc: {
                tiepTuc();
                break;
            }

        }
    }

    private void tiepTuc() {
        try {
            int gia = Integer.parseInt(edtGia.getText().toString());
            if (gia <= 0) {
                Toast.makeText(this, R.string.nhap_lai_gia, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(GiaActivity.this,
                        TieuDeActivity.class);
                PreferencesManager.saveGia(edtGia.getText().toString(), this);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            Toast.makeText(this, R.string.nhap_lai_gia, Toast.LENGTH_SHORT).show();
            edtGia.setText("");
        }

    }
}
