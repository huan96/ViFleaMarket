package com.haui.huantd.vifleamarket.activities.list_activity_add_product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.adapters.HinhAnhAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddImagesActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack, btnShow, btnAddImage;
    private LinearLayout layoutAddImage, layoutShowImage;
    private RecyclerView rvImage;
    private int PICK_IMAGE_MULTIPLE = 1;
    private List<String> imagesEncodedList;
    private HinhAnhAdapter hinhAnhAdapter;
    private Button btnTiepTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        initViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnShow = findViewById(R.id.btn_show);
        btnAddImage = findViewById(R.id.btn_add_image);
        layoutAddImage = findViewById(R.id.layout_add_image);
        layoutShowImage = findViewById(R.id.layout_show_image);
        rvImage = findViewById(R.id.rv_images);
        btnTiepTuc = findViewById(R.id.btn_tiep_tuc);

        btnBack.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        btnTiepTuc.setOnClickListener(this);

        imagesEncodedList = new ArrayList<>();

        hinhAnhAdapter = new HinhAnhAdapter(imagesEncodedList, this, new HinhAnhAdapter.OnImageClick() {
            @Override
            public void onImageClick() {
                addImage();
            }

            @Override
            public void onDeleteClick(int position) {
                imagesEncodedList.remove(position);
                hinhAnhAdapter.notifyDataSetChanged();
                if (imagesEncodedList.size() == 0) {
                    layoutShowImage.setVisibility(View.GONE);
                    layoutAddImage.setVisibility(View.VISIBLE);
                }
            }
        });

        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImage.setAdapter(hinhAnhAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load image tu csdl
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                Intent intent = new Intent(AddImagesActivity.this, HuyenActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_show: {
                Intent intent = new Intent(AddImagesActivity.this,
                        AddProductActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_tiep_tuc: {
                tiepTuc();
                break;
            }
            case R.id.btn_add_image: {
                addImage();
                break;
            }

        }
    }

    private void tiepTuc() {
        //luu list image sang csdl
    }


    private void addImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == PICK_IMAGE_MULTIPLE) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imagesEncodedList.add(imageUri.toString());
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                        }
                    } else if (data.getData() != null) {
                        String imagePath = data.getData().getPath();
                        imagesEncodedList.add(imagePath);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }

                    if (imagesEncodedList.size() > 0) {
                        showListImage();
                    } else {
                        Toast.makeText(this, "Chưa chọn hình ảnh nào!", Toast.LENGTH_SHORT).show();
                        layoutShowImage.setVisibility(View.GONE);
                        layoutAddImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Không thể chọn ảnh!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showListImage() {
        layoutShowImage.setVisibility(View.VISIBLE);
        layoutAddImage.setVisibility(View.GONE);
        if (!imagesEncodedList.get(imagesEncodedList.size() - 1).equals("")) {
            imagesEncodedList.add("");
        }
        hinhAnhAdapter.notifyDataSetChanged();

    }
}
