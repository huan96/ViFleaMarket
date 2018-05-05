package com.haui.huantd.vifleamarket.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.haui.huantd.vifleamarket.R;


/**
 * Created by binhnk on 7/25/2017.
 */

public class TiepTucTinDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnButtonClicked onButtonClicked;

    private TextView tvTiepTuc, tvTaoMoi;

    public TiepTucTinDialog(@NonNull Context context, OnButtonClicked onButtonClicked) {
        super(context);
        this.context = context;
        this.onButtonClicked = onButtonClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_tiep_tuc_dang);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvTiepTuc = findViewById(R.id.tv_tiep_tuc);
        tvTaoMoi = findViewById(R.id.tv_tao_moi);
        tvTiepTuc.setOnClickListener(this);
        tvTaoMoi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tao_moi:
                onButtonClicked.onTaoMoiClicked();
                dismiss();
                break;

            case R.id.tv_tiep_tuc:
                onButtonClicked.onTiepTucClicked();
                dismiss();
                break;

            default:
                dismiss();
                break;
        }
    }

    public interface OnButtonClicked {
        void onTiepTucClicked();

        void onTaoMoiClicked();
    }
}
