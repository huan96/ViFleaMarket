package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.utils.PreferencesManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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
        edtGia.addTextChangedListener(onTextChangedListener());
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
            Intent intent = new Intent(GiaActivity.this,
                    TieuDeActivity.class);
            PreferencesManager.saveGia(edtGia.getText().toString(), this);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, R.string.nhap_lai_gia, Toast.LENGTH_SHORT).show();
            edtGia.setText("");
        }

    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGia.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edtGia.setText(formattedString);
                    edtGia.setSelection(edtGia.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edtGia.addTextChangedListener(this);
            }
        };
    }
}
